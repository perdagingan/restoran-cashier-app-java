/**
 * Kelas Menu untuk merepresentasikan item menu restoran.
 * Memenuhi indikator: Tipe data/variabel, String.
 */
public class Menu {
    // Atribut menu (tipe data: String dan int)
    private String nama;
    private int harga;
    private String kategori; // "Makanan" atau "Minuman"

    /**
     * Constructor untuk membuat objek Menu.
     * @param nama Nama menu
     * @param harga Harga menu dalam Rupiah
     * @param kategori Kategori menu ("Makanan" atau "Minuman")
     */
    public Menu(String nama, int harga, String kategori) {
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
    }

    // Getter dan Setter
    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    /**
     * Menampilkan informasi menu dalam format string.
     * @return String representasi menu
     */
    @Override
    public String toString() {
        return nama + " - Rp " + String.format("%,d", harga);
    }
}
