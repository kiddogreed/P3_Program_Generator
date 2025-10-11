package com.church.programgenerator.service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.stereotype.Service;

import com.church.programgenerator.model.BishopricProgram;

@Service
public class BishopricProgramDocumentService {

    public byte[] generateDocument(BishopricProgram program) throws IOException {
        XWPFDocument document = new XWPFDocument();
        
        // Create title paragraph
        createTitle(document, program);
        
        // Add meeting details with table format
        createMeetingDetails(document, program);
        
        // Convert to byte array
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        document.write(out);
        document.close();
        
        return out.toByteArray();
    }
    
    public void saveDocument(BishopricProgram program, String filePath) throws IOException {
        byte[] documentBytes = generateDocument(program);
        
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(documentBytes);
        }
    }
    
    private void createTitle(XWPFDocument document, BishopricProgram program) {
        // Church name
        XWPFParagraph churchPara = document.createParagraph();
        churchPara.setAlignment(ParagraphAlignment.CENTER);
        churchPara.setSpacingAfter(0);
        XWPFRun churchRun = churchPara.createRun();
        churchRun.setText("THE CHURCH OF JESUS CHRIST OF LATTER-DAY SAINTS");
        churchRun.setFontSize(10);
        churchRun.setBold(true);
        
        // Ward name and title
        XWPFParagraph titlePara = document.createParagraph();
        titlePara.setAlignment(ParagraphAlignment.CENTER);
        titlePara.setSpacingAfter(0);
        XWPFRun titleRun = titlePara.createRun();
        titleRun.setText(program.getWardName().toUpperCase() + " WARD");
        titleRun.setFontSize(14);
        titleRun.setBold(true);
        
        // Meeting type
        XWPFParagraph subtitlePara = document.createParagraph();
        subtitlePara.setAlignment(ParagraphAlignment.CENTER);
        subtitlePara.setSpacingAfter(0);
        XWPFRun subtitleRun = subtitlePara.createRun();
        subtitleRun.setText("BISHOPRIC MEETING");
        subtitleRun.setFontSize(12);
        subtitleRun.setBold(true);
        
        // Date
        XWPFParagraph datePara = document.createParagraph();
        datePara.setAlignment(ParagraphAlignment.CENTER);
        datePara.setSpacingAfter(200);
        XWPFRun dateRun = datePara.createRun();
        String formattedDate = program.getMeetingDate().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy")).toUpperCase();
        dateRun.setText(formattedDate);
        dateRun.setFontSize(10);
        dateRun.setBold(true);
    }
    
    private void createMeetingDetails(XWPFDocument document, BishopricProgram program) {
        // Create Agenda header
        XWPFParagraph agendaHeader = document.createParagraph();
        agendaHeader.setSpacingAfter(0);
        
        XWPFRun agendaRun = agendaHeader.createRun();
        agendaRun.setText("Agenda");
        agendaRun.setBold(true);
        agendaRun.setFontSize(14);
        agendaRun.setColor("E74C3C");
        
        // Create date header (right aligned)
        XWPFRun dateRun = agendaHeader.createRun();
        dateRun.addTab();
        dateRun.addTab();
        dateRun.addTab();
        dateRun.addTab();
        String shortDate = program.getMeetingDate().format(DateTimeFormatter.ofPattern("MM-dd-yy"));
        dateRun.setText(shortDate);
        dateRun.setBold(true);
        dateRun.setFontSize(14);
        dateRun.setColor("E74C3C");
        
        // Create table
        XWPFTable table = document.createTable();
        table.setWidth("100%");
        
        // Remove default row
        table.removeRow(0);
        
        // Add rows for each detail
        if (program.getPresiding() != null && !program.getPresiding().trim().isEmpty()) {
            addTableRow(table, "Presiding:", program.getPresiding());
        }
        
        if (program.getConducting() != null && !program.getConducting().trim().isEmpty()) {
            addTableRow(table, "Conducting:", program.getConducting());
        }
        
        if (program.getOpeningPrayer() != null && !program.getOpeningPrayer().trim().isEmpty()) {
            addTableRow(table, "Opening Prayer:", program.getOpeningPrayer());
        }
        
        if (program.getHandbookSpiritual() != null && !program.getHandbookSpiritual().trim().isEmpty()) {
            addTableRow(table, "Handbook Spiritual Thought:", program.getHandbookSpiritual());
        }
        
        // Add agenda items
        if (program.getAgendaItems() != null && !program.getAgendaItems().isEmpty()) {
            String agendaText = String.join("\n", program.getAgendaItems());
            addTableRow(table, "Agenda Items:", agendaText);
        }
        
        if (program.getCallingsAndReleases() != null && !program.getCallingsAndReleases().trim().isEmpty()) {
            addTableRow(table, "Callings and Releases:", program.getCallingsAndReleases());
        }
        
        if (program.getClosingPrayer() != null && !program.getClosingPrayer().trim().isEmpty()) {
            addTableRow(table, "Closing Prayer:", program.getClosingPrayer());
        }
        
        // Add spacing after table
        XWPFParagraph spacingPara = document.createParagraph();
        spacingPara.setSpacingAfter(100);
    }
    
    private void addTableRow(XWPFTable table, String label, String value) {
        XWPFTableRow row = table.createRow();
        
        // First cell (label)
        XWPFTableCell labelCell = row.getCell(0);
        XWPFParagraph labelPara = labelCell.getParagraphs().get(0);
        XWPFRun labelRun = labelPara.createRun();
        labelRun.setText(label);
        labelRun.setBold(true);
        labelRun.setFontSize(10);
        
        // Second cell (value)
        XWPFTableCell valueCell = row.createCell();
        XWPFParagraph valuePara = valueCell.getParagraphs().get(0);
        XWPFRun valueRun = valuePara.createRun();
        valueRun.setText(value);
        valueRun.setFontSize(10);
    }

}