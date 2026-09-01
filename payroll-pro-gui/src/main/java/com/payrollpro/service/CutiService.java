package com.payrollpro.service;

import com.payrollpro.dao.HariLiburDAO;
import com.payrollpro.dao.SaldoCutiDAO;
import com.payrollpro.model.Karyawan;
import com.payrollpro.model.SaldoCuti;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class CutiService {

    private final SaldoCutiDAO saldoDAO     = new SaldoCutiDAO();
    private final HariLiburDAO hariLiburDAO = new HariLiburDAO();

    /** Hitung hari kerja aktual antara dua tanggal (exclude Sabtu, Minggu, libur). */
    public int hitungHariKerja(LocalDate mulai, LocalDate selesai) throws Exception {
        List<LocalDate> libur = hariLiburDAO.getAllTanggal();
        int count = 0;
        LocalDate cur = mulai;
        while (!cur.isAfter(selesai)) {
            DayOfWeek dow = cur.getDayOfWeek();
            if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY
                    && !libur.contains(cur)) {
                count++;
            }
            cur = cur.plusDays(1);
        }
        return count;
    }

    /** Cek apakah saldo cuti karyawan mencukupi. */
    public boolean cekSaldoCukup(String idKaryawan, int idJenis,
                                  int tahun, int hariDiminta) throws Exception {
        SaldoCuti saldo = saldoDAO.getByKaryawanJenisTahun(idKaryawan, idJenis, tahun);
        if (saldo == null) return false;
        return saldo.getSisa() >= hariDiminta;
    }

    public void kurangiSaldo(String idKaryawan, int idJenis,
                              int tahun, int jumlahHari) throws Exception {
        saldoDAO.kurangiSaldo(idKaryawan, idJenis, tahun, jumlahHari);
    }

    public void kembalikanSaldo(String idKaryawan, int idJenis,
                                 int tahun, int jumlahHari) throws Exception {
        saldoDAO.tambahSaldo(idKaryawan, idJenis, tahun, jumlahHari);
    }

    /** Hitung potongan gaji akibat alpha: gaji_pokok / 25 per hari. */
    public double hitungPotonganAlpha(double gajiPokok, int hariAlpha) {
        return Math.round((gajiPokok / 25.0) * hariAlpha);
    }

    /**
     * Reset saldo cuti tahunan tiap 1 Januari.
     * Hanya karyawan yang sudah >= 12 bulan masa kerja yang berhak.
     */
    public void resetSaldoTahunan(List<Karyawan> karyawanAktif, int tahunBaru) throws Exception {
        for (Karyawan k : karyawanAktif) {
            if (k.getMasaKerjaBulan() >= 12) {
                saldoDAO.upsert(k.getIdKaryawan(), 1, tahunBaru, 12);
            }
        }
    }
}
