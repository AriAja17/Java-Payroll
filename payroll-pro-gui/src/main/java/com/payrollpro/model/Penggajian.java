package com.payrollpro.model;

import java.time.LocalDateTime;

public class Penggajian {
    private int idGaji;
    private String idKaryawan, namaKaryawan, statusBayar, catatan;
    private int periodeBulan, periodeTahun;
    private double gajiPokok, tunjanganIstri, tunjanganAnak;
    private double transport, uangMakan, upahLembur;
    private double potonganCuti;
    private double pph21;
    private double bpjsKesKaryawan, bpjsJhtKaryawan, bpjsJpKaryawan;
    private double bpjsKesPerusahaan, bpjsJhtPerusahaan, bpjsJpPerusahaan;
    private double bpjsJkk, bpjsJkm;
    private double totalBruto, totalPotongan, gajiBersih;
    private boolean isProrata, isLocked;
    private Integer hariKerjaAktual, hariKerjaBulan;
    private LocalDateTime createdAt, updatedAt;

    public Penggajian() {}

    public int    getIdGaji()               { return idGaji; }
    public void   setIdGaji(int v)          { this.idGaji = v; }
    public String getIdKaryawan()           { return idKaryawan; }
    public void   setIdKaryawan(String v)   { this.idKaryawan = v; }
    public String getNamaKaryawan()         { return namaKaryawan; }
    public void   setNamaKaryawan(String v) { this.namaKaryawan = v; }
    public int    getPeriodeBulan()         { return periodeBulan; }
    public void   setPeriodeBulan(int v)    { this.periodeBulan = v; }
    public int    getPeriodeTahun()         { return periodeTahun; }
    public void   setPeriodeTahun(int v)    { this.periodeTahun = v; }
    public double getGajiPokok()            { return gajiPokok; }
    public void   setGajiPokok(double v)    { this.gajiPokok = v; }
    public double getTunjanganIstri()       { return tunjanganIstri; }
    public void   setTunjanganIstri(double v){ this.tunjanganIstri = v; }
    public double getTunjanganAnak()        { return tunjanganAnak; }
    public void   setTunjanganAnak(double v){ this.tunjanganAnak = v; }
    public double getTransport()            { return transport; }
    public void   setTransport(double v)    { this.transport = v; }
    public double getUangMakan()            { return uangMakan; }
    public void   setUangMakan(double v)    { this.uangMakan = v; }
    public double getUpahLembur()           { return upahLembur; }
    public void   setUpahLembur(double v)   { this.upahLembur = v; }
    public double getPotonganCuti()         { return potonganCuti; }
    public void   setPotonganCuti(double v) { this.potonganCuti = v; }
    public double getPph21()                { return pph21; }
    public void   setPph21(double v)        { this.pph21 = v; }
    public double getBpjsKesKaryawan()          { return bpjsKesKaryawan; }
    public void   setBpjsKesKaryawan(double v)  { this.bpjsKesKaryawan = v; }
    public double getBpjsJhtKaryawan()          { return bpjsJhtKaryawan; }
    public void   setBpjsJhtKaryawan(double v)  { this.bpjsJhtKaryawan = v; }
    public double getBpjsJpKaryawan()           { return bpjsJpKaryawan; }
    public void   setBpjsJpKaryawan(double v)   { this.bpjsJpKaryawan = v; }
    public double getBpjsKesPerusahaan()         { return bpjsKesPerusahaan; }
    public void   setBpjsKesPerusahaan(double v) { this.bpjsKesPerusahaan = v; }
    public double getBpjsJhtPerusahaan()         { return bpjsJhtPerusahaan; }
    public void   setBpjsJhtPerusahaan(double v) { this.bpjsJhtPerusahaan = v; }
    public double getBpjsJpPerusahaan()          { return bpjsJpPerusahaan; }
    public void   setBpjsJpPerusahaan(double v)  { this.bpjsJpPerusahaan = v; }
    public double getBpjsJkk()              { return bpjsJkk; }
    public void   setBpjsJkk(double v)     { this.bpjsJkk = v; }
    public double getBpjsJkm()              { return bpjsJkm; }
    public void   setBpjsJkm(double v)     { this.bpjsJkm = v; }
    public double getTotalBruto()           { return totalBruto; }
    public void   setTotalBruto(double v)   { this.totalBruto = v; }
    public double getTotalPotongan()        { return totalPotongan; }
    public void   setTotalPotongan(double v){ this.totalPotongan = v; }
    public double getGajiBersih()           { return gajiBersih; }
    public void   setGajiBersih(double v)   { this.gajiBersih = v; }
    public boolean isProrata()              { return isProrata; }
    public void    setProrata(boolean v)    { this.isProrata = v; }
    public boolean isLocked()              { return isLocked; }
    public void    setLocked(boolean v)    { this.isLocked = v; }
    public Integer getHariKerjaAktual()       { return hariKerjaAktual; }
    public void    setHariKerjaAktual(Integer v) { this.hariKerjaAktual = v; }
    public Integer getHariKerjaBulan()        { return hariKerjaBulan; }
    public void    setHariKerjaBulan(Integer v)  { this.hariKerjaBulan = v; }
    public String  getStatusBayar()         { return statusBayar; }
    public void    setStatusBayar(String v) { this.statusBayar = v; }
    public String  getCatatan()             { return catatan; }
    public void    setCatatan(String v)     { this.catatan = v; }
    public LocalDateTime getCreatedAt()     { return createdAt; }
    public void          setCreatedAt(LocalDateTime v) { this.createdAt = v; }
    public LocalDateTime getUpdatedAt()     { return updatedAt; }
    public void          setUpdatedAt(LocalDateTime v) { this.updatedAt = v; }
}
