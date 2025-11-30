# Restoran Cashier App

Aplikasi kasir restoran sederhana dengan dukungan CLI (Command Line Interface) dan Web Interface.

## Fitur

### Pemesanan Pelanggan
- Menampilkan menu dalam kategori (Makanan/Minuman)
- Input pesanan dengan validasi
- Perhitungan otomatis:
  - Subtotal
  - Pajak 10%
  - Biaya Pelayanan Rp 20.000
  - Diskon B1G1 untuk Minuman (jika subtotal > Rp 50.000 dan minimal 2 minuman)
  - Diskon 10% (jika total > Rp 100.000)
- Cetak struk pesanan

### Manajemen Menu (Pemilik)
- Tambah menu baru
- Ubah harga menu
- Hapus menu
- Konfirmasi sebelum perubahan

## Cara Menjalankan

### Mode CLI (Command Line)

**Opsi 1: Tanpa Maven (Langsung compile)**
```bash
cd src
javac Menu.java RestoranApp.java
java RestoranApp
```

**Opsi 2: Dengan Maven**
```bash
mvn compile exec:java -Dexec.mainClass="RestoranApp"
```

### Mode Web (Browser)

**Prasyarat:** Maven harus terinstall

```bash
# Build project
mvn clean package

# Jalankan web server
java -jar target/restoran-cashier-app-1.0-SNAPSHOT.jar --web

# Atau dengan port custom
java -jar target/restoran-cashier-app-1.0-SNAPSHOT.jar --web 3000
```

Buka browser dan akses: `http://localhost:8080` (atau port yang ditentukan)

## Teknologi

- **Java 17+**
- **Javalin** - Web framework ringan
- **Jackson** - JSON processing
- **HTML/CSS/JavaScript** - Frontend

## Struktur Proyek

```
restoran-cashier-app-java/
├── pom.xml              # Maven configuration
├── README.md            # Dokumentasi
└── src/
    ├── Menu.java        # Model class untuk menu
    ├── RestoranApp.java # Main app dengan CLI logic
    └── WebServer.java   # Web server dengan REST API
```

## API Endpoints (Mode Web)

| Method | Endpoint | Deskripsi |
|--------|----------|-----------|
| GET | `/api/menu` | Ambil semua menu |
| GET | `/api/menu/grouped` | Ambil menu berdasarkan kategori |
| POST | `/api/menu` | Tambah menu baru |
| PUT | `/api/menu/{index}` | Ubah harga menu |
| DELETE | `/api/menu/{index}` | Hapus menu |
| POST | `/api/order/calculate` | Hitung total pesanan |

## Screenshots

### Web Interface
Akses `http://localhost:8080` setelah menjalankan web server untuk melihat interface web dengan fitur:
- Pilih menu dengan UI yang intuitif
- Manajemen menu (CRUD)
- Struk pembayaran digital

### CLI Interface
Jalankan tanpa flag `--web` untuk mode command line tradisional.

## Lisensi

MIT License
