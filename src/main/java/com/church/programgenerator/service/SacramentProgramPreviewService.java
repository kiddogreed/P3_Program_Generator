package com.church.programgenerator.service;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.church.programgenerator.model.SacramentProgram;
import com.church.programgenerator.model.Speaker;

@Service
public class SacramentProgramPreviewService {

    public String generateHtmlPreview(SacramentProgram program) {
        StringBuilder html = new StringBuilder();
        
        html.append("<div class='program-preview'>");
        
        // Add church header
        addChurchHeader(html, program);
        
        // Add program details
        addProgramDetails(html, program);
        
        // Add music section
        addMusicSection(html, program);
        
        // Add program flow
        addProgramFlow(html, program);
        
        // Add speakers section
        addSpeakersSection(html, program);
        
        // Add closing elements
        addClosingElements(html, program);
        
        html.append("</div>");
        
        return html.toString();
    }
    
    private void addChurchHeader(StringBuilder html, SacramentProgram program) {
        html.append("<div class='church-header'>");
        html.append("<div class='church-logo'>");
        html.append("<img src='/images/LDS_LOGO.png' alt='Church Logo' class='logo-image' onerror='this.style.display=\"none\"; this.nextElementSibling.style.display=\"block\";'>");
        html.append("<div class='logo-fallback' style='display:none;'>");
        html.append("<div class='logo-placeholder'>⛪</div>");
        html.append("<div class='church-name'>THE CHURCH OF<br>JESUS CHRIST<br>OF LATTER-DAY SAINTS</div>");
        html.append("</div>");
        html.append("</div>");
        
        html.append("<h1 class='program-title'>");
        if (program.getStakeName() != null && !program.getStakeName().isEmpty()) {
            html.append(program.getStakeName()).append("<br>");
        }
        if (program.getWardName() != null && !program.getWardName().isEmpty()) {
            html.append(program.getWardName()).append("<br>");
        }
        html.append("Sacrament Program");
        html.append("</h1>");
        html.append("</div>");
    }
    
    private void addProgramDetails(StringBuilder html, SacramentProgram program) {
        html.append("<div class='program-details'>");
        
        html.append("<div class='detail-row'>");
        html.append("<strong>Date: </strong>");
        html.append(program.getDate() != null ? 
            program.getDate().format(DateTimeFormatter.ofPattern("MMMM d, yyyy")) : 
            "_____________");
        html.append("</div>");
        
        html.append("<div class='detail-row'>");
        html.append("<strong>Presiding: </strong>");
        html.append(program.getPresiding() != null ? program.getPresiding() : "_____________");
        html.append("</div>");
        
        html.append("<div class='detail-row'>");
        html.append("<strong>Conducting: </strong>");
        html.append(program.getConducting() != null ? program.getConducting() : "_____________");
        html.append("</div>");
        
        if (program.getAcknowledgement() != null && !program.getAcknowledgement().isEmpty()) {
            html.append("<div class='detail-row'>");
            html.append("<strong>Acknowledgement: </strong>");
            html.append(formatMultilineText(program.getAcknowledgement()));
            html.append("</div>");
        }
        
        if (program.getAnnouncements() != null && !program.getAnnouncements().isEmpty()) {
            html.append("<div class='detail-row'>");
            html.append("<strong>Announcements:</strong>");
            html.append("<ol>");
            for (String announcement : program.getAnnouncements()) {
                html.append("<li>").append(announcement).append("</li>");
            }
            html.append("</ol>");
            html.append("</div>");
        }
        
        html.append("</div>");
    }
    
    private void addMusicSection(StringBuilder html, SacramentProgram program) {
        html.append("<div class='music-section'>");
        html.append("<div class='music-row'>");
        html.append("<span><strong>Chorister: </strong>");
        html.append(program.getChorister() != null ? program.getChorister() : "_____________");
        html.append("</span>");
        html.append("<span style='margin: 0 2rem;'>|</span>");
        html.append("<span><strong>Pianist: </strong>");
        html.append(program.getPianist() != null ? program.getPianist() : "_____________");
        html.append("</span>");
        html.append("</div>");
        html.append("</div>");
    }
    
    private void addProgramFlow(StringBuilder html, SacramentProgram program) {
        html.append("<div class='program-flow'>");
        
        html.append("<div class='program-item'>");
        html.append("<strong>Opening Hymn: </strong>");
        html.append(program.getOpeningHymn() != null ? program.getOpeningHymn() : "_____________");
        html.append("</div>");
        
        html.append("<div class='program-item'>");
        html.append("<strong>Invocation: </strong>");
        html.append(program.getInvocation() != null ? program.getInvocation() : "_____________");
        html.append("</div>");
        
        if (program.getWardBusiness() != null && !program.getWardBusiness().isEmpty()) {
            html.append("<div class='program-item'>");
            html.append("<strong>Ward Business: </strong>");
            html.append(formatMultilineText(program.getWardBusiness()));
            html.append("</div>");
        }
        
        if (program.getStakeBusiness() != null && !program.getStakeBusiness().isEmpty()) {
            html.append("<div class='program-item'>");
            html.append("<strong>Stake Business: </strong>");
            html.append(formatMultilineText(program.getStakeBusiness()));
            html.append("</div>");
        }
        
        html.append("<div class='program-item'>");
        html.append("<strong>Sacrament Hymn: </strong>");
        html.append(program.getSacramentHymn() != null ? program.getSacramentHymn() : "_____________");
        html.append("</div>");
        
        html.append("<div class='sacrament-note'>");
        html.append("<em>Thank you for your reverence during the sacrament, and thank you to the priesthood brethren who bless and passed the bread and water. You may now Join your family.</em>");
        html.append("</div>");
        
        html.append("</div>");
    }
    
    private void addSpeakersSection(StringBuilder html, SacramentProgram program) {
        html.append("<div class='speakers-section'>");
        html.append("<div class='section-title'><strong>Speakers:</strong>");
        
        // Add auxiliary if specified
        if (program.getSpeakersAuxiliary() != null && !program.getSpeakersAuxiliary().isEmpty()) {
            html.append(" <em>(").append(program.getSpeakersAuxiliary()).append(")</em>");
        }
        
        html.append("</div>");
        
        if (program.getSpeakers() != null && !program.getSpeakers().isEmpty()) {
            for (Speaker speaker : program.getSpeakers()) {
                html.append("<div class='speaker-item'>");
                
                String speakerText = getOrdinalNumber(speaker.getOrder()) + " speaker: ";
                
                if (speaker.getTitle() != null && !speaker.getTitle().isEmpty()) {
                    speakerText += speaker.getTitle() + " ";
                }
                
                speakerText += (speaker.getName() != null ? speaker.getName() : "_____________");
                
                html.append(speakerText);
                
                if (speaker.getTopic() != null && !speaker.getTopic().isEmpty()) {
                    html.append(" - <em>").append(speaker.getTopic()).append("</em>");
                }
                
                html.append("</div>");
            }
        }
        
        html.append("</div>");
    }
    
    private void addClosingElements(StringBuilder html, SacramentProgram program) {
        html.append("<div class='closing-elements'>");
        
        html.append("<div class='program-item'>");
        html.append("<strong>Closing Hymn: </strong>");
        html.append(program.getClosingHymn() != null ? program.getClosingHymn() : "_____________");
        html.append("</div>");
        
        html.append("<div class='program-item'>");
        html.append("<strong>Benediction: </strong>");
        html.append(program.getBenediction() != null ? program.getBenediction() : "_____________");
        html.append("</div>");
        
        html.append("<div class='attendance-footer'>");
        html.append("<strong>Sacrament Attendance:________</strong>");
        html.append("</div>");
        
        html.append("</div>");
    }
    
    private String getOrdinalNumber(int number) {
        return switch (number) {
            case 1 -> "1st";
            case 2 -> "2nd";
            case 3 -> "3rd";
            case 4 -> "4th";
            default -> number + "th";
        };
    }
    
    private String formatMultilineText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }
        // Replace line breaks with HTML br tags and escape HTML entities
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\r\n", "<br>")
                  .replace("\n", "<br>")
                  .replace("\r", "<br>");
    }
}