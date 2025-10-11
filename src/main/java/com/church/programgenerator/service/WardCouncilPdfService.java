package com.church.programgenerator.service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.church.programgenerator.model.WardCouncilProgram;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

@Service
public class WardCouncilPdfService {

    public byte[] generatePdf(WardCouncilProgram program) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        
        // Create fonts
        PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        
        // Define colors
        DeviceRgb wardColor = new DeviceRgb(139, 115, 85); // #8B7355
        DeviceRgb councilColor = new DeviceRgb(90, 138, 170); // #5A8AAA
        DeviceRgb agendaColor = new DeviceRgb(210, 105, 30); // #D2691E
        
        // Create title section
        createTitle(document, program, boldFont, wardColor, councilColor, agendaColor);
        
        // Create agenda table
        createAgendaTable(document, program, boldFont, normalFont, wardColor, agendaColor);
        
        document.close();
        return baos.toByteArray();
    }
    
    public void savePdf(WardCouncilProgram program, String filePath) throws IOException {
        byte[] pdfBytes = generatePdf(program);
        
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(pdfBytes);
        }
    }
    
    private void createTitle(Document document, WardCouncilProgram program, PdfFont boldFont,
                           DeviceRgb wardColor, DeviceRgb councilColor, DeviceRgb agendaColor) {
        // Ward name
        Paragraph wardPara = new Paragraph()
                .add(new Text(program.getWardName().toUpperCase())
                        .setFont(boldFont)
                        .setFontSize(18)
                        .setFontColor(wardColor))
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);
        document.add(wardPara);
        
        // Ward Council title
        Paragraph titlePara = new Paragraph()
                .add(new Text("WARD COUNCIL")
                        .setFont(boldFont)
                        .setFontSize(20)
                        .setFontColor(councilColor))
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10);
        document.add(titlePara);
        
        // Agenda and date line
        String formattedDate = program.getMeetingDate().format(DateTimeFormatter.ofPattern("MM-dd-yy"));
        
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{50, 50}))
                .setWidth(UnitValue.createPercentValue(100));
        
        Cell agendaCell = new Cell()
                .add(new Paragraph()
                        .add(new Text("Agenda")
                                .setFont(boldFont)
                                .setFontSize(14)
                                .setFontColor(agendaColor)))
                .setTextAlignment(TextAlignment.LEFT)
                .setBorder(null);
        
        Cell dateCell = new Cell()
                .add(new Paragraph()
                        .add(new Text(formattedDate)
                                .setFont(boldFont)
                                .setFontSize(12)
                                .setFontColor(agendaColor)))
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(null);
        
        headerTable.addCell(agendaCell);
        headerTable.addCell(dateCell);
        
        document.add(headerTable);
        document.add(new Paragraph().setMarginBottom(20));
    }
    
    private void createAgendaTable(Document document, WardCouncilProgram program, PdfFont boldFont, 
                                 PdfFont normalFont, DeviceRgb wardColor, DeviceRgb agendaColor) {
        // Create table with 2 columns
        Table table = new Table(UnitValue.createPercentArray(new float[]{40, 60}))
                .setWidth(UnitValue.createPercentValue(100));
        
        // Add rows
        addTableRow(table, "Opening Prayer:", program.getOpeningPrayer(), boldFont, normalFont, wardColor);
        addTableRow(table, "Handbook reading scriptural thought", program.getHandbookReading(), boldFont, normalFont, wardColor);
        addTableRow(table, "Auxiliary", program.getAuxiliary(), boldFont, normalFont, wardColor);
        addTableRow(table, "Agenda", program.getAgenda(), boldFont, normalFont, wardColor);
        addTableRow(table, "Welfare", program.getWelfare(), boldFont, normalFont, wardColor);
        addTableRow(table, "Closing Prayer:", program.getClosingPrayer(), boldFont, normalFont, wardColor);
        
        document.add(table);
    }
    
    private void addTableRow(Table table, String label, String content, PdfFont boldFont, 
                           PdfFont normalFont, DeviceRgb wardColor) {
        DeviceRgb lightGray = new DeviceRgb(240, 240, 240);
        DeviceRgb contentColor = new DeviceRgb(74, 74, 74);
        
        // Label cell
        Cell labelCell = new Cell()
                .add(new Paragraph()
                        .add(new Text(label)
                                .setFont(boldFont)
                                .setFontSize(10)
                                .setFontColor(wardColor)))
                .setBackgroundColor(lightGray)
                .setPadding(10);
        
        // Content cell
        Cell contentCell = new Cell()
                .add(new Paragraph()
                        .add(new Text(content != null ? content : "")
                                .setFont(normalFont)
                                .setFontSize(10)
                                .setFontColor(contentColor)))
                .setPadding(10);
        
        table.addCell(labelCell);
        table.addCell(contentCell);
    }
}