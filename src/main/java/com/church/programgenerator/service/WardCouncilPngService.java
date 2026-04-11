package com.church.programgenerator.service;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.church.programgenerator.model.AgendaItem;
import com.church.programgenerator.model.WardCouncilProgram;

@Service
public class WardCouncilPngService {

    // ── palette (matches reference image) ─────────────────────────────────
    private static final Color BROWN      = new Color(0x8B, 0x73, 0x55);
    private static final Color BLUE       = new Color(0x2C, 0x52, 0x82);
    private static final Color ORANGE     = new Color(0xD2, 0x69, 0x1E);
    private static final Color TABLE_BG   = new Color(0xF4, 0xED, 0xE3);
    private static final Color TABLE_LINE = new Color(0xDD, 0xD0, 0xBB);
    private static final Color TEXT_DARK  = new Color(0x1E, 0x29, 0x3B);
    private static final Color TEXT_GRAY  = new Color(0x4A, 0x55, 0x68);

    // ── page dimensions (portrait, ~A4 feel at 96 dpi) ────────────────────
    private static final int W = 794;
    private static final int H = 1123;

    public byte[] generatePng(WardCouncilProgram program) throws IOException {

        BufferedImage img = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,        RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,   RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,           RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,   RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        // white background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, W, H);

        int margin  = 50;
        int contentW = W - 2 * margin;
        int y = margin;

        // ── LDS logo ──────────────────────────────────────────────────────
        y += 10;
        BufferedImage ldsLogo = loadImage("/static/images/LDS_LOGO_wbg.png");
        if (ldsLogo != null) {
            int logoW = 120;
            int logoH = (int) (ldsLogo.getHeight() * ((double) logoW / ldsLogo.getWidth()));
            g.drawImage(ldsLogo, (W - logoW) / 2, y, logoW, logoH, null);
            y += logoH + 12;
        } else {
            g.setFont(new Font("SansSerif", Font.BOLD, 9));
            g.setColor(BROWN);
            String church = "THE CHURCH OF JESUS CHRIST OF LATTER-DAY SAINTS";
            FontMetrics fm = g.getFontMetrics();
            g.drawString(church, (W - fm.stringWidth(church)) / 2, y + 12);
            y += 24;
        }

        // ── Ward name ─────────────────────────────────────────────────────
        String wardName = program.getWardName() != null ? program.getWardName() : "Pasay 3rd";
        g.setFont(new Font("SansSerif", Font.BOLD, 28));
        g.setColor(BROWN);
        FontMetrics wardFm = g.getFontMetrics();
        g.drawString(wardName, (W - wardFm.stringWidth(wardName)) / 2, y + wardFm.getAscent());
        y += wardFm.getHeight() + 4;

        // ── WARD COUNCIL title ────────────────────────────────────────────
        g.setFont(new Font("SansSerif", Font.BOLD, 30));
        g.setColor(BLUE);
        FontMetrics wcFm = g.getFontMetrics();
        String wcTitle = "WARD COUNCIL";
        g.drawString(wcTitle, (W - wcFm.stringWidth(wcTitle)) / 2, y + wcFm.getAscent());
        y += wcFm.getHeight() + 10;

        // ── horizontal rule ───────────────────────────────────────────────
        g.setColor(BROWN);
        g.setStroke(new BasicStroke(1.5f));
        g.drawLine(margin, y, W - margin, y);
        y += 14;

        // ── Agenda / date bar ─────────────────────────────────────────────
        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        g.setColor(ORANGE);
        FontMetrics barFm = g.getFontMetrics();
        g.drawString("Agenda", margin, y + barFm.getAscent());
        String dateStr = program.getMeetingDate() != null
                ? program.getMeetingDate().format(DateTimeFormatter.ofPattern("MM-dd-yy")) : "";
        g.drawString(dateStr, W - margin - barFm.stringWidth(dateStr), y + barFm.getAscent());
        y += barFm.getHeight() + 6;

        // thin separator
        g.setColor(ORANGE);
        g.setStroke(new BasicStroke(1f));
        g.drawLine(margin, y, W - margin, y);
        y += 12;

        // ── build table rows ─────────────────────────────────────────────
        List<String[]> rows = new ArrayList<>();
        addRow(rows, "Presiding",                program.getPresiding());
        addRow(rows, "Conducting",               program.getConducting());
        addRow(rows, "Opening Prayer",           program.getOpeningPrayer());
        addRow(rows, "Handbook Reading / Scriptural Thought", program.getHandbookReading());

        // agenda items into one table cell block
        if (program.getAgendaItems() != null && !program.getAgendaItems().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            int idx = 0;
            for (AgendaItem item : program.getAgendaItems()) {
                if (item.getTitle() != null && !item.getTitle().isBlank()) {
                    sb.append(++idx).append(". ").append(item.getTitle());
                    if (item.getDetails() != null) {
                        for (String d : item.getDetails()) {
                            if (d != null && !d.isBlank()) sb.append("\n    \u2022 ").append(d);
                        }
                    }
                    sb.append("\n");
                }
            }
            if (sb.length() > 0) rows.add(new String[]{"Agenda Items", sb.toString().trim()});
        }

        addRow(rows, "Welfare",        program.getWelfare());
        addRow(rows, "Closing Prayer", program.getClosingPrayer());

        // ── draw the table ────────────────────────────────────────────────
        int labelW  = 220;
        int valueW  = contentW - labelW;
        Font labelFont = new Font("SansSerif", Font.BOLD, 12);
        Font valueFont = new Font("SansSerif", Font.PLAIN, 12);
        int cellPadX = 10, cellPadY = 7;
        int rowMinH  = 30;

        for (String[] row : rows) {
            String label = row[0];
            String value = row[1] != null ? row[1] : "";

            // measure wrapped height for value cell
            g.setFont(valueFont);
            FontMetrics vfm = g.getFontMetrics();
            List<String> valueLines = wrapText(value, valueW - 2 * cellPadX, vfm);
            int cellH = Math.max(rowMinH, valueLines.size() * vfm.getHeight() + 2 * cellPadY);

            // label cell background
            g.setColor(TABLE_BG);
            g.fillRect(margin, y, labelW, cellH);

            // value cell background
            g.setColor(Color.WHITE);
            g.fillRect(margin + labelW, y, valueW, cellH);

            // borders
            g.setColor(TABLE_LINE);
            g.setStroke(new BasicStroke(1f));
            g.drawRect(margin, y, labelW, cellH);
            g.drawRect(margin + labelW, y, valueW, cellH);

            // label text
            g.setFont(labelFont);
            FontMetrics lfm = g.getFontMetrics();
            g.setColor(new Color(0x5A, 0x3E, 0x1B));
            int labelTextY = y + cellPadY + lfm.getAscent();
            g.drawString(label, margin + cellPadX, labelTextY);

            // value text (multi-line aware)
            g.setFont(valueFont);
            g.setColor(TEXT_DARK);
            int vTextY = y + cellPadY + vfm.getAscent();
            for (String line : valueLines) {
                if (line.startsWith("    \u2022 ")) {
                    g.setColor(TEXT_GRAY);
                    g.drawString(line, margin + labelW + cellPadX + 8, vTextY);
                    g.setColor(TEXT_DARK);
                } else {
                    g.drawString(line, margin + labelW + cellPadX, vTextY);
                }
                vTextY += vfm.getHeight();
            }

            y += cellH;
        }

        // ── P3 logo footer ────────────────────────────────────────────────
        y += 18;
        BufferedImage p3Logo = loadImage("/static/images/P3_LOGO.png");
        if (p3Logo != null && y + 80 < H - margin) {
            int lW = 70;
            int lH = (int) (p3Logo.getHeight() * ((double) lW / p3Logo.getWidth()));
            g.drawImage(p3Logo, (W - lW) / 2, y, lW, lH, null);
        }

        g.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        return baos.toByteArray();
    }

    // ── helper: only add row if value is non-blank ─────────────────────
    private void addRow(List<String[]> rows, String label, String value) {
        if (value != null && !value.isBlank()) {
            rows.add(new String[]{label, value});
        }
    }

    // ── helper: load image from classpath ────────────────────────────────
    private BufferedImage loadImage(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is != null) return ImageIO.read(is);
        } catch (IOException ignored) {}
        return null;
    }

    // ── helper: wrap text to fit width ───────────────────────────────────
    private List<String> wrapText(String text, int maxWidth, FontMetrics fm) {
        List<String> lines = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            lines.add("");
            return lines;
        }
        for (String rawLine : text.split("\n")) {
            if (fm.stringWidth(rawLine) <= maxWidth) {
                lines.add(rawLine);
            } else {
                // word-wrap
                String[] words = rawLine.split(" ");
                StringBuilder current = new StringBuilder();
                for (String word : words) {
                    String test = current.length() == 0 ? word : current + " " + word;
                    if (fm.stringWidth(test) <= maxWidth) {
                        current = new StringBuilder(test);
                    } else {
                        if (current.length() > 0) lines.add(current.toString());
                        current = new StringBuilder(word);
                    }
                }
                if (current.length() > 0) lines.add(current.toString());
            }
        }
        return lines;
    }
}
