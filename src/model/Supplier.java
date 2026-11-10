package model;

/**
 * Merepresentasikan data supplier (pemasok barang) toko.
 *
 * @author POS-KasirJAVA Refactor
 */
public class Supplier {

    private int    id;
    private String nama;
    private String email;
    private String noRek;
    private String npwp;
    private String alamat;

    /**
     * Membuat objek Supplier dengan data lengkap.
     *
     * @param id     ID supplier (auto-increment dari database)
     * @param nama   nama perusahaan/supplier
     * @param email  email supplier (boleh null)
     * @param noRek  nomor rekening supplier (boleh null)
     * @param npwp   nomor NPWP supplier (boleh null)
     * @param alamat alamat supplier (boleh null)
     */
    public Supplier(int id, String nama, String email, String noRek, String npwp, String alamat) {
        this.id     = id;
        this.nama   = nama;
        this.email  = email;
        this.noRek  = noRek;
        this.npwp   = npwp;
        this.alamat = alamat;
    }

    // --- Getter & Setter ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNoRek() { return noRek; }
    public void setNoRek(String noRek) { this.noRek = noRek; }

    public String getNpwp() { return npwp; }
    public void setNpwp(String npwp) { this.npwp = npwp; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    @Override
    public String toString() {
        return "Supplier[" + id + "] " + nama;
    }
}

