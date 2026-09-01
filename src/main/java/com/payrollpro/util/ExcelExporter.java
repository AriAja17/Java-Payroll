package com.payrollpro.util;

import com.payrollpro.model.Penggajian;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.List;

public class ExcelExporter {

    public static File exportRekapGaji(List<Penggajian> data, int bulan, int tahun,
                                        String outputDir) throws IOException {
        String filename = "rekap_gaji_" + ValidationUtil.getPeriodeLabel(bulan, tahun)
                .replace(" ", "_") + ".xlsx";
        File file = new File(outputDir, filename);

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Rekap Gaji");

            // Style header
            CellStyle styleHeader = wb.createCellStyle();
            Font fh = wb.createFont();
            fh.setBold(true);
            fh.setColor(IndexedColors.WHITE.getIndex());
            styleHeader.setFont(fh);
            styleHeader.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            styleHeader.setAlignment(HorizontalAlignment.CENTER);
            styleHeader.setBorderBottom(BorderStyle.THIN);

            // Style angka
            CellStyle styleNumber = wb.createCellStyle();
            DataFormat df = wb.createDataFormat();
            styleNumber.setDataFormat(df.getFormat("#,##0"));

            // Style total
            CellStyle styleTotal = wb.createCellStyle();
            Font ft = wb.createFont();
            ft.setBold(true);
            styleTotal.setFont(ft);
            styleTotal.setDataFormat(df.getFormat("#,##0"));
            styleTotal.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            styleTotal.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Judul
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("REKAP GAJI KARYAWAN - " +
                    ValidationUtil.getPeriodeLabel(bulan, tahun).toUpperCase());
            CellStyle ts = wb.createCellStyle();
            Font tf = wb.createFont(); tf.setBold(true); tf.setFontHeightInPoints((short) 13);
            ts.setFont(tf);
            titleCell.setCellStyle(ts);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 16));

            // Header kolom
            String[] headers = {
                "No", "ID Karyawan", "Nama", "Gaji Pokok", "Tj. Istri", "Tj. Anak",
                "Transport", "Uang Makan", "Upah Lembur", "Pot. Cuti",
                "Total Bruto", "PPh 21", "BPJS Kes.", "BPJS JHT", "BPJS JP",
                "Total Potongan", "Gaji Bersih"
            };
            Row headerRow = sheet.createRow(2);
            for (int i = 0; i < headers.length; i++) {
                Cell c = headerRow.createCell(i);
                c.setCellValue(headers[i]);
                c.setCellStyle(styleHeader);
            }

            // Data
            int rowNum = 3, no = 1;
            double sumBruto = 0, sumBersih = 0, sumPph = 0;
            for (Penggajian p : data) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(no++);
                row.createCell(1).setCellValue(p.getIdKaryawan());
                row.createCell(2).setCellValue(p.getNamaKaryawan());
                setNum(row, 3,  p.getGajiPokok(),      styleNumber);
                setNum(row, 4,  p.getTunjanganIstri(),  styleNumber);
                setNum(row, 5,  p.getTunjanganAnak(),   styleNumber);
                setNum(row, 6,  p.getTransport(),       styleNumber);
                setNum(row, 7,  p.getUangMakan(),       styleNumber);
                setNum(row, 8,  p.getUpahLembur(),      styleNumber);
                setNum(row, 9,  p.getPotonganCuti(),    styleNumber);
                setNum(row, 10, p.getTotalBruto(),      styleNumber);
                setNum(row, 11, p.getPph21(),           styleNumber);
                setNum(row, 12, p.getBpjsKesKaryawan(), styleNumber);
                setNum(row, 13, p.getBpjsJhtKaryawan(), styleNumber);
                setNum(row, 14, p.getBpjsJpKaryawan(),  styleNumber);
                setNum(row, 15, p.getTotalPotongan(),   styleNumber);
                setNum(row, 16, p.getGajiBersih(),      styleNumber);
                sumBruto  += p.getTotalBruto();
                sumBersih += p.getGajiBersih();
                sumPph    += p.getPph21();
            }

            // Row total
            Row totalRow = sheet.createRow(rowNum);
            totalRow.createCell(2).setCellValue("TOTAL");
            totalRow.getCell(2).setCellStyle(styleTotal);
            setNum(totalRow, 10, sumBruto,  styleTotal);
            setNum(totalRow, 11, sumPph,    styleTotal);
            setNum(totalRow, 16, sumBersih, styleTotal);

            // Auto-size kolom
            for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);

            wb.write(new FileOutputStream(file));
        }
        return file;
    }

    private static void setNum(Row row, int col, double value, CellStyle style) {
        Cell c = row.createCell(col);
        c.setCellValue(value);
        c.setCellStyle(style);
    }
}
