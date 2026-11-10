import model.*;
import repository.*;
import service.*;

/**
 * Uji coba alur kerja Fase 4 (Package Service):
 * - Skenario A: Login & Sesi
 * - Skenario B: Tambah barang ke Keranjang lewat TransaksiService
 * - Skenario C: Pembayaran transaksi DB (Commit) dan Rollback jika gagal
 */
public class TestService {
    public static void main(String[] args) {
        System.out.println("=== Test Package Service (Fase 4) ===");

        AuthService authService = new AuthService();
        TransaksiService txService = new TransaksiService();
        BarangRepository barangRepo = new BarangRepository();
        PenggunaRepository penggunaRepo = new PenggunaRepository();
        TransaksiRepository txRepo = new TransaksiRepository();

        // Siapkan data dummy akun & barang untuk pengujian
        String userAdmin = "adm_svc_" + System.currentTimeMillis();
        String userKasir = "ksr_svc_" + System.currentTimeMillis();
        String kodeBarang = "BRG_SVC_" + (System.currentTimeMillis() % 10000);

        Admin testAdmin = new Admin(0, userAdmin, "pass123", "adm@pos.test");
        Kasir testKasir = new Kasir(0, userKasir, "passKasir", "ksr@pos.test");
        Barang testBarang = new Barang(kodeBarang, "Kopi Sachet", 1500, 2500, 10);

        try {
            penggunaRepo.tambah(testAdmin);
            penggunaRepo.tambah(testKasir);
            barangRepo.tambah(testBarang);

            // ==========================================
            // Skenario A: Login & Sesi
            // ==========================================
            System.out.println("\n--- Skenario A: Login & Sesi ---");
            // 1. Login gagal karena password salah
            Pengguna loginGagal = authService.login(userKasir, "salahPassword", "KASIR");
            assert loginGagal == null : "Login dengan password salah harus null";
            assert !Sesi.isLogin() : "Sesi tidak boleh aktif jika login gagal";

            // 2. Login gagal karena role tidak cocok
            Pengguna loginWrongRole = authService.login(userKasir, "passKasir", "ADMIN");
            assert loginWrongRole == null : "Login kasir sebagai admin harus gagal";

            // 3. Login sukses sebagai Kasir
            Pengguna loginSukses = authService.login(userKasir, "passKasir", "KASIR");
            assert loginSukses != null : "Login kasir seharusnya berhasil";
            assert Sesi.isLogin() : "Sesi harus aktif";
            assert Sesi.getPenggunaAktif().getId() == testKasir.getId();
            System.out.println("[OK] Skenario A (Login & Sesi) lulus uji.");

            // ==========================================
            // Skenario B: Tambah Barang ke Keranjang
            // ==========================================
            System.out.println("\n--- Skenario B: Tambah Barang ke Keranjang ---");
            Keranjang keranjang = new Keranjang();

            // Tambah barang valid
            boolean okTambah1 = txService.tambahKeKeranjang(keranjang, kodeBarang);
            assert okTambah1 : "Tambah barang valid harus true";
            assert keranjang.getItems().size() == 1;
            assert keranjang.getItems().get(0).getQty() == 1;

            // Tambah barang yang sama lagi (qty harus naik)
            boolean okTambah2 = txService.tambahKeKeranjang(keranjang, kodeBarang);
            assert okTambah2;
            assert keranjang.getItems().get(0).getQty() == 2;
            assert keranjang.hitungTotal() == 5000 : "Total harus 5000 (2 * 2500)";

            // Tambah kode barang fiktif (harus false)
            boolean okFiktif = txService.tambahKeKeranjang(keranjang, "KODE_TIDAK_ADA_999");
            assert !okFiktif : "Kode tidak ada harus mengembalikan false";
            System.out.println("[OK] Skenario B (Tambah ke Keranjang) lulus uji.");

            // ==========================================
            // Skenario C: Pembayaran Sukses (Commit)
            // ==========================================
            System.out.println("\n--- Skenario C: Pembayaran Sukses & Update Stok ---");
            int bayarNominal = 10000;
            Transaksi txResult = txService.bayar(keranjang, bayarNominal);

            assert txResult != null : "Hasil transaksi tidak boleh null";
            assert txResult.getTotal() == 5000;
            assert txResult.hitungKembalian(bayarNominal) == 5000;

            // Verifikasi stok di DB berkurang dari 10 menjadi 8
            Barang barangAfter = barangRepo.cariById(kodeBarang);
            assert barangAfter.getStok() == 8 : "Stok di database harus berkurang menjadi 8, sekarang: " + barangAfter.getStok();
            System.out.println("[OK] Pembayaran berhasil. Stok di database terpotong dari 10 menjadi " + barangAfter.getStok());

            // ==========================================
            // Skenario D: Pembayaran Gagal & Rollback Atomik
            // ==========================================
            System.out.println("\n--- Skenario D: Rollback DB Saat Pembayaran Gagal ---");
            // Keranjang baru minta 15 pcs padahal sisa stok hanya 8
            Keranjang keranjangMelebihi = new Keranjang();
            // Manipulasi barang dummy agar keranjang bisa dibuat dengan qty 15
            Barang barangStokBesarSementara = new Barang(kodeBarang, "Kopi Sachet", 1500, 2500, 20);
            keranjangMelebihi.tambahItem(barangStokBesarSementara);
            keranjangMelebihi.ubahQty(kodeBarang, 15);

            boolean rollbackBerhasil = false;
            try {
                txService.bayar(keranjangMelebihi, 50000);
            } catch (DataAccessException e) {
                rollbackBerhasil = true;
                System.out.println("[OK] Terdeteksi kegagalan stok: " + e.getMessage());
            }
            assert rollbackBerhasil : "Harus melempar exception dan rollback jika stok di DB tidak mencukupi";

            // Pastikan stok di DB tetap 8 (tidak berkurang atau korup)
            Barang barangAfterRollback = barangRepo.cariById(kodeBarang);
            assert barangAfterRollback.getStok() == 8 : "Stok harus tetap 8 setelah rollback";
            System.out.println("[OK] Rollback database terbukti bekerja! Stok DB tetap: " + barangAfterRollback.getStok());

            // ==========================================
            // Cleanup Data Uji
            // ==========================================
            txRepo.hapus(txResult.getNoTransaksi());
            barangRepo.hapus(kodeBarang);
            penggunaRepo.hapus(String.valueOf(testAdmin.getId()));
            penggunaRepo.hapus(String.valueOf(testKasir.getId()));
            authService.logout();

            System.out.println("\n[LULUS SEMUA] Fase 4 selesai - Service layer & transaksi DB berfungsi sempurna!");

        } catch (Exception e) {
            System.err.println("[GAGAL] Error pengujian service: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}

