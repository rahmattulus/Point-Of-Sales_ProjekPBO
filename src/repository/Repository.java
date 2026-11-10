package repository;

import java.util.List;

/**
 * Interface generik untuk operasi CRUD (Create, Read, Update, Delete) pada data persistensi.
 * Menerapkan prinsip <b>Interface</b> dan <b>Polimorfisme</b> dalam PBO.
 *
 * @param <T> tipe model domain yang dikelola
 * @author POS-KasirJAVA Refactor
 */
public interface Repository<T> {

    /**
     * Menyimpan objek baru ke database.
     *
     * @param obj objek model yang akan ditambahkan
     * @return {@code true} jika berhasil disimpan, {@code false} jika tidak
     * @throws DataAccessException jika terjadi kesalahan akses basis data
     */
    boolean tambah(T obj);

    /**
     * Memperbarui data objek yang sudah ada di database.
     *
     * @param obj objek model dengan data baru
     * @return {@code true} jika data berhasil diubah, {@code false} jika tidak
     * @throws DataAccessException jika terjadi kesalahan akses basis data
     */
    boolean ubah(T obj);

    /**
     * Menghapus data dari database berdasarkan identitas (ID/kode).
     *
     * @param id kunci identitas objek yang akan dihapus
     * @return {@code true} jika berhasil dihapus, {@code false} jika tidak ditemukan/gagal
     * @throws DataAccessException jika terjadi kesalahan akses basis data
     */
    boolean hapus(String id);

    /**
     * Mengambil semua data model dari database.
     *
     * @return {@link List} berisi seluruh objek yang ditemukan
     * @throws DataAccessException jika terjadi kesalahan akses basis data
     */
    List<T> cariSemua();

    /**
     * Mencari satu objek berdasarkan identitas (ID/kode).
     *
     * @param id kunci identitas objek yang dicari
     * @return objek model jika ditemukan, atau {@code null} jika tidak ada
     * @throws DataAccessException jika terjadi kesalahan akses basis data
     */
    T cariById(String id);
}

