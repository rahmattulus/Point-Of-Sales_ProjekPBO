package repository;

import model.Admin;
import model.Kasir;
import model.Pengguna;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository untuk entitas {@link Pengguna} terhadap tabel {@code pengguna}.
 * Menerapkan pola Single Table Inheritance: menghasilkan objek {@link Admin}
 * atau {@link Kasir} berdasarkan kolom {@code role}.
 *
 * @author POS-KasirJAVA Refactor
 */
public class PenggunaRepository implements Repository<Pengguna> {

    @Override
    public boolean tambah(Pengguna pengguna) {
        String sql = "INSERT INTO pengguna (username, password, email, role) VALUES (?, ?, ?, ?)";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, pengguna.getUsername());
            ps.setString(2, pengguna.getPassword());
            ps.setString(3, pengguna.getEmail());
            ps.setString(4, pengguna.getRole());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        pengguna.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new DataAccessException("Gagal menambah pengguna '" + pengguna.getUsername() + "': " + e.getMessage(), e);
        }
    }

    @Override
    public boolean ubah(Pengguna pengguna) {
        String sql = "UPDATE pengguna SET username = ?, password = ?, email = ?, role = ? WHERE id = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, pengguna.getUsername());
            ps.setString(2, pengguna.getPassword());
            ps.setString(3, pengguna.getEmail());
            ps.setString(4, pengguna.getRole());
            ps.setInt(5, pengguna.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Gagal mengubah pengguna ID " + pengguna.getId() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public boolean hapus(String id) {
        String sql = "DELETE FROM pengguna WHERE id = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID pengguna harus berupa angka: " + id, e);
        } catch (SQLException e) {
            throw new DataAccessException("Gagal menghapus pengguna ID " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<Pengguna> cariSemua() {
        String sql = "SELECT id, username, password, email, role FROM pengguna ORDER BY id ASC";
        List<Pengguna> daftar = new ArrayList<>();
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                daftar.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Gagal mengambil data pengguna: " + e.getMessage(), e);
        }
        return daftar;
    }

    @Override
    public Pengguna cariById(String id) {
        String sql = "SELECT id, username, password, email, role FROM pengguna WHERE id = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID pengguna harus berupa angka: " + id, e);
        } catch (SQLException e) {
            throw new DataAccessException("Gagal mencari pengguna ID " + id + ": " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Mencari pengguna berdasarkan username saja.
     *
     * @param username username yang dicari
     * @return {@link Pengguna} (Admin atau Kasir) jika ditemukan, atau null
     */
    public Pengguna cariByUsername(String username) {
        String sql = "SELECT id, username, password, email, role FROM pengguna WHERE username = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Gagal mencari pengguna dengan username '" + username + "': " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Mencari pengguna berdasarkan username dan peran (role).
     *
     * @param username username yang dicari
     * @param peran    peran pengguna ("ADMIN" atau "KASIR")
     * @return {@link Pengguna} jika ditemukan dan perannya cocok, atau null
     */
    public Pengguna cariByUsername(String username, String peran) {
        String sql = "SELECT id, username, password, email, role FROM pengguna WHERE username = ? AND role = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, peran);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Gagal mencari pengguna '" + username + "' (" + peran + "): " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Memetakan baris ResultSet ke objek {@link Admin} atau {@link Kasir}
     * berdasarkan nilai kolom {@code role}.
     */
    private Pengguna mapRow(ResultSet rs) throws SQLException {
        int id          = rs.getInt("id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        String email    = rs.getString("email");
        String role     = rs.getString("role");

        if ("ADMIN".equalsIgnoreCase(role)) {
            return new Admin(id, username, password, email);
        } else if ("KASIR".equalsIgnoreCase(role)) {
            return new Kasir(id, username, password, email);
        } else {
            throw new IllegalStateException("Role tidak dikenal di database: " + role);
        }
    }
}

