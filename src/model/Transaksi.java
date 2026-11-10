package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Merepresentasikan data transaksi penjualan yang telah atau sedang diproses.
 * Memuat nomor transaksi, petugas kasir, waktu transaksi, rincian item, dan total.
 *
 * @author POS-KasirJAVA Refactor
 */
public class Transaksi {

    private String              noTransaksi;
    private Kasir               kasir;
    private LocalDateTime       waktu;
    private List<ItemTransaksi> items;
    private int                 total;

    /**
     * Membuat objek Transaksi baru dari keranjang belanja.
     * Total dihitung secara otomatis dari daftar item.
     *
     * @param noTransaksi nomor faktur transaksi (mis. "TSK001")
     * @param kasir       petugas kasir yang melayani
     * @param waktu       waktu transaksi dicatat
     * @param items       daftar item yang dibeli
     */
    public Transaksi(String noTransaksi, Kasir kasir, LocalDateTime waktu, List<ItemTransaksi> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Transaksi harus memiliki minimal satu item.");
        }
        this.noTransaksi = noTransaksi;
        this.kasir       = kasir;
        this.waktu       = (waktu != null) ? waktu : LocalDateTime.now();
        this.items       = new ArrayList<>(items);
        this.total       = hitungTotalDariItems();
    }

    /**
     * Membuat objek Transaksi dengan data eksplisit (berguna saat membaca riwayat dari database).
     *
     * @param noTransaksi nomor faktur transaksi
     * @param kasir       petugas kasir
     * @param waktu       waktu transaksi
     * @param total       total nominal transaksi
     */
    public Transaksi(String noTransaksi, Kasir kasir, LocalDateTime waktu, int total) {
        this.noTransaksi = noTransaksi;
        this.kasir       = kasir;
        this.waktu       = (waktu != null) ? waktu : LocalDateTime.now();
        this.items       = new ArrayList<>();
        this.total       = total;
    }

    /**
     * Menghitung total nilai transaksi dari daftar item transaksi saat ini.
     *
     * @return total transaksi
     */
    private int hitungTotalDariItems() {
        int t = 0;
        for (ItemTransaksi item : items) {
            t += item.getSubtotal();
        }
        return t;
    }

    /**
     * Menghitung uang kembalian berdasarkan nominal yang dibayarkan pelanggan.
     *
     * @param bayar nominal uang yang dibayarkan
     * @return nominal uang kembalian
     * @throws IllegalArgumentException jika uang pembayaran kurang dari total
     */
    public int hitungKembalian(int bayar) {
        if (bayar < total) {
            throw new IllegalArgumentException(
                "Pembayaran kurang. Total: " + total + ", Dibayar: " + bayar + ", Kurang: " + (total - bayar)
            );
        }
        return bayar - total;
    }

    // --- Getter & Setter ---

    public String getNoTransaksi() {
        return noTransaksi;
    }

    public void setNoTransaksi(String noTransaksi) {
        this.noTransaksi = noTransaksi;
    }

    public Kasir getKasir() {
        return kasir;
    }

    public void setKasir(Kasir kasir) {
        this.kasir = kasir;
    }

    public LocalDateTime getWaktu() {
        return waktu;
    }

    public void setWaktu(LocalDateTime waktu) {
        this.waktu = waktu;
    }

    public List<ItemTransaksi> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void setItems(List<ItemTransaksi> items) {
        this.items = (items != null) ? new ArrayList<>(items) : new ArrayList<>();
        this.total = hitungTotalDariItems();
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}

