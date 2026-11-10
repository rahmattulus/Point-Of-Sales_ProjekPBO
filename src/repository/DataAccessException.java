package repository;

/**
 * Exception runtime yang membungkus {@code SQLException} agar error database
 * tidak bocor keluar dari package {@code repository}.
 * <p>
 * Layer {@code view} hanya perlu menangkap {@code DataAccessException}
 * tanpa harus meng-import {@code java.sql}.
 * </p>
 *
 * @author POS-KasirJAVA Refactor
 */
public class DataAccessException extends RuntimeException {

    /**
     * Membuat exception baru dengan pesan dan penyebab (cause).
     *
     * @param message pesan deskripsi error
     * @param cause   exception asli yang menjadi penyebab
     */
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Membuat exception baru hanya dengan pesan.
     *
     * @param message pesan deskripsi error
     */
    public DataAccessException(String message) {
        super(message);
    }
}

