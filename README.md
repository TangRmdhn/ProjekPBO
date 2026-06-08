# Dokumentasi Project Akhir PBO - Sistem Kasir (POS)

Aplikasi desktop sederhana berbasis **Java Swing** yang menerapkan konsep PBO (Pemrograman Berorientasi Objek) dengan pola **MVC** dan terhubung ke database **MySQL**.

> **Catatan penting:** Database dan tabel dibuat **OTOMATIS** oleh aplikasi saat pertama dijalankan. Anda **tidak perlu** membuat database `kasir_db` atau menjalankan `database.sql` secara manual. Cukup pastikan server MySQL menyala.

---

## Daftar Isi
1. [Yang Harus Diinstall Dulu](#1-yang-harus-diinstall-dulu)
2. [Cara Clone Repo dari GitHub](#2-cara-clone-repo-dari-github)
3. [Setup Koneksi Database](#3-setup-koneksi-database)
4. [Pastikan MySQL Sudah Menyala](#4-pastikan-mysql-sudah-menyala)
5. [Cara Membuka Project di NetBeans](#5-cara-membuka-project-di-netbeans)
6. [Menjalankan Aplikasi](#6-menjalankan-aplikasi)
7. [Struktur Folder & Penjelasan MVC](#struktur-folder--penjelasan-mvc)
8. [Konsep OOP yang Digunakan](#konsep-oop-yang-digunakan)
9. [Troubleshooting (Kalau Error)](#troubleshooting-kalau-error)

---

## 1. Yang Harus Diinstall Dulu

Pastikan laptop sudah terpasang:

| Aplikasi | Fungsi | Catatan |
|----------|--------|---------|
| **JDK 8 atau lebih baru** | Menjalankan kode Java | Wajib |
| **NetBeans IDE** | Tempat menulis & menjalankan project | Wajib |
| **XAMPP** (atau MySQL Server) | Menyediakan database MySQL | Wajib |
| **Git** | Untuk clone repo dari GitHub | Opsional (bisa download ZIP) |

> Driver MySQL (`mysql-connector-j`) **sudah disertakan** di dalam folder `lib/`, jadi tidak perlu download lagi.

---

## 2. Cara Clone Repo dari GitHub

Buka terminal / Git Bash / CMD, lalu jalankan:

```bash
git clone https://github.com/TangRmdhn/ProjekPBO.git
cd ProjekPBO
```

> **Tidak punya Git?** Buka halaman repo di browser → tombol hijau **Code** → **Download ZIP** → lalu ekstrak.

---

## 3. Setup Koneksi Database

Buka file:

```
src/database/DatabaseConnection.java
```

Bagian atas file berisi konfigurasi. **Nilai default sudah cocok untuk XAMPP standar**, jadi biasanya tidak perlu diubah:

```java
private static final String HOST = "jdbc:mysql://localhost:3306/"; // port default MySQL
private static final String NAMA_DATABASE = "kasir_db";            // nama database (dibuat otomatis)
private static final String USER = "root";                         // username default XAMPP
private static final String PASSWORD = "";                         // password default XAMPP (kosong)
```

Sesuaikan **hanya jika** setup MySQL Anda berbeda:

- **Port bukan 3306?** Ganti angka `3306` (misal XAMPP lama memakai `3307`).
- **Punya password root?** Isi di antara tanda kutip `PASSWORD`, contoh: `"admin123"`.
- **Username bukan root?** Ganti nilai `USER`.

---

## 4. Pastikan MySQL Sudah Menyala

Aplikasi tidak akan jalan kalau server MySQL mati.

**Jika pakai XAMPP:**
1. Buka **XAMPP Control Panel**.
2. Klik tombol **Start** pada baris **MySQL**.
3. Tunggu sampai tulisannya berubah hijau (Running).

> Tidak perlu buka phpMyAdmin atau membuat database manual — aplikasi yang akan membuat database `kasir_db` beserta tabel `products` dan `transactions` secara otomatis, lengkap dengan beberapa data contoh.

---

## 5. Cara Membuka Project di NetBeans

Repo ini sudah berbentuk project NetBeans (ada folder `nbproject` dan file `build.xml`), jadi langsung bisa dibuka:

1. Buka **NetBeans IDE**.
2. Menu **File → Open Project**.
3. Arahkan ke folder hasil clone tadi (`ProjekPBO`), lalu klik **Open Project**.
4. Project akan muncul di panel **Projects** di kiri.

### Pastikan Library JDBC terpasang
Driver sudah ada di folder `lib/`. Cek di panel Projects → buka **Libraries**. Harusnya ada `mysql-connector-j-...jar`.

Kalau **tidak ada** (atau muncul tanda error merah):
1. Klik kanan folder **Libraries** → **Add JAR/Folder**.
2. Arahkan ke `lib/mysql-connector-j-9.7.0/` dan pilih file `.jar`-nya.
3. Klik **Open**.

---

## 6. Menjalankan Aplikasi

1. Pastikan **MySQL sudah menyala** (langkah 4).
2. Di panel Projects, buka `src` → `main` → `Main.java`.
3. Klik kanan `Main.java` → **Run File** (atau tekan **Shift + F6**).
4. Jendela aplikasi kasir akan terbuka.

Saat pertama jalan, di output NetBeans akan muncul:
```
Database dan tabel siap digunakan!
Koneksi Database Berhasil!
```

---

## Struktur Folder & Penjelasan MVC

Project memakai pola **MVC berlapis** supaya tiap bagian punya tugas jelas:

```text
src/
 ├── main/
 │    └── Main.java                 # Titik awal aplikasi (menjalankan View)
 ├── view/
 │    └── MainView.java             # TAMPILAN (GUI Swing) — hanya urusan tampilan
 ├── controller/
 │    ├── ProductController.java    # JEMBATAN View <-> DAO + validasi sederhana
 │    └── TransactionController.java# Jembatan transaksi + hitung total (logika bisnis)
 ├── dao/
 │    ├── ProductDAO.java           # SEMUA perintah SQL produk berkumpul di sini
 │    └── TransactionDAO.java       # Semua perintah SQL transaksi
 ├── model/
 │    ├── Product.java              # Superclass produk (abstract)
 │    ├── FoodProduct.java          # Subclass Makanan
 │    ├── DrinkProduct.java         # Subclass Minuman
 │    ├── ProductFactory.java       # "Pabrik": memilih kelas berdasar kategori
 │    └── Transaction.java          # Model data transaksi
 └── database/
      └── DatabaseConnection.java   # Koneksi JDBC + auto-buat database & tabel
```

**Alur kerja (contoh tambah produk):**
```
MainView (klik tombol Tambah)
   -> ProductController (validasi + buat objek lewat ProductFactory)
      -> ProductDAO (jalankan INSERT ke MySQL)
```

Tiap lapisan hanya bicara dengan lapisan tetangganya:
- **View** tidak menulis SQL dan tidak menghitung apa pun — hanya menampilkan & menerima input.
- **Controller** tidak tahu soal SQL — hanya mengatur logika dan meneruskan ke DAO.
- **DAO** satu-satunya yang menyentuh database.
- **Model** hanya menyimpan data (apa itu sebuah Produk/Transaksi).

---

## Konsep OOP yang Digunakan

1. **Class & Object** — `Product`, `Transaction`, dll. adalah blueprint; `new FoodProduct(...)` adalah object.
2. **Encapsulation** — field di model bersifat `private`/`protected`, diakses lewat getter/setter.
3. **Inheritance** — `FoodProduct` dan `DrinkProduct` memakai `extends Product`.
4. **Polymorphism** — method `getProductInfo()` di-override; banyak objek anak disimpan dalam satu `ArrayList<Product>` (upcasting).
5. **Abstraction** — `Product` adalah `abstract class` dengan method abstrak `getProductInfo()`.
6. **Constructor** — memberi nilai awal saat object dibuat.
7. **Exception Handling** — `try-catch` untuk `SQLException` (DAO) dan `NumberFormatException` (View).
8. **Collection** — `ArrayList<Product>` & `ArrayList<Transaction>` untuk menampung data sebelum ditampilkan ke `JTable`.
9. **Multithreading** — jam pada header berjalan di Thread terpisah (`startClockThread`).
10. **Database Transaction** — `setAutoCommit(false)` + `commit()`/`rollback()` di `TransactionDAO` agar data tidak inkonsisten.

---

## Troubleshooting (Kalau Error)

| Pesan / Gejala | Penyebab | Solusi |
|----------------|----------|--------|
| "Gagal terhubung ke database!" | MySQL belum menyala | Start MySQL di XAMPP (langkah 4) |
| `Communications link failure` | Port salah | Cek port di `DatabaseConnection.java` (3306 vs 3307) |
| `Access denied for user 'root'` | Password/username salah | Sesuaikan `USER` & `PASSWORD` di `DatabaseConnection.java` |
| `Driver JDBC tidak ditemukan` | Library belum dipasang | Tambahkan `.jar` dari folder `lib/` ke Libraries NetBeans |
| Data produk dobel | Wajar — data contoh hanya diisi saat tabel masih kosong | Abaikan, atau hapus lewat aplikasi |

---

> **Tips Presentasi:**
> 1. Tunjukkan database otomatis terbuat saat aplikasi pertama dijalankan.
> 2. Buka `Product` lalu `FoodProduct` untuk menjelaskan Inheritance & Abstraction.
> 3. Tunjukkan alur View → Controller → DAO untuk menjelaskan pemisahan tugas (MVC).
> 4. Praktekkan transaksi, lalu tunjukkan stok berkurang & riwayat bertambah di database.
