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

import com.church.programgenerator.model.WardCouncilProgram;

@Service
public class WardCouncilDocumentService {

    public byte[] generateDocument(WardCouncilProgram program) throws IOException {
        XWPFDocument document = new XWPFDocument();
        
        // Create title section
        createTitle(document, program);
        
        // Create agenda table
        createAgendaTable(document, program);
        
        // Convert to byte array
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        document.write(out);
        document.close();
        
        return out.toByteArray();
    }
    
    public void saveDocument(WardCouncilProgram program, String filePath) throws IOException {
        byte[] documentBytes = generateDocument(program);
        
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(documentBytes);
        }
    }
    
    private void createTitle(XWPFDocument document, WardCouncilProgram program) {
        // Ward name
        XWPFParagraph wardPara = document.createParagraph();
        wardPara.setAlignment(ParagraphAlignment.CENTER);
        wardPara.setSpacingAfter(0);
        XWPFRun wardRun = wardPara.createRun();
        wardRun.setText(program.getWardName().toUpperCase());
        wardRun.setFontSize(18);
        wardRun.setBold(true);
        wardRun.setColor("8B7355");
        
        // Ward Council title
        XWPFParagraph titlePara = document.createParagraph();
        titlePara.setAlignment(ParagraphAlignment.CENTER);
        titlePara.setSpacingAfter(0);
        XWPFRun titleRun = titlePara.createRun();
        titleRun.setText("WARD COUNCIL");
        titleRun.setFontSize(20);
        titleRun.setBold(true);
        titleRun.setColor("5A8AAA");
        
        // Agenda and date
        XWPFParagraph agendaDatePara = document.createParagraph();
        agendaDatePara.setAlignment(ParagraphAlignment.CENTER);
        agendaDatePara.setSpacingAfter(200);
        
        XWPFRun agendaRun = agendaDatePara.createRun();
        agendaRun.setText("Agenda");
        agendaRun.setFontSize(14);
        agendaRun.setBold(true);
        agendaRun.setColor("D2691E");
        
        XWPFRun spaceRun = agendaDatePara.createRun();
        spaceRun.addTab();
        spaceRun.addTab();
        spaceRun.addTab();
        spaceRun.addTab();
        
        XWPFRun dateRun = agendaDatePara.createRun();
        String formattedDate = program.getMeetingDate().format(DateTimeFormatter.ofPattern("MM-dd-yy"));
        dateRun.setText(formattedDate);
        dateRun.setFontSize(12);
        dateRun.setBold(true);
        dateRun.setColor("D2691E");
    }
    
    private void createAgendaTable(XWPFDocument document, WardCouncilProgram program) {
        // Create table
        XWPFTable table = document.createTable(6, 2);
        table.setWidth("100%");
        
        // Set table borders
        table.getCTTbl().getTblPr().addNewTblBorders();
        
        // Row 1: Opening Prayer
        setTableRow(table, 0, "Opening Prayer:", program.getOpeningPrayer());
        
        // Row 2: Handbook reading
        setTableRow(table, 1, "Handbook reading scriptural thought", program.getHandbookReading());
        
        // Row 3: Auxiliary
        setTableRow(table, 2, "Auxiliary", program.getAuxiliary());
        
        // Row 4: Agenda
        setTableRow(table, 3, "Agenda", program.getAgenda());
        
        // Row 5: Welfare
        setTableRow(table, 4, "Welfare", program.getWelfare());
        
        // Row 6: Closing Prayer
        setTableRow(table, 5, "Closing Prayer:", program.getClosingPrayer());
    }
    
    private void setTableRow(XWPFTable table, int rowIndex, String label, String content) {
        XWPFTableRow row = table.getRow(rowIndex);
        
        // Set label cell
        XWPFTableCell labelCell = row.getCell(0);
        labelCell.removeParagraph(0);
        XWPFParagraph labelPara = labelCell.addParagraph();
        XWPFRun labelRun = labelPara.createRun();
        labelRun.setText(label);
        labelRun.setBold(true);
        labelRun.setFontSize(10);
        labelRun.setColor("8B7355");
        
        // Set content cell
        XWPFTableCell contentCell = row.getCell(1);
        contentCell.removeParagraph(0);
        XWPFParagraph contentPara = contentCell.addParagraph();
        XWPFRun contentRun = contentPara.createRun();
        contentRun.setText(content != null ? content : "");
        contentRun.setFontSize(10);
        contentRun.setColor("4A4A4A");
        
        // Set cell width
        labelCell.getCTTc().addNewTcPr().addNewTcW().setW(java.math.BigInteger.valueOf(3000));
        contentCell.getCTTc().addNewTcPr().addNewTcW().setW(java.math.BigInteger.valueOf(6000));
    }
}