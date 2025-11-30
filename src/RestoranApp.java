import java.util.Scanner;
public class RestoranApp {

    private static Menu[] daftarMenu = new Menu[12];

    public static void inisialisasiMenu() {
        // Kategori Lauk
        daftarMenu[0] = new Menu("Ayam Bakar", 25000.0, "Lauk");
        daftarMenu[1] = new Menu("Ikan Goreng", 28000.0, "Lauk");
        daftarMenu[2] = new Menu("Tahu & Tempe", 10000.0, "Lauk");
        daftarMenu[3] = new Menu("Udang Sambal", 35000.0, "Lauk");

        // Kategori Sayur
        daftarMenu[4] = new Menu("Gulai Daun Singkong", 15000.0, "Sayur");
        daftarMenu[5] = new Menu("Sayur Sop", 12000.0, "Sayur");
        daftarMenu[6] = new Menu("Gulai Nangka Muda", 18000.0, "Sayur");
        daftarMenu[7] = new Menu("Sayur Daun Ubi Tumbuk", 16000.0, "Sayur");

        // Kategori Minuman
        daftarMenu[8] = new Menu("Air Putih", 5000.0, "Minuman");
        daftarMenu[9] = new Menu("Teh Tawar", 6000.0, "Minuman");
        daftarMenu[10] = new Menu("Teh Manis", 8000.0, "Minuman");
        daftarMenu[11] = new Menu("Jeruk Hangat", 10000.0, "Minuman");
    }


    public static void tampilkanMenu() {
        System.out.println("========= Selamat Datang di Restoran Sederhana =========");
        System.out.println("Berikut adalah daftar menu kami:");

        // Menampilkan Lauk (Indeks 0-3)
        System.out.println("\n--- Lauk ---");
        System.out.println("1. " + daftarMenu[0].getNama() + " - Rp " + daftarMenu[0].getHarga());
        System.out.println("2. " + daftarMenu[1].getNama() + " - Rp " + daftarMenu[1].getHarga());
        System.out.println("3. " + daftarMenu[2].getNama() + " - Rp " + daftarMenu[2].getHarga());
        System.out.println("4. " + daftarMenu[3].getNama() + " - Rp " + daftarMenu[3].getHarga());

        // Menampilkan Sayur (Indeks 4-7)
        System.out.println("\n--- Sayur ---");
        System.out.println("5. " + daftarMenu[4].getNama() + " - Rp " + daftarMenu[4].getHarga());
        System.out.println("6. " + daftarMenu[5].getNama() + " - Rp " + daftarMenu[5].getHarga());
        System.out.println("7. " + daftarMenu[6].getNama() + " - Rp " + daftarMenu[6].getHarga());
        System.out.println("8. " + daftarMenu[7].getNama() + " - Rp " + daftarMenu[7].getHarga());

        // Menampilkan Minuman (Indeks 8-11)
        System.out.println("\n--- Minuman ---");
        System.out.println("9. " + daftarMenu[8].getNama() + " - Rp " + daftarMenu[8].getHarga());
        System.out.println("10. " + daftarMenu[9].getNama() + " - Rp " + daftarMenu[9].getHarga());
        System.out.println("11. " + daftarMenu[10].getNama() + " - Rp " + daftarMenu[10].getHarga());
        System.out.println("12. " + daftarMenu[11].getNama() + " - Rp " + daftarMenu[11].getHarga());

        System.out.println("\n=======================================================");
    }


    public static Menu cariMenu(String namaMenu) {
        if (namaMenu.equalsIgnoreCase(daftarMenu[0].getNama())) {
            return daftarMenu[0];
        } else if (namaMenu.equalsIgnoreCase(daftarMenu[1].getNama())) {
            return daftarMenu[1];
        } else if (namaMenu.equalsIgnoreCase(daftarMenu[2].getNama())) {
            return daftarMenu[2];
        } else if (namaMenu.equalsIgnoreCase(daftarMenu[3].getNama())) {
            return daftarMenu[3];
        } else if (namaMenu.equalsIgnoreCase(daftarMenu[4].getNama())) {
            return daftarMenu[4];
        } else if (namaMenu.equalsIgnoreCase(daftarMenu[5].getNama())) {
            return daftarMenu[5];
        } else if (namaMenu.equalsIgnoreCase(daftarMenu[6].getNama())) {
            return daftarMenu[6];
        } else if (namaMenu.equalsIgnoreCase(daftarMenu[7].getNama())) {
            return daftarMenu[7];
        } else if (namaMenu.equalsIgnoreCase(daftarMenu[8].getNama())) {
            return daftarMenu[8];
        } else if (namaMenu.equalsIgnoreCase(daftarMenu[9].getNama())) {
            return daftarMenu[9];
        } else if (namaMenu.equalsIgnoreCase(daftarMenu[10].getNama())) {
            return daftarMenu[10];
        } else if (namaMenu.equalsIgnoreCase(daftarMenu[11].getNama())) {
            return daftarMenu[11];
        } else {
            return null; // Menu tidak ditemukan
        }
    }

    public static void cetakStruk(
            Menu item1, int jumlah1, double totalItem1,
            Menu item2, int jumlah2, double totalItem2,
            Menu item3, int jumlah3, double totalItem3,
            Menu item4, int jumlah4, double totalItem4,
            double subTotalAwal, double diskonMinuman, double subTotalSetelahBOGO,
            double biayaPajak, double biayaPelayanan, double totalSementara,
            double diskonUmum, double totalAkhir) {

        System.out.println("\n\n========= STRUK PEMBAYARAN ANDA =========\n");
        System.out.println("--- Rincian Pesanan ---");

        // Struktur Keputusan untuk mencetak item hanya jika dipesan (jumlah > 0)
        if (item1 != null && jumlah1 > 0) {
            System.out.println(item1.getNama() + "\t" + jumlah1 + " x " + item1.getHarga() + "\t = Rp " + totalItem1);
        }
        if (item2 != null && jumlah2 > 0) {
            System.out.println(item2.getNama() + "\t" + jumlah2 + " x " + item2.getHarga() + "\t = Rp " + totalItem2);
        }
        if (item3 != null && jumlah3 > 0) {
            System.out.println(item3.getNama() + "\t" + jumlah3 + " x " + item3.getHarga() + "\t = Rp " + totalItem3);
        }
        if (item4 != null && jumlah4 > 0) {
            System.out.println(item4.getNama() + "\t" + jumlah4 + " x " + item4.getHarga() + "\t = Rp " + totalItem4);
        }

        System.out.println("-------------------------------------------");
        System.out.println("Subtotal Awal: \t\t\t Rp " + subTotalAwal);

        // Struktur Keputusan: Tampilkan diskon B1G1 hanya jika ada
        if (diskonMinuman > 0) {
            System.out.println("Diskon B1G1 Minuman: \t\t -Rp " + diskonMinuman);
            System.out.println("Subtotal (setelah B1G1): \t Rp " + subTotalSetelahBOGO);
        }

        System.out.println("\n--- Rincian Biaya ---");
        System.out.println("Pajak (10%): \t\t\t Rp " + biayaPajak);
        System.out.println("Biaya Pelayanan: \t\t Rp " + biayaPelayanan);
        System.out.println("Total Sementara: \t\t Rp " + totalSementara);

        // Struktur Keputusan: Tampilkan diskon 10% hanya jika ada
        if (diskonUmum > 0) {
            System.out.println("Diskon 10% (Total > 100rb): \t -Rp " + diskonUmum);
        }

        System.out.println("===========================================");
        System.out.println("TOTAL AKHIR: \t\t\t Rp " + totalAkhir);
        System.out.println("===========================================");
        System.out.println("Terima kasih atas kunjungan Anda!");
    }

    public static void main(String[] args) {
        // 1. Inisialisasi dan Tampilkan Menu
        inisialisasiMenu();
        tampilkanMenu();

        // 2. Pemesanan (Maks 4 Menu)
        Scanner scanner = new Scanner(System.in);
        System.out.println("Silakan masukkan pesanan Anda (maks 4 item).");
        System.out.println("Format: Nama Menu = Jumlah (Contoh: Ayam Bakar = 2)");
        System.out.println("Jika selesai, ketik 'Selesai = 0'");

        // Menerima input 4 baris pesanan
        System.out.print("Pesanan 1: ");
        String line1 = scanner.nextLine();
        System.out.print("Pesanan 2: ");
        String line2 = scanner.nextLine();
        System.out.print("Pesanan 3: ");
        String line3 = scanner.nextLine();
        System.out.print("Pesanan 4: ");
        String line4 = scanner.nextLine();

        scanner.close();

        // Variabel untuk menyimpan 4 pesanan
        String namaMenu1 = "", namaMenu2 = "", namaMenu3 = "", namaMenu4 = "";
        int jumlah1 = 0, jumlah2 = 0, jumlah3 = 0, jumlah4 = 0;
        Menu item1 = null, item2 = null, item3 = null, item4 = null;
        double totalItem1 = 0, totalItem2 = 0, totalItem3 = 0, totalItem4 = 0;

        // Memproses Pesanan 1 (menggunakan split dan pencarian manual)
        String[] parts1 = line1.split(" = ");
        if (parts1.length == 2) { // Validasi input sederhana
            namaMenu1 = parts1[0].trim();
            jumlah1 = Integer.parseInt(parts1[1].trim());
            item1 = cariMenu(namaMenu1); // Menggunakan method 'cariMenu'
            if (item1 != null) {
                totalItem1 = item1.getHarga() * jumlah1;
            }
        }

        // Memproses Pesanan 2
        String[] parts2 = line2.split(" = ");
        if (parts2.length == 2) {
            namaMenu2 = parts2[0].trim();
            jumlah2 = Integer.parseInt(parts2[1].trim());
            item2 = cariMenu(namaMenu2);
            if (item2 != null) {
                totalItem2 = item2.getHarga() * jumlah2;
            }
        }

        // Memproses Pesanan 3
        String[] parts3 = line3.split(" = ");
        if (parts3.length == 2) {
            namaMenu3 = parts3[0].trim();
            jumlah3 = Integer.parseInt(parts3[1].trim());
            item3 = cariMenu(namaMenu3);
            if (item3 != null) {
                totalItem3 = item3.getHarga() * jumlah3;
            }
        }

        // Memproses Pesanan 4
        String[] parts4 = line4.split(" = ");
        if (parts4.length == 2) {
            namaMenu4 = parts4[0].trim();
            jumlah4 = Integer.parseInt(parts4[1].trim());
            item4 = cariMenu(namaMenu4);
            if (item4 != null) {
                totalItem4 = item4.getHarga() * jumlah4;
            }
        }

        // 3. Menghitung Total Biaya (Implementasi Modul 4)

        // Hitung Subtotal Awal
        double subTotalAwal = totalItem1 + totalItem2 + totalItem3 + totalItem4;

        // --- SKENARIO STRUKTUR KEPUTUSAN 1: Penawaran B1G1 Minuman ---
        // Kondisi: subTotalAwal > 50.000 DAN ada >= 2 minuman dipesan.
        double diskonMinuman = 0.0;
        int jumlahMinumanDipesan = 0;
        double hargaMinumanTermurah = Double.MAX_VALUE; // Mulai dari nilai tak terhingga

        // Karena tidak boleh loop, kita periksa 4 item secara manual
        if (item1 != null && item1.getKategori().equals("Minuman") && jumlah1 > 0) {
            jumlahMinumanDipesan += jumlah1;
            if (item1.getHarga() < hargaMinumanTermurah) {
                hargaMinumanTermurah = item1.getHarga();
            }
        }
        if (item2 != null && item2.getKategori().equals("Minuman") && jumlah2 > 0) {
            jumlahMinumanDipesan += jumlah2;
            if (item2.getHarga() < hargaMinumanTermurah) {
                hargaMinumanTermurah = item2.getHarga();
            }
        }
        if (item3 != null && item3.getKategori().equals("Minuman") && jumlah3 > 0) {
            jumlahMinumanDipesan += jumlah3;
            if (item3.getHarga() < hargaMinumanTermurah) {
                hargaMinumanTermurah = item3.getHarga();
            }
        }
        if (item4 != null && item4.getKategori().equals("Minuman") && jumlah4 > 0) {
            jumlahMinumanDipesan += jumlah4;
            if (item4.getHarga() < hargaMinumanTermurah) {
                hargaMinumanTermurah = item4.getHarga();
            }
        }

        // Struktur Keputusan (if-else) untuk B1G1
        if (subTotalAwal > 50000.0 && jumlahMinumanDipesan >= 2) {
            // Kondisi terpenuhi: dapat diskon seharga minuman termurah
            diskonMinuman = hargaMinumanTermurah;
        } else {
            // Kondisi tidak terpenuhi
            diskonMinuman = 0.0;
        }

        // Hitung ulang subtotal setelah B1G1
        double subTotalSetelahBOGO = subTotalAwal - diskonMinuman;

        // Hitung Pajak (10%) dan Biaya Pelayanan
        double biayaPajak = subTotalSetelahBOGO * 0.10;
        double biayaPelayanan = 2500.0;
        double totalSementara = subTotalSetelahBOGO + biayaPajak + biayaPelayanan;

        // --- SKENARIO STRUKTUR KEPUTUSAN 2: Diskon Umum 10% ---
        // Kondisi: totalSementara > 100.000
        double diskonUmum = 0.0;

        // Struktur Keputusan (if-else) untuk Diskon Umum
        if (totalSementara > 100000.0) {
            // KONDISI 1: Total Lebih dari Rp 100.000
            diskonUmum = totalSementara * 0.10;
        } else {
            // KONDISI 2: Total Kurang dari atau Sama dengan Rp 100.000
            diskonUmum = 0.0;
        }

        // Hitung Total Akhir
        double totalAkhir = totalSementara - diskonUmum;

        // 4. Mencetak Struk Pesanan
        cetakStruk(item1, jumlah1, totalItem1,
                item2, jumlah2, totalItem2,
                item3, jumlah3, totalItem3,
                item4, jumlah4, totalItem4,
                subTotalAwal, diskonMinuman, subTotalSetelahBOGO,
                biayaPajak, biayaPelayanan, totalSementara,
                diskonUmum, totalAkhir);
    }
}
