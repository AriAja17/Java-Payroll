-- ============================================================
-- PayrollPro Database Schema
-- Database: payroll_pro_db
-- ============================================================

CREATE DATABASE IF NOT EXISTS payroll_pro_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE payroll_pro_db;

-- ------------------------------------------------------------
-- Tabel: ump_master (UMP per wilayah, update tiap tahun)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS ump_master (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    wilayah     VARCHAR(100) NOT NULL,
    tahun       INT NOT NULL,
    nilai_ump   DECIMAL(15,2) NOT NULL,
    UNIQUE KEY uk_wilayah_tahun (wilayah, tahun)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Tabel: golongan
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS golongan (
    id_golongan     VARCHAR(10) PRIMARY KEY,
    nama_golongan   VARCHAR(50) NOT NULL,
    gaji_pokok      DECIMAL(15,2) NOT NULL DEFAULT 0,
    tunjangan_istri DECIMAL(15,2) NOT NULL DEFAULT 0,
    tunjangan_anak  DECIMAL(15,2) NOT NULL DEFAULT 0,
    transport       DECIMAL(15,2) NOT NULL DEFAULT 0,
    uang_makan      DECIMAL(15,2) NOT NULL DEFAULT 0,
    tarif_lembur    DECIMAL(15,2) GENERATED ALWAYS AS (gaji_pokok / 173) STORED,
    gaji_harian     DECIMAL(15,2) GENERATED ALWAYS AS (gaji_pokok / 25) STORED
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Tabel: karyawan
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS karyawan (
    id_karyawan     VARCHAR(15) PRIMARY KEY,
    id_golongan     VARCHAR(10) NOT NULL,
    nama            VARCHAR(100) NOT NULL,
    jenis_kelamin   ENUM('Laki-laki','Perempuan') NOT NULL,
    tempat_lahir    VARCHAR(50),
    tanggal_lahir   DATE,
    alamat          TEXT,
    status_nikah    ENUM('Menikah','Belum Menikah') NOT NULL DEFAULT 'Belum Menikah',
    jumlah_anak     INT NOT NULL DEFAULT 0,
    npwp            VARCHAR(25),
    tanggal_masuk   DATE NOT NULL,
    status_karyawan ENUM('Aktif','Probation','Nonaktif','Resign','PHK') NOT NULL DEFAULT 'Probation',
    tanggal_resign  DATE,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_karyawan_golongan FOREIGN KEY (id_golongan)
        REFERENCES golongan(id_golongan) ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Tabel: hari_libur_nasional
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS hari_libur (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    tanggal     DATE NOT NULL UNIQUE,
    keterangan  VARCHAR(100) NOT NULL
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Tabel: lembur
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS lembur (
    id_lembur       INT AUTO_INCREMENT PRIMARY KEY,
    id_karyawan     VARCHAR(15) NOT NULL,
    tanggal_lembur  DATE NOT NULL,
    jam_lembur      INT NOT NULL DEFAULT 0,
    is_hari_libur   BOOLEAN NOT NULL DEFAULT FALSE,
    tarif_per_jam   DECIMAL(15,2) NOT NULL DEFAULT 0,
    total_upah      DECIMAL(15,2) NOT NULL DEFAULT 0,
    keterangan      VARCHAR(200),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_lembur_karyawan FOREIGN KEY (id_karyawan)
        REFERENCES karyawan(id_karyawan) ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Tabel: jenis_cuti
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS jenis_cuti (
    id_jenis        INT AUTO_INCREMENT PRIMARY KEY,
    nama_cuti       VARCHAR(50) NOT NULL,
    kuota_hari      INT NOT NULL DEFAULT 0,
    is_dibayar      BOOLEAN NOT NULL DEFAULT TRUE,
    butuh_dokumen   BOOLEAN NOT NULL DEFAULT FALSE,
    keterangan      VARCHAR(200)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Tabel: saldo_cuti
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS saldo_cuti (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    id_karyawan     VARCHAR(15) NOT NULL,
    id_jenis        INT NOT NULL,
    tahun           INT NOT NULL,
    kuota           INT NOT NULL DEFAULT 0,
    terpakai        INT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_saldo (id_karyawan, id_jenis, tahun),
    CONSTRAINT fk_saldo_karyawan FOREIGN KEY (id_karyawan)
        REFERENCES karyawan(id_karyawan) ON UPDATE CASCADE,
    CONSTRAINT fk_saldo_jenis FOREIGN KEY (id_jenis)
        REFERENCES jenis_cuti(id_jenis)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Tabel: pengajuan_cuti
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS pengajuan_cuti (
    id_cuti         INT AUTO_INCREMENT PRIMARY KEY,
    id_karyawan     VARCHAR(15) NOT NULL,
    id_jenis        INT NOT NULL,
    tgl_mulai       DATE NOT NULL,
    tgl_selesai     DATE NOT NULL,
    jumlah_hari     INT NOT NULL DEFAULT 1,
    alasan          TEXT,
    status          ENUM('Pending','Disetujui','Ditolak') NOT NULL DEFAULT 'Pending',
    diproses_oleh   VARCHAR(50),
    tgl_proses      DATETIME,
    catatan_admin   TEXT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cuti_karyawan FOREIGN KEY (id_karyawan)
        REFERENCES karyawan(id_karyawan) ON UPDATE CASCADE,
    CONSTRAINT fk_cuti_jenis FOREIGN KEY (id_jenis)
        REFERENCES jenis_cuti(id_jenis)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Tabel: penggajian
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS penggajian (
    id_gaji             INT AUTO_INCREMENT PRIMARY KEY,
    id_karyawan         VARCHAR(15) NOT NULL,
    periode_bulan       INT NOT NULL,
    periode_tahun       INT NOT NULL,
    gaji_pokok          DECIMAL(15,2) NOT NULL DEFAULT 0,
    tunjangan_istri     DECIMAL(15,2) NOT NULL DEFAULT 0,
    tunjangan_anak      DECIMAL(15,2) NOT NULL DEFAULT 0,
    transport           DECIMAL(15,2) NOT NULL DEFAULT 0,
    uang_makan          DECIMAL(15,2) NOT NULL DEFAULT 0,
    upah_lembur         DECIMAL(15,2) NOT NULL DEFAULT 0,
    potongan_cuti       DECIMAL(15,2) NOT NULL DEFAULT 0,
    pph21               DECIMAL(15,2) NOT NULL DEFAULT 0,
    bpjs_kes_karyawan   DECIMAL(15,2) NOT NULL DEFAULT 0,
    bpjs_jht_karyawan   DECIMAL(15,2) NOT NULL DEFAULT 0,
    bpjs_jp_karyawan    DECIMAL(15,2) NOT NULL DEFAULT 0,
    bpjs_kes_perusahaan DECIMAL(15,2) NOT NULL DEFAULT 0,
    bpjs_jht_perusahaan DECIMAL(15,2) NOT NULL DEFAULT 0,
    bpjs_jp_perusahaan  DECIMAL(15,2) NOT NULL DEFAULT 0,
    bpjs_jkk            DECIMAL(15,2) NOT NULL DEFAULT 0,
    bpjs_jkm            DECIMAL(15,2) NOT NULL DEFAULT 0,
    total_bruto         DECIMAL(15,2) NOT NULL DEFAULT 0,
    total_potongan      DECIMAL(15,2) NOT NULL DEFAULT 0,
    gaji_bersih         DECIMAL(15,2) NOT NULL DEFAULT 0,
    is_prorata          BOOLEAN NOT NULL DEFAULT FALSE,
    hari_kerja_aktual   INT,
    hari_kerja_bulan    INT,
    status_bayar        ENUM('Proses','Lunas') NOT NULL DEFAULT 'Proses',
    is_locked           BOOLEAN NOT NULL DEFAULT FALSE,
    catatan             TEXT,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_periode (id_karyawan, periode_bulan, periode_tahun),
    CONSTRAINT fk_gaji_karyawan FOREIGN KEY (id_karyawan)
        REFERENCES karyawan(id_karyawan) ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Tabel: thr
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS thr (
    id_thr          INT AUTO_INCREMENT PRIMARY KEY,
    id_karyawan     VARCHAR(15) NOT NULL,
    tahun           INT NOT NULL,
    jenis_hari_raya VARCHAR(30) NOT NULL,
    masa_kerja_bln  INT NOT NULL,
    gaji_pokok      DECIMAL(15,2) NOT NULL,
    faktor          DECIMAL(5,4) NOT NULL,
    nominal_thr     DECIMAL(15,2) NOT NULL,
    status_bayar    ENUM('Belum','Lunas') NOT NULL DEFAULT 'Belum',
    tanggal_bayar   DATE,
    UNIQUE KEY uk_thr (id_karyawan, tahun, jenis_hari_raya),
    CONSTRAINT fk_thr_karyawan FOREIGN KEY (id_karyawan)
        REFERENCES karyawan(id_karyawan) ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Tabel: users (login & role)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    username        VARCHAR(50) NOT NULL UNIQUE,
    password_hash   VARCHAR(100) NOT NULL,
    nama_lengkap    VARCHAR(100),
    role            ENUM('Admin','Operator') NOT NULL DEFAULT 'Operator',
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    last_login      DATETIME,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Tabel: audit_log
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS audit_log (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    username        VARCHAR(50) NOT NULL,
    aksi            ENUM('INSERT','UPDATE','DELETE','LOGIN','LOGOUT','LOCK','EXPORT','BACKUP') NOT NULL,
    tabel           VARCHAR(50),
    id_record       VARCHAR(50),
    data_sebelum    TEXT,
    data_sesudah    TEXT,
    keterangan      VARCHAR(200),
    ip_address      VARCHAR(50),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ============================================================
-- DATA AWAL
-- ============================================================

-- User default:
--   admin     / admin123
--   operator1 / operator123
INSERT INTO users (username, password_hash, nama_lengkap, role) VALUES
('admin',     '$2a$12$1g2fJ5YZ6LaKvGGbMoFGZeeXNZZUZ8TXu03Ci5Jp/wOwVSLEuVQgq', 'Administrator', 'Admin'),
('operator1', '$2a$12$nTOGB45H76WKq9enNLi7du1gEZlfOBT7QC1klbnQpdT90zhGuKtAm', 'Operator Satu', 'Operator');

-- Golongan default
INSERT INTO golongan (id_golongan, nama_golongan, gaji_pokok, tunjangan_istri, tunjangan_anak, transport, uang_makan) VALUES
('G-01', 'Staff Junior',      5000000, 500000, 200000, 300000, 200000),
('G-02', 'Staff Senior',      6500000, 650000, 250000, 350000, 200000),
('G-03', 'Supervisor',        8000000, 800000, 300000, 400000, 250000),
('G-04', 'Manajer',          10000000, 1000000, 350000, 500000, 300000),
('G-05', 'General Manager',  15000000, 1500000, 400000, 750000, 350000);

-- Jenis cuti default
INSERT INTO jenis_cuti (nama_cuti, kuota_hari, is_dibayar, butuh_dokumen, keterangan) VALUES
('Cuti Tahunan',    12, TRUE,  FALSE, 'Hak cuti 12 hari per tahun sesuai UU 13/2003'),
('Cuti Sakit',       0, TRUE,  TRUE,  'Tidak terbatas, wajib surat dokter > 1 hari'),
('Cuti Melahirkan', 90, TRUE,  TRUE,  '1.5 bulan sebelum + 1.5 bulan sesudah melahirkan'),
('Keperluan Khusus', 0, TRUE,  FALSE, 'Nikah, menikahkan anak, keluarga meninggal, dll'),
('Alpha',            0, FALSE, FALSE, 'Tidak masuk tanpa keterangan, gaji dipotong');

-- UMP 2025
INSERT INTO ump_master (wilayah, tahun, nilai_ump) VALUES
('DKI Jakarta',     2025, 5067381),
('Jawa Barat',      2025, 2191238),
('Jawa Tengah',     2025, 2169349),
('Jawa Timur',      2025, 2165244),
('Banten',          2025, 2727812),
('DI Yogyakarta',   2025, 2264080),
('Bali',            2025, 2996560);

-- Hari libur 2025
INSERT INTO hari_libur (tanggal, keterangan) VALUES
('2025-01-01', 'Tahun Baru Masehi'),
('2025-01-27', 'Isra Miraj'),
('2025-01-29', 'Tahun Baru Imlek'),
('2025-03-29', 'Hari Raya Nyepi'),
('2025-03-31', 'Idul Fitri 1446 H'),
('2025-04-01', 'Idul Fitri 1446 H'),
('2025-04-18', 'Wafat Isa Almasih'),
('2025-04-20', 'Kebangkitan Isa Almasih'),
('2025-05-01', 'Hari Buruh Internasional'),
('2025-05-12', 'Hari Raya Waisak'),
('2025-05-29', 'Kenaikan Isa Almasih'),
('2025-06-01', 'Hari Lahir Pancasila'),
('2025-06-06', 'Idul Adha 1446 H'),
('2025-06-27', 'Tahun Baru Islam 1447 H'),
('2025-08-17', 'Hari Kemerdekaan RI'),
('2025-09-05', 'Maulid Nabi Muhammad SAW'),
('2025-12-25', 'Hari Raya Natal'),
('2025-12-26', 'Cuti Bersama Natal');

-- Karyawan contoh
INSERT INTO karyawan (id_karyawan, id_golongan, nama, jenis_kelamin, tempat_lahir, tanggal_lahir, alamat, status_nikah, jumlah_anak, npwp, tanggal_masuk, status_karyawan) VALUES
('KRY-001', 'G-01', 'Budi Santoso',   'Laki-laki',  'Jakarta',   '1990-05-10', 'Jl. Merdeka No.1, Jakarta',   'Menikah',       2, '12.345.678.9-012.345', '2022-03-01', 'Aktif'),
('KRY-002', 'G-02', 'Siti Rahayu',    'Perempuan',  'Bandung',   '1992-08-22', 'Jl. Sudirman No.5, Bandung',  'Belum Menikah', 0, '23.456.789.0-123.456', '2019-07-15', 'Aktif'),
('KRY-003', 'G-01', 'Andi Wijaya',    'Laki-laki',  'Surabaya',  '1998-11-30', 'Jl. Pahlawan No.3, Surabaya', 'Belum Menikah', 0, NULL,                   '2025-03-01', 'Probation'),
('KRY-004', 'G-03', 'Dewi Kusuma',    'Perempuan',  'Yogyakarta','1985-02-14', 'Jl. Malioboro No.10, Yogya',  'Menikah',       3, '34.567.890.1-234.567', '2017-01-10', 'Aktif');

-- Saldo cuti 2025
INSERT INTO saldo_cuti (id_karyawan, id_jenis, tahun, kuota, terpakai) VALUES
('KRY-001', 1, 2025, 12, 2),
('KRY-002', 1, 2025, 12, 7),
('KRY-004', 1, 2025, 12, 0);
