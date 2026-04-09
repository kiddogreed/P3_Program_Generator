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
import com.church.programgenerator.service.ConductorService;
import com.church.programgenerator.service.FileStorageService;
import com.church.programgenerator.service.ProgramStorageService;
import com.church.programgenerator.service.SacramentProgramDocumentService;
import com.church.programgenerator.service.SacramentProgramPreviewService;

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

    @GetMapping
    public String sacramentProgram(Model model) {
        model.addAttribute("pageTitle", "Sacrament Meeting Program");
        model.addAttribute("sacramentProgram", new SacramentProgram());
        model.addAttribute("conductors", conductorService.getAll());
        model.addAttribute("speakerNames", Collections.emptyList());
        model.addAttribute("speakerTitles", Collections.emptyList());
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
        model.addAttribute("conductors", conductorService.getAll());
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
        model.addAttribute("speakerNames", speakerNames != null ? speakerNames : Collections.emptyList());
        model.addAttribute("speakerTitles", speakerTitles != null ? speakerTitles : Collections.emptyList());
        model.addAttribute("speakersAuxiliary", speakersAuxiliary);
        model.addAttribute("announcementsText", announcements);

        return "sacrament-preview";
    }

    @GetMapping("/test-preview")
    public String testPreview(Model model) {
        SacramentProgram program = new SacramentProgram();
        program.setStakeName("Pasay Philippine Stake");
        program.setWardName("Pasay 3rd Ward");
        program.setDate(LocalDate.of(2026, 4, 12));
        program.setPresiding("Bishop Sherwin Tan");
        program.setConducting("(2nd Co) Bro. Joenice Gaco");
        program.setAcknowledgement("(2nd Co) Bro. Jonathan Ordillas, Bro. Adrian Matro (Wrd Clrk), Johanne Perlas (Asst. Clrk. rec). Bro. Norman Oliva (Asst. Clrk. fin), John Russelle Domingo, Genesis Ferareza, To all Visitors and Stake Leaders (Welcome).");
        program.setChorister("Sis. Kyle Domingo");
        program.setPianist("Bro. Oscar Driz");
        program.setOpeningHymn("#26 \"Joseph Smith's First Prayer\"");
        program.setSacramentHymn("#181 \"Jesus of Nazareth, Savior and King\"");
        program.setClosingHymn("#270 \"I'll Go Where You Want Me to Go\"");
        program.setInvocation("Sis. Izabel Ann Mamaril Oliva");
        program.setWardBusiness("n/a");
        program.setStakeBusiness("Bro. Gajultos JR");
        program.setBenediction("Sis. Zharich Villalobos Ebro");
        program.setSpeakersAuxiliary("Relief Society");

        String announcementsText = "Ongoing Sports (Invite to participate or watch and support our teams)\nApril 5 Easter Sunday";
        processAnnouncements(program, announcementsText);

        List<String> speakerNames = List.of("Lyka Villanueva", "Myrna Driz", "Meraluna Docabo");
        List<String> speakerTitles = List.of("Sis.", "Sis.", "Sis.");
        addSpeakersToProgram(program, speakerNames, speakerTitles);

        String previewHtml = previewService.generateHtmlPreview(program);

        model.addAttribute("previewHtml", previewHtml);
        model.addAttribute("sacramentProgram", program);
        model.addAttribute("speakerNames", speakerNames);
        model.addAttribute("speakerTitles", speakerTitles);
        model.addAttribute("speakersAuxiliary", "Relief Society");
        model.addAttribute("announcementsText", announcementsText);

        return "sacrament-preview";
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
        return "sacrament" +
                (program.getDate() != null
                        ? program.getDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                        : LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))) +
                extension;
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

