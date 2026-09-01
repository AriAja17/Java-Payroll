package com.payrollpro.model;

import java.time.LocalDate;

public class HariLibur {
    private int id;
    private LocalDate tanggal;
    private String keterangan;

    public HariLibur() {}
    public HariLibur(LocalDate tanggal, String keterangan) {
        this.tanggal = tanggal;
        this.keterangan = keterangan;
    }

    public int    getId()                   { return id; }
    public void   setId(int v)              { this.id = v; }
    public LocalDate getTanggal()           { return tanggal; }
    public void      setTanggal(LocalDate v){ this.tanggal = v; }
    public String getKeterangan()           { return keterangan; }
    public void   setKeterangan(String v)   { this.keterangan = v; }
}
