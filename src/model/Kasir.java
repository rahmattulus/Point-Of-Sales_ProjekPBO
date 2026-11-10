package model;

/**
 * Merepresentasikan pengguna dengan peran Kasir.
 * Mewarisi {@link Pengguna} dan mengimplementasikan {@link #getRole()}
 * mengembalikan {@code "KASIR"}.
 *
 * @author POS-KasirJAVA Refactor
 */
public class Kasir extends Pengguna {

    /**
     * Membuat objek Kasir dengan data lengkap.
     *
     * @param id       ID pengguna dari database
     * @param username username unik kasir
     * @param password password kasir
     * @param email    alamat email kasir (boleh null)
     */
    public Kasir(int id, String username, String password, String email) {
        super(id, username, password, email);
    }

    /**
     * Mengembalikan peran pengguna sebagai {@code "KASIR"}.
     *
     * @return string {@code "KASIR"}
     */
    @Override
    public String getRole() {
        return "KASIR";
    }
}

