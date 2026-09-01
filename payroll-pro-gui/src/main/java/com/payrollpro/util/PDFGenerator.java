package com.payrollpro.util;

import com.payrollpro.model.Karyawan;
import com.payrollpro.model.Penggajian;
import com.payrollpro.service.PPh21Calculator;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PDFGenerator {

    public static File generateSlipGaji(Penggajian p, Karyawan k, String outputDir)
            throws IOException {
        String filename = "slip_gaji_" + k.getIdKaryawan() + "_"
                + ValidationUtil.getPeriodeLabel(p.getPeriodeBulan(), p.getPeriodeTahun())
                        .replace(" ", "_") + ".pdf";
        File file = new File(outputDir, filename);

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A5);
            doc.addPage(page);

            PDFont fontBold   = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDFont fontNormal = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                float y = 545, margin = 40, w = page.getMediaBox().getWidth() - margin * 2;

                // ---- Header ----
                cs.setFont(fontBold, 14);
                drawText(cs, "SLIP GAJI KARYAWAN", margin, y); y -= 18;
                cs.setFont(fontNormal, 9);
                drawText(cs, "Periode: " + ValidationUtil.getPeriodeLabel(
                        p.getPeriodeBulan(), p.getPeriodeTahun()), margin, y); y -= 8;
                drawLine(cs, margin, y, margin + w, y); y -= 14;

                // ---- Info karyawan ----
                cs.setFont(fontBold, 9);
                drawText(cs, "Data Karyawan", margin, y); y -= 12;
                cs.setFont(fontNormal, 9);
                drawRow(cs, "ID Karyawan",    k.getIdKaryawan(), margin, y, w); y -= 11;
                drawRow(cs, "Nama",           k.getNama(), margin, y, w); y -= 11;
                drawRow(cs, "Golongan",       k.getIdGolongan() + " - " +
                        (k.getGolongan() != null ? k.getGolongan().getNamaGolongan() : ""),
                        margin, y, w); y -= 11;
                drawRow(cs, "PTKP",
                        PPh21Calculator.getKodePTKP(k.isMenikah(), k.getJumlahAnak()),
                        margin, y, w); y -= 8;
                drawLine(cs, margin, y, margin + w, y); y -= 14;

                // ---- Pendapatan ----
                cs.setFont(fontBold, 9);
                drawText(cs, "Komponen Pendapatan", margin, y); y -= 12;
                cs.setFont(fontNormal, 9);
                drawRow(cs, "Gaji Pokok" + (p.isProrata() ? " (Prorata)" : ""),
                        ValidationUtil.formatRupiah(p.getGajiPokok()), margin, y, w); y -= 11;
                if (p.getTunjanganIstri() > 0) {
                    drawRow(cs, "Tunjangan Istri",
                            ValidationUtil.formatRupiah(p.getTunjanganIstri()), margin, y, w); y -= 11;
                }
                if (p.getTunjanganAnak() > 0) {
                    drawRow(cs, "Tunjangan Anak",
                            ValidationUtil.formatRupiah(p.getTunjanganAnak()), margin, y, w); y -= 11;
                }
                drawRow(cs, "Transport",   ValidationUtil.formatRupiah(p.getTransport()), margin, y, w); y -= 11;
                drawRow(cs, "Uang Makan",  ValidationUtil.formatRupiah(p.getUangMakan()), margin, y, w); y -= 11;
                if (p.getUpahLembur() > 0) {
                    drawRow(cs, "Upah Lembur", ValidationUtil.formatRupiah(p.getUpahLembur()), margin, y, w); y -= 11;
                }
                drawRow(cs, "Total Bruto", ValidationUtil.formatRupiah(p.getTotalBruto()), margin, y, w); y -= 8;
                drawLine(cs, margin, y, margin + w, y); y -= 14;

                // ---- Potongan ----
                cs.setFont(fontBold, 9);
                drawText(cs, "Komponen Potongan", margin, y); y -= 12;
                cs.setFont(fontNormal, 9);
                if (p.getPotonganCuti() > 0) {
                    drawRow(cs, "Pot. Alpha/Cuti TDB",
                            "- " + ValidationUtil.formatRupiah(p.getPotonganCuti()), margin, y, w); y -= 11;
                }
                drawRow(cs, "PPh 21 (" + PPh21Calculator.getKodePTKP(k.isMenikah(), k.getJumlahAnak()) + ")",
                        "- " + ValidationUtil.formatRupiah(p.getPph21()), margin, y, w); y -= 11;
                drawRow(cs, "BPJS Kesehatan (1%)",
                        "- " + ValidationUtil.formatRupiah(p.getBpjsKesKaryawan()), margin, y, w); y -= 11;
                drawRow(cs, "BPJS JHT (2%)",
                        "- " + ValidationUtil.formatRupiah(p.getBpjsJhtKaryawan()), margin, y, w); y -= 11;
                drawRow(cs, "BPJS JP (1%)",
                        "- " + ValidationUtil.formatRupiah(p.getBpjsJpKaryawan()), margin, y, w); y -= 8;
                drawLine(cs, margin, y, margin + w, y); y -= 14;

                // ---- Total ----
                cs.setFont(fontBold, 10);
                drawRow(cs, "GAJI BERSIH DITERIMA",
                        ValidationUtil.formatRupiah(p.getGajiBersih()), margin, y, w); y -= 20;

                // ---- Tanggungan perusahaan (info) ----
                cs.setFont(fontNormal, 8);
                drawLine(cs, margin, y, margin + w, y); y -= 10;
                drawText(cs, "Tanggungan Perusahaan (tidak dipotong dari gaji):", margin, y); y -= 10;
                drawRow(cs, "BPJS Kes. 4% + JHT 3.7% + JP 2% + JKK + JKM",
                        ValidationUtil.formatRupiah(
                                p.getBpjsKesPerusahaan() + p.getBpjsJhtPerusahaan() +
                                p.getBpjsJpPerusahaan() + p.getBpjsJkk() + p.getBpjsJkm()),
                        margin, y, w); y -= 14;

                // ---- Footer ----
                drawLine(cs, margin, y, margin + w, y); y -= 12;
                cs.setFont(fontNormal, 8);
                drawText(cs, "Dicetak: " + LocalDate.now().format(
                        DateTimeFormatter.ofPattern("dd MMMM yyyy")), margin, y);
                drawText(cs, "Dokumen ini dicetak oleh sistem PayrollPro | Developed by Syauqi XI RPL 1\"",
                        margin + w - 180, y);
            }

            doc.save(file);
        }
        return file;
    }

    private static void drawText(PDPageContentStream cs, String text, float x, float y)
            throws IOException {
        cs.beginText();
        cs.newLineAtOffset(x, y);
        cs.showText(text != null ? text : "");
        cs.endText();
    }

    private static void drawRow(PDPageContentStream cs, String label, String value,
                                 float x, float y, float width) throws IOException {
        drawText(cs, label, x, y);
        // Rata kanan untuk nilai
        float vx = x + width - 130;
        drawText(cs, value != null ? value : "-", vx, y);
    }

    private static void drawLine(PDPageContentStream cs, float x1, float y,
                                  float x2, float y2) throws IOException {
        cs.moveTo(x1, y);
        cs.lineTo(x2, y2);
        cs.setLineWidth(0.5f);
        cs.stroke();
    }
}
