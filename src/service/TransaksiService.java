package service;

import model.Barang;
import model.ItemTransaksi;
import model.Kasir;
import model.Keranjang;
import model.Pengguna;
import model.Transaksi;
import repository.BarangRepository;
import repository.DataAccessException;
import repository.TransaksiRepository;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Service untuk menangani proses transaksi penjualan di kasir.
 * Mengelola alur penambahan barang ke keranjang, pembuatan nomor faktur otomatis,
 * serta proses pembayaran dengan transaksi database yang atomik (ACID commit/rollback).
 *
 * @author POS-KasirJAVA Refactor
 */
public class TransaksiService {

    private final BarangRepository    barangRepo;
    private final TransaksiRepository transaksiRepo;

    /**
     * Membuat TransaksiService dengan repository default.
     */
    public TransaksiService() {
        this.barangRepo    = new BarangRepository();
        this.transaksiRepo = new TransaksiRepository();
    }

    /**
     * Membuat TransaksiService dengan dependensi repository yang disuntikkan.
     *
     * @param barangRepo    repository barang
     * @param transaksiRepo repository transaksi
     */
    public TransaksiService(BarangRepository barangRepo, TransaksiRepository transaksiRepo) {
        this.barangRepo    = barangRepo;
        this.transaksiRepo = transaksiRepo;
    }

    /**
     * Membuat nomor faktur transaksi baru secara berurutan (mis. TSK001, TSK002).
     * Dibuat berdasarkan nomor transaksi terakhir di database.
     *
     * @return nomor faktur transaksi baru
     */
    public String buatNomorFaktur() {
        String noTerakhir = transaksiRepo.cariNomorTerakhir();
        if (noTerakhir == null || !noTerakhir.toUpperCase().startsWith("TSK")) {
            return "TSK001";
        }

        try {
            String numPart = noTerakhir.substring(3).trim();
            int nextNumber = Integer.parseInt(numPart) + 1;
            return String.format("TSK%03d", nextNumber);
        } catch (Exception e) {
            return "TSK001";
        }
    }

    /**
     * Menambahkan barang ke dalam keranjang belanja berdasarkan kode barang.
     *
     * @param keranjang keranjang belanja aktif
     * @param kode      kode barang yang diinput
     * @return {@code true} jika barang ditemukan dan berhasil ditambahkan ke keranjang,
     *         {@code false} jika barang tidak ditemukan atau stok tidak mencukupi
     */
    public boolean tambahKeKeranjang(Keranjang keranjang, String kode) {
        if (keranjang == null || kode == null || kode.trim().isEmpty()) {
            return false;
        }

        Barang barang = barangRepo.cariById(kode.trim());
        if (barang == null) {
            return false;
        }

        try {
            keranjang.tambahItem(barang);
            return true;
        } catch (IllegalArgumentException e) {
            // Stok habis atau tidak mencukupi
            return false;
        }
    }

    /**
     * Memproses pembayaran transaksi kasir secara atomik dalam satu transaksi database.
     * Jika terjadi kegagalan (misal stok tiba-tiba kurang atau error DB),
     * seluruh perubahan akan dibatalkan (rollback).
     *
     * @param keranjang keranjang belanja yang berisi item pembelian
     * @param nominal   jumlah uang tunai yang dibayarkan oleh pembeli
     * @return objek {@link Transaksi} yang berhasil disimpan
     * @throws IllegalArgumentException jika keranjang kosong atau nominal bayar kurang
     * @throws IllegalStateException    jika tidak ada kasir yang login
     * @throws DataAccessException      jika proses simpan database gagal
     */
    public Transaksi bayar(Keranjang keranjang, int nominal) {
        if (keranjang == null || keranjang.getItems().isEmpty()) {
            throw new IllegalArgumentException("Keranjang belanja masih kosong.");
        }

        int total = keranjang.hitungTotal();
        if (nominal < total) {
            throw new IllegalArgumentException(
                "Pembayaran kurang. Total belanja: " + total + ", dibayar: " + nominal
            );
        }

        Pengguna penggunaAktif = Sesi.getPenggunaAktif();
        if (penggunaAktif == null) {
            throw new IllegalStateException("Tidak ada petugas kasir yang sedang login di sesi.");
        }

        Kasir kasir;
        if (penggunaAktif instanceof Kasir) {
            kasir = (Kasir) penggunaAktif;
        } else {
            // Jika akun admin sedang mengoperasikan kasir
            kasir = new Kasir(
                penggunaAktif.getId(),
                penggunaAktif.getUsername(),
                penggunaAktif.getPassword(),
                penggunaAktif.getEmail()
            );
        }

        String noFaktur = buatNomorFaktur();
        Transaksi transaksi = new Transaksi(noFaktur, kasir, LocalDateTime.now(), keranjang.getItems());

        // Jalankan transaksi database (ACID)
        Connection con = DatabaseConnection.getInstance().getConnection();
        boolean originalAutoCommit = true;

        try {
            originalAutoCommit = con.getAutoCommit();
            con.setAutoCommit(false);

            // 1. Simpan header transaksi dan detail_transaksi
            transaksiRepo.tambah(transaksi);

            // 2. Kurangi stok setiap barang di database
            for (ItemTransaksi item : transaksi.getItems()) {
                Barang b = barangRepo.cariById(item.getBarang().getKode());
                if (b == null) {
                    throw new IllegalStateException(
                        "Barang dengan kode '" + item.getBarang().getKode() + "' tidak ditemukan."
                    );
                }
                b.kurangiStok(item.getQty());
                barangRepo.ubah(b);
            }

            con.commit();
            return transaksi;

        } catch (Exception e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                // Ignore rollback exception
            }
            throw new DataAccessException("Proses pembayaran gagal dan dibatalkan (rollback): " + e.getMessage(), e);

        } finally {
            try {
                con.setAutoCommit(originalAutoCommit);
            } catch (SQLException e) {
                // Ignore
            }
        }
    }
}

