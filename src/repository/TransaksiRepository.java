package repository;

import model.Barang;
import model.ItemTransaksi;
import model.Kasir;
import model.Pengguna;
import model.Transaksi;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository untuk entitas {@link Transaksi} terhadap tabel {@code transaksi}
 * dan {@code detail_transaksi}.
 *
 * @author POS-KasirJAVA Refactor
 */
public class TransaksiRepository implements Repository<Transaksi> {

    private final PenggunaRepository penggunaRepo = new PenggunaRepository();

    @Override
    public boolean tambah(Transaksi transaksi) {
        String sqlHeader = "INSERT INTO transaksi (no_transaksi, id_petugas, total, waktu_transaksi) VALUES (?, ?, ?, ?)";
        String sqlDetail = "INSERT INTO detail_transaksi (no_transaksi, code_barang, harga_satuan, qty) VALUES (?, ?, ?, ?)";

        Connection con = DatabaseConnection.getInstance().getConnection();
        try {
            // Simpan header transaksi
            try (PreparedStatement psHeader = con.prepareStatement(sqlHeader)) {
                psHeader.setString(1, transaksi.getNoTransaksi());
                psHeader.setInt(2, (transaksi.getKasir() != null) ? transaksi.getKasir().getId() : 0);
                psHeader.setInt(3, transaksi.getTotal());
                psHeader.setTimestamp(4, Timestamp.valueOf(transaksi.getWaktu()));
                psHeader.executeUpdate();
            }

            // Simpan detail transaksi
            try (PreparedStatement psDetail = con.prepareStatement(sqlDetail)) {
                for (ItemTransaksi item : transaksi.getItems()) {
                    psDetail.setString(1, transaksi.getNoTransaksi());
                    psDetail.setString(2, item.getBarang().getKode());
                    psDetail.setInt(3, item.getHargaSatuan());
                    psDetail.setInt(4, item.getQty());
                    psDetail.addBatch();
                }
                psDetail.executeBatch();
            }
            return true;
        } catch (SQLException e) {
            throw new DataAccessException("Gagal menyimpan transaksi '" + transaksi.getNoTransaksi() + "': " + e.getMessage(), e);
        }
    }

    @Override
    public boolean ubah(Transaksi transaksi) {
        String sql = "UPDATE transaksi SET total = ?, waktu_transaksi = ? WHERE no_transaksi = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, transaksi.getTotal());
            ps.setTimestamp(2, Timestamp.valueOf(transaksi.getWaktu()));
            ps.setString(3, transaksi.getNoTransaksi());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Gagal mengubah transaksi '" + transaksi.getNoTransaksi() + "': " + e.getMessage(), e);
        }
    }

    @Override
    public boolean hapus(String noTransaksi) {
        // Karena fk_detail_transaksi ON DELETE CASCADE, menghapus header otomatis menghapus detail
        String sql = "DELETE FROM transaksi WHERE no_transaksi = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, noTransaksi);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Gagal menghapus transaksi '" + noTransaksi + "': " + e.getMessage(), e);
        }
    }

    @Override
    public List<Transaksi> cariSemua() {
        String sql = "SELECT no_transaksi, id_petugas, total, waktu_transaksi FROM transaksi ORDER BY waktu_transaksi DESC";
        List<Transaksi> daftar = new ArrayList<>();
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                daftar.add(mapHeader(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Gagal mengambil seluruh transaksi: " + e.getMessage(), e);
        }
        return daftar;
    }

    @Override
    public Transaksi cariById(String noTransaksi) {
        String sql = "SELECT no_transaksi, id_petugas, total, waktu_transaksi FROM transaksi WHERE no_transaksi = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, noTransaksi);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Transaksi tx = mapHeader(rs);
                    tx.setItems(cariDetailByNoTransaksi(noTransaksi));
                    return tx;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Gagal mencari transaksi '" + noTransaksi + "': " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Mengambil nomor transaksi terakhir dari database untuk keperluan pembuatan
     * nomor faktur otomatis berikutnya (mis. "TSK001", "TSK002").
     *
     * @return nomor transaksi terakhir, atau null jika belum ada transaksi sama sekali
     */
    public String cariNomorTerakhir() {
        String sql = "SELECT no_transaksi FROM transaksi ORDER BY no_transaksi DESC LIMIT 1";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString("no_transaksi");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Gagal mencari nomor transaksi terakhir: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Mengambil daftar rincian item berdasarkan nomor transaksi.
     */
    public List<ItemTransaksi> cariDetailByNoTransaksi(String noTransaksi) {
        String sql = "SELECT dt.code_barang, dt.harga_satuan, dt.qty, "
                   + "       b.nama_barang, b.harga_beli, b.harga_jual, b.stok_barang "
                   + "FROM detail_transaksi dt "
                   + "JOIN databarang b ON dt.code_barang = b.code_barang "
                   + "WHERE dt.no_transaksi = ?";
        List<ItemTransaksi> items = new ArrayList<>();
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, noTransaksi);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Barang barang = new Barang(
                        rs.getString("code_barang"),
                        rs.getString("nama_barang"),
                        rs.getInt("harga_beli"),
                        rs.getInt("harga_jual"),
                        rs.getInt("stok_barang")
                    );
                    items.add(new ItemTransaksi(barang, rs.getInt("qty"), rs.getInt("harga_satuan")));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Gagal mengambil detail transaksi '" + noTransaksi + "': " + e.getMessage(), e);
        }
        return items;
    }

    private Transaksi mapHeader(ResultSet rs) throws SQLException {
        String noTransaksi = rs.getString("no_transaksi");
        int idPetugas      = rs.getInt("id_petugas");
        int total          = rs.getInt("total");
        Timestamp ts       = rs.getTimestamp("waktu_transaksi");

        Kasir kasir = null;
        if (idPetugas > 0) {
            Pengguna p = penggunaRepo.cariById(String.valueOf(idPetugas));
            if (p instanceof Kasir) {
                kasir = (Kasir) p;
            }
        }

        return new Transaksi(noTransaksi, kasir, ts.toLocalDateTime(), total);
    }
}

