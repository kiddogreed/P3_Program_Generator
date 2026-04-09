package com.church.programgenerator.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;

import com.church.programgenerator.model.SacramentProgram;
import com.church.programgenerator.model.Speaker;

@Service
public class SacramentProgramDocumentService {

    public byte[] generateSacramentProgram(SacramentProgram program) throws IOException {
        XWPFDocument document = new XWPFDocument();
        
        try {
            // Add church logo at the top
            addChurchLogo(document);
            
            // Add program header
            addProgramHeader(document, program);
            
            // Add program details
            addProgramDetails(document, program);
            
            // Add music section
            addMusicSection(document, program);
            
            // Add program flow
            addProgramFlow(document, program);
            
            // Add speakers section
            addSpeakersSection(document, program);
            
            // Add closing elements
            addClosingElements(document, program);
            
            // Convert to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.write(outputStream);
            return outputStream.toByteArray();
            
        } finally {
            document.close();
        }
    }
    
    private void addChurchLogo(XWPFDocument document) {
        XWPFParagraph logoParagraph = document.createParagraph();
        logoParagraph.setAlignment(ParagraphAlignment.CENTER);
        
        XWPFRun logoRun = logoParagraph.createRun();
        
        // Try to add LDS logo if available, otherwise add text placeholder
        try (InputStream logoStream = getClass().getResourceAsStream("/static/images/LDS_LOGO.png")) {
            if (logoStream != null) {
                logoRun.addPicture(logoStream, XWPFDocument.PICTURE_TYPE_PNG, "LDS_LOGO.png", 
                    Units.toEMU(80), Units.toEMU(80));
            } else {
                // Fallback text logo
                logoRun.setText("THE CHURCH OF");
                logoRun.addBreak();
                logoRun.setText("JESUS CHRIST");
                logoRun.addBreak();
                logoRun.setText("OF LATTER-DAY SAINTS");
                logoRun.setBold(true);
                logoRun.setFontSize(12);
            }
        } catch (Exception e) {
            // Fallback text logo
            logoRun.setText("THE CHURCH OF JESUS CHRIST OF LATTER-DAY SAINTS");
            logoRun.setBold(true);
            logoRun.setFontSize(12);
        }
        
    }
    
    private void addProgramHeader(XWPFDocument document, SacramentProgram program) {
        // Stake and Ward name
        XWPFParagraph headerParagraph = document.createParagraph();
        headerParagraph.setAlignment(ParagraphAlignment.CENTER);
        
        XWPFRun headerRun = headerParagraph.createRun();
        headerRun.setText(program.getStakeName() != null ? program.getStakeName() : "Stake Name");
        headerRun.addBreak();
        headerRun.setText(program.getWardName() != null ? program.getWardName() : "Ward Name");
        headerRun.addBreak();
        headerRun.setText("Sacrament Program");
        headerRun.setBold(true);
        headerRun.setFontSize(12);
        headerRun.setColor("2c5282");
    }
    
    private void addProgramDetails(XWPFDocument document, SacramentProgram program) {
        int varFont = computeDocxAdaptiveFontSize(program);
        // Date
        XWPFParagraph dateParagraph = document.createParagraph();
        dateParagraph.setSpacingAfter(0); // No line spacing
        XWPFRun dateRun = dateParagraph.createRun();
        dateRun.setText("Date: ");
        dateRun.setBold(true);
        dateRun.setFontSize(11);
        XWPFRun dateValueRun = dateParagraph.createRun();
        dateValueRun.setText(program.getDate() != null ? 
            program.getDate().format(DateTimeFormatter.ofPattern("MMMM d, yyyy")) : 
            "_____________");
        dateValueRun.setFontSize(11);
        
        // Presiding
        XWPFParagraph presidingParagraph = document.createParagraph();
        presidingParagraph.setSpacingAfter(0); // No line spacing
        XWPFRun presidingRun = presidingParagraph.createRun();
        presidingRun.setText("Presiding: ");
        presidingRun.setBold(true);
        presidingRun.setFontSize(11);
        XWPFRun presidingValueRun = presidingParagraph.createRun();
        presidingValueRun.setText(program.getPresiding() != null ? program.getPresiding() : "_____________");
        presidingValueRun.setFontSize(11);
        
        // Conducting
        XWPFParagraph conductingParagraph = document.createParagraph();
        conductingParagraph.setSpacingAfter(0); // No line spacing
        XWPFRun conductingRun = conductingParagraph.createRun();
        conductingRun.setText("Conducting: ");
        conductingRun.setBold(true);
        conductingRun.setFontSize(11);
        XWPFRun conductingValueRun = conductingParagraph.createRun();
        conductingValueRun.setText(program.getConducting() != null ? program.getConducting() : "_____________");
        conductingValueRun.setFontSize(11);
        
        // Acknowledgement
        if (program.getAcknowledgement() != null && !program.getAcknowledgement().isEmpty()) {
            XWPFParagraph ackParagraph = document.createParagraph();
            ackParagraph.setSpacingAfter(0); // No line spacing
            XWPFRun ackRun = ackParagraph.createRun();
            ackRun.setText("Acknowledgement: ");
            ackRun.setBold(true);
            ackRun.setFontSize(11);
            XWPFRun ackValueRun = ackParagraph.createRun();
            ackValueRun.setFontSize(varFont);
            addMultilineText(ackValueRun, program.getAcknowledgement());
            // Blank line for hand-written additions
            XWPFParagraph ackBlankLine = document.createParagraph();
            ackBlankLine.setSpacingAfter(0);
            ackBlankLine.createRun().setFontSize(11);
        }
        
        // Announcements
        if (program.getAnnouncements() != null && !program.getAnnouncements().isEmpty()) {
            XWPFParagraph announcementsParagraph = document.createParagraph();
            announcementsParagraph.setSpacingAfter(0); // No line spacing
            XWPFRun announcementsRun = announcementsParagraph.createRun();
            announcementsRun.setText("Announcements:");
            announcementsRun.setBold(true);
            announcementsRun.setFontSize(11);
            
            for (int i = 0; i < program.getAnnouncements().size(); i++) {
                XWPFParagraph itemParagraph = document.createParagraph();
                itemParagraph.setSpacingAfter(0);
                XWPFRun itemRun = itemParagraph.createRun();
                itemRun.setText((i + 1) + ". " + program.getAnnouncements().get(i));
                itemRun.setFontSize(varFont);
            }
            // Blank line for hand-written additions
            XWPFParagraph annBlankLine = document.createParagraph();
            annBlankLine.setSpacingAfter(0);
            annBlankLine.createRun().setFontSize(11);
        }
    }
    
    private void addMusicSection(XWPFDocument document, SacramentProgram program) {
        // Chorister and Pianist on same line
        XWPFParagraph musicParagraph = document.createParagraph();
        musicParagraph.setAlignment(ParagraphAlignment.CENTER);
        musicParagraph.setSpacingBefore(120);
        musicParagraph.setSpacingAfter(120);
        
        XWPFRun choristerRun = musicParagraph.createRun();
        choristerRun.setText("Chorister: ");
        choristerRun.setBold(true);
        choristerRun.setFontSize(11);
        XWPFRun choristerValueRun = musicParagraph.createRun();
        choristerValueRun.setText(program.getChorister() != null ? program.getChorister() : "_____________");
        choristerValueRun.setFontSize(11);
        
        // Add spacing and pipe separator
        XWPFRun separatorRun = musicParagraph.createRun();
        separatorRun.setText("     |     ");
        separatorRun.setFontSize(11);
        
        XWPFRun pianistRun = musicParagraph.createRun();
        pianistRun.setText("Pianist: ");
        pianistRun.setBold(true);
        pianistRun.setFontSize(11);
        XWPFRun pianistValueRun = musicParagraph.createRun();
        pianistValueRun.setText(program.getPianist() != null ? program.getPianist() : "_____________");
        pianistValueRun.setFontSize(11);
    }
    
    private void addProgramFlow(XWPFDocument document, SacramentProgram program) {
        int varFont = computeDocxAdaptiveFontSize(program);
        // Opening Hymn
        XWPFParagraph openingHymnParagraph = document.createParagraph();
        openingHymnParagraph.setSpacingAfter(0); // No line spacing
        XWPFRun openingHymnRun = openingHymnParagraph.createRun();
        openingHymnRun.setText("Opening Hymn: ");
        openingHymnRun.setBold(true);
        openingHymnRun.setFontSize(11);
        XWPFRun openingHymnValueRun = openingHymnParagraph.createRun();
        openingHymnValueRun.setText(program.getOpeningHymn() != null ? program.getOpeningHymn() : "_____________");
        openingHymnValueRun.setFontSize(11);
        
        // Invocation
        XWPFParagraph invocationParagraph = document.createParagraph();
        invocationParagraph.setSpacingAfter(0); // No line spacing
        XWPFRun invocationRun = invocationParagraph.createRun();
        invocationRun.setText("Invocation: ");
        invocationRun.setBold(true);
        invocationRun.setFontSize(11);
        XWPFRun invocationValueRun = invocationParagraph.createRun();
        invocationValueRun.setText(program.getInvocation() != null ? program.getInvocation() : "_____________");
        invocationValueRun.setFontSize(11);
        
        // Ward Business
        if (program.getWardBusiness() != null && !program.getWardBusiness().isEmpty()) {
            XWPFParagraph wardBusinessParagraph = document.createParagraph();
            wardBusinessParagraph.setSpacingAfter(0); // No line spacing
            XWPFRun wardBusinessRun = wardBusinessParagraph.createRun();
            wardBusinessRun.setText("Ward Business: ");
            wardBusinessRun.setBold(true);
            wardBusinessRun.setFontSize(11);
            XWPFRun wardBusinessValueRun = wardBusinessParagraph.createRun();
            wardBusinessValueRun.setFontSize(varFont);
            addMultilineText(wardBusinessValueRun, program.getWardBusiness());
            // Blank line for hand-written additions
            XWPFParagraph wbBlankLine = document.createParagraph();
            wbBlankLine.setSpacingAfter(0);
            wbBlankLine.createRun().setFontSize(11);
        }
        
        // Stake Business
        if (program.getStakeBusiness() != null && !program.getStakeBusiness().isEmpty()) {
            XWPFParagraph stakeBusinessParagraph = document.createParagraph();
            stakeBusinessParagraph.setSpacingAfter(0); // No line spacing
            XWPFRun stakeBusinessRun = stakeBusinessParagraph.createRun();
            stakeBusinessRun.setText("Stake Business: ");
            stakeBusinessRun.setBold(true);
            stakeBusinessRun.setFontSize(11);
            XWPFRun stakeBusinessValueRun = stakeBusinessParagraph.createRun();
            stakeBusinessValueRun.setFontSize(varFont);
            addMultilineText(stakeBusinessValueRun, program.getStakeBusiness());
        }
        
        // Sacrament Hymn
        XWPFParagraph sacramentHymnParagraph = document.createParagraph();
        sacramentHymnParagraph.setSpacingAfter(0); // No line spacing
        XWPFRun sacramentHymnRun = sacramentHymnParagraph.createRun();
        sacramentHymnRun.setText("Sacrament Hymn: ");
        sacramentHymnRun.setBold(true);
        sacramentHymnRun.setFontSize(11);
        XWPFRun sacramentHymnValueRun = sacramentHymnParagraph.createRun();
        sacramentHymnValueRun.setText(program.getSacramentHymn() != null ? program.getSacramentHymn() : "_____________");
        sacramentHymnValueRun.setFontSize(11);
        
        // Sacrament note
        XWPFParagraph sacramentNoteParagraph = document.createParagraph();
        sacramentNoteParagraph.setSpacingAfter(0); // No line spacing
        XWPFRun sacramentNoteRun = sacramentNoteParagraph.createRun();
        sacramentNoteRun.setText("Thank you for your reverence during the sacrament, and thank you to the priesthood brethren who bless and passed the bread and water. You may now Join your family.");
        sacramentNoteRun.setItalic(true);
        sacramentNoteRun.setFontSize(9);
    }
    
    private void addSpeakersSection(XWPFDocument document, SacramentProgram program) {
        int varFont = computeDocxAdaptiveFontSize(program);
        XWPFParagraph speakerHeaderParagraph = document.createParagraph();
        speakerHeaderParagraph.setSpacingAfter(0); // No line spacing
        XWPFRun speakerHeaderRun = speakerHeaderParagraph.createRun();
        speakerHeaderParagraph.setSpacingBefore(120);
        speakerHeaderRun.setText("Speakers: ");
        speakerHeaderRun.setBold(true);
        speakerHeaderRun.setFontSize(11);
        
        // Add auxiliary if specified
        if (program.getSpeakersAuxiliary() != null && !program.getSpeakersAuxiliary().isEmpty()) {
            XWPFRun auxiliaryRun = speakerHeaderParagraph.createRun();
            auxiliaryRun.setText(program.getSpeakersAuxiliary());
            auxiliaryRun.setFontSize(11);
            auxiliaryRun.setBold(true);
        }
        
        if (program.getSpeakers() != null && !program.getSpeakers().isEmpty()) {
            for (Speaker speaker : program.getSpeakers()) {
                XWPFParagraph speakerParagraph = document.createParagraph();
                speakerParagraph.setSpacingAfter(0); // No line spacing
                XWPFRun speakerRun = speakerParagraph.createRun();
                
                String speakerText = getOrdinalNumber(speaker.getOrder()) + " speaker: " + 
                    (speaker.getName() != null ? speaker.getName() : "_____________");
                
                if (speaker.getTitle() != null && !speaker.getTitle().isEmpty()) {
                    speakerText = getOrdinalNumber(speaker.getOrder()) + " speaker: " + speaker.getTitle() + " " + 
                        (speaker.getName() != null ? speaker.getName() : "_____________");
                }
                
                speakerRun.setText(speakerText);
                speakerRun.setFontSize(varFont);
                
                if (speaker.getTopic() != null && !speaker.getTopic().isEmpty()) {
                    XWPFRun topicRun = speakerParagraph.createRun();
                    topicRun.setText(" - " + speaker.getTopic());
                    topicRun.setItalic(true);
                    topicRun.setFontSize(varFont);
                }
            }
        }
        
        // Add spacing
        document.createParagraph().createRun().addBreak();
    }
    
    private void addClosingElements(XWPFDocument document, SacramentProgram program) {
        // Closing Hymn
        XWPFParagraph closingHymnParagraph = document.createParagraph();
        closingHymnParagraph.setSpacingAfter(0); // No line spacing
        XWPFRun closingHymnRun = closingHymnParagraph.createRun();
        closingHymnRun.setText("Closing Hymn: ");
        closingHymnRun.setBold(true);
        closingHymnRun.setFontSize(11);
        XWPFRun closingHymnValueRun = closingHymnParagraph.createRun();
        closingHymnValueRun.setText(program.getClosingHymn() != null ? program.getClosingHymn() : "_____________");
        closingHymnValueRun.setFontSize(11);
        
        // Benediction
        XWPFParagraph benedictionParagraph = document.createParagraph();
        benedictionParagraph.setSpacingAfter(0);
        XWPFRun benedictionRun = benedictionParagraph.createRun();
        benedictionRun.setText("Benediction: ");
        benedictionRun.setBold(true);
        benedictionRun.setFontSize(11);
        XWPFRun benedictionValueRun = benedictionParagraph.createRun();
        benedictionValueRun.setText(program.getBenediction() != null ? program.getBenediction() : "_____________");
        benedictionValueRun.setFontSize(11);
        
        // Attendance footer
        XWPFParagraph attendanceParagraph = document.createParagraph();
        attendanceParagraph.setAlignment(ParagraphAlignment.RIGHT);
        attendanceParagraph.setSpacingBefore(120);
        XWPFRun attendanceRun = attendanceParagraph.createRun();
        attendanceRun.setText("Sacrament Attendance:________");
        attendanceRun.setBold(true);
        attendanceRun.setFontSize(11);
    }
    
    private String getOrdinalNumber(int number) {
        switch (number) {
            case 1: return "1st";
            case 2: return "2nd";
            case 3: return "3rd";
            case 4: return "4th";
            default: return number + "th";
        }
    }
    
    private void addMultilineText(XWPFRun run, String text) {
        if (text == null || text.trim().isEmpty()) {
            return;
        }
        
        String[] lines = text.split("\\r?\\n");
        for (int i = 0; i < lines.length; i++) {
            if (i > 0) {
                run.addBreak();
            }
            run.setText(lines[i]);
        }
    }

    /**
     * Computes a reduced font size for variable-length content fields
     * based on total character count across all variable fields.
     */
    private int computeDocxAdaptiveFontSize(SacramentProgram program) {
        int total = 0;
        if (program.getAcknowledgement() != null) total += program.getAcknowledgement().length();
        if (program.getAnnouncements() != null)
            total += program.getAnnouncements().stream().mapToInt(String::length).sum();
        if (program.getWardBusiness() != null) total += program.getWardBusiness().length();
        if (program.getStakeBusiness() != null) total += program.getStakeBusiness().length();
        if (program.getSpeakers() != null)
            for (Speaker s : program.getSpeakers()) {
                if (s.getName() != null) total += s.getName().length();
                if (s.getTitle() != null) total += s.getTitle().length();
            }
        if (total > 1000) return 8;
        if (total > 700)  return 9;
        if (total > 450)  return 10;
        return 11;
    }
}