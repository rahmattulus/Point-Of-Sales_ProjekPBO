package model;

/**
 * Abstract class induk untuk semua pengguna sistem (Admin dan Kasir).
 * Menerapkan konsep <b>Abstract Class</b> dan <b>Encapsulation</b>.
 * Subclass wajib mengimplementasikan {@link #getRole()}.
 *
 * @author POS-KasirJAVA Refactor
 */
public abstract class Pengguna {

    private int    id;
    private String username;
    private String password;
    private String email;

    /**
     * Membuat objek Pengguna dengan data lengkap.
     *
     * @param id       ID pengguna dari database
     * @param username username unik pengguna
     * @param password password pengguna
     * @param email    alamat email pengguna (boleh null)
     */
    public Pengguna(int id, String username, String password, String email) {
        this.id       = id;
        this.username = username;
        this.password = password;
        this.email    = email;
    }

    /**
     * Mengembalikan peran (role) pengguna.
     * Harus di-override oleh subclass: {@code "ADMIN"} atau {@code "KASIR"}.
     *
     * @return string peran pengguna
     */
    public abstract String getRole();

    // --- Getter & Setter ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return getRole() + "[" + id + "] " + username;
    }
}

