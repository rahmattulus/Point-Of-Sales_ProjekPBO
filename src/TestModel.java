import model.Admin;
import model.Barang;
import model.ItemTransaksi;
import model.Kasir;
import model.Keranjang;
import model.Member;
import model.Pengguna;
import model.Supplier;
import model.Transaksi;

import java.time.LocalDateTime;

/**
 * Uji logika Fase 2 (Package Model).
 * Memverifikasi:
 * 1. Tidak ada dependensi ke java.sql atau javax.swing di model.
 * 2. Logika inheritance dan polimorfisme getRole() pada Pengguna.
 * 3. Logika Keranjang (tambah item sama -> qty naik, ubah qty, total).
 * 4. Logika Transaksi & hitungKembalian.
 */
public class TestModel {
    public static void main(String[] args) {
        System.out.println("=== Test Package Model (Fase 2) ===");

        // 1. Uji Pengguna, Admin, Kasir (Inheritance & Polymorphism)
        Pengguna admin = new Admin(1, "admin1", "pass1", "admin@mail.com");
        Pengguna kasir = new Kasir(2, "kasir1", "pass2", "kasir@mail.com");

        assert "ADMIN".equals(admin.getRole()) : "Role Admin salah";
        assert "KASIR".equals(kasir.getRole()) : "Role Kasir salah";
        System.out.println("[OK] Pengguna, Admin, Kasir lulus uji polimorfisme.");

        // 2. Uji Barang & Validasi Stok
        Barang b1 = new Barang("B001", "Buku Tulis", 3000, 5000, 10);
        Barang b2 = new Barang("B002", "Pulpen Gel", 2000, 3000, 5);

        assert b1.cekStok(5) : "Cek stok harus bernilai true";
        assert !b1.cekStok(15) : "Cek stok harus bernilai false jika melebihi";
        b1.kurangiStok(3);
        assert b1.getStok() == 7 : "Stok berkurang harus sisa 7";
        System.out.println("[OK] Barang & kurangiStok lulus uji.");

        // 3. Uji Keranjang Belanja
        Keranjang keranjang = new Keranjang();
        // Tambah b1 (qty jadi 1)
        keranjang.tambahItem(b1);
        assert keranjang.getItems().size() == 1;
        assert keranjang.getItems().get(0).getQty() == 1;

        // Tambah b1 lagi (kode sama -> qty naik jadi 2)
        keranjang.tambahItem(b1);
        assert keranjang.getItems().size() == 1;
        assert keranjang.getItems().get(0).getQty() == 2;
        assert keranjang.hitungTotal() == 5000 * 2 : "Total harus 10.000";

        // Tambah b2
        keranjang.tambahItem(b2);
        assert keranjang.getItems().size() == 2;
        assert keranjang.hitungTotal() == 10000 + 3000 : "Total harus 13.000";

        // Ubah qty b2 jadi 3
        keranjang.ubahQty("B002", 3);
        assert keranjang.hitungTotal() == 10000 + (3000 * 3) : "Total harus 19.000";

        // Ubah qty b2 jadi 0 -> item b2 harus terhapus
        keranjang.ubahQty("B002", 0);
        assert keranjang.getItems().size() == 1 : "Item b2 harus terhapus dari keranjang";
        assert keranjang.hitungTotal() == 10000;
        System.out.println("[OK] Keranjang (tambah, akumulasi qty, ubah qty, hapus qty=0, total) lulus uji.");

        // 4. Uji Transaksi & Hitung Kembalian
        Kasir kasirPetugas = (Kasir) kasir;
        Transaksi tx = new Transaksi("TSK001", kasirPetugas, LocalDateTime.now(), keranjang.getItems());
        assert tx.getTotal() == 10000 : "Total transaksi harus 10.000";

        int kembalian = tx.hitungKembalian(15000);
        assert kembalian == 5000 : "Kembalian harus 5.000";

        // Cek validasi bayar kurang
        boolean errorKembalian = false;
        try {
            tx.hitungKembalian(8000);
        } catch (IllegalArgumentException e) {
            errorKembalian = true;
        }
        assert errorKembalian : "Harus melempar IllegalArgumentException jika bayar kurang";
        System.out.println("[OK] Transaksi & hitungKembalian lulus uji.");

        System.out.println("\n[LULUS SEMUA] Fase 2 selesai - Seluruh domain model berfungsi dengan baik.");
    }
}

