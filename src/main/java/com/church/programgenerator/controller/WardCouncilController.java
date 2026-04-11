package com.church.programgenerator.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.church.programgenerator.model.AgendaItem;
import com.church.programgenerator.model.WardCouncilProgram;
import com.church.programgenerator.service.AuxiliaryService;
import com.church.programgenerator.service.ConductorService;
import com.church.programgenerator.service.FileStorageService;
import com.church.programgenerator.service.ProgramStorageService;
import com.church.programgenerator.service.WardCouncilPngService;
import com.church.programgenerator.service.WardConfigService;
import com.church.programgenerator.model.Auxiliary;
import com.church.programgenerator.model.WardConfig;
import com.church.programgenerator.model.Conductor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/ward-council")
public class WardCouncilController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ProgramStorageService programStorageService;

    @Autowired
    private AuxiliaryService auxiliaryService;

    @Autowired
    private ConductorService conductorService;

    @Autowired
    private WardCouncilPngService pngService;

    @Autowired
    private WardConfigService wardConfigService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private void addDropdownModel(Model model) {
        model.addAttribute("auxiliaries", auxiliaryService.getAll());
        model.addAttribute("bishopricConductors", conductorService.getByType("bishopric"));
    }

    @GetMapping
    public String wardCouncilMeeting(Model model) {
        WardConfig cfg = wardConfigService.getConfig();
        WardCouncilProgram program = new WardCouncilProgram();
        program.setWardName(cfg.getWardName());
        program.setMeetingDate(wardConfigService.nextWardCouncilDate());

        java.util.List<Conductor> bishops = conductorService.getByType("bishopric");
        Conductor suggested = wardConfigService.getSuggestedConductor(
                bishops, cfg.getLastBishopricConductorId());
        if (suggested != null) {
            program.setConducting(suggested.getName());
            // Advance the round-robin so the next load gets the next conductor
            wardConfigService.markConductorUsed("bishopric", suggested.getId());
        }
        // Bishop always presides — find conductor whose name starts with "Bishop"
        bishops.stream()
                .filter(c -> c.getName() != null && c.getName().toLowerCase().startsWith("bishop"))
                .findFirst()
                .ifPresent(b -> program.setPresiding(b.getName()));

        // Auto-assign opening prayer, handbook, closing prayer from auxiliaries (each different)
        List<Auxiliary> auxiliaries = auxiliaryService.getAll();
        if (!auxiliaries.isEmpty()) {
            int[] idxs = wardConfigService.nextThreeIndices(auxiliaries, cfg.getWcHandbookIdx());
            program.setOpeningPrayer(auxiliaries.get(idxs[0]).getName());
            program.setHandbookReading(auxiliaries.get(idxs[1]).getName());
            program.setClosingPrayer(auxiliaries.get(idxs[2]).getName());
            // Save the last used index as the rolling base for all three
            wardConfigService.markWardCouncilAssignments(idxs[2], idxs[2], idxs[2]);
        }

        model.addAttribute("pageTitle", "Ward Council Meeting");
        model.addAttribute("wardCouncilProgram", program);
        model.addAttribute("agendaItemsJson", "[]");
        addDropdownModel(model);
        return "ward-council";
    }

    @PostMapping("/edit")
    public String editWardCouncil(
            @ModelAttribute WardCouncilProgram wardCouncilProgram,
            @RequestParam(required = false) String agendaItemsJson,
            Model model) {
        model.addAttribute("pageTitle", "Ward Council Meeting");
        model.addAttribute("wardCouncilProgram", wardCouncilProgram);
        model.addAttribute("agendaItemsJson", agendaItemsJson != null ? agendaItemsJson : "[]");
        addDropdownModel(model);
        return "ward-council";
    }

    @PostMapping("/preview")
    public String previewWardCouncilProgram(
            @ModelAttribute WardCouncilProgram wardCouncilProgram,
            @RequestParam(required = false) String agendaItemsJson,
            Model model) {
        List<AgendaItem> items = parseAgendaJson(agendaItemsJson);
        if (items != null && !items.isEmpty()) {
            wardCouncilProgram.setAgendaItems(items);
        }
        model.addAttribute("pageTitle", "Ward Council Meeting Preview");
        model.addAttribute("wardCouncilProgram", wardCouncilProgram);
        model.addAttribute("agendaItemsList", items);
        model.addAttribute("agendaItemsJson", agendaItemsJson != null ? agendaItemsJson : "[]");
        addDropdownModel(model);
        return "ward-council-preview";
    }

    @PostMapping("/export/png")
    public ResponseEntity<byte[]> exportToPng(
            @RequestParam String wardName,
            @RequestParam LocalDate meetingDate,
            @RequestParam(required = false) String presiding,
            @RequestParam(required = false) String conducting,
            @RequestParam(required = false) String openingPrayer,
            @RequestParam(required = false) String handbookReading,
            @RequestParam(required = false) String auxiliary,
            @RequestParam(required = false) String welfare,
            @RequestParam(required = false) String closingPrayer,
            @RequestParam(required = false) String agendaItemsJson) {
        try {
            WardCouncilProgram program = buildProgram(wardName, meetingDate, presiding, conducting,
                    openingPrayer, handbookReading, auxiliary, welfare, closingPrayer, agendaItemsJson);
            byte[] pngBytes = pngService.generatePng(program);
            try { programStorageService.saveWardCouncilProgram(program); } catch (Exception ignored) {}
            String filename = fileStorageService.generateFilename("WardCouncil", meetingDate, ".png");
            fileStorageService.savePngFile("wardcouncil", filename, pngBytes);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.IMAGE_PNG)
                    .body(pngBytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(("Error: " + e.getMessage()).getBytes());
        }
    }

    private WardCouncilProgram buildProgram(String wardName, LocalDate meetingDate,
            String presiding, String conducting,
            String openingPrayer, String handbookReading, String auxiliary,
            String welfare, String closingPrayer, String agendaItemsJson) {
        WardCouncilProgram p = new WardCouncilProgram();
        p.setWardName(wardName);
        p.setMeetingDate(meetingDate);
        p.setPresiding(presiding);
        p.setConducting(conducting);
        p.setOpeningPrayer(openingPrayer);
        p.setHandbookReading(handbookReading);
        p.setAuxiliary(auxiliary);
        p.setWelfare(welfare);
        p.setClosingPrayer(closingPrayer);
        List<AgendaItem> items = parseAgendaJson(agendaItemsJson);
        if (items != null && !items.isEmpty()) p.setAgendaItems(items);
        return p;
    }

    private List<AgendaItem> parseAgendaJson(String json) {
        if (json == null || json.isBlank()) return null;
        try {
            List<AgendaItem> items = objectMapper.readValue(json, new TypeReference<List<AgendaItem>>() {});
            return items.stream().filter(i -> i.getTitle() != null && !i.getTitle().isBlank()).toList();
        } catch (Exception e) {
            return null;
        }
    }
}