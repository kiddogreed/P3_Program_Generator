package com.church.programgenerator.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
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

import com.church.programgenerator.model.SacramentProgram;
import com.church.programgenerator.model.Speaker;
import com.church.programgenerator.model.WardConfig;
import com.church.programgenerator.service.AuxiliaryService;
import com.church.programgenerator.service.ConductorService;
import com.church.programgenerator.service.FileStorageService;
import com.church.programgenerator.service.MusicianService;
import com.church.programgenerator.service.ProgramStorageService;
import com.church.programgenerator.service.SacramentProgramDocumentService;
import com.church.programgenerator.service.SacramentProgramPreviewService;
import com.church.programgenerator.service.WardConfigService;

@Controller
@RequestMapping("/sacrament")

public class SacramentController {

    @Autowired
    private SacramentProgramDocumentService documentService;

    @Autowired
    private SacramentProgramPreviewService previewService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ProgramStorageService programStorageService;


    @Autowired
    private ConductorService conductorService;

    @Autowired
    private AuxiliaryService auxiliaryService;

    @Autowired
    private WardConfigService wardConfigService;

    @Autowired
    private MusicianService musicianService;

    @GetMapping
    public String sacramentProgram(Model model) {
        WardConfig cfg = wardConfigService.getConfig();
        SacramentProgram program = new SacramentProgram();
        program.setStakeName(cfg.getStakeName());
        program.setWardName(cfg.getWardName());
        program.setDate(wardConfigService.nextSacramentDate());
        program.setAcknowledgement(cfg.getAcknowledgementTemplate());

        java.util.List<com.church.programgenerator.model.Conductor> conductors =
                conductorService.getByType("sacrament");
        com.church.programgenerator.model.Conductor suggested =
                wardConfigService.getSuggestedConductor(conductors, cfg.getLastSacramentConductorId());
        if (suggested != null) {
            program.setConducting(suggested.getName());
        }

        model.addAttribute("pageTitle", "Sacrament Meeting Program");
        model.addAttribute("sacramentProgram", program);
        model.addAttribute("conductors", conductors);
        model.addAttribute("auxiliaries", auxiliaryService.getAll());
        model.addAttribute("choristers", musicianService.getChoristers());
        model.addAttribute("pianists", musicianService.getPianists());
        model.addAttribute("speakerNames", Collections.emptyList());
        model.addAttribute("speakerTitles", Collections.emptyList());
        model.addAttribute("speakerTypeHint",
                wardConfigService.getSpeakerTypeLabel(wardConfigService.nextSacramentDate()));
        model.addAttribute("acknowledgementTemplate", cfg.getAcknowledgementTemplate());
        return "sacrament";
    }

    @PostMapping("/edit")
    public String editProgram(@ModelAttribute SacramentProgram program,
                              @RequestParam(value = "speakerNames", required = false) List<String> speakerNames,
                              @RequestParam(value = "speakerTitles", required = false) List<String> speakerTitles,
                              @RequestParam(name = "speakersAuxiliary", required = false) String speakersAuxiliary,
                              @RequestParam(name = "announcements", required = false) String announcements,
                              Model model) {

        if (announcements != null && !announcements.isBlank()) {
            processAnnouncements(program, announcements);
        }
        addSpeakersToProgram(program, speakerNames, speakerTitles);
        program.setSpeakersAuxiliary(speakersAuxiliary);

        model.addAttribute("pageTitle", "Sacrament Meeting Program");
        model.addAttribute("sacramentProgram", program);
        model.addAttribute("conductors", conductorService.getByType("sacrament"));
        model.addAttribute("auxiliaries", auxiliaryService.getAll());
        model.addAttribute("choristers", musicianService.getChoristers());
        model.addAttribute("pianists", musicianService.getPianists());
        model.addAttribute("speakerNames", speakerNames != null ? speakerNames : Collections.emptyList());
        model.addAttribute("speakerTitles", speakerTitles != null ? speakerTitles : Collections.emptyList());
        model.addAttribute("speakersAuxiliary", speakersAuxiliary);
        model.addAttribute("announcementsText", announcements);

        return "sacrament";
    }

    @PostMapping("/preview")
    public String previewProgram(@ModelAttribute SacramentProgram program,
                                 @RequestParam(value = "speakerNames", required = false) List<String> speakerNames,
                                 @RequestParam(value = "speakerTitles", required = false) List<String> speakerTitles,
                                 @RequestParam(name = "speakersAuxiliary", required = false) String speakersAuxiliary,
                                 @RequestParam(value = "announcements", required = false) String announcements,
                                 Model model) {

        if (announcements != null && !announcements.isBlank()) {
            processAnnouncements(program, announcements);
        }
        addSpeakersToProgram(program, speakerNames, speakerTitles);
        program.setSpeakersAuxiliary(speakersAuxiliary);

        String previewHtml = previewService.generateHtmlPreview(program);

        model.addAttribute("previewHtml", previewHtml);
        model.addAttribute("sacramentProgram", program);
        model.addAttribute("auxiliaries", auxiliaryService.getAll());
        model.addAttribute("choristers", musicianService.getChoristers());
        model.addAttribute("pianists", musicianService.getPianists());
        model.addAttribute("speakerNames", speakerNames != null ? speakerNames : Collections.emptyList());
        model.addAttribute("speakerTitles", speakerTitles != null ? speakerTitles : Collections.emptyList());
        model.addAttribute("speakersAuxiliary", speakersAuxiliary);
        model.addAttribute("announcementsText", announcements);

        return "sacrament-preview";
    }

    /** Legacy test-preview endpoint — redirects to the sacrament form. */
    @GetMapping("/test-preview")
    public String testPreview() {
        return "redirect:/sacrament";
    }

    @PostMapping("/export/docx")
    public ResponseEntity<byte[]> exportDocx(@ModelAttribute SacramentProgram program,
                                             @RequestParam(value = "speakerNames", required = false) List<String> speakerNames,
                                             @RequestParam(value = "speakerTitles", required = false) List<String> speakerTitles,
                                             @RequestParam(name = "speakersAuxiliary", required = false) String speakersAuxiliary,
                                             @RequestParam(value = "announcements", required = false) String announcements) {
        try {
            if (announcements != null && !announcements.isBlank()) {
                processAnnouncements(program, announcements);
            }
            addSpeakersToProgram(program, speakerNames, speakerTitles);
            program.setSpeakersAuxiliary(speakersAuxiliary);

            fileStorageService.saveDocxFile(program);
            try { programStorageService.saveSacramentProgram(program); } catch (Exception ignored) {}

            byte[] documentBytes = fileStorageService.getDocxBytes(program);
            String filename = generateFilename(program, ".docx");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", filename);

            return ResponseEntity.ok().headers(headers).body(documentBytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/export/pdf")
    public ResponseEntity<byte[]> exportPdf(@ModelAttribute SacramentProgram program,
                                            @RequestParam(value = "speakerNames", required = false) List<String> speakerNames,
                                            @RequestParam(value = "speakerTitles", required = false) List<String> speakerTitles,
                                            @RequestParam(name = "speakersAuxiliary", required = false) String speakersAuxiliary,
                                            @RequestParam(value = "announcements", required = false) String announcements) {
        try {
            if (announcements != null && !announcements.isBlank()) {
                processAnnouncements(program, announcements);
            }
            addSpeakersToProgram(program, speakerNames, speakerTitles);
            program.setSpeakersAuxiliary(speakersAuxiliary);

            fileStorageService.savePdfFile(program);
            try { programStorageService.saveSacramentProgram(program); } catch (Exception ignored) {}

            byte[] pdfBytes = fileStorageService.getPdfBytes(program);
            String filename = generateFilename(program, ".pdf");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);

            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /** Legacy endpoint — delegates to exportDocx */
    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateProgram(@ModelAttribute SacramentProgram program,
                                                  @RequestParam(value = "speakerNames", required = false) List<String> speakerNames,
                                                  @RequestParam(value = "speakerTitles", required = false) List<String> speakerTitles,
                                                  @RequestParam(name = "speakersAuxiliary", required = false) String speakersAuxiliary,
                                                  @RequestParam(value = "announcements", required = false) String announcements) {
        return exportDocx(program, speakerNames, speakerTitles, speakersAuxiliary, announcements);
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private void addSpeakersToProgram(SacramentProgram program, List<String> names, List<String> titles) {
        if (names == null || names.isEmpty()) return;
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            if (name != null && !name.isBlank()) {
                String title = (titles != null && i < titles.size() && titles.get(i) != null)
                        ? titles.get(i) : "";
                program.addSpeaker(new Speaker(i + 1, name.trim(), title));
            }
        }
    }

    private String generateFilename(SacramentProgram program, String extension) {
        // Format: Sacrament-Program-APR-20-2025.docx
        LocalDate date = program.getDate() != null ? program.getDate() : LocalDate.now();
        String formattedDate = date.format(DateTimeFormatter.ofPattern("MMM-dd-yyyy")).toUpperCase();
        return "Sacrament-Program-" + formattedDate + extension;
    }

    private void processAnnouncements(SacramentProgram program, String announcements) {
        if (announcements != null && !announcements.isBlank()) {
            program.getAnnouncements().clear();
            String[] lines = announcements.split("[\\r\\n]+|,");
            for (String line : lines) {
                String clean = line.trim();
                if (!clean.isEmpty()) {
                    program.addAnnouncement(clean);
                }
            }
        }
    }
}

