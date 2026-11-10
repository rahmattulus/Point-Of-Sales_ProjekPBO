package model;

/**
 * Merepresentasikan satu item dalam keranjang atau rincian transaksi penjualan.
 * Menyimpan salinan harga jual saat transaksi/item dimasukkan ke keranjang
 * agar subtotal konsisten meskipun harga master barang berubah di kemudian hari.
 *
 * @author POS-KasirJAVA Refactor
 */
public class ItemTransaksi {

    private Barang barang;
    private int    qty;
    private int    hargaSatuan;

    /**
     * Membuat ItemTransaksi baru dengan barang dan kuantitas awal.
     * Harga satuan disalin dari harga jual barang saat ini.
     *
     * @param barang objek Barang yang dibeli
     * @param qty    kuantitas barang yang dibeli (harus > 0)
     */
    public ItemTransaksi(Barang barang, int qty) {
        if (barang == null) {
            throw new IllegalArgumentException("Barang tidak boleh null.");
        }
        if (qty <= 0) {
            throw new IllegalArgumentException("Qty harus lebih dari 0.");
        }
        this.barang      = barang;
        this.qty         = qty;
        this.hargaSatuan = barang.getHargaJual();
    }

    /**
     * Membuat ItemTransaksi dengan data eksplisit (berguna saat membaca dari database detail_transaksi).
     *
     * @param barang      objek Barang
     * @param qty         kuantitas barang
     * @param hargaSatuan harga satuan saat transaksi terjadi
     */
    public ItemTransaksi(Barang barang, int qty, int hargaSatuan) {
        if (barang == null) {
            throw new IllegalArgumentException("Barang tidak boleh null.");
        }
        if (qty <= 0) {
            throw new IllegalArgumentException("Qty harus lebih dari 0.");
        }
        this.barang      = barang;
        this.qty         = qty;
        this.hargaSatuan = hargaSatuan;
    }

    /**
     * Menghitung subtotal dari item ini (hargaSatuan * qty).
     *
     * @return nilai subtotal
     */
    public int getSubtotal() {
        return hargaSatuan * qty;
    }

    // --- Getter & Setter ---

    public Barang getBarang() {
        return barang;
    }

    public void setBarang(Barang barang) {
        this.barang = barang;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getHargaSatuan() {
        return hargaSatuan;
    }

    public void setHargaSatuan(int hargaSatuan) {
        this.hargaSatuan = hargaSatuan;
    }
}

