package service;

import model.Pengguna;
import repository.PenggunaRepository;

/**
 * Service untuk mengelola autentikasi dan otorisasi pengguna (Login, Logout, Registrasi).
 * Berinteraksi dengan {@link PenggunaRepository} dan mengelola {@link Sesi}.
 *
 * @author POS-KasirJAVA Refactor
 */
public class AuthService {

    private final PenggunaRepository penggunaRepo;

    /**
     * Membuat AuthService dengan instance PenggunaRepository default.
     */
    public AuthService() {
        this.penggunaRepo = new PenggunaRepository();
    }

    /**
     * Membuat AuthService dengan PenggunaRepository yang disuntikkan (Dependency Injection).
     *
     * @param penggunaRepo repository pengguna yang digunakan
     */
    public AuthService(PenggunaRepository penggunaRepo) {
        this.penggunaRepo = penggunaRepo;
    }

    /**
     * Memproses login pengguna berdasarkan username, password, dan peran yang dipilih.
     * Jika kredensial cocok, pengguna akan didaftarkan ke {@link Sesi}.
     *
     * @param username username pengguna
     * @param password password pengguna
     * @param peran    peran yang diharapkan ("ADMIN" atau "KASIR")
     * @return objek {@link Pengguna} jika berhasil, atau {@code null} jika gagal
     */
    public Pengguna login(String username, String password, String peran) {
        if (username == null || username.trim().isEmpty() || password == null) {
            return null;
        }

        Pengguna pengguna = penggunaRepo.cariByUsername(username.trim(), peran);
        if (pengguna != null && password.equals(pengguna.getPassword())) {
            Sesi.masuk(pengguna);
            return pengguna;
        }

        return null;
    }

    /**
     * Memproses logout pengguna aktif dan membersihkan sesi.
     */
    public void logout() {
        Sesi.keluar();
    }

    /**
     * Mendaftarkan akun pengguna baru (Admin atau Kasir).
     *
     * @param pengguna objek pengguna yang akan didaftarkan
     * @return {@code true} jika registrasi berhasil, {@code false} jika gagal
     * @throws IllegalArgumentException jika data akun tidak valid atau username sudah terdaftar
     */
    public boolean registrasi(Pengguna pengguna) {
        if (pengguna == null) {
            throw new IllegalArgumentException("Data pengguna tidak boleh kosong.");
        }
        if (pengguna.getUsername() == null || pengguna.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username tidak boleh kosong.");
        }
        if (pengguna.getPassword() == null || pengguna.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password tidak boleh kosong.");
        }

        // Cek apakah username sudah dipakai
        Pengguna existing = penggunaRepo.cariByUsername(pengguna.getUsername().trim());
        if (existing != null) {
            throw new IllegalArgumentException("Username '" + pengguna.getUsername() + "' sudah digunakan.");
        }

        return penggunaRepo.tambah(pengguna);
    }
}

