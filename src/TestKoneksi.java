import util.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Script uji sederhana untuk memverifikasi DatabaseConnection (Fase 1).
 * Jalankan: javac -cp lib/mysql-connector-j-26.10.0.jar;src src/TestKoneksi.java -d out
 *           java  -cp lib/mysql-connector-j-26.10.0.jar;src;out TestKoneksi
 */
public class TestKoneksi {
    public static void main(String[] args) {
        System.out.println("=== Test DatabaseConnection (Fase 1) ===");
        try {
            DatabaseConnection db = DatabaseConnection.getInstance();
            Connection con = db.getConnection();

            // Uji SELECT 1
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("SELECT 1 AS ok");
            if (rs.next()) {
                System.out.println("[OK] Koneksi berhasil! SELECT 1 = " + rs.getInt("ok"));
            }
            rs.close();
            stm.close();

            // Uji tabel pengguna sudah ada
            Statement stm2 = con.createStatement();
            ResultSet rs2 = stm2.executeQuery("SELECT COUNT(*) AS jml FROM pengguna");
            if (rs2.next()) {
                System.out.println("[OK] Tabel 'pengguna' ditemukan. Jumlah baris: " + rs2.getInt("jml"));
            }
            rs2.close();
            stm2.close();

            System.out.println("\n[LULUS] Fase 1 selesai - DatabaseConnection berfungsi.");

        } catch (repository.DataAccessException e) {
            System.err.println("[GAGAL] Error koneksi: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[GAGAL] Error tidak terduga: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

