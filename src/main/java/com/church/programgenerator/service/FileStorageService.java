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

    public String saveDocxFile(SacramentProgram program) throws IOException {
        // Ensure reports directory exists
        createReportsDirectory();
        
        // Generate document
        byte[] documentBytes = documentService.generateSacramentProgram(program);
        
        // Create filename
        String filename = generateFilename(program, ".docx");
        String filePath = REPORTS_DIR + "/" + filename;
        
        // Save file
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(documentBytes);
        }
        
        return filePath;
    }

    public String savePdfFile(SacramentProgram program) throws IOException {
        // Ensure reports directory exists
        createReportsDirectory();
        
        // Generate PDF
        byte[] pdfBytes = generatePdfDocument(program);
        
        // Create filename
        String filename = generateFilename(program, ".pdf");
        String filePath = REPORTS_DIR + "/" + filename;
        
        // Save file
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
            
            // Set tight margins to fit more content
            document.setMargins(20, 25, 20, 25);
            
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
                        .setWidth(60)
                        .setHeight(60)
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
                .setFontSize(8);
        document.add(title);
    }

    private void addPdfProgramDetails(Document document, SacramentProgram program) {
        // Date
        document.add(new Paragraph()
                .add(new Text("Date: ").setBold())
                .add(program.getDate() != null ? 
                    program.getDate().format(DateTimeFormatter.ofPattern("MMMM d, yyyy")) : 
                    "_____________")
                .setFontSize(7));
        
        // Presiding
        document.add(new Paragraph()
                .add(new Text("Presiding: ").setBold())
                .add(program.getPresiding() != null ? program.getPresiding() : "_____________")
                .setFontSize(7));
        
        // Conducting
        document.add(new Paragraph()
                .add(new Text("Conducting: ").setBold())
                .add(program.getConducting() != null ? program.getConducting() : "_____________")
                .setFontSize(7));
        
        // Acknowledgement
        if (program.getAcknowledgement() != null && !program.getAcknowledgement().isEmpty()) {
            document.add(createMultilineParagraph("Acknowledgement: ", program.getAcknowledgement()));
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
                .setFontSize(7);
        
        document.add(musicParagraph);
    }

    private void addPdfProgramFlow(Document document, SacramentProgram program) {
        // Opening Hymn
        document.add(new Paragraph()
                .add(new Text("Opening Hymn: ").setBold())
                .add(program.getOpeningHymn() != null ? program.getOpeningHymn() : "_____________")
                .setFontSize(7));
        
        // Invocation
        document.add(new Paragraph()
                .add(new Text("Invocation: ").setBold())
                .add(program.getInvocation() != null ? program.getInvocation() : "_____________")
                .setFontSize(7));
        
        // Ward Business
        if (program.getWardBusiness() != null && !program.getWardBusiness().isEmpty()) {
            document.add(createMultilineParagraph("Ward Business: ", program.getWardBusiness()));
        }
        
        // Stake Business
        if (program.getStakeBusiness() != null && !program.getStakeBusiness().isEmpty()) {
            document.add(createMultilineParagraph("Stake Business: ", program.getStakeBusiness()));
        }
        
        // Sacrament Hymn
        document.add(new Paragraph()
                .add(new Text("Sacrament Hymn: ").setBold())
                .add(program.getSacramentHymn() != null ? program.getSacramentHymn() : "_____________")
                .setFontSize(7));
        
        // Sacrament note
        document.add(new Paragraph("Thank you for your reverence during the sacrament, and thank you to the priesthood brethren who bless and passed the bread and water. You may now Join your family.")
                .setItalic()
                .setFontSize(6));
    }

    private void addPdfSpeakers(Document document, SacramentProgram program) {
        // Create speakers header with auxiliary
        Paragraph speakersHeader = new Paragraph();
        speakersHeader.add(new Text("Speakers: ").setBold().setFontSize(7));
        
        if (program.getSpeakersAuxiliary() != null && !program.getSpeakersAuxiliary().isEmpty()) {
            speakersHeader.add(new Text("(" + program.getSpeakersAuxiliary() + ")").setFontSize(6).setItalic());
        }
        
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
                
                document.add(new Paragraph(speakerText.toString()).setFontSize(7));
            }
        }
    }

    private void addPdfClosing(Document document, SacramentProgram program) {
        // Closing Hymn
        document.add(new Paragraph()
                .add(new Text("Closing Hymn: ").setBold())
                .add(program.getClosingHymn() != null ? program.getClosingHymn() : "_____________")
                .setFontSize(7));
        
        // Benediction
        document.add(new Paragraph()
                .add(new Text("Benediction: ").setBold())
                .add(program.getBenediction() != null ? program.getBenediction() : "_____________")
                .setFontSize(7));
        
        // Attendance footer
        document.add(new Paragraph("Sacrament Attendance:________")
                .setBold()
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(6));
    }

    private void createReportsDirectory() throws IOException {
        Path reportsPath = Paths.get(REPORTS_DIR);
        if (!Files.exists(reportsPath)) {
            Files.createDirectories(reportsPath);
        }
    }

    private String generateFilename(SacramentProgram program, String extension) {
        return "sacrament" + 
                (program.getDate() != null ? 
                    program.getDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")) : 
                    LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))) + 
                extension;
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
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Text(label).setBold());
        
        if (content != null && !content.trim().isEmpty()) {
            String[] lines = content.split("\\r?\\n");
            for (int i = 0; i < lines.length; i++) {
                if (i > 0) {
                    paragraph.add("\n");
                }
                paragraph.add(lines[i]);
            }
        }
        return paragraph.setFontSize(7);
    }
}