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

import com.church.programgenerator.model.BishopricProgram;
import com.church.programgenerator.model.SacramentProgram;
import com.church.programgenerator.model.SavedProgram;
import com.church.programgenerator.model.Speaker;
import com.church.programgenerator.model.WardCouncilProgram;
import com.church.programgenerator.service.ConductorService;
import com.church.programgenerator.service.ProgramStorageService;

@Controller
@RequestMapping("/history")
public class HistoryController {

    private static final int PAGE_SIZE = 15;

    private final ProgramStorageService storageService;
    private final ConductorService conductorService;

    public HistoryController(ProgramStorageService storageService, ConductorService conductorService) {
        this.storageService = storageService;
        this.conductorService = conductorService;
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
        model.addAttribute("conductors", conductorService.getAll());
        model.addAttribute("speakerNames", speakerNames);
        model.addAttribute("speakerTitles", speakerTitles);
        model.addAttribute("announcementsText", announcementsText);
        model.addAttribute("successMessage", "Program loaded from history.");
        return "sacrament";
    }

    @GetMapping("/load/bishopric/{id}")
    public String loadBishopric(@PathVariable Long id, Model model) {
        BishopricProgram program = storageService.loadBishopricProgram(id);
        model.addAttribute("pageTitle", "Bishopric Meeting");
        model.addAttribute("bishopricProgram", program);
        model.addAttribute("successMessage", "Program loaded from history.");
        return "bishopric";
    }

    @GetMapping("/load/ward-council/{id}")
    public String loadWardCouncil(@PathVariable Long id, Model model) {
        WardCouncilProgram program = storageService.loadWardCouncilProgram(id);
        model.addAttribute("pageTitle", "Ward Council Meeting");
        model.addAttribute("wardCouncilProgram", program);
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

