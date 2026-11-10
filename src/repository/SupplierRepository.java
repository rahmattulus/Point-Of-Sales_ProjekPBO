package repository;

import model.Supplier;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository untuk entitas {@link Supplier} terhadap tabel {@code supplier}.
 * Menggunakan {@link PreparedStatement} dan kunci utama {@code id_supplier} auto-increment.
 *
 * @author POS-KasirJAVA Refactor
 */
public class SupplierRepository implements Repository<Supplier> {

    @Override
    public boolean tambah(Supplier supplier) {
        String sql = "INSERT INTO supplier (nama, email, noRek, npwp, alamat) VALUES (?, ?, ?, ?, ?)";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, supplier.getNama());
            ps.setString(2, supplier.getEmail());
            ps.setString(3, supplier.getNoRek());
            ps.setString(4, supplier.getNpwp());
            ps.setString(5, supplier.getAlamat());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        supplier.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new DataAccessException("Gagal menambah supplier '" + supplier.getNama() + "': " + e.getMessage(), e);
        }
    }

    @Override
    public boolean ubah(Supplier supplier) {
        String sql = "UPDATE supplier SET nama = ?, email = ?, noRek = ?, npwp = ?, alamat = ? WHERE id_supplier = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, supplier.getNama());
            ps.setString(2, supplier.getEmail());
            ps.setString(3, supplier.getNoRek());
            ps.setString(4, supplier.getNpwp());
            ps.setString(5, supplier.getAlamat());
            ps.setInt(6, supplier.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Gagal mengubah supplier ID " + supplier.getId() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public boolean hapus(String id) {
        String sql = "DELETE FROM supplier WHERE id_supplier = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID supplier harus berupa angka: " + id, e);
        } catch (SQLException e) {
            throw new DataAccessException("Gagal menghapus supplier ID " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<Supplier> cariSemua() {
        String sql = "SELECT id_supplier, nama, email, noRek, npwp, alamat FROM supplier ORDER BY id_supplier ASC";
        List<Supplier> daftar = new ArrayList<>();
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                daftar.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Gagal mengambil data supplier: " + e.getMessage(), e);
        }
        return daftar;
    }

    @Override
    public Supplier cariById(String id) {
        String sql = "SELECT id_supplier, nama, email, noRek, npwp, alamat FROM supplier WHERE id_supplier = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID supplier harus berupa angka: " + id, e);
        } catch (SQLException e) {
            throw new DataAccessException("Gagal mencari supplier ID " + id + ": " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Memetakan baris ResultSet ke objek {@link Supplier}.
     */
    private Supplier mapRow(ResultSet rs) throws SQLException {
        return new Supplier(
            rs.getInt("id_supplier"),
            rs.getString("nama"),
            rs.getString("email"),
            rs.getString("noRek"),
            rs.getString("npwp"),
            rs.getString("alamat")
        );
    }
}

