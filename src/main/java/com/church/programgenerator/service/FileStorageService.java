package com.church.programgenerator.service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.church.programgenerator.model.SacramentProgram;
import com.church.programgenerator.model.Speaker;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;

@Service
public class FileStorageService {

    @Autowired
    private SacramentProgramDocumentService documentService;

    private static final String REPORTS_DIR = "src/reports";
    
    // Organized subdirectories for different meeting types
    private static final String SACRAMENT_DIR = REPORTS_DIR + "/sacrament";
    private static final String BISHOPRIC_DIR = REPORTS_DIR + "/bishopric";
    private static final String WARDCOUNCIL_DIR = REPORTS_DIR + "/wardcouncil";

    public String saveDocxFile(SacramentProgram program) throws IOException {
        // Ensure sacrament reports directory exists
        createSacramentDirectory();
        
        // Generate document
        byte[] documentBytes = documentService.generateSacramentProgram(program);
        
        // Create filename
        String filename = generateFilename(program, ".docx");
        String filePath = SACRAMENT_DIR + "/" + filename;
        
        // Save file
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(documentBytes);
        }
        
        return filePath;
    }

    public String savePdfFile(SacramentProgram program) throws IOException {
        // Ensure sacrament reports directory exists
        createSacramentDirectory();
        
        // Generate PDF
        byte[] pdfBytes = generatePdfDocument(program);
        
        // Create filename
        String filename = generateFilename(program, ".pdf");
        String filePath = SACRAMENT_DIR + "/" + filename;
        
        // Save file
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(pdfBytes);
        }
        
        return filePath;
    }
    
    // Generic file saving methods for other meeting types
    public String saveDocxFile(String meetingType, String filename, byte[] documentBytes) throws IOException {
        String directoryPath = getDirectoryForMeetingType(meetingType);
        createDirectoryIfNotExists(directoryPath);
        
        String filePath = directoryPath + "/" + filename;
        
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(documentBytes);
        }
        
        return filePath;
    }
    
    public String savePdfFile(String meetingType, String filename, byte[] pdfBytes) throws IOException {
        String directoryPath = getDirectoryForMeetingType(meetingType);
        createDirectoryIfNotExists(directoryPath);
        
        String filePath = directoryPath + "/" + filename;
        
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(pdfBytes);
        }
        
        return filePath;
    }

    public byte[] getDocxBytes(SacramentProgram program) throws IOException {
        return documentService.generateSacramentProgram(program);
    }

    public byte[] getPdfBytes(SacramentProgram program) throws IOException {
        return generatePdfDocument(program);
    }

    private byte[] generatePdfDocument(SacramentProgram program) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try (PdfWriter writer = new PdfWriter(outputStream);
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document document = new Document(pdfDoc)) {
            
            // Set tight margins: top, right, bottom, left (in points)
            document.setMargins(24, 40, 24, 40);
            
            // Add church header
            addPdfHeader(document, program);
            
            // Add program details
            addPdfProgramDetails(document, program);
            
            // Add music section
            addPdfMusicSection(document, program);
            
            // Add program flow
            addPdfProgramFlow(document, program);
            
            // Add speakers
            addPdfSpeakers(document, program);
            
            // Add closing
            addPdfClosing(document, program);
        }
        
        return outputStream.toByteArray();
    }

    private void addPdfHeader(Document document, SacramentProgram program) {
        // Try to add LDS logo
        try (InputStream logoStream = getClass().getResourceAsStream("/static/images/LDS_LOGO.png")) {
            if (logoStream != null) {
                byte[] logoBytes = logoStream.readAllBytes();
                Image logo = new Image(ImageDataFactory.create(logoBytes))
                        .setWidth(50)
                        .setHeight(50)
                        .setHorizontalAlignment(HorizontalAlignment.CENTER);
                document.add(logo);
            } else {
                // Fallback church name if logo not available
                Paragraph churchName = new Paragraph("THE CHURCH OF JESUS CHRIST OF LATTER-DAY SAINTS")
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBold()
                        .setFontSize(9);
                document.add(churchName);
            }
        } catch (Exception e) {
            // Fallback church name
            Paragraph churchName = new Paragraph("THE CHURCH OF JESUS CHRIST OF LATTER-DAY SAINTS")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(9);
            document.add(churchName);
        }
        
        // Program title
        StringBuilder titleBuilder = new StringBuilder();
        if (program.getStakeName() != null && !program.getStakeName().isEmpty()) {
            titleBuilder.append(program.getStakeName()).append("\n");
        }
        if (program.getWardName() != null && !program.getWardName().isEmpty()) {
            titleBuilder.append(program.getWardName()).append("\n");
        }
        titleBuilder.append("Sacrament Program");
        
        Paragraph title = new Paragraph(titleBuilder.toString())
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFontColor(new DeviceRgb(0x2c, 0x52, 0x82))
                .setFontSize(10)
                .setMarginBottom(4);
        document.add(title);
    }

    private void addPdfProgramDetails(Document document, SacramentProgram program) {
        float varFont = computePdfAdaptiveFontSize(program);
        // Date
        document.add(new Paragraph()
                .add(new Text("Date: ").setBold())
                .add(program.getDate() != null ?
                    program.getDate().format(DateTimeFormatter.ofPattern("MMMM d, yyyy")) :
                    "_____________")
                .setFontSize(9).setMarginBottom(1));

        // Presiding
        document.add(new Paragraph()
                .add(new Text("Presiding: ").setBold())
                .add(program.getPresiding() != null ? program.getPresiding() : "_____________")
                .setFontSize(9).setMarginBottom(1));

        // Conducting
        document.add(new Paragraph()
                .add(new Text("Conducting: ").setBold())
                .add(program.getConducting() != null ? program.getConducting() : "_____________")
                .setFontSize(9).setMarginBottom(1));

        // Acknowledgement
        if (program.getAcknowledgement() != null && !program.getAcknowledgement().isEmpty()) {
            document.add(createMultilineParagraph("Acknowledgement: ", program.getAcknowledgement(), varFont));
        }

        // Announcements
        if (program.getAnnouncements() != null && !program.getAnnouncements().isEmpty()) {
            Paragraph announcementsHeader = new Paragraph()
                    .add(new Text("Announcements:").setBold())
                    .setFontSize(9).setMarginBottom(1);
            document.add(announcementsHeader);
            for (int i = 0; i < program.getAnnouncements().size(); i++) {
                document.add(new Paragraph((i + 1) + ". " + program.getAnnouncements().get(i))
                        .setFontSize(varFont).setMarginBottom(1));
            }
        }
    }

    private void addPdfMusicSection(Document document, SacramentProgram program) {
        // Music section on same line
        Paragraph musicParagraph = new Paragraph()
                .add(new Text("Chorister: ").setBold())
                .add(program.getChorister() != null ? program.getChorister() : "_____________")
                .add("     |     ")
                .add(new Text("Pianist: ").setBold())
                .add(program.getPianist() != null ? program.getPianist() : "_____________")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(9)
                .setMarginTop(2)
                .setMarginBottom(2);
        
        document.add(musicParagraph);
    }

    private void addPdfProgramFlow(Document document, SacramentProgram program) {
        float varFont = computePdfAdaptiveFontSize(program);
        // Opening Hymn
        document.add(new Paragraph()
                .add(new Text("Opening Hymn: ").setBold())
                .add(program.getOpeningHymn() != null ? program.getOpeningHymn() : "_____________")
                .setFontSize(9).setMarginBottom(1));

        // Invocation
        document.add(new Paragraph()
                .add(new Text("Invocation: ").setBold())
                .add(program.getInvocation() != null ? program.getInvocation() : "_____________")
                .setFontSize(9).setMarginBottom(1));

        // Ward Business
        if (program.getWardBusiness() != null && !program.getWardBusiness().isEmpty()) {
            document.add(createMultilineParagraph("Ward Business: ", program.getWardBusiness(), varFont));
        }

        // Stake Business
        if (program.getStakeBusiness() != null && !program.getStakeBusiness().isEmpty()) {
            document.add(createMultilineParagraph("Stake Business: ", program.getStakeBusiness(), varFont));
        }

        // Sacrament Hymn
        document.add(new Paragraph()
                .add(new Text("Sacrament Hymn: ").setBold())
                .add(program.getSacramentHymn() != null ? program.getSacramentHymn() : "_____________")
                .setFontSize(9).setMarginBottom(1));

        // Sacrament note
        document.add(new Paragraph("Thank you for your reverence during the sacrament, and thank you to the priesthood brethren who bless and passed the bread and water. You may now Join your family.")
                .setItalic()
                .setFontSize(8)
                .setMarginTop(2)
                .setMarginBottom(2));
    }

    private void addPdfSpeakers(Document document, SacramentProgram program) {
        float varFont = computePdfAdaptiveFontSize(program);
        // Create speakers header with auxiliary
        Paragraph speakersHeader = new Paragraph();
        speakersHeader.add(new Text("Speakers: ").setBold().setFontSize(9));

        if (program.getSpeakersAuxiliary() != null && !program.getSpeakersAuxiliary().isEmpty()) {
            speakersHeader.add(new Text(program.getSpeakersAuxiliary()).setFontSize(9).setBold());
        }
        speakersHeader.setMarginTop(2);
        document.add(speakersHeader);
        
        if (program.getSpeakers() != null && !program.getSpeakers().isEmpty()) {
            for (Speaker speaker : program.getSpeakers()) {
                StringBuilder speakerText = new StringBuilder();
                speakerText.append(getOrdinalNumber(speaker.getOrder())).append(" speaker: ");
                
                if (speaker.getTitle() != null && !speaker.getTitle().isEmpty()) {
                    speakerText.append(speaker.getTitle()).append(" ");
                }
                
                speakerText.append(speaker.getName() != null ? speaker.getName() : "_____________");
                
                if (speaker.getTopic() != null && !speaker.getTopic().isEmpty()) {
                    speakerText.append(" - ").append(speaker.getTopic());
                }
                
                document.add(new Paragraph(speakerText.toString()).setFontSize(varFont).setMarginBottom(2));
            }
        }
    }

    private void addPdfClosing(Document document, SacramentProgram program) {
        // Closing Hymn
        document.add(new Paragraph()
                .add(new Text("Closing Hymn: ").setBold())
                .add(program.getClosingHymn() != null ? program.getClosingHymn() : "_____________")
                .setFontSize(9).setMarginTop(2).setMarginBottom(1));

        // Benediction
        document.add(new Paragraph()
                .add(new Text("Benediction: ").setBold())
                .add(program.getBenediction() != null ? program.getBenediction() : "_____________")
                .setFontSize(9).setMarginBottom(1));

        // Attendance footer
        document.add(new Paragraph("Sacrament Attendance:________")
                .setBold()
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(9)
                .setMarginTop(4));
    }

    private void createSacramentDirectory() throws IOException {
        Path sacramentPath = Paths.get(SACRAMENT_DIR);
        if (!Files.exists(sacramentPath)) {
            Files.createDirectories(sacramentPath);
        }
    }
    
    private void createDirectoryIfNotExists(String directoryPath) throws IOException {
        Path path = Paths.get(directoryPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }
    
    private String getDirectoryForMeetingType(String meetingType) {
        return switch (meetingType.toLowerCase()) {
            case "sacrament" -> SACRAMENT_DIR;
            case "bishopric" -> BISHOPRIC_DIR;
            case "wardcouncil", "ward-council" -> WARDCOUNCIL_DIR;
            default -> REPORTS_DIR; // fallback to main reports directory
        };
    }

    private String generateFilename(SacramentProgram program, String extension) {
        String dateStr = program.getDate() != null ? 
            program.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : 
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return "sacramentProgram" + dateStr + extension;
    }
    
    // Generate filename for other meeting types
    public String generateFilename(String meetingType, LocalDate date, String extension) {
        String dateStr = date != null ? 
            date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : 
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return meetingType + "Meeting" + dateStr + extension;
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
    
    private Paragraph createMultilineParagraph(String label, String content) {
        return createMultilineParagraph(label, content, 10f);
    }

    private Paragraph createMultilineParagraph(String label, String content, float contentFontSize) {
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Text(label).setBold().setFontSize(Math.max(contentFontSize, 9f)));
        
        if (content != null && !content.trim().isEmpty()) {
            String[] lines = content.split("\\r?\\n");
            for (int i = 0; i < lines.length; i++) {
                if (i > 0) {
                    paragraph.add(new Text("\n").setFontSize(contentFontSize));
                }
                paragraph.add(new Text(lines[i]).setFontSize(contentFontSize));
            }
        }
        return paragraph.setMarginBottom(2);
    }

    /**
     * Computes adaptive PDF font size based on total variable-length content.
     * Reduces font for dense programs to keep everything on one page.
     */
    private float computePdfAdaptiveFontSize(SacramentProgram program) {
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
        if (total > 800) return 7f;
        if (total > 500) return 8f;
        if (total > 250) return 9f;
        return 10f;
    }
}