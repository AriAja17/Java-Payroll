package com.payrollpro.util;

import java.text.NumberFormat;
import java.util.Locale;

public class ValidationUtil {

    private static final String NPWP_PATTERN = "\\d{2}\\.\\d{3}\\.\\d{3}\\.\\d-\\d{3}\\.\\d{3}";

    public static boolean isNpwpValid(String npwp) {
        if (npwp == null || npwp.isBlank()) return true; // NPWP opsional
        return npwp.matches(NPWP_PATTERN);
    }

    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isPositiveNumber(String value) {
        try {
            return Double.parseDouble(value) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidJumlahAnak(int anak) {
        return anak >= 0 && anak <= 10;
    }

    /** Format angka ke format Rupiah: Rp 5.000.000 */
    public static String formatRupiah(double amount) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("id", "ID"));
        return "Rp " + nf.format(Math.round(amount));
    }

    /** Parse string Rupiah kembali ke double. */
    public static double parseRupiah(String text) {
        try {
            String clean = text.replaceAll("[^\\d]", "");
            return clean.isEmpty() ? 0 : Double.parseDouble(clean);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /** Nama bulan dalam Bahasa Indonesia. */
    public static String getNamaBulan(int bulan) {
        String[] nama = {"", "Januari", "Februari", "Maret", "April", "Mei",
                "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        return (bulan >= 1 && bulan <= 12) ? nama[bulan] : "?";
    }

    public static String getPeriodeLabel(int bulan, int tahun) {
        return getNamaBulan(bulan) + " " + tahun;
    }
}
