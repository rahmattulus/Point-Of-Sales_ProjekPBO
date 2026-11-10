package model;

/**
 * Merepresentasikan data barang di toko.
 * Menerapkan <b>Encapsulation</b> dan menyimpan logika bisnis
 * terkait stok di dalam class ini sendiri.
 *
 * @author POS-KasirJAVA Refactor
 */
public class Barang {

    private String kode;
    private String nama;
    private int    hargaBeli;
    private int    hargaJual;
    private int    stok;

    /**
     * Membuat objek Barang dengan data lengkap.
     *
     * @param kode      kode unik barang (VARCHAR, mis. "1001" atau "BRG-A")
     * @param nama      nama barang
     * @param hargaBeli harga beli barang (dari supplier)
     * @param hargaJual harga jual barang (ke pelanggan)
     * @param stok      jumlah stok saat ini
     */
    public Barang(String kode, String nama, int hargaBeli, int hargaJual, int stok) {
        this.kode      = kode;
        this.nama      = nama;
        this.hargaBeli = hargaBeli;
        this.hargaJual = hargaJual;
        this.stok      = stok;
    }

    /**
     * Memeriksa apakah stok mencukupi untuk sejumlah {@code qty} yang diminta.
     *
     * @param qty jumlah yang diinginkan
     * @return {@code true} jika {@code stok >= qty}
     */
    public boolean cekStok(int qty) {
        return stok >= qty;
    }

    /**
     * Mengurangi stok sejumlah {@code qty}.
     *
     * @param qty jumlah yang akan dikurangi
     * @throws IllegalArgumentException jika stok tidak mencukupi
     */
    public void kurangiStok(int qty) {
        if (!cekStok(qty)) {
            throw new IllegalArgumentException(
                "Stok tidak cukup. Stok tersedia: " + stok + ", diminta: " + qty
            );
        }
        this.stok -= qty;
    }

    // --- Getter & Setter ---

    public String getKode() { return kode; }
    public void setKode(String kode) { this.kode = kode; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public int getHargaBeli() { return hargaBeli; }
    public void setHargaBeli(int hargaBeli) { this.hargaBeli = hargaBeli; }

    public int getHargaJual() { return hargaJual; }
    public void setHargaJual(int hargaJual) { this.hargaJual = hargaJual; }

    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }

    @Override
    public String toString() {
        return "[" + kode + "] " + nama + " | Jual: " + hargaJual + " | Stok: " + stok;
    }
}

