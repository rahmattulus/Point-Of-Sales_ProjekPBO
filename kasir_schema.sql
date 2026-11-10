-- =====================================================================
-- Skema database POS Kasir (database: kasir)
-- Disusun dari query yang ada di kode (pengguna [admin/kasir], member,
-- supplier, databarang, transaksi) + tabel baru detail_transaksi.
-- Cocok untuk MySQL 5.7+ / MariaDB 10.x (XAMPP).
-- Catatan: jika database lama sudah berisi data, backup dulu. Skrip
-- ini membuat tabel baru dari nol (DROP TABLE di bagian atas).
-- =====================================================================

CREATE DATABASE IF NOT EXISTS kasir
  CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE kasir;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS detail_transaksi;
DROP TABLE IF EXISTS transaksi;
DROP TABLE IF EXISTS databarang;
DROP TABLE IF EXISTS supplier;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS pengguna;
SET FOREIGN_KEY_CHECKS = 1;

-- ---------------------------------------------------------------------
-- Akun pengguna tunggal (Single Table Inheritance: Admin & Kasir)
-- role: 'ADMIN' atau 'KASIR'
-- password VARCHAR(255) agar muat hash (mis. BCrypt/SHA-256)
-- ---------------------------------------------------------------------
CREATE TABLE pengguna (
  id        INT                    NOT NULL AUTO_INCREMENT,
  username  VARCHAR(50)            NOT NULL,
  password  VARCHAR(255)           NOT NULL,
  email     VARCHAR(100)           NULL,
  role      ENUM('ADMIN', 'KASIR') NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_pengguna_username (username)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Data master
-- ---------------------------------------------------------------------
CREATE TABLE member (
  id_member VARCHAR(30) NOT NULL,
  username  VARCHAR(50) NOT NULL,
  no_tlp    VARCHAR(20) NULL,
  poin      INT         NOT NULL DEFAULT 0,
  PRIMARY KEY (id_member)
) ENGINE=InnoDB;

-- Di kode lama supplier tidak punya kunci utama dan dihapus lewat nama.
-- Di sini ditambah id_supplier agar hapus/ubah lewat id (lebih aman).
CREATE TABLE supplier (
  id_supplier INT          NOT NULL AUTO_INCREMENT,
  nama        VARCHAR(100) NOT NULL,
  email       VARCHAR(100) NULL,
  noRek       VARCHAR(30)  NULL,
  npwp        VARCHAR(30)  NULL,
  alamat      VARCHAR(255) NULL,
  PRIMARY KEY (id_supplier)
) ENGINE=InnoDB;

CREATE TABLE databarang (
  code_barang  VARCHAR(30)  NOT NULL,
  nama_barang  VARCHAR(100) NOT NULL,
  harga_beli   INT          NOT NULL DEFAULT 0,
  harga_jual   INT          NOT NULL DEFAULT 0,
  stok_barang  INT          NOT NULL DEFAULT 0,
  PRIMARY KEY (code_barang),
  CONSTRAINT chk_barang_stok CHECK (stok_barang >= 0)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Transaksi (header) -> class Transaksi
-- Perubahan dari kode lama:
--   petugas_kasir (teks username) diganti id_petugas (FK ke pengguna)
--   waktu_transaksi dari teks menjadi DATETIME
--   no_transaksi format TSK001, TSK002, ... (dibuat oleh aplikasi)
-- ---------------------------------------------------------------------
CREATE TABLE transaksi (
  no_transaksi    VARCHAR(10) NOT NULL,
  id_petugas      INT         NOT NULL,
  total           INT         NOT NULL,
  waktu_transaksi DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (no_transaksi),
  KEY idx_transaksi_petugas (id_petugas),
  CONSTRAINT fk_transaksi_petugas
    FOREIGN KEY (id_petugas) REFERENCES pengguna (id)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Rincian transaksi (BARU) -> class ItemTransaksi
-- harga_satuan disimpan sebagai salinan harga saat transaksi terjadi,
-- agar laporan lama tidak berubah ketika harga_jual barang diubah.
-- Subtotal tidak disimpan: dihitung dari harga_satuan * qty.
-- ---------------------------------------------------------------------
CREATE TABLE detail_transaksi (
  id_detail    INT         NOT NULL AUTO_INCREMENT,
  no_transaksi VARCHAR(10) NOT NULL,
  code_barang  VARCHAR(30) NOT NULL,
  harga_satuan INT         NOT NULL,
  qty          INT         NOT NULL,
  PRIMARY KEY (id_detail),
  KEY idx_detail_transaksi (no_transaksi),
  KEY idx_detail_barang (code_barang),
  CONSTRAINT chk_detail_qty CHECK (qty > 0),
  CONSTRAINT fk_detail_transaksi
    FOREIGN KEY (no_transaksi) REFERENCES transaksi (no_transaksi)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_detail_barang
    FOREIGN KEY (code_barang) REFERENCES databarang (code_barang)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Contoh data awal (opsional, hapus tanda komentar bila perlu)
-- Ganti password dengan milik Anda; jangan memakai nilai contoh ini.
-- ---------------------------------------------------------------------
-- INSERT INTO pengguna (username, password, email, role) VALUES ('admin', 'admin123', 'admin@example.com', 'ADMIN');
-- INSERT INTO pengguna (username, password, email, role) VALUES ('kasir1', 'kasir123', 'kasir1@example.com', 'KASIR');
-- INSERT INTO databarang VALUES ('1001', 'Contoh Barang', 4000, 5000, 50);
