# PayrollPro - Sistem Penggajian

Merupakan rancangan/prototype aplikasi penggajian berbasis Java yang dikembangkan menggunakan NetBeans untuk memenuhi Tugas Akhir Semester.

PayrollPro adalah aplikasi desktop untuk mengelola proses penggajian karyawan, mulai dari data karyawan, absensi/lembur, cuti, perhitungan gaji (termasuk BPJS dan PPh 21), THR, hingga pembuatan slip gaji dalam format PDF.

## Fitur

- **Manajemen Karyawan** - data karyawan, golongan/jabatan, dan status kepegawaian
- **Manajemen Cuti** - pengajuan cuti, saldo cuti, dan jenis cuti
- **Lembur** - pencatatan dan perhitungan jam lembur
- **Penggajian** - perhitungan gaji otomatis dengan komponen:
  - BPJS (Kesehatan & Ketenagakerjaan)
  - PPh 21
  - Proration gaji (pro-rata)
  - Lembur
- **THR** - perhitungan dan pencatatan Tunjangan Hari Raya
- **UMP** - referensi Upah Minimum Provinsi
- **Hari Libur** - pengaturan kalender hari libur
- **Laporan** - export laporan ke Excel dan slip gaji ke PDF
- **Audit Log** - pencatatan aktivitas pengguna
- **Manajemen User & Role** - login dengan hak akses berbasis role (admin/staf)
- **Backup Database** - fitur backup basis data

## Teknologi yang Digunakan

| Komponen | Teknologi |
|---|---|
| Bahasa | Java 25 |
| Build Tool | Maven |
| GUI Framework | Java Swing + [FlatLaf](https://www.formdev.com/flatlaf/) (modern look and feel) |
| Database | MySQL |
| DB Driver | MySQL Connector/J |
| Date Picker | JCalendar |
| Export PDF | Apache PDFBox |
| Export Excel | Apache POI |
| Enkripsi Password | BCrypt (jBCrypt) |
| IDE Pengembangan | NetBeans |

## Struktur Proyek

```
├── src/main/java/com/payrollpro/
│   ├── config/       # Konfigurasi koneksi database
│   ├── dao/          # Data Access Object (query ke database)
│   ├── model/        # Model/entity data
│   ├── service/      # Logika bisnis (perhitungan gaji, BPJS, PPh21, dll)
│   ├── ui/            # Tampilan (Login, Main Frame, Splash Screen)
│   │   └── panels/    # Panel-panel fitur (Karyawan, Cuti, Lembur, Penggajian, dll)
│   ├── util/          # Utilitas (export Excel/PDF, backup DB, validasi, role)
│   └── Main.java      # Entry point aplikasi
├── src/main/resources/
│   └── schema.sql          # Skema database MySQL
├── pom.xml
└── dependency-reduced-pom.xml
```

## Cara Menjalankan

### Prasyarat
- JDK 25
- Maven
- MySQL Server

### Langkah

1. **Clone repository**
   ```bash
   git clone <url-repo-ini>
   cd <nama-folder-repo>
   ```

2. **Buat database**

   Buat database MySQL baru, lalu import skema dari `src/main/resources/schema.sql`:
   ```bash
   mysql -u root -p -e "CREATE DATABASE payroll_pro_db"
   mysql -u root -p payroll_pro_db < src/main/resources/schema.sql
   ```

3. **Sesuaikan konfigurasi database**

   Edit kredensial koneksi di `src/main/java/com/payrollpro/config/DatabaseConfig.java` sesuai environment kamu (host, port, nama database, user, password).

4. **Build & jalankan**
   ```bash
   mvn clean package
   java -jar target/payroll-pro-1.0.0-shaded.jar
   ```

   Atau jalankan langsung melalui NetBeans dengan menekan tombol Run (F6).

## Catatan

Proyek ini merupakan prototype/tugas akhir semester dan masih dapat dikembangkan lebih lanjut, terutama dari sisi keamanan (kredensial database sebaiknya dipindah ke environment variable atau file konfigurasi terpisah, bukan hardcode di source code).

## Lisensi

Proyek ini dibuat untuk keperluan tugas akademik.
