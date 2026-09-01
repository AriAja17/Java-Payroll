package com.payrollpro.service;

/**
 * Kalkulasi BPJS sesuai regulasi 2025.
 *
 * BPJS KESEHATAN (Perpres 64/2020, max upah Rp 12.000.000):
 *   Perusahaan : 4%  | Karyawan : 1%
 *
 * BPJS KETENAGAKERJAAN:
 *   JHT  : Perusahaan 3.7%  | Karyawan 2%
 *   JP   : Perusahaan 2%    | Karyawan 1%   (max upah Rp 9.559.600)
 *   JKK  : Perusahaan 0.24% (kantor/risiko sangat rendah)
 *   JKM  : Perusahaan 0.3%
 */
public class BPJSCalculator {

    private static final double MAX_UPAH_BKS = 12_000_000;
    private static final double MAX_UPAH_JP  =  9_559_600;

    public static Result hitung(double gajiPokok) {
        double upahKes = Math.min(gajiPokok, MAX_UPAH_BKS);
        double upahJp  = Math.min(gajiPokok, MAX_UPAH_JP);

        Result r = new Result();
        r.kesKaryawan    = Math.round(upahKes    * 0.010);
        r.kesPerusahaan  = Math.round(upahKes    * 0.040);
        r.jhtKaryawan    = Math.round(gajiPokok  * 0.020);
        r.jhtPerusahaan  = Math.round(gajiPokok  * 0.037);
        r.jpKaryawan     = Math.round(upahJp     * 0.010);
        r.jpPerusahaan   = Math.round(upahJp     * 0.020);
        r.jkk            = Math.round(gajiPokok  * 0.0024);
        r.jkm            = Math.round(gajiPokok  * 0.0030);
        return r;
    }

    public static class Result {
        public double kesKaryawan,  kesPerusahaan;
        public double jhtKaryawan,  jhtPerusahaan;
        public double jpKaryawan,   jpPerusahaan;
        public double jkk, jkm;

        public double totalPotonganKaryawan() {
            return kesKaryawan + jhtKaryawan + jpKaryawan;
        }

        public double totalTanggunganPerusahaan() {
            return kesPerusahaan + jhtPerusahaan + jpPerusahaan + jkk + jkm;
        }
    }
}
