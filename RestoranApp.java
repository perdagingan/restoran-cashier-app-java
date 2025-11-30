import java.util.ArrayList;
import java.util.Scanner;

/**
 * Aplikasi Restoran Sederhana
 * Memenuhi indikator: tipe data/variabel, Array/String, if/switch, loop (for/while/do-while).
 */
public class RestoranApp {
    // ArrayList untuk menyimpan daftar menu
    private static ArrayList<Menu> daftarMenu = new ArrayList<>();
    // Scanner untuk input
    private static Scanner scanner = new Scanner(System.in);
    // Konstanta untuk biaya
    private static final double PAJAK = 0.10; // 10%
    private static final int BIAYA_PELAYANAN = 20000; // Rp 20.000
    private static final double DISKON_BESAR = 0.10; // 10% diskon untuk total > 100.000
    private static final int BATAS_DISKON_BESAR = 100000;
    private static final int BATAS_PROMO_MINUMAN = 50000;

    public static void main(String[] args) {
        // Inisialisasi menu dengan minimal 4 makanan dan 4 minuman
        inisialisasiMenu();

        int pilihan;
        // do-while untuk perulangan menu utama
        do {
            tampilkanMenuUtama();
            pilihan = bacaInputAngka("Pilih menu: ");

            switch (pilihan) {
                case 1:
                    pemesananPelanggan();
                    break;
                case 2:
                    manajemenMenu();
                    break;
                case 0:
                    System.out.println("\nTerima kasih telah menggunakan aplikasi Restoran!");
                    System.out.println("Sampai jumpa kembali!");
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silakan pilih 1, 2, atau 0.");
            }
        } while (pilihan != 0);

        scanner.close();
    }

    /**
     * Inisialisasi daftar menu dengan minimal 4 Makanan dan 4 Minuman.
     */
    private static void inisialisasiMenu() {
        // Menambahkan 4 Makanan
        daftarMenu.add(new Menu("Nasi Goreng", 25000, "Makanan"));
        daftarMenu.add(new Menu("Mie Goreng", 22000, "Makanan"));
        daftarMenu.add(new Menu("Ayam Bakar", 35000, "Makanan"));
        daftarMenu.add(new Menu("Sate Ayam", 30000, "Makanan"));

        // Menambahkan 4 Minuman
        daftarMenu.add(new Menu("Es Teh Manis", 8000, "Minuman"));
        daftarMenu.add(new Menu("Es Jeruk", 10000, "Minuman"));
        daftarMenu.add(new Menu("Jus Alpukat", 15000, "Minuman"));
        daftarMenu.add(new Menu("Kopi Hitam", 12000, "Minuman"));
    }

    /**
     * Menampilkan menu utama aplikasi.
     */
    private static void tampilkanMenuUtama() {
        System.out.println("\n========================================");
        System.out.println("       APLIKASI RESTORAN SEDERHANA      ");
        System.out.println("========================================");
        System.out.println("1. Pemesanan Pelanggan");
        System.out.println("2. Manajemen Menu (Pemilik)");
        System.out.println("0. Keluar");
        System.out.println("========================================");
    }

    /**
     * Fungsi Pemesanan Pelanggan.
     * Menggunakan for-each untuk menampilkan menu,
     * while loop untuk input pesanan.
     */
    private static void pemesananPelanggan() {
        System.out.println("\n========================================");
        System.out.println("         PEMESANAN PELANGGAN            ");
        System.out.println("========================================");

        // Tampilkan menu dalam format terkelompok menggunakan for-each
        tampilkanMenuTerkelompok();

        // ArrayList untuk menyimpan pesanan
        ArrayList<String> pesananNama = new ArrayList<>();
        ArrayList<Integer> pesananJumlah = new ArrayList<>();
        ArrayList<Integer> pesananHarga = new ArrayList<>();
        ArrayList<String> pesananKategori = new ArrayList<>();

        System.out.println("\nMasukkan pesanan Anda.");
        System.out.println("Ketik 'selesai' untuk mengakhiri pemesanan.");

        // while loop untuk input pesanan
        while (true) {
            System.out.print("\nNama menu (atau 'selesai'): ");
            String inputNama = scanner.nextLine().trim();

            if (inputNama.equalsIgnoreCase("selesai")) {
                break;
            }

            // Validasi input menu - berulang jika tidak valid
            Menu menuDitemukan = null;
            while (menuDitemukan == null) {
                menuDitemukan = cariMenu(inputNama);
                if (menuDitemukan == null) {
                    System.out.println("Menu '" + inputNama + "' tidak ditemukan!");
                    System.out.print("Masukkan nama menu yang valid (atau 'selesai'): ");
                    inputNama = scanner.nextLine().trim();
                    if (inputNama.equalsIgnoreCase("selesai")) {
                        break;
                    }
                }
            }

            if (inputNama.equalsIgnoreCase("selesai")) {
                break;
            }

            // Input jumlah pesanan
            int jumlah = bacaInputAngka("Jumlah pesanan: ");
            while (jumlah <= 0) {
                System.out.println("Jumlah harus lebih dari 0!");
                jumlah = bacaInputAngka("Jumlah pesanan: ");
            }

            // Simpan pesanan
            pesananNama.add(menuDitemukan.getNama());
            pesananJumlah.add(jumlah);
            pesananHarga.add(menuDitemukan.getHarga());
            pesananKategori.add(menuDitemukan.getKategori());

            System.out.println(">> " + menuDitemukan.getNama() + " x" + jumlah + " ditambahkan ke pesanan.");
        }

        // Jika tidak ada pesanan
        if (pesananNama.isEmpty()) {
            System.out.println("\nTidak ada pesanan. Kembali ke menu utama.");
            return;
        }

        // Hitung dan cetak struk
        cetakStruk(pesananNama, pesananJumlah, pesananHarga, pesananKategori);
    }

    /**
     * Menampilkan daftar menu dalam format terkelompok menggunakan for-each.
     */
    private static void tampilkanMenuTerkelompok() {
        System.out.println("\n--- DAFTAR MENU ---");

        // Tampilkan Makanan
        System.out.println("\n[MAKANAN]");
        for (Menu menu : daftarMenu) {
            if (menu.getKategori().equals("Makanan")) {
                System.out.println("  - " + menu);
            }
        }

        // Tampilkan Minuman
        System.out.println("\n[MINUMAN]");
        for (Menu menu : daftarMenu) {
            if (menu.getKategori().equals("Minuman")) {
                System.out.println("  - " + menu);
            }
        }
    }

    /**
     * Mencari menu berdasarkan nama (case-insensitive).
     * @param nama Nama menu yang dicari
     * @return Menu jika ditemukan, null jika tidak
     */
    private static Menu cariMenu(String nama) {
        for (Menu menu : daftarMenu) {
            if (menu.getNama().equalsIgnoreCase(nama)) {
                return menu;
            }
        }
        return null;
    }

    /**
     * Mencetak struk pesanan dengan perhitungan total, pajak, diskon, dan biaya pelayanan.
     */
    private static void cetakStruk(ArrayList<String> pesananNama, ArrayList<Integer> pesananJumlah,
                                   ArrayList<Integer> pesananHarga, ArrayList<String> pesananKategori) {
        System.out.println("\n========================================");
        System.out.println("            STRUK PESANAN               ");
        System.out.println("========================================");

        int subtotal = 0;
        int totalMinuman = 0;
        int jumlahMinuman = 0;

        // Hitung subtotal dan lacak minuman untuk promo
        for (int i = 0; i < pesananNama.size(); i++) {
            int hargaItem = pesananHarga.get(i) * pesananJumlah.get(i);
            subtotal += hargaItem;

            if (pesananKategori.get(i).equals("Minuman")) {
                totalMinuman += hargaItem;
                jumlahMinuman += pesananJumlah.get(i);
            }

            System.out.printf("%-20s x%d  Rp %,d%n",
                    pesananNama.get(i), pesananJumlah.get(i), hargaItem);
        }

        System.out.println("----------------------------------------");
        System.out.printf("Subtotal:                   Rp %,d%n", subtotal);

        // Variabel untuk diskon
        int diskon = 0;
        int potonganMinumanGratis = 0;
        String keteranganDiskon = "";

        // if-else if untuk menentukan diskon/penawaran
        if (subtotal > BATAS_DISKON_BESAR) {
            // Diskon 10% jika total > Rp 100.000
            diskon = (int) (subtotal * DISKON_BESAR);
            keteranganDiskon = "Diskon 10% (Total > Rp 100.000)";
        } else if (subtotal > BATAS_PROMO_MINUMAN && jumlahMinuman > 0) {
            // Beli 1 Gratis 1 untuk minuman jika total > Rp 50.000
            // Hitung jumlah minuman gratis (separuh dari total minuman, dibulatkan ke bawah)
            int jumlahMinumanGratis = jumlahMinuman / 2;
            if (jumlahMinumanGratis > 0) {
                // Cari harga minuman termurah untuk dijadikan gratis
                int hargaMinumanTerMurah = Integer.MAX_VALUE;
                for (int i = 0; i < pesananNama.size(); i++) {
                    if (pesananKategori.get(i).equals("Minuman")) {
                        if (pesananHarga.get(i) < hargaMinumanTerMurah) {
                            hargaMinumanTerMurah = pesananHarga.get(i);
                        }
                    }
                }
                potonganMinumanGratis = hargaMinumanTerMurah * jumlahMinumanGratis;
                keteranganDiskon = "Promo Beli 1 Gratis 1 Minuman (" + jumlahMinumanGratis + " minuman gratis)";
            }
        }

        // Tampilkan diskon jika ada
        if (diskon > 0) {
            System.out.printf("%-28s -Rp %,d%n", keteranganDiskon, diskon);
        }
        if (potonganMinumanGratis > 0) {
            System.out.printf("%-28s -Rp %,d%n", keteranganDiskon, potonganMinumanGratis);
        }

        // Hitung subtotal setelah diskon
        int subtotalSetelahDiskon = subtotal - diskon - potonganMinumanGratis;

        // Hitung pajak 10%
        int pajak = (int) (subtotalSetelahDiskon * PAJAK);
        System.out.printf("Pajak (10%%):                Rp %,d%n", pajak);

        // Biaya pelayanan
        System.out.printf("Biaya Pelayanan:            Rp %,d%n", BIAYA_PELAYANAN);

        System.out.println("========================================");

        // Total akhir
        int totalAkhir = subtotalSetelahDiskon + pajak + BIAYA_PELAYANAN;
        System.out.printf("TOTAL BAYAR:                Rp %,d%n", totalAkhir);
        System.out.println("========================================");
        System.out.println("       Terima kasih atas kunjungan Anda!");
        System.out.println("========================================");
    }

    /**
     * Fungsi Manajemen Menu (Pemilik).
     * Menggunakan switch untuk navigasi sub-menu.
     */
    private static void manajemenMenu() {
        int pilihan;
        do {
            System.out.println("\n========================================");
            System.out.println("       MANAJEMEN MENU (PEMILIK)         ");
            System.out.println("========================================");
            System.out.println("1. Tambah Menu Baru");
            System.out.println("2. Ubah Harga Menu");
            System.out.println("3. Hapus Menu");
            System.out.println("4. Kembali ke Menu Utama");
            System.out.println("========================================");

            pilihan = bacaInputAngka("Pilih menu: ");

            // switch untuk navigasi
            switch (pilihan) {
                case 1:
                    tambahMenu();
                    break;
                case 2:
                    ubahHargaMenu();
                    break;
                case 3:
                    hapusMenu();
                    break;
                case 4:
                    System.out.println("Kembali ke menu utama...");
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silakan pilih 1-4.");
            }
        } while (pilihan != 4);
    }

    /**
     * Fungsi untuk menambah menu baru.
     */
    private static void tambahMenu() {
        System.out.println("\n--- TAMBAH MENU BARU ---");

        System.out.print("Nama menu: ");
        String nama = scanner.nextLine().trim();

        // Validasi nama tidak kosong
        while (nama.isEmpty()) {
            System.out.println("Nama tidak boleh kosong!");
            System.out.print("Nama menu: ");
            nama = scanner.nextLine().trim();
        }

        // Cek apakah menu sudah ada
        if (cariMenu(nama) != null) {
            System.out.println("Menu dengan nama '" + nama + "' sudah ada!");
            return;
        }

        int harga = bacaInputAngka("Harga menu (Rp): ");
        while (harga <= 0) {
            System.out.println("Harga harus lebih dari 0!");
            harga = bacaInputAngka("Harga menu (Rp): ");
        }

        System.out.println("Kategori: 1. Makanan  2. Minuman");
        int kategoriPilihan = bacaInputAngka("Pilih kategori (1/2): ");
        String kategori;
        while (kategoriPilihan != 1 && kategoriPilihan != 2) {
            System.out.println("Pilihan kategori tidak valid!");
            kategoriPilihan = bacaInputAngka("Pilih kategori (1/2): ");
        }
        kategori = (kategoriPilihan == 1) ? "Makanan" : "Minuman";

        // Konfirmasi sebelum menambahkan
        System.out.println("\nMenu baru: " + nama + " - Rp " + String.format("%,d", harga) + " (" + kategori + ")");
        if (konfirmasiAksi("Tambahkan menu ini?")) {
            daftarMenu.add(new Menu(nama, harga, kategori));
            System.out.println("Menu berhasil ditambahkan!");
        } else {
            System.out.println("Penambahan menu dibatalkan.");
        }
    }

    /**
     * Fungsi untuk mengubah harga menu.
     * Menampilkan menu dengan nomor urut.
     */
    private static void ubahHargaMenu() {
        System.out.println("\n--- UBAH HARGA MENU ---");

        if (daftarMenu.isEmpty()) {
            System.out.println("Tidak ada menu yang tersedia.");
            return;
        }

        // Tampilkan menu dengan nomor urut
        tampilkanMenuDenganNomor();

        int nomor = bacaInputAngka("Pilih nomor menu yang akan diubah (0 untuk batal): ");

        if (nomor == 0) {
            System.out.println("Pengubahan harga dibatalkan.");
            return;
        }

        // Validasi nomor
        while (nomor < 1 || nomor > daftarMenu.size()) {
            System.out.println("Nomor tidak valid!");
            nomor = bacaInputAngka("Pilih nomor menu yang akan diubah (0 untuk batal): ");
            if (nomor == 0) {
                System.out.println("Pengubahan harga dibatalkan.");
                return;
            }
        }

        Menu menu = daftarMenu.get(nomor - 1);
        System.out.println("Menu terpilih: " + menu);
        System.out.println("Harga saat ini: Rp " + String.format("%,d", menu.getHarga()));

        int hargaBaru = bacaInputAngka("Masukkan harga baru (Rp): ");
        while (hargaBaru <= 0) {
            System.out.println("Harga harus lebih dari 0!");
            hargaBaru = bacaInputAngka("Masukkan harga baru (Rp): ");
        }

        // Konfirmasi dengan 'Ya' sebelum eksekusi
        System.out.println("\nUbah harga " + menu.getNama() + " dari Rp " +
                String.format("%,d", menu.getHarga()) + " menjadi Rp " + String.format("%,d", hargaBaru) + "?");
        if (konfirmasiAksi("Konfirmasi perubahan?")) {
            menu.setHarga(hargaBaru);
            System.out.println("Harga berhasil diubah!");
        } else {
            System.out.println("Pengubahan harga dibatalkan.");
        }
    }

    /**
     * Fungsi untuk menghapus menu.
     * Menampilkan menu dengan nomor urut.
     */
    private static void hapusMenu() {
        System.out.println("\n--- HAPUS MENU ---");

        if (daftarMenu.isEmpty()) {
            System.out.println("Tidak ada menu yang tersedia.");
            return;
        }

        // Tampilkan menu dengan nomor urut
        tampilkanMenuDenganNomor();

        int nomor = bacaInputAngka("Pilih nomor menu yang akan dihapus (0 untuk batal): ");

        if (nomor == 0) {
            System.out.println("Penghapusan dibatalkan.");
            return;
        }

        // Validasi nomor
        while (nomor < 1 || nomor > daftarMenu.size()) {
            System.out.println("Nomor tidak valid!");
            nomor = bacaInputAngka("Pilih nomor menu yang akan dihapus (0 untuk batal): ");
            if (nomor == 0) {
                System.out.println("Penghapusan dibatalkan.");
                return;
            }
        }

        Menu menu = daftarMenu.get(nomor - 1);
        System.out.println("Menu terpilih: " + menu);

        // Konfirmasi dengan 'Ya' sebelum penghapusan - menggunakan while loop
        System.out.println("\nAnda yakin ingin menghapus menu '" + menu.getNama() + "'?");
        if (konfirmasiAksi("Konfirmasi penghapusan?")) {
            daftarMenu.remove(nomor - 1);
            System.out.println("Menu berhasil dihapus!");
        } else {
            System.out.println("Penghapusan dibatalkan.");
        }
    }

    /**
     * Menampilkan semua menu dengan nomor urut.
     */
    private static void tampilkanMenuDenganNomor() {
        System.out.println("\nDaftar Menu:");
        int nomor = 1;
        for (Menu menu : daftarMenu) {
            System.out.println(nomor + ". " + menu + " [" + menu.getKategori() + "]");
            nomor++;
        }
    }

    /**
     * Fungsi konfirmasi aksi dengan input 'Ya' atau 'Tidak'.
     * Menggunakan while loop untuk validasi input.
     * @param pesan Pesan konfirmasi
     * @return true jika user menjawab 'Ya', false jika 'Tidak'
     */
    private static boolean konfirmasiAksi(String pesan) {
        String input;
        // while loop untuk validasi input konfirmasi
        while (true) {
            System.out.print(pesan + " (Ya/Tidak): ");
            input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("Ya") || input.equalsIgnoreCase("Y")) {
                return true;
            } else if (input.equalsIgnoreCase("Tidak") || input.equalsIgnoreCase("T") || input.equalsIgnoreCase("No") || input.equalsIgnoreCase("N")) {
                return false;
            } else {
                System.out.println("Input tidak valid! Masukkan 'Ya' atau 'Tidak'.");
            }
        }
    }

    /**
     * Membaca input angka dari user dengan penanganan error.
     * @param pesan Pesan prompt
     * @return Angka yang diinput user
     */
    private static int bacaInputAngka(String pesan) {
        while (true) {
            System.out.print(pesan);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka!");
            }
        }
    }
}
