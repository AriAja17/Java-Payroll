package com.payrollpro.service;

/**
 * Kalkulasi lembur sesuai UU Ketenagakerjaan No.13/2003 dan PP 35/2021.
 *
 * HARI KERJA BIASA:
 *   Jam ke-1     = 1.5x upah per jam
 *   Jam ke-2 dst = 2.0x upah per jam
 *
 * HARI LIBUR / HARI RAYA:
 *   Jam ke-1 s/d 8  = 2.0x upah per jam
 *   Jam ke-9        = 3.0x upah per jam
 *   Jam ke-10 dst   = 4.0x upah per jam
 *
 * Upah per jam = Gaji Pokok / 173
 */
public class LemburCalculator {

    public static double hitungTotalLembur(double gajiPokok, int jamLembur, boolean isHariLibur) {
        double upahPerJam = gajiPokok / 173.0;
        double total = 0;

        if (!isHariLibur) {
            for (int jam = 1; jam <= jamLembur; jam++) {
                if (jam == 1) total += upahPerJam * 1.5;
                else          total += upahPerJam * 2.0;
            }
        } else {
            for (int jam = 1; jam <= jamLembur; jam++) {
                if (jam <= 8)      total += upahPerJam * 2.0;
                else if (jam == 9) total += upahPerJam * 3.0;
                else               total += upahPerJam * 4.0;
            }
        }

        return Math.round(total);
    }

    public static String getKeteranganMultiplier(int jamLembur, boolean isHariLibur) {
        if (!isHariLibur) {
            if (jamLembur == 1) return "1.5x (jam ke-1)";
            return "1.5x (jam ke-1) + 2x (jam ke-2 dst)";
        } else {
            if (jamLembur <= 8) return "2x (hari libur, jam 1-8)";
            if (jamLembur == 9) return "2x (1-8) + 3x (jam ke-9)";
            return "2x (1-8) + 3x (9) + 4x (10+)";
        }
    }
}
