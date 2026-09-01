package com.payrollpro.service;

/**
 * Kalkulasi PPh 21 sesuai UU HPP No.7/2021 (berlaku 2022-2025).
 *
 * PTKP:
 *   TK/0 = Rp 54.000.000 | K/0  = Rp 58.500.000
 *   TK/1 = Rp 58.500.000 | K/1  = Rp 63.000.000
 *   TK/2 = Rp 63.000.000 | K/2  = Rp 67.500.000
 *   TK/3 = Rp 67.500.000 | K/3  = Rp 72.000.000
 *
 * Tarif Progresif PKP:
 *   s/d Rp 60 jt        = 5%
 *   60 jt - 250 jt      = 15%
 *   250 jt - 500 jt     = 25%
 *   500 jt - 5 M        = 30%
 *   > 5 M               = 35%
 */
public class PPh21Calculator {

    private static final double PTKP_WP        = 54_000_000;
    private static final double PTKP_KAWIN     =  4_500_000;
    private static final double PTKP_TANGGUNGAN=  4_500_000;

    public static double getPTKP(boolean menikah, int jumlahAnak) {
        int tanggungan = Math.min(jumlahAnak, 3);
        double ptkp = PTKP_WP;
        if (menikah) ptkp += PTKP_KAWIN;
        ptkp += tanggungan * PTKP_TANGGUNGAN;
        return ptkp;
    }

    public static String getKodePTKP(boolean menikah, int jumlahAnak) {
        return (menikah ? "K/" : "TK/") + Math.min(jumlahAnak, 3);
    }

    /**
     * Hitung PPh 21 bulanan dari penghasilan bruto sebulan.
     * Biaya jabatan 5% (max Rp 500.000/bln) sudah diperhitungkan.
     */
    public static double hitungPPh21Bulanan(double penghasilanBrutoSebulan,
                                             boolean menikah, int jumlahAnak) {
        double biayaJabatan = Math.min(penghasilanBrutoSebulan * 0.05, 500_000);
        double netoBulanan  = penghasilanBrutoSebulan - biayaJabatan;
        double netoSetahun  = netoBulanan * 12;

        double ptkp = getPTKP(menikah, jumlahAnak);
        double pkp  = Math.max(netoSetahun - ptkp, 0);
        // PKP dibulatkan ke ribuan ke bawah
        pkp = Math.floor(pkp / 1000) * 1000;

        double pajakSetahun  = hitungProgresif(pkp);
        double pajakBulanan  = pajakSetahun / 12;
        return Math.round(pajakBulanan);
    }

    private static double hitungProgresif(double pkp) {
        double pajak = 0;
        if (pkp > 0) {
            double l1 = Math.min(pkp, 60_000_000);
            pajak += l1 * 0.05;
        }
        if (pkp > 60_000_000) {
            double l2 = Math.min(pkp - 60_000_000, 190_000_000);
            pajak += l2 * 0.15;
        }
        if (pkp > 250_000_000) {
            double l3 = Math.min(pkp - 250_000_000, 250_000_000);
            pajak += l3 * 0.25;
        }
        if (pkp > 500_000_000) {
            double l4 = Math.min(pkp - 500_000_000, 4_500_000_000.0);
            pajak += l4 * 0.30;
        }
        if (pkp > 5_000_000_000.0) {
            pajak += (pkp - 5_000_000_000.0) * 0.35;
        }
        return pajak;
    }
}
