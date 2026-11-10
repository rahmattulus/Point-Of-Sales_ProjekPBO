package model;

/**
 * Merepresentasikan pengguna dengan peran Admin.
 * Mewarisi {@link Pengguna} dan mengimplementasikan {@link #getRole()}
 * mengembalikan {@code "ADMIN"}.
 *
 * @author POS-KasirJAVA Refactor
 */
public class Admin extends Pengguna {

    /**
     * Membuat objek Admin dengan data lengkap.
     *
     * @param id       ID pengguna dari database
     * @param username username unik admin
     * @param password password admin
     * @param email    alamat email admin (boleh null)
     */
    public Admin(int id, String username, String password, String email) {
        super(id, username, password, email);
    }

    /**
     * Mengembalikan peran pengguna sebagai {@code "ADMIN"}.
     *
     * @return string {@code "ADMIN"}
     */
    @Override
    public String getRole() {
        return "ADMIN";
    }
}

