package com.payrollpro.model;

public class Golongan {
    private String idGolongan;
    private String namaGolongan;
    private double gajiPokok;
    private double tunjanganIstri;
    private double tunjanganAnak;
    private double transport;
    private double uangMakan;

    public Golongan() {}

    public Golongan(String idGolongan, String namaGolongan, double gajiPokok,
                    double tunjanganIstri, double tunjanganAnak,
                    double transport, double uangMakan) {
        this.idGolongan = idGolongan;
        this.namaGolongan = namaGolongan;
        this.gajiPokok = gajiPokok;
        this.tunjanganIstri = tunjanganIstri;
        this.tunjanganAnak = tunjanganAnak;
        this.transport = transport;
        this.uangMakan = uangMakan;
    }

    public double getTarifLembur() { return gajiPokok / 173.0; }
    public double getGajiHarian()  { return gajiPokok / 25.0; }

    public String getIdGolongan()         { return idGolongan; }
    public void   setIdGolongan(String v) { this.idGolongan = v; }
    public String getNamaGolongan()         { return namaGolongan; }
    public void   setNamaGolongan(String v) { this.namaGolongan = v; }
    public double getGajiPokok()          { return gajiPokok; }
    public void   setGajiPokok(double v)  { this.gajiPokok = v; }
    public double getTunjanganIstri()           { return tunjanganIstri; }
    public void   setTunjanganIstri(double v)   { this.tunjanganIstri = v; }
    public double getTunjanganAnak()            { return tunjanganAnak; }
    public void   setTunjanganAnak(double v)    { this.tunjanganAnak = v; }
    public double getTransport()          { return transport; }
    public void   setTransport(double v)  { this.transport = v; }
    public double getUangMakan()          { return uangMakan; }
    public void   setUangMakan(double v)  { this.uangMakan = v; }

    @Override public String toString() { return idGolongan + " - " + namaGolongan; }
}
