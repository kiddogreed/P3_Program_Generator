package com.church.programgenerator.controller;

import java.time.LocalDate;

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

import com.church.programgenerator.model.WardCouncilProgram;
import com.church.programgenerator.service.FileStorageService;
import com.church.programgenerator.service.WardCouncilDocumentService;
import com.church.programgenerator.service.WardCouncilPdfService;

@Controller
@RequestMapping("/ward-council")
public class WardCouncilController {
    
    @Autowired
    private WardCouncilDocumentService documentService;
    
    @Autowired
    private WardCouncilPdfService pdfService;
    
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public String wardCouncilMeeting(Model model) {
        model.addAttribute("pageTitle", "Ward Council Meeting");
        model.addAttribute("wardCouncilProgram", new WardCouncilProgram());
        return "ward-council";
    }
    
    @PostMapping("/preview")
    public String previewWardCouncilProgram(@ModelAttribute WardCouncilProgram wardCouncilProgram, Model model) {
        model.addAttribute("pageTitle", "Ward Council Meeting Preview");
        model.addAttribute("wardCouncilProgram", wardCouncilProgram);
        return "ward-council-preview";
    }
    
    @PostMapping("/export/docx")
    public ResponseEntity<byte[]> exportToWord(
            @RequestParam String wardName,
            @RequestParam LocalDate meetingDate,
            @RequestParam(required = false) String openingPrayer,
            @RequestParam(required = false) String handbookReading,
            @RequestParam(required = false) String auxiliary,
            @RequestParam(required = false) String agenda,
            @RequestParam(required = false) String welfare,
            @RequestParam(required = false) String closingPrayer) {
        
        try {
            // Create ward council program object
            WardCouncilProgram program = createWardCouncilProgram(wardName, meetingDate, 
                openingPrayer, handbookReading, auxiliary, agenda, welfare, closingPrayer);
            
            // Generate document
            byte[] documentBytes = documentService.generateDocument(program);
            
            // Create filename with date
            String filename = fileStorageService.generateFilename("wardCouncil", meetingDate, ".docx");
            
            // Save to organized wardcouncil directory
            String filePath = fileStorageService.saveDocxFile("wardcouncil", filename, documentBytes);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(documentBytes);
                    
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error generating Word document".getBytes());
        }
    }
    
    @PostMapping("/export/pdf")
    public ResponseEntity<byte[]> exportToPdf(
            @RequestParam String wardName,
            @RequestParam LocalDate meetingDate,
            @RequestParam(required = false) String openingPrayer,
            @RequestParam(required = false) String handbookReading,
            @RequestParam(required = false) String auxiliary,
            @RequestParam(required = false) String agenda,
            @RequestParam(required = false) String welfare,
            @RequestParam(required = false) String closingPrayer) {
        
        try {
            // Create ward council program object
            WardCouncilProgram program = createWardCouncilProgram(wardName, meetingDate, 
                openingPrayer, handbookReading, auxiliary, agenda, welfare, closingPrayer);
            
            // Generate PDF
            byte[] pdfBytes = pdfService.generatePdf(program);
            
            // Create filename with date
            String filename = fileStorageService.generateFilename("wardCouncil", meetingDate, ".pdf");
            
            // Save to organized wardcouncil directory
            fileStorageService.savePdfFile("wardcouncil", filename, pdfBytes);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
                    
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error generating PDF document".getBytes());
        }
    }
    
    private WardCouncilProgram createWardCouncilProgram(String wardName, LocalDate meetingDate, 
            String openingPrayer, String handbookReading, String auxiliary, 
            String agenda, String welfare, String closingPrayer) {
        
        WardCouncilProgram program = new WardCouncilProgram();
        program.setWardName(wardName);
        program.setMeetingDate(meetingDate);
        program.setOpeningPrayer(openingPrayer);
        program.setHandbookReading(handbookReading);
        program.setAuxiliary(auxiliary);
        program.setAgenda(agenda);
        program.setWelfare(welfare);
        program.setClosingPrayer(closingPrayer);
        
        return program;
    }
}