package repository;

import model.Barang;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository untuk entitas {@link Barang} terhadap tabel {@code databarang}.
 * Menggunakan {@link PreparedStatement} untuk mencegah SQL Injection.
 *
 * @author POS-KasirJAVA Refactor
 */
public class BarangRepository implements Repository<Barang> {

    @Override
    public boolean tambah(Barang barang) {
        String sql = "INSERT INTO databarang (code_barang, nama_barang, harga_beli, harga_jual, stok_barang) "
                   + "VALUES (?, ?, ?, ?, ?)";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, barang.getKode());
            ps.setString(2, barang.getNama());
            ps.setInt(3, barang.getHargaBeli());
            ps.setInt(4, barang.getHargaJual());
            ps.setInt(5, barang.getStok());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Gagal menambah barang '" + barang.getNama() + "': " + e.getMessage(), e);
        }
    }

    @Override
    public boolean ubah(Barang barang) {
        String sql = "UPDATE databarang SET nama_barang = ?, harga_beli = ?, harga_jual = ?, stok_barang = ? "
                   + "WHERE code_barang = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, barang.getNama());
            ps.setInt(2, barang.getHargaBeli());
            ps.setInt(3, barang.getHargaJual());
            ps.setInt(4, barang.getStok());
            ps.setString(5, barang.getKode());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Gagal mengubah barang '" + barang.getKode() + "': " + e.getMessage(), e);
        }
    }

    @Override
    public boolean hapus(String id) {
        String sql = "DELETE FROM databarang WHERE code_barang = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Gagal menghapus barang '" + id + "': " + e.getMessage(), e);
        }
    }

    @Override
    public List<Barang> cariSemua() {
        String sql = "SELECT code_barang, nama_barang, harga_beli, harga_jual, stok_barang FROM databarang ORDER BY code_barang ASC";
        List<Barang> daftar = new ArrayList<>();
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                daftar.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Gagal mengambil data barang: " + e.getMessage(), e);
        }
        return daftar;
    }

    @Override
    public Barang cariById(String id) {
        String sql = "SELECT code_barang, nama_barang, harga_beli, harga_jual, stok_barang FROM databarang WHERE code_barang = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Gagal mencari barang dengan kode '" + id + "': " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Memetakan baris ResultSet ke objek {@link Barang}.
     */
    private Barang mapRow(ResultSet rs) throws SQLException {
        return new Barang(
            rs.getString("code_barang"),
            rs.getString("nama_barang"),
            rs.getInt("harga_beli"),
            rs.getInt("harga_jual"),
            rs.getInt("stok_barang")
        );
    }
}

