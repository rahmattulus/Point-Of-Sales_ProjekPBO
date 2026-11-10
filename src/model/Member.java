package model;

/**
 * Merepresentasikan data member (pelanggan terdaftar) di toko.
 *
 * @author POS-KasirJAVA Refactor
 */
public class Member {

    private String id;
    private String username;
    private String noTlp;
    private int    poin;

    /**
     * Membuat objek Member dengan data lengkap.
     *
     * @param id       ID member (VARCHAR, mis. "MBR001")
     * @param username nama/username member
     * @param noTlp    nomor telepon member (boleh null)
     * @param poin     jumlah poin yang dimiliki member
     */
    public Member(String id, String username, String noTlp, int poin) {
        this.id       = id;
        this.username = username;
        this.noTlp    = noTlp;
        this.poin     = poin;
    }

    /**
     * Menambah poin member sejumlah {@code jumlah}.
     *
     * @param jumlah jumlah poin yang akan ditambahkan (harus positif)
     * @throws IllegalArgumentException jika {@code jumlah} tidak positif
     */
    public void tambahPoin(int jumlah) {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah poin yang ditambahkan harus positif.");
        }
        this.poin += jumlah;
    }

    // --- Getter & Setter ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getNoTlp() { return noTlp; }
    public void setNoTlp(String noTlp) { this.noTlp = noTlp; }

    public int getPoin() { return poin; }
    public void setPoin(int poin) { this.poin = poin; }

    @Override
    public String toString() {
        return "Member[" + id + "] " + username + " | Poin: " + poin;
    }
}

