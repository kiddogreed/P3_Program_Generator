package com.church.programgenerator.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
import com.church.programgenerator.service.FileStorageService;
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

    @GetMapping
    public String sacramentProgram(Model model) {
        model.addAttribute("pageTitle", "Sacrament Meeting Program");
        model.addAttribute("sacramentProgram", new SacramentProgram());
        return "sacrament";
    }
    
    @PostMapping("/edit")
    public String editProgram(@ModelAttribute SacramentProgram program,
                             @RequestParam("speaker1Name") String speaker1Name,
                             @RequestParam("speaker2Name") String speaker2Name,
                             @RequestParam("speaker3Name") String speaker3Name,
                             @RequestParam("speaker4Name") String speaker4Name,
                             @RequestParam("speaker1Title") String speaker1Title,
                             @RequestParam("speaker2Title") String speaker2Title,
                             @RequestParam("speaker3Title") String speaker3Title,
                             @RequestParam("speaker4Title") String speaker4Title,
                             @RequestParam(name = "speakersAuxiliary", required = false) String speakersAuxiliary,
                             @RequestParam(name = "announcements", required = false) String announcements,
                             Model model) {
        
        // Process speakers using existing method
        addSpeakersToProgram(program, speaker1Name, speaker2Name, speaker3Name, speaker4Name,
                           speaker1Title, speaker2Title, speaker3Title, speaker4Title);
        
        // Set speakers auxiliary
        program.setSpeakersAuxiliary(speakersAuxiliary);
        
        // Process announcements using existing method
        processAnnouncements(program, announcements);
        
        model.addAttribute("pageTitle", "Sacrament Meeting Program");
        model.addAttribute("sacramentProgram", program);
        model.addAttribute("speaker1Name", speaker1Name);
        model.addAttribute("speaker2Name", speaker2Name);
        model.addAttribute("speaker3Name", speaker3Name);
        model.addAttribute("speaker4Name", speaker4Name);
        model.addAttribute("speaker1Title", speaker1Title);
        model.addAttribute("speaker2Title", speaker2Title);
        model.addAttribute("speaker3Title", speaker3Title);
        model.addAttribute("speaker4Title", speaker4Title);
        model.addAttribute("speakersAuxiliary", speakersAuxiliary);
        model.addAttribute("announcementsText", announcements);
        
        return "sacrament";
    }
    
    @PostMapping("/preview")
    public String previewProgram(@ModelAttribute SacramentProgram program,
                                @RequestParam("speaker1Name") String speaker1Name,
                                @RequestParam("speaker2Name") String speaker2Name,
                                @RequestParam("speaker3Name") String speaker3Name,
                                @RequestParam("speaker4Name") String speaker4Name,
                                @RequestParam("speaker1Title") String speaker1Title,
                                @RequestParam("speaker2Title") String speaker2Title,
                                @RequestParam("speaker3Title") String speaker3Title,
                                @RequestParam("speaker4Title") String speaker4Title,
                                @RequestParam(name = "speakersAuxiliary", required = false) String speakersAuxiliary,
                                @RequestParam(value = "announcements", required = false) String announcements,
                                Model model) {
        
        // Process announcements
        if (announcements != null && !announcements.trim().isEmpty()) {
            processAnnouncements(program, announcements);
        }
        
        // Process speakers
        addSpeakersToProgram(program, speaker1Name, speaker2Name, speaker3Name, speaker4Name,
                           speaker1Title, speaker2Title, speaker3Title, speaker4Title);
        
        // Set speakers auxiliary
        program.setSpeakersAuxiliary(speakersAuxiliary);
        
        // Generate preview HTML
        String previewHtml = previewService.generateHtmlPreview(program);
        
        model.addAttribute("previewHtml", previewHtml);
        model.addAttribute("sacramentProgram", program);
        model.addAttribute("speaker1Name", speaker1Name);
        model.addAttribute("speaker2Name", speaker2Name);
        model.addAttribute("speaker3Name", speaker3Name);
        model.addAttribute("speaker4Name", speaker4Name);
        model.addAttribute("speaker1Title", speaker1Title);
        model.addAttribute("speaker2Title", speaker2Title);
        model.addAttribute("speaker3Title", speaker3Title);
        model.addAttribute("speaker4Title", speaker4Title);
        model.addAttribute("speakersAuxiliary", speakersAuxiliary);
        
        return "sacrament-preview";
    }
    
    @PostMapping("/export/docx")
    public ResponseEntity<byte[]> exportDocx(@ModelAttribute SacramentProgram program,
                                           @RequestParam("speaker1Name") String speaker1Name,
                                           @RequestParam("speaker2Name") String speaker2Name,
                                           @RequestParam("speaker3Name") String speaker3Name,
                                           @RequestParam("speaker4Name") String speaker4Name,
                                           @RequestParam("speaker1Title") String speaker1Title,
                                           @RequestParam("speaker2Title") String speaker2Title,
                                           @RequestParam("speaker3Title") String speaker3Title,
                                           @RequestParam("speaker4Title") String speaker4Title,
                                           @RequestParam(name = "speakersAuxiliary", required = false) String speakersAuxiliary,
                                           @RequestParam(value = "announcements", required = false) String announcements) {
        try {
            // Process announcements
            if (announcements != null && !announcements.trim().isEmpty()) {
                processAnnouncements(program, announcements);
            }
            
            // Process speakers
            addSpeakersToProgram(program, speaker1Name, speaker2Name, speaker3Name, speaker4Name,
                               speaker1Title, speaker2Title, speaker3Title, speaker4Title);
            
            // Set speakers auxiliary
            program.setSpeakersAuxiliary(speakersAuxiliary);
            
            // Save to file system
            String filePath = fileStorageService.saveDocxFile(program);
            
            // Also return for download
            byte[] documentBytes = fileStorageService.getDocxBytes(program);
            String filename = generateFilename(program, ".docx");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", filename);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(documentBytes);
                    
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping("/export/pdf")
    public ResponseEntity<byte[]> exportPdf(@ModelAttribute SacramentProgram program,
                                          @RequestParam("speaker1Name") String speaker1Name,
                                          @RequestParam("speaker2Name") String speaker2Name,
                                          @RequestParam("speaker3Name") String speaker3Name,
                                          @RequestParam("speaker4Name") String speaker4Name,
                                          @RequestParam("speaker1Title") String speaker1Title,
                                          @RequestParam("speaker2Title") String speaker2Title,
                                          @RequestParam("speaker3Title") String speaker3Title,
                                          @RequestParam("speaker4Title") String speaker4Title,
                                          @RequestParam(name = "speakersAuxiliary", required = false) String speakersAuxiliary,
                                          @RequestParam(value = "announcements", required = false) String announcements) {
        try {
            // Process announcements
            if (announcements != null && !announcements.trim().isEmpty()) {
                processAnnouncements(program, announcements);
            }
            
            // Process speakers
            addSpeakersToProgram(program, speaker1Name, speaker2Name, speaker3Name, speaker4Name,
                               speaker1Title, speaker2Title, speaker3Title, speaker4Title);
            
            // Set speakers auxiliary
            program.setSpeakersAuxiliary(speakersAuxiliary);
            
            // Save to file system
            String filePath = fileStorageService.savePdfFile(program);
            
            // Also return for download
            byte[] pdfBytes = fileStorageService.getPdfBytes(program);
            String filename = generateFilename(program, ".pdf");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
                    
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Legacy endpoint for backward compatibility
    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateProgram(@ModelAttribute SacramentProgram program,
                                                  @RequestParam("speaker1Name") String speaker1Name,
                                                  @RequestParam("speaker2Name") String speaker2Name,
                                                  @RequestParam("speaker3Name") String speaker3Name,
                                                  @RequestParam("speaker4Name") String speaker4Name,
                                                  @RequestParam("speaker1Title") String speaker1Title,
                                                  @RequestParam("speaker2Title") String speaker2Title,
                                                  @RequestParam("speaker3Title") String speaker3Title,
                                                  @RequestParam("speaker4Title") String speaker4Title,
                                                  @RequestParam(name = "speakersAuxiliary", required = false) String speakersAuxiliary,
                                                  @RequestParam(value = "announcements", required = false) String announcements) {
        return exportDocx(program, speaker1Name, speaker2Name, speaker3Name, speaker4Name,
                         speaker1Title, speaker2Title, speaker3Title, speaker4Title, speakersAuxiliary, announcements);
    }
    
    private void addSpeakersToProgram(SacramentProgram program, String speaker1Name, String speaker2Name, 
                                    String speaker3Name, String speaker4Name, String speaker1Title, 
                                    String speaker2Title, String speaker3Title, String speaker4Title) {
        if (speaker1Name != null && !speaker1Name.trim().isEmpty()) {
            program.addSpeaker(new Speaker(1, speaker1Name.trim(), speaker1Title));
        }
        if (speaker2Name != null && !speaker2Name.trim().isEmpty()) {
            program.addSpeaker(new Speaker(2, speaker2Name.trim(), speaker2Title));
        }
        if (speaker3Name != null && !speaker3Name.trim().isEmpty()) {
            program.addSpeaker(new Speaker(3, speaker3Name.trim(), speaker3Title));
        }
        if (speaker4Name != null && !speaker4Name.trim().isEmpty()) {
            program.addSpeaker(new Speaker(4, speaker4Name.trim(), speaker4Title));
        }
    }
    
    private String generateFilename(SacramentProgram program, String extension) {
        return "sacrament" + 
                (program.getDate() != null ? 
                    program.getDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")) : 
                    LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))) + 
                extension;
    }
    
    private void processAnnouncements(SacramentProgram program, String announcements) {
        if (announcements != null && !announcements.trim().isEmpty()) {
            // Clear existing announcements
            program.getAnnouncements().clear();
            
            // Split by line breaks or commas and clean up
            String[] announcementLines = announcements.split("[\\r\\n]+|,");
            for (String line : announcementLines) {
                String cleanLine = line.trim();
                if (!cleanLine.isEmpty()) {
                    program.addAnnouncement(cleanLine);
                }
            }
        }
    }
}