import model.*;
import repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Uji coba CRUD Fase 3 (Package Repository) terhadap database MySQL kasir.
 */
public class TestRepository {
    public static void main(String[] args) {
        System.out.println("=== Test Package Repository (Fase 3) ===");

        BarangRepository barangRepo = new BarangRepository();
        MemberRepository memberRepo = new MemberRepository();
        SupplierRepository supplierRepo = new SupplierRepository();
        PenggunaRepository penggunaRepo = new PenggunaRepository();
        TransaksiRepository txRepo = new TransaksiRepository();

        try {
            // 1. Uji PenggunaRepository (Single Table Inheritance)
            System.out.println("--- 1. Uji PenggunaRepository ---");
            String usernameAdmin = "admin_test_" + System.currentTimeMillis();
            String usernameKasir = "kasir_test_" + System.currentTimeMillis();

            Admin newAdmin = new Admin(0, usernameAdmin, "passAdmin", "admin@test.com");
            Kasir newKasir = new Kasir(0, usernameKasir, "passKasir", "kasir@test.com");

            boolean okAdmin = penggunaRepo.tambah(newAdmin);
            boolean okKasir = penggunaRepo.tambah(newKasir);
            assert okAdmin && newAdmin.getId() > 0 : "Tambah Admin gagal";
            assert okKasir && newKasir.getId() > 0 : "Tambah Kasir gagal";
            System.out.println("[OK] Tambah Admin (ID=" + newAdmin.getId() + ") & Kasir (ID=" + newKasir.getId() + ") berhasil.");

            Pengguna foundKasir = penggunaRepo.cariByUsername(usernameKasir, "KASIR");
            assert foundKasir instanceof Kasir : "Objek harus berupa instance Kasir";
            assert foundKasir.getUsername().equals(usernameKasir);
            System.out.println("[OK] cariByUsername() mengembalikan objek turunan yang tepat (Polymorphism).");

            // 2. Uji BarangRepository
            System.out.println("--- 2. Uji BarangRepository ---");
            String kodeBarang = "TEST_" + (System.currentTimeMillis() % 10000);
            Barang b = new Barang(kodeBarang, "Barang Uji Coba", 1000, 2500, 50);
            boolean okB = barangRepo.tambah(b);
            assert okB : "Tambah barang gagal";

            Barang bFound = barangRepo.cariById(kodeBarang);
            assert bFound != null && bFound.getHargaJual() == 2500;

            bFound.setHargaJual(3000);
            barangRepo.ubah(bFound);
            Barang bUpdated = barangRepo.cariById(kodeBarang);
            assert bUpdated.getHargaJual() == 3000;
            System.out.println("[OK] Tambah, cariById, dan ubah Barang berhasil.");

            // 3. Uji MemberRepository
            System.out.println("--- 3. Uji MemberRepository ---");
            String idMember = "MBR_" + (System.currentTimeMillis() % 10000);
            Member m = new Member(idMember, "Member Uji", "08123456789", 100);
            boolean okM = memberRepo.tambah(m);
            assert okM : "Tambah member gagal";

            Member mFound = memberRepo.cariById(idMember);
            assert mFound != null && mFound.getPoin() == 100;
            System.out.println("[OK] Tambah & cariById Member berhasil.");

            // 4. Uji SupplierRepository
            System.out.println("--- 4. Uji SupplierRepository ---");
            Supplier sp = new Supplier(0, "PT Test Jaya", "test@ptjaya.com", "123456", "987654", "Jl. Test No. 1");
            boolean okSp = supplierRepo.tambah(sp);
            assert okSp && sp.getId() > 0 : "Tambah supplier gagal";

            Supplier spFound = supplierRepo.cariById(String.valueOf(sp.getId()));
            assert spFound != null && "PT Test Jaya".equals(spFound.getNama());
            System.out.println("[OK] Tambah (auto-increment ID=" + sp.getId() + ") & cariById Supplier berhasil.");

            // 5. Uji TransaksiRepository & detail_transaksi
            System.out.println("--- 5. Uji TransaksiRepository ---");
            String noTx = "TX_" + (System.currentTimeMillis() % 10000);
            List<ItemTransaksi> items = new ArrayList<>();
            items.add(new ItemTransaksi(bUpdated, 2, bUpdated.getHargaJual()));

            Transaksi tx = new Transaksi(noTx, newKasir, LocalDateTime.now(), items);
            boolean okTx = txRepo.tambah(tx);
            assert okTx : "Tambah transaksi gagal";

            Transaksi txFound = txRepo.cariById(noTx);
            assert txFound != null : "Transaksi harus ditemukan";
            assert txFound.getTotal() == 6000 : "Total transaksi harus 6000 (2 * 3000)";
            assert txFound.getItems().size() == 1 : "Item detail transaksi harus berjumlah 1";
            assert txFound.getItems().get(0).getQty() == 2;
            System.out.println("[OK] Tambah transaksi & simpan detail_transaksi berhasil.");

            String nomorTerakhir = txRepo.cariNomorTerakhir();
            assert nomorTerakhir != null;
            System.out.println("[OK] cariNomorTerakhir() = " + nomorTerakhir);

            // 6. Cleanup data uji agar DB tetap bersih
            System.out.println("--- 6. Cleanup Data Uji ---");
            txRepo.hapus(noTx); // CASCADE menghapus detail_transaksi
            barangRepo.hapus(kodeBarang);
            memberRepo.hapus(idMember);
            supplierRepo.hapus(String.valueOf(sp.getId()));
            penggunaRepo.hapus(String.valueOf(newAdmin.getId()));
            penggunaRepo.hapus(String.valueOf(newKasir.getId()));
            System.out.println("[OK] Data uji berhasil dibersihkan.");

            System.out.println("\n[LULUS SEMUA] Fase 3 selesai - Seluruh repository CRUD dan relasi berfungsi dengan sempurna!");

        } catch (Exception e) {
            System.err.println("[GAGAL] Error pengujian repository: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}

