package com.payrollpro.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Kalkulasi gaji prorata untuk karyawan yang masuk atau resign di tengah bulan.
 * Rumus: (hari_kerja_aktual / hari_kerja_bulan) x gaji_penuh
 * Hari kerja = semua hari kecuali Sabtu, Minggu, dan hari libur nasional.
 */
public class ProRataCalculator {

    /**
     * @param gajiBulananPenuh  gaji penuh seandainya masuk full sebulan
     * @param tanggalMulai      tanggal karyawan mulai kerja / tanggal awal bulan
     * @param tanggalSelesai    tanggal terakhir kerja / tanggal akhir bulan
     * @param hariLibur         list tanggal libur nasional bulan tersebut
     */
    public static Result hitung(double gajiBulananPenuh,
                                 LocalDate tanggalMulai,
                                 LocalDate tanggalSelesai,
                                 List<LocalDate> hariLibur) {
        YearMonth bulan       = YearMonth.from(tanggalMulai);
        LocalDate awalBulan   = bulan.atDay(1);
        LocalDate akhirBulan  = bulan.atEndOfMonth();

        int hariKerjaBulan   = hitungHariKerja(awalBulan, akhirBulan, hariLibur);
        int hariKerjaAktual  = hitungHariKerja(tanggalMulai, tanggalSelesai, hariLibur);

        Result r             = new Result();
        r.hariKerjaBulan     = hariKerjaBulan;
        r.hariKerjaAktual    = hariKerjaAktual;
        r.gajiPenuh          = gajiBulananPenuh;
        r.gajiProrata        = hariKerjaBulan > 0
            ? Math.round(gajiBulananPenuh * hariKerjaAktual / (double) hariKerjaBulan)
            : 0;
        return r;
    }

    public static int hitungHariKerja(LocalDate mulai, LocalDate selesai,
                                       List<LocalDate> hariLibur) {
        int count = 0;
        LocalDate cur = mulai;
        while (!cur.isAfter(selesai)) {
            DayOfWeek dow = cur.getDayOfWeek();
            if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY
                    && !hariLibur.contains(cur)) {
                count++;
            }
            cur = cur.plusDays(1);
        }
        return count;
    }

    public static class Result {
        public int    hariKerjaBulan, hariKerjaAktual;
        public double gajiPenuh, gajiProrata;
    }
}
