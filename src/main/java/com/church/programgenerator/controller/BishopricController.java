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
import com.church.programgenerator.model.BishopricProgram;
import com.church.programgenerator.service.BishopricProgramDocumentService;
import com.church.programgenerator.service.BishopricProgramPdfService;
import com.church.programgenerator.service.ConductorService;
import com.church.programgenerator.service.FileStorageService;
import com.church.programgenerator.service.ProgramStorageService;
import com.church.programgenerator.service.WardConfigService;
import com.church.programgenerator.model.WardConfig;
import com.church.programgenerator.model.Conductor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/bishopric")
public class BishopricController {

    @Autowired
    private BishopricProgramDocumentService documentService;

    @Autowired
    private BishopricProgramPdfService pdfService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ProgramStorageService programStorageService;

    @Autowired
    private ConductorService conductorService;

    @Autowired
    private WardConfigService wardConfigService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping
    public String bishopricMeeting(Model model) {
        WardConfig cfg = wardConfigService.getConfig();
        BishopricProgram program = new BishopricProgram();
        program.setWardName(cfg.getWardName());
        program.setMeetingDate(wardConfigService.nextBishopricDate());

        java.util.List<Conductor> conductors = conductorService.getByType("bishopric");
        Conductor suggested = wardConfigService.getSuggestedConductor(
                conductors, cfg.getLastBishopricConductorId());
        if (suggested != null) {
            program.setConducting(suggested.getName());
            // Advance the round-robin so the next load gets the next conductor
            wardConfigService.markConductorUsed("bishopric", suggested.getId());
        }
        // Bishop presides — find conductor whose name starts with "Bishop" (case-insensitive)
        conductors.stream()
                .filter(c -> c.getName() != null && c.getName().toLowerCase().startsWith("bishop"))
                .findFirst()
                .ifPresent(b -> program.setPresiding(b.getName()));

        // Auto-assign opening prayer, closing prayer, handbook from conductors (each different)
        if (!conductors.isEmpty()) {
            int[] idxs = wardConfigService.nextThreeIndices(conductors, cfg.getBpHandbookIdx());
            program.setOpeningPrayer(conductors.get(idxs[0]).getName());
            program.setClosingPrayer(conductors.get(idxs[1]).getName());
            program.setHandbookSpiritual(conductors.get(idxs[2]).getName());
            // Save the last used index as the rolling base for all three
            wardConfigService.markBishopricAssignments(idxs[2], idxs[2], idxs[2]);
        }

        model.addAttribute("pageTitle", "Bishopric Meeting");
        model.addAttribute("bishopricProgram", program);
        model.addAttribute("conductors", conductors);
        model.addAttribute("agendaItemsJson", "[]");
        return "bishopric";
    }

    @PostMapping("/edit")
    public String editBishopricProgram(
            @ModelAttribute BishopricProgram bishopricProgram,
            @RequestParam(required = false) String agendaItemsJson,
            Model model) {
        model.addAttribute("pageTitle", "Bishopric Meeting");
        model.addAttribute("bishopricProgram", bishopricProgram);
        model.addAttribute("conductors", conductorService.getByType("bishopric"));
        model.addAttribute("agendaItemsJson", agendaItemsJson != null ? agendaItemsJson : "[]");
        return "bishopric";
    }

    @PostMapping("/preview")
    public String previewBishopricProgram(
            @ModelAttribute BishopricProgram bishopricProgram,
            @RequestParam(required = false) String agendaItemsJson,
            Model model) {

        List<AgendaItem> agendaItemsList = parseAgendaJson(agendaItemsJson);
        if (agendaItemsList != null && !agendaItemsList.isEmpty()) {
            bishopricProgram.setAgendaItems(agendaItemsList);
        } else {
            agendaItemsList = null;
        }

        model.addAttribute("pageTitle", "Bishopric Meeting Preview");
        model.addAttribute("bishopricProgram", bishopricProgram);
        model.addAttribute("agendaItemsList", agendaItemsList);
        model.addAttribute("agendaItemsJson", agendaItemsJson != null ? agendaItemsJson : "[]");
        return "bishopric-preview";
    }

    @PostMapping("/export/docx")
    public ResponseEntity<byte[]> exportToWord(
            @RequestParam String wardName,
            @RequestParam LocalDate meetingDate,
            @RequestParam(required = false) String presiding,
            @RequestParam(required = false) String conducting,
            @RequestParam(required = false) String openingPrayer,
            @RequestParam(required = false) String closingPrayer,
            @RequestParam(required = false) String handbookSpiritual,
            @RequestParam(required = false) String agendaItemsJson) {

        try {
            BishopricProgram program = buildProgram(wardName, meetingDate, presiding,
                    conducting, openingPrayer, closingPrayer, handbookSpiritual, agendaItemsJson);
            byte[] bytes = documentService.generateDocument(program);
            try { programStorageService.saveBishopricProgram(program); } catch (Exception ignored) {}
            String filename = fileStorageService.generateFilename("bishopric", meetingDate, ".docx");
            fileStorageService.saveDocxFile("bishopric", filename, bytes);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(bytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error generating Word document".getBytes());
        }
    }

    @PostMapping("/export/pdf")
    public ResponseEntity<byte[]> exportToPdf(
            @RequestParam String wardName,
            @RequestParam LocalDate meetingDate,
            @RequestParam(required = false) String presiding,
            @RequestParam(required = false) String conducting,
            @RequestParam(required = false) String openingPrayer,
            @RequestParam(required = false) String closingPrayer,
            @RequestParam(required = false) String handbookSpiritual,
            @RequestParam(required = false) String agendaItemsJson) {

        try {
            BishopricProgram program = buildProgram(wardName, meetingDate, presiding,
                    conducting, openingPrayer, closingPrayer, handbookSpiritual, agendaItemsJson);
            byte[] pdfBytes = pdfService.generatePdf(program);
            try { programStorageService.saveBishopricProgram(program); } catch (Exception ignored) {}
            String filename = fileStorageService.generateFilename("bishopric", meetingDate, ".pdf");
            fileStorageService.savePdfFile("bishopric", filename, pdfBytes);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error generating PDF document".getBytes());
        }
    }

    private BishopricProgram buildProgram(String wardName, LocalDate meetingDate,
            String presiding, String conducting, String openingPrayer, String closingPrayer,
            String handbookSpiritual, String agendaItemsJson) {
        BishopricProgram p = new BishopricProgram();
        p.setWardName(wardName);
        p.setMeetingDate(meetingDate);
        p.setPresiding(presiding);
        p.setConducting(conducting);
        p.setOpeningPrayer(openingPrayer);
        p.setClosingPrayer(closingPrayer);
        p.setHandbookSpiritual(handbookSpiritual);
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
