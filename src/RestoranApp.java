import java.util.ArrayList;
import java.util.Scanner;

public class RestoranApp {

    // Konstanta untuk biaya dan diskon
    private static final int BIAYA_PELAYANAN = 20000;
    private static final int MINIMUM_B1G1_AMOUNT = 50000;
    private static final int MINIMUM_DISCOUNT_AMOUNT = 100000;

    // Menggunakan ArrayList<Menu> untuk menyimpan daftar menu
    private static ArrayList<Menu> daftarMenu = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void inisialisasiMenu() {
        // Kategori Makanan (minimal 4)
        daftarMenu.add(new Menu("Nasi Goreng", 25000, "Makanan"));
        daftarMenu.add(new Menu("Mie Goreng", 22000, "Makanan"));
        daftarMenu.add(new Menu("Ayam Bakar", 35000, "Makanan"));
        daftarMenu.add(new Menu("Sate Ayam", 30000, "Makanan"));

        // Kategori Minuman (minimal 4)
        daftarMenu.add(new Menu("Es Teh Manis", 8000, "Minuman"));
        daftarMenu.add(new Menu("Es Jeruk", 10000, "Minuman"));
        daftarMenu.add(new Menu("Kopi Hitam", 12000, "Minuman"));
        daftarMenu.add(new Menu("Jus Alpukat", 15000, "Minuman"));
    }

    // Menampilkan menu dalam format terkelompok menggunakan for-each loop
    public static void tampilkanMenu() {
        System.out.println("\n========= DAFTAR MENU =========");

        // Tampilkan Makanan menggunakan for-each loop
        System.out.println("\n--- MAKANAN ---");
        int nomor = 1;
        for (Menu menu : daftarMenu) {
            if (menu.getKategori().equals("Makanan")) {
                System.out.println(nomor + ". " + menu.getNama() + " - Rp " + menu.getHarga());
                nomor++;
            }
        }

        // Tampilkan Minuman menggunakan for-each loop
        System.out.println("\n--- MINUMAN ---");
        nomor = 1;
        for (Menu menu : daftarMenu) {
            if (menu.getKategori().equals("Minuman")) {
                System.out.println(nomor + ". " + menu.getNama() + " - Rp " + menu.getHarga());
                nomor++;
            }
        }
        System.out.println("================================\n");
    }

    // Menampilkan menu dengan nomor urut untuk manajemen
    public static void tampilkanMenuDenganNomor() {
        System.out.println("\n========= DAFTAR MENU =========");
        int nomor = 1;
        for (Menu menu : daftarMenu) {
            System.out.println(nomor + ". " + menu.getNama() + " (Rp " + menu.getHarga() + ") - " + menu.getKategori());
            nomor++;
        }
        System.out.println("================================\n");
    }

    // Mencari menu berdasarkan nama
    public static Menu cariMenu(String namaMenu) {
        for (Menu menu : daftarMenu) {
            if (menu.getNama().equalsIgnoreCase(namaMenu)) {
                return menu;
            }
        }
        return null;
    }

    // Fungsi Pemesanan Pelanggan
    public static void pemesananPelanggan() {
        ArrayList<String> namaPesanan = new ArrayList<>();
        ArrayList<Integer> jumlahPesanan = new ArrayList<>();
        ArrayList<Menu> menuPesanan = new ArrayList<>();

        tampilkanMenu();

        System.out.println("Silakan masukkan pesanan Anda.");
        System.out.println("Ketik nama menu dan jumlahnya. Ketik 'selesai' untuk mengakhiri pesanan.\n");

        // Menggunakan while loop untuk input pesanan hingga 'selesai'
        while (true) {
            System.out.print("Nama menu (atau 'selesai'): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("selesai")) {
                break;
            }

            // Validasi input menu dengan while loop - berulang jika tidak valid
            Menu menuDitemukan = cariMenu(input);
            while (menuDitemukan == null) {
                System.out.println("Menu tidak ditemukan! Silakan masukkan nama menu yang valid.");
                System.out.print("Nama menu (atau 'selesai'): ");
                input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase("selesai")) {
                    break;
                }
                menuDitemukan = cariMenu(input);
            }

            if (input.equalsIgnoreCase("selesai")) {
                break;
            }

            // Input jumlah dengan validasi
            int jumlah = 0;
            boolean jumlahValid = false;
            while (!jumlahValid) {
                System.out.print("Jumlah: ");
                String jumlahInput = scanner.nextLine().trim();
                try {
                    jumlah = Integer.parseInt(jumlahInput);
                    if (jumlah > 0) {
                        jumlahValid = true;
                    } else {
                        System.out.println("Jumlah harus lebih dari 0!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Input tidak valid! Masukkan angka.");
                }
            }

            namaPesanan.add(menuDitemukan.getNama());
            jumlahPesanan.add(jumlah);
            menuPesanan.add(menuDitemukan);
            System.out.println(">> " + menuDitemukan.getNama() + " x " + jumlah + " ditambahkan.\n");
        }

        // Jika tidak ada pesanan
        if (namaPesanan.isEmpty()) {
            System.out.println("Tidak ada pesanan. Kembali ke menu utama.\n");
            return;
        }

        // Perhitungan Total Biaya
        int subtotal = 0;
        int jumlahMinuman = 0;
        int hargaMinumanTermurah = Integer.MAX_VALUE;

        // Hitung subtotal dan lacak minuman
        for (int i = 0; i < menuPesanan.size(); i++) {
            Menu menu = menuPesanan.get(i);
            int qty = jumlahPesanan.get(i);
            subtotal += menu.getHarga() * qty;

            // Melacak jumlah minuman untuk B1G1
            if (menu.getKategori().equals("Minuman")) {
                jumlahMinuman += qty;
                if (menu.getHarga() < hargaMinumanTermurah) {
                    hargaMinumanTermurah = menu.getHarga();
                }
            }
        }

        // Diskon/Penawaran menggunakan if-else if
        int diskonB1G1 = 0;
        int diskon10Persen = 0;

        // Penawaran B1G1 untuk Minuman jika total > Rp 50.000
        if (subtotal > MINIMUM_B1G1_AMOUNT && jumlahMinuman >= 2) {
            // Beli 1 Gratis 1: memberikan diskon seharga 1 minuman termurah
            diskonB1G1 = hargaMinumanTermurah;
        }

        int subtotalSetelahB1G1 = subtotal - diskonB1G1;

        // Pajak 10%
        int pajak = (int) (subtotalSetelahB1G1 * 0.10);

        // Biaya Pelayanan Rp 20.000
        int biayaPelayanan = BIAYA_PELAYANAN;

        int totalSementara = subtotalSetelahB1G1 + pajak + biayaPelayanan;

        // Diskon 10% jika total > Rp 100.000
        if (totalSementara > MINIMUM_DISCOUNT_AMOUNT) {
            diskon10Persen = (int) (totalSementara * 0.10);
        }

        int totalAkhir = totalSementara - diskon10Persen;

        // Cetak Struk Pesanan
        cetakStruk(namaPesanan, jumlahPesanan, menuPesanan, subtotal, diskonB1G1,
                subtotalSetelahB1G1, pajak, biayaPelayanan, totalSementara, diskon10Persen, totalAkhir);
    }

    // Mencetak struk pesanan
    public static void cetakStruk(ArrayList<String> namaPesanan, ArrayList<Integer> jumlahPesanan,
                                   ArrayList<Menu> menuPesanan, int subtotal, int diskonB1G1,
                                   int subtotalSetelahB1G1, int pajak, int biayaPelayanan,
                                   int totalSementara, int diskon10Persen, int totalAkhir) {

        System.out.println("\n============ STRUK PESANAN ============");
        System.out.println("--- Rincian Pesanan ---");

        // Menggunakan for loop untuk mencetak detail item
        for (int i = 0; i < menuPesanan.size(); i++) {
            Menu menu = menuPesanan.get(i);
            int qty = jumlahPesanan.get(i);
            int totalItem = menu.getHarga() * qty;
            System.out.printf("%-15s %d x Rp %,d = Rp %,d%n",
                    menu.getNama(), qty, menu.getHarga(), totalItem);
        }

        System.out.println("---------------------------------------");
        System.out.printf("Subtotal:                    Rp %,d%n", subtotal);

        // Tampilkan diskon B1G1 jika ada
        if (diskonB1G1 > 0) {
            System.out.printf("Diskon B1G1 Minuman:        -Rp %,d%n", diskonB1G1);
            System.out.printf("Subtotal setelah B1G1:       Rp %,d%n", subtotalSetelahB1G1);
        }

        System.out.println("\n--- Rincian Biaya ---");
        System.out.printf("Pajak (10%%):                 Rp %,d%n", pajak);
        System.out.printf("Biaya Pelayanan:             Rp %,d%n", biayaPelayanan);
        System.out.printf("Total Sementara:             Rp %,d%n", totalSementara);

        // Tampilkan diskon 10% jika ada
        if (diskon10Persen > 0) {
            System.out.printf("Diskon 10%% (>100rb):        -Rp %,d%n", diskon10Persen);
        }

        System.out.println("=======================================");
        System.out.printf("TOTAL AKHIR:                 Rp %,d%n", totalAkhir);
        System.out.println("=======================================");
        System.out.println("Terima kasih atas kunjungan Anda!\n");
    }

    // Fungsi Manajemen Menu (Pemilik)
    public static void manajemenMenu() {
        int pilihan;

        // Menggunakan do-while untuk sub-menu manajemen
        do {
            System.out.println("\n===== MANAJEMEN MENU (PEMILIK) =====");
            System.out.println("1. Tambah Menu Baru");
            System.out.println("2. Ubah Harga Menu");
            System.out.println("3. Hapus Menu");
            System.out.println("4. Kembali ke Menu Utama");
            System.out.print("Pilih opsi (1-4): ");

            pilihan = 0;
            try {
                pilihan = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid!");
                continue;
            }

            // Menggunakan switch untuk navigasi
            switch (pilihan) {
                case 1:
                    tambahMenuBaru();
                    break;
                case 2:
                    ubahHargaMenu();
                    break;
                case 3:
                    hapusMenu();
                    break;
                case 4:
                    System.out.println("Kembali ke menu utama...\n");
                    break;
                default:
                    System.out.println("Pilihan tidak valid! Silakan pilih 1-4.");
            }
        } while (pilihan != 4);
    }

    // Tambah Menu Baru
    public static void tambahMenuBaru() {
        System.out.println("\n--- Tambah Menu Baru ---");

        System.out.print("Nama menu baru: ");
        String nama = scanner.nextLine().trim();

        // Validasi harga dengan while loop
        int harga = 0;
        boolean hargaValid = false;
        while (!hargaValid) {
            System.out.print("Harga (Rp): ");
            try {
                harga = Integer.parseInt(scanner.nextLine().trim());
                if (harga > 0) {
                    hargaValid = true;
                } else {
                    System.out.println("Harga harus lebih dari 0!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid! Masukkan angka.");
            }
        }

        // Pilih kategori dengan switch
        String kategori = "";
        boolean kategoriValid = false;
        while (!kategoriValid) {
            System.out.print("Kategori (1=Makanan, 2=Minuman): ");
            String kat = scanner.nextLine().trim();
            switch (kat) {
                case "1":
                    kategori = "Makanan";
                    kategoriValid = true;
                    break;
                case "2":
                    kategori = "Minuman";
                    kategoriValid = true;
                    break;
                default:
                    System.out.println("Pilihan tidak valid! Pilih 1 atau 2.");
            }
        }

        daftarMenu.add(new Menu(nama, harga, kategori));
        System.out.println("Menu '" + nama + "' berhasil ditambahkan!\n");
    }

    // Ubah Harga Menu
    public static void ubahHargaMenu() {
        System.out.println("\n--- Ubah Harga Menu ---");
        tampilkanMenuDenganNomor();

        System.out.print("Masukkan nomor menu yang ingin diubah: ");
        int nomor = 0;
        try {
            nomor = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Input tidak valid!");
            return;
        }

        if (nomor < 1 || nomor > daftarMenu.size()) {
            System.out.println("Nomor menu tidak valid!");
            return;
        }

        Menu menuPilih = daftarMenu.get(nomor - 1);
        System.out.println("Menu dipilih: " + menuPilih.getNama() + " (Harga saat ini: Rp " + menuPilih.getHarga() + ")");

        // Konfirmasi dengan while loop untuk validasi input
        String konfirmasi = "";
        while (!konfirmasi.equalsIgnoreCase("Ya") && !konfirmasi.equalsIgnoreCase("Tidak")) {
            System.out.print("Apakah Anda yakin ingin mengubah harga? (Ya/Tidak): ");
            konfirmasi = scanner.nextLine().trim();
            if (!konfirmasi.equalsIgnoreCase("Ya") && !konfirmasi.equalsIgnoreCase("Tidak")) {
                System.out.println("Input tidak valid! Ketik 'Ya' atau 'Tidak'.");
            }
        }

        if (konfirmasi.equalsIgnoreCase("Ya")) {
            int hargaBaru = 0;
            boolean hargaValid = false;
            while (!hargaValid) {
                System.out.print("Masukkan harga baru (Rp): ");
                try {
                    hargaBaru = Integer.parseInt(scanner.nextLine().trim());
                    if (hargaBaru > 0) {
                        hargaValid = true;
                    } else {
                        System.out.println("Harga harus lebih dari 0!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Input tidak valid! Masukkan angka.");
                }
            }
            menuPilih.setHarga(hargaBaru);
            System.out.println("Harga menu '" + menuPilih.getNama() + "' berhasil diubah menjadi Rp " + hargaBaru + "!\n");
        } else {
            System.out.println("Perubahan dibatalkan.\n");
        }
    }

    // Hapus Menu
    public static void hapusMenu() {
        System.out.println("\n--- Hapus Menu ---");
        tampilkanMenuDenganNomor();

        System.out.print("Masukkan nomor menu yang ingin dihapus: ");
        int nomor = 0;
        try {
            nomor = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Input tidak valid!");
            return;
        }

        if (nomor < 1 || nomor > daftarMenu.size()) {
            System.out.println("Nomor menu tidak valid!");
            return;
        }

        Menu menuPilih = daftarMenu.get(nomor - 1);
        System.out.println("Menu dipilih: " + menuPilih.getNama() + " (Rp " + menuPilih.getHarga() + ")");

        // Konfirmasi dengan while loop untuk validasi input
        String konfirmasi = "";
        while (!konfirmasi.equalsIgnoreCase("Ya") && !konfirmasi.equalsIgnoreCase("Tidak")) {
            System.out.print("Apakah Anda yakin ingin menghapus menu ini? (Ya/Tidak): ");
            konfirmasi = scanner.nextLine().trim();
            if (!konfirmasi.equalsIgnoreCase("Ya") && !konfirmasi.equalsIgnoreCase("Tidak")) {
                System.out.println("Input tidak valid! Ketik 'Ya' atau 'Tidak'.");
            }
        }

        if (konfirmasi.equalsIgnoreCase("Ya")) {
            String namaMenu = menuPilih.getNama();
            daftarMenu.remove(nomor - 1);
            System.out.println("Menu '" + namaMenu + "' berhasil dihapus!\n");
        } else {
            System.out.println("Penghapusan dibatalkan.\n");
        }
    }

    public static void main(String[] args) {
        // Check for --web flag to start web server
        if (args.length > 0 && args[0].equals("--web")) {
            int port = 8080;
            if (args.length > 1) {
                try {
                    port = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid port number, using default 8080");
                }
            }
            WebServer.start(port);
            return;
        }
        
        // CLI Mode
        // Inisialisasi Menu
        inisialisasiMenu();

        int pilihanUtama;

        // Menggunakan do-while untuk perulangan menu utama
        do {
            System.out.println("========================================");
            System.out.println("   SELAMAT DATANG DI RESTORAN KAMI");
            System.out.println("========================================");
            System.out.println("1. Pemesanan Pelanggan");
            System.out.println("2. Manajemen Menu (Pemilik)");
            System.out.println("0. Keluar");
            System.out.print("Pilih opsi: ");

            pilihanUtama = -1;
            try {
                pilihanUtama = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid! Silakan masukkan angka.\n");
                continue;
            }

            // Menggunakan switch untuk navigasi menu utama
            switch (pilihanUtama) {
                case 1:
                    pemesananPelanggan();
                    break;
                case 2:
                    manajemenMenu();
                    break;
                case 0:
                    System.out.println("Terima kasih telah mengunjungi restoran kami!");
                    System.out.println("Sampai jumpa kembali!\n");
                    break;
                default:
                    System.out.println("Pilihan tidak valid! Silakan pilih 0, 1, atau 2.\n");
            }
        } while (pilihanUtama != 0);

        scanner.close();
    }
}
