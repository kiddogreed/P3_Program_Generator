package com.church.programgenerator.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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

import com.church.programgenerator.model.BishopricProgram;
import com.church.programgenerator.service.BishopricProgramDocumentService;
import com.church.programgenerator.service.BishopricProgramPdfService;

@Controller
@RequestMapping("/bishopric")
public class BishopricController {
    
    @Autowired
    private BishopricProgramDocumentService documentService;
    
    @Autowired
    private BishopricProgramPdfService pdfService;

    @GetMapping
    public String bishopricMeeting(Model model) {
        model.addAttribute("pageTitle", "Bishopric Meeting");
        model.addAttribute("bishopricProgram", new BishopricProgram());
        return "bishopric";
    }
    
    @PostMapping("/preview")
    public String previewBishopricProgram(
            @ModelAttribute BishopricProgram bishopricProgram,
            @RequestParam(required = false) String agendaItemsText,
            Model model) {
        
        // Process agenda items from textarea
        List<String> agendaItemsList = null;
        if (agendaItemsText != null && !agendaItemsText.trim().isEmpty()) {
            agendaItemsList = Arrays.stream(agendaItemsText.split("\n"))
                    .map(String::trim)
                    .filter(item -> !item.isEmpty())
                    .toList();
            bishopricProgram.setAgendaItems(agendaItemsList);
        }
        
        model.addAttribute("pageTitle", "Bishopric Meeting Preview");
        model.addAttribute("bishopricProgram", bishopricProgram);
        model.addAttribute("agendaItemsList", agendaItemsList);
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
            @RequestParam(required = false) String agendaItemsText,
            @RequestParam(required = false) String callingsAndReleases) {
        
        try {
            // Create bishopric program object
            BishopricProgram program = createBishopricProgram(wardName, meetingDate, presiding, 
                conducting, openingPrayer, closingPrayer, handbookSpiritual, agendaItemsText, callingsAndReleases);
            
            // Generate document
            byte[] documentBytes = documentService.generateDocument(program);
            
            // Create filename with date
            String filename = String.format("bishopricMeeting%s.docx", 
                meetingDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            
            // Save to reports directory
            String reportPath = String.format("src/reports/%s", filename);
            documentService.saveDocument(program, reportPath);
            
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
            @RequestParam(required = false) String presiding,
            @RequestParam(required = false) String conducting,
            @RequestParam(required = false) String openingPrayer,
            @RequestParam(required = false) String closingPrayer,
            @RequestParam(required = false) String handbookSpiritual,
            @RequestParam(required = false) String agendaItemsText,
            @RequestParam(required = false) String callingsAndReleases) {
        
        try {
            // Create bishopric program object
            BishopricProgram program = createBishopricProgram(wardName, meetingDate, presiding, 
                conducting, openingPrayer, closingPrayer, handbookSpiritual, agendaItemsText, callingsAndReleases);
            
            // Generate PDF
            byte[] pdfBytes = pdfService.generatePdf(program);
            
            // Create filename with date
            String filename = String.format("bishopricMeeting%s.pdf", 
                meetingDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            
            // Save to reports directory
            String reportPath = String.format("src/reports/%s", filename);
            pdfService.savePdf(program, reportPath);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
                    
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error generating PDF document".getBytes());
        }
    }
    
    private BishopricProgram createBishopricProgram(String wardName, LocalDate meetingDate, 
            String presiding, String conducting, String openingPrayer, String closingPrayer, 
            String handbookSpiritual, String agendaItemsText, String callingsAndReleases) {
        
        BishopricProgram program = new BishopricProgram();
        program.setWardName(wardName);
        program.setMeetingDate(meetingDate);
        program.setPresiding(presiding);
        program.setConducting(conducting);
        program.setOpeningPrayer(openingPrayer);
        program.setClosingPrayer(closingPrayer);
        program.setHandbookSpiritual(handbookSpiritual);
        program.setCallingsAndReleases(callingsAndReleases);
        
        // Process agenda items
        if (agendaItemsText != null && !agendaItemsText.trim().isEmpty()) {
            List<String> agendaItems = Arrays.stream(agendaItemsText.split("\n"))
                    .map(String::trim)
                    .filter(item -> !item.isEmpty())
                    .toList();
            program.setAgendaItems(agendaItems);
        }
        
        return program;
    }
}