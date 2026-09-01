package com.payrollpro.service;

/**
 * Kalkulasi THR sesuai PP No.36/2021.
 *
 * Masa kerja >= 12 bulan : 1x gaji sebulan (pokok + tunjangan tetap)
 * Masa kerja 1-11 bulan  : (masa_kerja / 12) x gaji sebulan
 * Masa kerja < 1 bulan   : tidak berhak THR
 */
public class THRCalculator {

    public static Result hitung(double gajiPokok, double tunjanganTetap, int masaKerjaBulan) {
        Result r       = new Result();
        r.gajiSebulan  = gajiPokok + tunjanganTetap;
        r.masaKerjaBulan = masaKerjaBulan;

        if (masaKerjaBulan < 1) {
            r.faktor     = 0;
            r.nominalThr = 0;
            r.keterangan = "Belum berhak (masa kerja < 1 bulan)";
        } else if (masaKerjaBulan >= 12) {
            r.faktor     = 1.0;
            r.nominalThr = Math.round(r.gajiSebulan);
            r.keterangan = "Penuh 1x gaji (masa kerja >= 12 bulan)";
        } else {
            r.faktor     = masaKerjaBulan / 12.0;
            r.nominalThr = Math.round(r.gajiSebulan * r.faktor);
            r.keterangan = "Prorata " + masaKerjaBulan + "/12 bulan";
        }

        return r;
    }

    public static class Result {
        public double gajiSebulan, faktor, nominalThr;
        public int    masaKerjaBulan;
        public String keterangan;
    }
}
