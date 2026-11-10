package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Merepresentasikan keranjang belanja transaksi kasir saat ini.
 * Mengelola daftar {@link ItemTransaksi}, penambahan item, perubahan qty,
 * validasi ketersediaan stok, dan perhitungan total belanja.
 *
 * @author POS-KasirJAVA Refactor
 */
public class Keranjang {

    private final List<ItemTransaksi> items;

    /**
     * Membuat objek Keranjang baru dengan daftar item kosong.
     */
    public Keranjang() {
        this.items = new ArrayList<>();
    }

    /**
     * Menambahkan barang ke dalam keranjang.
     * Jika kode barang sudah ada di keranjang, kuantitas akan ditambah 1.
     * Jika belum ada, item baru akan ditambahkan dengan qty 1.
     *
     * @param barang objek Barang yang ditambahkan
     * @throws IllegalArgumentException jika stok barang tidak mencukupi
     */
    public void tambahItem(Barang barang) {
        if (barang == null) {
            throw new IllegalArgumentException("Barang tidak boleh null.");
        }

        ItemTransaksi existingItem = cariItemByKode(barang.getKode());
        if (existingItem != null) {
            int newQty = existingItem.getQty() + 1;
            if (!barang.cekStok(newQty)) {
                throw new IllegalArgumentException(
                    "Stok tidak cukup untuk menambah qty barang '" + barang.getNama() + "'."
                );
            }
            existingItem.setQty(newQty);
        } else {
            if (!barang.cekStok(1)) {
                throw new IllegalArgumentException(
                    "Stok habis untuk barang '" + barang.getNama() + "'."
                );
            }
            items.add(new ItemTransaksi(barang, 1));
        }
    }

    /**
     * Mengubah kuantitas item berdasarkan kode barang.
     * Jika {@code qty <= 0}, item akan dihapus dari keranjang.
     *
     * @param kode kode unik barang
     * @param qty  kuantitas baru
     * @throws IllegalArgumentException jika barang tidak ditemukan atau qty melebihi stok
     */
    public void ubahQty(String kode, int qty) {
        ItemTransaksi item = cariItemByKode(kode);
        if (item == null) {
            throw new IllegalArgumentException("Barang dengan kode '" + kode + "' tidak ada di keranjang.");
        }

        if (qty <= 0) {
            items.remove(item);
            return;
        }

        if (!item.getBarang().cekStok(qty)) {
            throw new IllegalArgumentException(
                "Stok barang '" + item.getBarang().getNama() + "' tidak cukup untuk qty " + qty + "."
            );
        }

        item.setQty(qty);
    }

    /**
     * Menghitung total harga belanja seluruh item dalam keranjang.
     *
     * @return total harga belanja
     */
    public int hitungTotal() {
        int total = 0;
        for (ItemTransaksi item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    /**
     * Mengosongkan keranjang belanja.
     */
    public void kosongkan() {
        items.clear();
    }

    /**
     * Mengembalikan daftar item transaksi yang bersifat read-only (unmodifiable).
     *
     * @return daftar {@link ItemTransaksi}
     */
    public List<ItemTransaksi> getItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Helper privat untuk mencari ItemTransaksi berdasarkan kode barang.
     *
     * @param kode kode barang yang dicari
     * @return ItemTransaksi jika ditemukan, atau null jika belum ada
     */
    private ItemTransaksi cariItemByKode(String kode) {
        for (ItemTransaksi item : items) {
            if (item.getBarang().getKode().equalsIgnoreCase(kode)) {
                return item;
            }
        }
        return null;
    }
}

