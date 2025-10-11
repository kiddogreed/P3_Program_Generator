package com.church.programgenerator.service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.church.programgenerator.model.BishopricProgram;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;

@Service
public class BishopricProgramPdfService {

    public byte[] generatePdf(BishopricProgram program) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        
        // Create fonts
        PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        
        // Create title section
        createTitle(document, program, boldFont, normalFont);
        
        // Add meeting details with table format
        createMeetingDetails(document, program, boldFont, normalFont);
        
        document.close();
        return baos.toByteArray();
    }
    
    public void savePdf(BishopricProgram program, String filePath) throws IOException {
        byte[] pdfBytes = generatePdf(program);
        
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(pdfBytes);
        }
    }
    
    private void createTitle(Document document, BishopricProgram program, PdfFont boldFont, PdfFont normalFont) {
        // Church name
        Paragraph churchPara = new Paragraph()
                .add(new Text("THE CHURCH OF JESUS CHRIST OF LATTER-DAY SAINTS")
                        .setFont(boldFont)
                        .setFontSize(10))
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);
        document.add(churchPara);
        
        // Ward name and title
        Paragraph titlePara = new Paragraph()
                .add(new Text(program.getWardName().toUpperCase() + " WARD")
                        .setFont(boldFont)
                        .setFontSize(14))
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);
        document.add(titlePara);
        
        // Meeting type
        Paragraph subtitlePara = new Paragraph()
                .add(new Text("BISHOPRIC MEETING")
                        .setFont(boldFont)
                        .setFontSize(12))
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);
        document.add(subtitlePara);
        
        // Date
        String formattedDate = program.getMeetingDate().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy")).toUpperCase();
        Paragraph datePara = new Paragraph()
                .add(new Text(formattedDate)
                        .setFont(boldFont)
                        .setFontSize(10))
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
        document.add(datePara);
    }
    
    private void createMeetingDetails(Document document, BishopricProgram program, PdfFont boldFont, PdfFont normalFont) {
        // Create Agenda header with date
        Paragraph agendaHeaderPara = new Paragraph()
                .add(new Text("Agenda").setFont(boldFont).setFontSize(14).setFontColor(ColorConstants.RED))
                .add(new Text("                                                         ").setFont(normalFont)) // spacing
                .add(new Text(program.getMeetingDate().format(DateTimeFormatter.ofPattern("MM-dd-yy")))
                        .setFont(boldFont).setFontSize(14).setFontColor(ColorConstants.RED))
                .setMarginBottom(10);
        document.add(agendaHeaderPara);
        
        // Create table
        Table table = new Table(new float[]{2, 3});
        table.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));
        
        // Add rows for each detail
        if (program.getPresiding() != null && !program.getPresiding().trim().isEmpty()) {
            addTableRow(table, "Presiding:", program.getPresiding(), boldFont, normalFont);
        }
        
        if (program.getConducting() != null && !program.getConducting().trim().isEmpty()) {
            addTableRow(table, "Conducting:", program.getConducting(), boldFont, normalFont);
        }
        
        if (program.getOpeningPrayer() != null && !program.getOpeningPrayer().trim().isEmpty()) {
            addTableRow(table, "Opening Prayer:", program.getOpeningPrayer(), boldFont, normalFont);
        }
        
        if (program.getHandbookSpiritual() != null && !program.getHandbookSpiritual().trim().isEmpty()) {
            addTableRow(table, "Handbook Spiritual Thought:", program.getHandbookSpiritual(), boldFont, normalFont);
        }
        
        // Add agenda items
        if (program.getAgendaItems() != null && !program.getAgendaItems().isEmpty()) {
            String agendaText = String.join("\n", program.getAgendaItems());
            addTableRow(table, "Agenda Items:", agendaText, boldFont, normalFont);
        }
        
        if (program.getCallingsAndReleases() != null && !program.getCallingsAndReleases().trim().isEmpty()) {
            addTableRow(table, "Callings and Releases:", program.getCallingsAndReleases(), boldFont, normalFont);
        }
        
        if (program.getClosingPrayer() != null && !program.getClosingPrayer().trim().isEmpty()) {
            addTableRow(table, "Closing Prayer:", program.getClosingPrayer(), boldFont, normalFont);
        }
        
        document.add(table);
        
        // Add spacing
        document.add(new Paragraph().setMarginBottom(10));
    }
    
    private void addTableRow(Table table, String label, String value, PdfFont boldFont, PdfFont normalFont) {
        // Label cell
        Cell labelCell = new Cell()
                .add(new Paragraph(label).setFont(boldFont).setFontSize(10))
                .setBorder(new SolidBorder(ColorConstants.RED, 1))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY, 0.1f)
                .setPadding(8);
        
        // Value cell  
        Cell valueCell = new Cell()
                .add(new Paragraph(value).setFont(normalFont).setFontSize(10))
                .setBorder(new SolidBorder(ColorConstants.RED, 1))
                .setPadding(8);
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }

}