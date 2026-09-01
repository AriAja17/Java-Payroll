    package com.payrollpro.service;

    import com.payrollpro.model.*;

    /**
     * Engine utama kalkulasi gaji bulanan.
     * Mengintegrasikan PPh21, BPJS, lembur, prorata, dan potongan cuti.
     */
    public class GajiCalculator {

        /**
         * @param karyawan          data karyawan beserta golongannya
         * @param totalUpahLembur   total upah lembur bulan ini (dari tabel lembur)
         * @param potonganCuti      total potongan alpha/cuti tidak dibayar
         * @param prorata           true jika karyawan masuk/resign di tengah bulan
         * @param hariKerjaAktual   hari kerja aktual (untuk prorata)
         * @param hariKerjaBulan    total hari kerja bulan ini (untuk prorata)
         */
        public static Penggajian hitung(Karyawan karyawan,
                                         double totalUpahLembur,
                                         double potonganCuti,
                                         boolean prorata,
                                         int hariKerjaAktual,
                                         int hariKerjaBulan) {
            Golongan gol = karyawan.getGolongan();
            Penggajian p = new Penggajian();
            p.setIdKaryawan(karyawan.getIdKaryawan());
            p.setNamaKaryawan(karyawan.getNama());

            // --- Gaji pokok (prorata jika perlu) ---
            double gajiPokok = gol.getGajiPokok();
            if (prorata && hariKerjaBulan > 0) {
                gajiPokok = Math.round(gajiPokok * hariKerjaAktual / (double) hariKerjaBulan);
                p.setProrata(true);
                p.setHariKerjaAktual(hariKerjaAktual);
                p.setHariKerjaBulan(hariKerjaBulan);
            }
            p.setGajiPokok(gajiPokok);

            // --- Tunjangan (tidak berlaku untuk karyawan probation) ---
            double tjIstri = 0, tjAnak = 0;
            if (!karyawan.isProba() && karyawan.isMenikah()) {
                tjIstri = gol.getTunjanganIstri();
                tjAnak  = Math.min(karyawan.getJumlahAnak(), 3) * gol.getTunjanganAnak();
            }
            p.setTunjanganIstri(tjIstri);
            p.setTunjanganAnak(tjAnak);
            p.setTransport(gol.getTransport());
            p.setUangMakan(gol.getUangMakan());
            p.setUpahLembur(totalUpahLembur);
            p.setPotonganCuti(potonganCuti);

            // --- Total bruto (sebelum pajak & BPJS) ---
            double bruto = gajiPokok + tjIstri + tjAnak
                         + gol.getTransport() + gol.getUangMakan()
                         + totalUpahLembur - potonganCuti;
            p.setTotalBruto(bruto);

            // --- PPh 21 ---
            double pph21 = PPh21Calculator.hitungPPh21Bulanan(
                    bruto, karyawan.isMenikah(), karyawan.getJumlahAnak());
            p.setPph21(pph21);

            // --- BPJS ---
            BPJSCalculator.Result bpjs = BPJSCalculator.hitung(gajiPokok);

            p.setBpjsKesKaryawan(bpjs.kesKaryawan);
            p.setBpjsJhtKaryawan(bpjs.jhtKaryawan);
            p.setBpjsJpKaryawan(bpjs.jpKaryawan);

            p.setBpjsKesPerusahaan(bpjs.kesPerusahaan);
            p.setBpjsJhtPerusahaan(bpjs.jhtPerusahaan);
            p.setBpjsJpPerusahaan(bpjs.jpPerusahaan);

            p.setBpjsJkk(bpjs.jkk);
            p.setBpjsJkm(bpjs.jkm);

            // --- Gaji bersih ---
            double totalPotong = pph21 + bpjs.totalPotonganKaryawan();
            p.setTotalPotongan(totalPotong);
            p.setGajiBersih(Math.round(bruto - totalPotong));
            p.setStatusBayar("Proses");

            return p;
        }
    }
