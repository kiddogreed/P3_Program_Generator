package com.church.programgenerator.service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.church.programgenerator.model.BishopricProgram;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;

@Service
public class BishopricProgramPdfService {

    private static final DeviceRgb BG_COLOR = new DeviceRgb(0xED, 0xE9, 0xF5);
    private static final DeviceRgb NAVY     = new DeviceRgb(0x1A, 0x2E, 0x5A);

    public byte[] generatePdf(BishopricProgram program) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PageSize pageSize = PageSize.A4;
        float W = pageSize.getWidth();   // 595.28
        float H = pageSize.getHeight();  // 841.89

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(baos));
        pdfDoc.addNewPage(pageSize);

        // Draw decorative background and border frame first (acts as background layer)
        PdfCanvas pdfCanvas = new PdfCanvas(pdfDoc.getPage(1));
        drawBackground(pdfCanvas, W, H);
        drawBorderFrame(pdfCanvas, W, H);
        pdfCanvas.release();

        // Layout document
        float margin = 60f;
        Document document = new Document(pdfDoc, pageSize, false);
        document.setMargins(margin, margin, margin, margin);

        PdfFont bold   = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
        PdfFont normal = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);

        float s = computeScale(program);

        // ── Logo ──────────────────────────────────────────────────────────
        addLogo(document, s);

        // ── Church name ───────────────────────────────────────────────────
        document.add(new Paragraph("THE CHURCH OF JESUS CHRIST OF LATTER-DAY SAINTS")
                .setFont(normal).setFontSize(sc(8f, s)).setFontColor(NAVY)
                .setCharacterSpacing(1.5f)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(sc(10f, s)));

        // ── Ward name ─────────────────────────────────────────────────────
        String ward = program.getWardName() != null ? program.getWardName().toUpperCase() : "WARD";
        document.add(new Paragraph(ward + " WARD")
                .setFont(bold).setFontSize(sc(24f, s)).setFontColor(NAVY)
                .setCharacterSpacing(2.5f)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(sc(12f, s)));

        // ── BISHOPRIC MEETING ─────────────────────────────────────────────
        document.add(new Paragraph("BISHOPRIC MEETING")
                .setFont(bold).setFontSize(sc(14f, s)).setFontColor(NAVY)
                .setCharacterSpacing(2.5f)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(sc(6f, s)));

        // ── Date ──────────────────────────────────────────────────────────
        if (program.getMeetingDate() != null) {
            String date = program.getMeetingDate()
                    .format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")).toUpperCase();
            document.add(new Paragraph(date)
                    .setFont(normal).setFontSize(sc(10f, s)).setFontColor(NAVY)
                    .setCharacterSpacing(1.5f)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(sc(28f, s)));
        }

        // ── Divider ───────────────────────────────────────────────────────
        SolidLine sl = new SolidLine(0.5f);
        sl.setColor(NAVY);
        document.add(new LineSeparator(sl).setMarginBottom(sc(28f, s)));

        // ── Meeting details ───────────────────────────────────────────────
        float detailSize = sc(13f, s);
        float detailGap  = sc(22f, s);
        if (notEmpty(program.getPresiding()))
            document.add(detail("Presiding", program.getPresiding(), bold, normal, detailSize, detailGap));
        if (notEmpty(program.getConducting()))
            document.add(detail("Conducting", program.getConducting(), bold, normal, detailSize, detailGap));
        if (notEmpty(program.getOpeningPrayer()))
            document.add(detail("Opening Prayer", program.getOpeningPrayer(), bold, normal, detailSize, detailGap));
        if (notEmpty(program.getHandbookSpiritual()))
            document.add(detail("Handbook Spiritual", program.getHandbookSpiritual(), bold, normal, detailSize, detailGap));

        // ── Agenda ────────────────────────────────────────────────────────
        boolean hasAgendaItems = program.getAgendaItems() != null && !program.getAgendaItems().isEmpty();
        boolean hasCallings    = notEmpty(program.getCallingsAndReleases());
        if (hasAgendaItems || hasCallings) {
            document.add(new Paragraph()
                    .add(new Text("• ").setFont(bold).setFontSize(sc(16f, s)).setFontColor(NAVY))
                    .add(new Text("AGENDA").setFont(bold).setFontSize(sc(13f, s)).setFontColor(NAVY))
                    .setCharacterSpacing(2f)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(sc(36f, s))
                    .setMarginBottom(sc(14f, s)));

            if (hasAgendaItems) {
                for (String item : program.getAgendaItems()) {
                    if (item == null || item.trim().isEmpty()) continue;
                    document.add(new Paragraph(item.trim())
                            .setFont(normal).setFontSize(sc(12f, s)).setFontColor(NAVY)
                            .setCharacterSpacing(1f)
                            .setTextAlignment(TextAlignment.CENTER)
                            .setMarginBottom(sc(14f, s)));
                }
            }
            if (hasCallings) {
                document.add(new Paragraph(program.getCallingsAndReleases())
                        .setFont(normal).setFontSize(sc(12f, s)).setFontColor(NAVY)
                        .setCharacterSpacing(1f)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginBottom(sc(14f, s)));
            }
        }

        // ── Closing prayer ────────────────────────────────────────────────
        if (notEmpty(program.getClosingPrayer())) {
            document.add(new Paragraph().setMarginTop(sc(36f, s)));
            document.add(detail("Closing Prayer", program.getClosingPrayer(), bold, normal, detailSize, sc(8f, s)));
        }

        document.close();
        return baos.toByteArray();
    }

    public void savePdf(BishopricProgram program, String filePath) throws IOException {
        byte[] pdfBytes = generatePdf(program);
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(pdfBytes);
        }
    }

    // ─── content helpers ─────────────────────────────────────────────────

    private void addLogo(Document doc, float scale) {
        try (InputStream is = getClass().getResourceAsStream("/static/images/LDS_LOGO.png")) {
            if (is != null) {
                Image img = new Image(ImageDataFactory.create(is.readAllBytes()))
                        .setWidth(sc(78f, scale)).setHeight(sc(78f, scale))
                        .setHorizontalAlignment(HorizontalAlignment.CENTER);
                doc.add(img);
            }
        } catch (Exception ignored) {}
    }

    private Paragraph detail(String label, String value, PdfFont bold, PdfFont normal,
                             float fontSize, float marginBottom) {
        return new Paragraph()
                .add(new Text(label + ": ").setFont(bold).setFontSize(fontSize).setFontColor(NAVY))
                .add(new Text(value).setFont(normal).setFontSize(fontSize).setFontColor(NAVY))
                .setCharacterSpacing(1f)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(marginBottom);
    }

    private boolean notEmpty(String s) { return s != null && !s.trim().isEmpty(); }

    private float sc(float base, float scale) { return base * scale; }

    /**
     * Scale factor 0.70–1.0 based on total variable-length content.
     * Heavy agendas/callings shrink all spacing and font sizes proportionally.
     */
    private float computeScale(BishopricProgram program) {
        int total = 0;
        if (program.getHandbookSpiritual() != null)  total += program.getHandbookSpiritual().length() * 2;
        if (program.getCallingsAndReleases() != null) total += program.getCallingsAndReleases().length() * 2;
        if (program.getAgendaItems() != null)
            total += program.getAgendaItems().size() * 50
                   + program.getAgendaItems().stream().mapToInt(String::length).sum();
        if (total > 700) return 0.72f;
        if (total > 500) return 0.80f;
        if (total > 300) return 0.88f;
        return 1.0f;
    }

    // ─── drawing helpers ─────────────────────────────────────────────────

    private void drawBackground(PdfCanvas canvas, float W, float H) {
        canvas.setFillColor(BG_COLOR);
        canvas.rectangle(0, 0, W, H);
        canvas.fill();
    }

    private void drawBorderFrame(PdfCanvas canvas, float W, float H) {
        float outer = 18f;
        float inner = 30f;
        float d     = outer + 22f; // corner ornament centre distance from page edge

        canvas.setStrokeColor(NAVY);
        canvas.setFillColor(NAVY);

        // Outer border (2pt)
        canvas.setLineWidth(2f);
        canvas.rectangle(outer, outer, W - 2 * outer, H - 2 * outer);
        canvas.stroke();

        // Inner border (0.75pt)
        canvas.setLineWidth(0.75f);
        canvas.rectangle(inner, inner, W - 2 * inner, H - 2 * inner);
        canvas.stroke();

        // Corner ornaments — (cx, cy, hx, vy)
        // hx: +1=right / -1=left along horizontal border edge
        // vy: -1=down  / +1=up   along vertical border edge (PDF y-up coords)
        drawCornerOrnament(canvas, d,     H - d,  1f, -1f);  // top-left
        drawCornerOrnament(canvas, W - d, H - d, -1f, -1f);  // top-right
        drawCornerOrnament(canvas, d,     d,      1f,  1f);  // bottom-left
        drawCornerOrnament(canvas, W - d, d,     -1f,  1f);  // bottom-right
    }

    /**
     * Draws a scroll ornament at corner (cx, cy).
     * hx = horizontal inward direction (+1 right, -1 left)
     * vy = vertical inward direction (-1 down from top, +1 up from bottom)
     */
    private void drawCornerOrnament(PdfCanvas canvas, float cx, float cy, float hx, float vy) {
        canvas.saveState();
        canvas.setStrokeColor(NAVY);
        canvas.setFillColor(NAVY);
        canvas.setLineWidth(1.5f);
        canvas.setLineCapStyle(1);   // round
        canvas.setLineJoinStyle(1);  // round

        // Central dot
        canvas.circle(cx, cy, 4f);
        canvas.fill();

        // Horizontal C-scroll (along horizontal border, curling inward)
        canvas.moveTo(cx + hx * 7,  cy);
        canvas.curveTo(cx + hx * 18, cy + vy * 6,
                       cx + hx * 22, cy + vy * 16,
                       cx + hx * 14, cy + vy * 16);
        canvas.curveTo(cx + hx * 7,  cy + vy * 16,
                       cx + hx * 7,  cy + vy * 8,
                       cx + hx * 13, cy + vy * 9);
        canvas.stroke();

        // Vertical C-scroll (along vertical border, curling inward)
        canvas.moveTo(cx,            cy + vy * 7);
        canvas.curveTo(cx + hx * 6,  cy + vy * 18,
                       cx + hx * 16, cy + vy * 22,
                       cx + hx * 16, cy + vy * 14);
        canvas.curveTo(cx + hx * 16, cy + vy * 7,
                       cx + hx * 8,  cy + vy * 7,
                       cx + hx * 9,  cy + vy * 13);
        canvas.stroke();

        // Tip dots at scroll ends
        canvas.circle(cx + hx * 13, cy + vy * 12, 2.5f);
        canvas.fill();
        canvas.circle(cx + hx * 12, cy + vy * 13, 2.5f);
        canvas.fill();

        canvas.restoreState();
    }
}
