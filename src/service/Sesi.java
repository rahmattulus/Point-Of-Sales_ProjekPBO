package service;

import model.Pengguna;

/**
 * Pengelola sesi pengguna yang sedang login ke sistem POS Kasir.
 * Menggantikan variabel publik statis lama ({@code koneksi.username})
 * dengan enkapsulasi yang aman.
 *
 * @author POS-KasirJAVA Refactor
 */
public class Sesi {

    private static Pengguna penggunaAktif;

    /** Constructor privat agar class ini tidak dapat diinstansiasi secara bebas. */
    private Sesi() {}

    /**
     * Mendaftarkan pengguna yang berhasil login sebagai pengguna aktif saat ini.
     *
     * @param pengguna objek {@link Pengguna} yang berhasil login
     */
    public static void masuk(Pengguna pengguna) {
        penggunaAktif = pengguna;
    }

    /**
     * Menghapus sesi pengguna aktif saat logout.
     */
    public static void keluar() {
        penggunaAktif = null;
    }

    /**
     * Mengambil data pengguna yang sedang aktif/login.
     *
     * @return objek {@link Pengguna} yang sedang login, atau {@code null} jika belum login
     */
    public static Pengguna getPenggunaAktif() {
        return penggunaAktif;
    }

    /**
     * Memeriksa apakah saat ini ada pengguna yang sedang login.
     *
     * @return {@code true} jika ada pengguna aktif, {@code false} jika tidak
     */
    public static boolean isLogin() {
        return penggunaAktif != null;
    }

    /**
     * Mengambil peran (role) dari pengguna aktif saat ini.
     *
     * @return string peran ("ADMIN" atau "KASIR"), atau null jika belum login
     */
    public static String getRolePenggunaAktif() {
        return (penggunaAktif != null) ? penggunaAktif.getRole() : null;
    }
}

