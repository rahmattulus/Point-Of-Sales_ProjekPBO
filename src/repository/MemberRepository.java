package repository;

import model.Member;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository untuk entitas {@link Member} terhadap tabel {@code member}.
 * Menggunakan {@link PreparedStatement} untuk mencegah SQL Injection.
 *
 * @author POS-KasirJAVA Refactor
 */
public class MemberRepository implements Repository<Member> {

    @Override
    public boolean tambah(Member member) {
        String sql = "INSERT INTO member (id_member, username, no_tlp, poin) VALUES (?, ?, ?, ?)";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, member.getId());
            ps.setString(2, member.getUsername());
            ps.setString(3, member.getNoTlp());
            ps.setInt(4, member.getPoin());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Gagal menambah member '" + member.getUsername() + "': " + e.getMessage(), e);
        }
    }

    @Override
    public boolean ubah(Member member) {
        String sql = "UPDATE member SET username = ?, no_tlp = ?, poin = ? WHERE id_member = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, member.getUsername());
            ps.setString(2, member.getNoTlp());
            ps.setInt(3, member.getPoin());
            ps.setString(4, member.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Gagal mengubah member '" + member.getId() + "': " + e.getMessage(), e);
        }
    }

    @Override
    public boolean hapus(String id) {
        String sql = "DELETE FROM member WHERE id_member = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Gagal menghapus member '" + id + "': " + e.getMessage(), e);
        }
    }

    @Override
    public List<Member> cariSemua() {
        String sql = "SELECT id_member, username, no_tlp, poin FROM member ORDER BY id_member ASC";
        List<Member> daftar = new ArrayList<>();
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                daftar.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Gagal mengambil data member: " + e.getMessage(), e);
        }
        return daftar;
    }

    @Override
    public Member cariById(String id) {
        String sql = "SELECT id_member, username, no_tlp, poin FROM member WHERE id_member = ?";
        Connection con = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Gagal mencari member dengan ID '" + id + "': " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Memetakan baris ResultSet ke objek {@link Member}.
     */
    private Member mapRow(ResultSet rs) throws SQLException {
        return new Member(
            rs.getString("id_member"),
            rs.getString("username"),
            rs.getString("no_tlp"),
            rs.getInt("poin")
        );
    }
}

