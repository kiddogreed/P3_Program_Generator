package com.church.programgenerator.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.church.programgenerator.model.AgendaItem;
import com.church.programgenerator.model.BishopricProgram;
import com.church.programgenerator.model.SacramentProgram;
import com.church.programgenerator.model.SavedProgram;
import com.church.programgenerator.model.Speaker;
import com.church.programgenerator.model.WardCouncilProgram;
import com.church.programgenerator.service.AuxiliaryService;
import com.church.programgenerator.service.ConductorService;
import com.church.programgenerator.service.MusicianService;
import com.church.programgenerator.service.ProgramStorageService;
import com.church.programgenerator.service.WardConfigService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/history")
public class HistoryController {

    private static final int PAGE_SIZE = 15;

    private final ProgramStorageService storageService;
    private final ConductorService conductorService;
    private final AuxiliaryService auxiliaryService;
    private final MusicianService musicianService;
    private final WardConfigService wardConfigService;

    public HistoryController(ProgramStorageService storageService, ConductorService conductorService,
                             AuxiliaryService auxiliaryService, MusicianService musicianService,
                             WardConfigService wardConfigService) {
        this.storageService = storageService;
        this.conductorService = conductorService;
        this.auxiliaryService = auxiliaryService;
        this.musicianService = musicianService;
        this.wardConfigService = wardConfigService;
    }

    @GetMapping
    public String listHistory(
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Page<SavedProgram> programsPage;

        if (type != null && !type.isBlank()) {
            programsPage = storageService.getProgramsByType(type.toUpperCase(), page, PAGE_SIZE);
            model.addAttribute("activeFilter", type.toUpperCase());
        } else {
            programsPage = storageService.getAllPrograms(page, PAGE_SIZE);
            model.addAttribute("activeFilter", "ALL");
        }

        model.addAttribute("programs", programsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", programsPage.getTotalPages());
        model.addAttribute("totalItems", programsPage.getTotalElements());
        model.addAttribute("activeType", type);
        model.addAttribute("pageTitle", "Saved Programs");
        return "history";
    }

    @GetMapping("/load/sacrament/{id}")
    public String loadSacrament(@PathVariable Long id, Model model) {
        SacramentProgram program = storageService.loadSacramentProgram(id);

        // Extract speaker names and titles for the dynamic speaker form rows
        List<String> speakerNames = new ArrayList<>();
        List<String> speakerTitles = new ArrayList<>();
        if (program.getSpeakers() != null) {
            program.getSpeakers().stream()
                    .sorted(Comparator.comparingInt(Speaker::getOrder))
                    .forEach(s -> {
                        speakerNames.add(s.getName() != null ? s.getName() : "");
                        speakerTitles.add(s.getTitle() != null ? s.getTitle() : "");
                    });
        }

        // Convert announcements list back to a textarea-friendly string
        String announcementsText = "";
        if (program.getAnnouncements() != null && !program.getAnnouncements().isEmpty()) {
            announcementsText = String.join("\n", program.getAnnouncements());
        }

        model.addAttribute("pageTitle", "Sacrament Meeting Program");
        model.addAttribute("sacramentProgram", program);
        model.addAttribute("conductors", conductorService.getByType("sacrament"));
        model.addAttribute("auxiliaries", auxiliaryService.getAll());
        model.addAttribute("choristers", musicianService.getChoristers());
        model.addAttribute("pianists", musicianService.getPianists());
        model.addAttribute("speakerNames", speakerNames);
        model.addAttribute("speakerTitles", speakerTitles);
        model.addAttribute("announcementsText", announcementsText);
        model.addAttribute("speakerTypeHint",
                wardConfigService.getSpeakerTypeLabel(program.getDate()));
        model.addAttribute("successMessage", "Program loaded from history.");
        return "sacrament";
    }

    @GetMapping("/load/bishopric/{id}")
    public String loadBishopric(@PathVariable Long id, Model model) {
        BishopricProgram program = storageService.loadBishopricProgram(id);
        model.addAttribute("pageTitle", "Bishopric Meeting");
        model.addAttribute("bishopricProgram", program);
        model.addAttribute("conductors", conductorService.getByType("bishopric"));
        model.addAttribute("successMessage", "Program loaded from history.");
        // Serialize agendaItems to JSON for the UI
        try {
            ObjectMapper mapper = new ObjectMapper();
            String agendaJson = mapper.writeValueAsString(program.getAgendaItems() != null ? program.getAgendaItems() : new java.util.ArrayList<AgendaItem>());
            model.addAttribute("agendaItemsJson", agendaJson);
        } catch (Exception e) {
            model.addAttribute("agendaItemsJson", "[]");
        }
        return "bishopric";
    }

    @GetMapping("/load/ward-council/{id}")
    public String loadWardCouncil(@PathVariable Long id, Model model) {
        WardCouncilProgram program = storageService.loadWardCouncilProgram(id);
        String agendaJson = "[]";
        try {
            ObjectMapper mapper = new ObjectMapper();
            agendaJson = mapper.writeValueAsString(
                    program.getAgendaItems() != null ? program.getAgendaItems() : new java.util.ArrayList<AgendaItem>());
        } catch (Exception ignored) {}
        model.addAttribute("pageTitle", "Ward Council Meeting");
        model.addAttribute("wardCouncilProgram", program);
        model.addAttribute("agendaItemsJson", agendaJson);
        model.addAttribute("auxiliaries", auxiliaryService.getAll());
        model.addAttribute("bishopricConductors", conductorService.getByType("bishopric"));
        model.addAttribute("successMessage", "Program loaded from history.");
        return "ward-council";
    }

    @PostMapping("/delete/{id}")
    public String deleteProgram(
            @PathVariable Long id,
            RedirectAttributes redirectAttrs) {
        storageService.deleteProgram(id);
        redirectAttrs.addFlashAttribute("successMessage", "Program deleted successfully.");
        return "redirect:/history";
    }
}

