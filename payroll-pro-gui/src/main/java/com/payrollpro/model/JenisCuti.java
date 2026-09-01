package com.payrollpro.model;

public class JenisCuti {
    private int idJenis;
    private String namaCuti, keterangan;
    private int kuotaHari;
    private boolean isDibayar, butuhDokumen;

    public JenisCuti() {}

    public int    getIdJenis()              { return idJenis; }
    public void   setIdJenis(int v)         { this.idJenis = v; }
    public String getNamaCuti()             { return namaCuti; }
    public void   setNamaCuti(String v)     { this.namaCuti = v; }
    public String getKeterangan()           { return keterangan; }
    public void   setKeterangan(String v)   { this.keterangan = v; }
    public int    getKuotaHari()            { return kuotaHari; }
    public void   setKuotaHari(int v)       { this.kuotaHari = v; }
    public boolean isDibayar()              { return isDibayar; }
    public void    setDibayar(boolean v)    { this.isDibayar = v; }
    public boolean isButuhDokumen()         { return butuhDokumen; }
    public void    setButuhDokumen(boolean v){ this.butuhDokumen = v; }

    @Override public String toString() { return namaCuti; }
}
