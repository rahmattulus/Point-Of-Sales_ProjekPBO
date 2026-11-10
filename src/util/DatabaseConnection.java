package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton untuk mengelola koneksi ke database MySQL.
 * <p>
 * Hanya membuat satu objek {@code Connection} selama aplikasi berjalan.
 * Koneksi dibuat ulang secara otomatis jika sudah tertutup.
 * Tidak memakai {@code JOptionPane} di sini; error dilempar sebagai
 * {@link repository.DataAccessException} agar layer {@code view} yang
 * memutuskan cara menampilkan pesan.
 * </p>
 *
 * @author POS-KasirJAVA Refactor
 */
public class DatabaseConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/kasir";
    private static final String USER     = "root";
    private static final String PASSWORD = "";

    /** Satu-satunya instance (Singleton). */
    private static DatabaseConnection instance;

    /** Objek koneksi yang dipakai bersama. */
    private Connection connection;

    /** Constructor private agar tidak bisa di-instantiate dari luar. */
    private DatabaseConnection() {
        // dibuat lewat getInstance()
    }

    /**
     * Mengembalikan satu-satunya instance {@code DatabaseConnection}.
     * Thread-safe dengan synchronized.
     *
     * @return instance singleton {@code DatabaseConnection}
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Mengembalikan objek {@code Connection} yang aktif.
     * Jika koneksi belum pernah dibuat atau sudah tertutup,
     * metode ini akan membuat koneksi baru secara otomatis.
     *
     * @return {@code Connection} yang siap dipakai
     * @throws repository.DataAccessException jika koneksi gagal dibuat
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            throw new repository.DataAccessException(
                "Gagal membuka koneksi ke database: " + e.getMessage(), e
            );
        }
        return connection;
    }
}

