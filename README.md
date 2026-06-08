# Dokumentasi Project Akhir PBO - Sistem Kasir (POS)

Project ini adalah aplikasi desktop sederhana berbasis Java Swing yang menerapkan konsep PBO (Pemrograman Berorientasi Objek) dan terhubung dengan database MySQL.

## Struktur Folder Project

```text
src/
 ├── main/
 │    └── Main.java                 # Entry point aplikasi
 ├── database/
 │    └── DatabaseConnection.java   # Konfigurasi koneksi JDBC ke MySQL
 ├── model/
 │    ├── Product.java              # Superclass (Produk)
 │    ├── FoodProduct.java          # Subclass (Makanan)
 │    ├── DrinkProduct.java         # Subclass (Minuman)
 │    └── Transaction.java          # Model untuk data transaksi
 ├── controller/
 │    ├── ProductController.java    # Logika CRUD Produk (Data Access)
 │    └── TransactionController.java# Logika transaksi dan riwayat
 └── view/
      └── MainView.java             # Antarmuka grafis (GUI Java Swing)
```

## Penjelasan Konsep OOP yang Digunakan

1. **Class dan Object**:
   - Class bertindak sebagai blueprint. Contoh: `Product.java`.
   - Object adalah instansiasi class. Contoh pembuatan object: `Product p = new Product(...)`.

2. **Encapsulation**:
   - Di class `Product` dan `Transaction`, semua variabel dideklarasikan sebagai `private` atau `protected`.
   - Akses data menggunakan method Getter (`getNamaProduk()`) dan Setter (`setNamaProduk()`).

3. **Inheritance (Pewarisan)**:
   - `FoodProduct` dan `DrinkProduct` menggunakan keyword `extends Product`.
   - Class anak mewarisi atribut dan method dari class induk (`Product`), sehingga tidak perlu menulis ulang kode.

4. **Polymorphism**:
   - Terjadi Overriding method `getProductInfo()` di class `FoodProduct` dan `DrinkProduct`.
   - Di dalam Controller, kita menyimpan objek `FoodProduct` dan `DrinkProduct` di dalam `ArrayList<Product>`, ini merupakan konsep *Upcasting*.

5. **Constructor**:
   - Digunakan untuk memberi nilai awal ketika objek diciptakan (`new Product(...)`).

6. **Exception Handling**:
   - Blok `try-catch` digunakan secara intensif di Controller untuk menangkap `SQLException` saat koneksi gagal.
   - Digunakan juga di View saat parsing input angka (`NumberFormatException`).

7. **Collection (ArrayList)**:
   - `ArrayList<Product>` dan `ArrayList<Transaction>` digunakan untuk menyimpan daftar produk dan riwayat dari database sementara sebelum ditampilkan ke JTable.

## Penjelasan Setiap Class

- **`Main.java`**: Menjalankan aplikasi dengan memanggil dan menampilkan form GUI.
- **`DatabaseConnection.java`**: Menyimpan URL, User, dan Password database, dan menggunakan Driver Manager JDBC untuk menghubungkan Java dengan MySQL Server.
- **`Product.java`**: Blueprint dasar dari sebuah produk. Menyimpan ID, Nama, Harga, Stok, dan Kategori.
- **`FoodProduct` & `DrinkProduct`**: Bentuk turunan produk. Menambahkan logika spesifik (melalui constructor super dan overriding).
- **`Transaction.java`**: Mempresentasikan satu baris data history transaksi.
- **`ProductController.java`**: Melakukan *Prepared Statements* untuk Query SQL: INSERT (Tambah), SELECT (Tampil/Cari), UPDATE (Edit), dan DELETE (Hapus) ke tabel `products`.
- **`TransactionController.java`**: Mengurus alur transaksi. Di dalamnya terdapat konsep *Database Transaction* sederhana (`setAutoCommit(false)`, `commit()`, `rollback()`) agar jika stok gagal diupdate, riwayat tidak akan tersimpan secara otomatis (Mencegah data inkonsisten).
- **`MainView.java`**: Menggunakan komponen GUI: `JFrame` sebagai jendela, `JTabbedPane` untuk membuat 3 tab (Kelola, Kasir, Riwayat), `JPanel` dibantu `BorderLayout` dan `GridLayout` untuk merapikan `JLabel`, `JTextField`, `JButton`, dan `JTable`. Menangkap event `ActionListener` ketika tombol diklik.

## Penjelasan CRUD

- **Create (Tambah)**: Input data dari GUI -> Validasi (Tipe Angka?) -> Jadikan Object `Product` -> Kirim ke `ProductController.addProduct()` -> Simpan ke DB dengan `INSERT`.
- **Read (Tampil & Cari)**: Memanggil `getAllProducts()` atau `searchProduct(keyword)`. Data diambil dari `ResultSet` MySQL -> Disimpan ke `ArrayList` -> Dilakukan perulangan (`for`) untuk ditambahkan per baris ke `DefaultTableModel` JTable.
- **Update (Edit)**: User mengklik JTable -> Data di-load ke TextField -> User ganti -> Tekan Edit -> Kirim object beserta `idProduk` ke `updateProduct()` -> Eksekusi perintah `UPDATE`.
- **Delete (Hapus)**: Ambil `idProduk` dari TextField -> Peringatkan User via `JOptionPane` -> Jika Yes, jalankan perintah `DELETE` di database.

## Cara Menjalankan Project (Untuk Presentasi / NetBeans)

1. Pastikan Anda sudah menginstall XAMPP dan menyalakan **MySQL**.
2. Buka aplikasi manajemen database Anda (phpMyAdmin atau HeidiSQL).
3. Buat database baru bernama `kasir_db` jika belum ada.
4. Salin isi dari file `database.sql` dan eksekusi (Run) pada database tersebut untuk membuat tabel beserta contoh datanya.
5. Buka **NetBeans IDE**.
6. Buat **New Project** -> **Java Application** -> Uncheck *Create Main Class*.
7. Copy/pindahkan folder `src` (yang ada `controller`, `database`, `main`, `model`, `view`) ke dalam folder source packages project Anda di NetBeans.
8. **Tambahkan Library JDBC**: 
   - Di bagian Projects tree NetBeans, klik kanan pada folder **Libraries** -> **Add JAR/Folder**.
   - Cari file `mysql-connector-java-xxx.jar` Anda, lalu pilih OK. (Jika tidak punya, Anda harus download terlebih dahulu).
9. Klik kanan pada class `Main.java` lalu pilih **Run File** (atau tekan Shift+F6).
10. Aplikasi Java Swing Anda akan terbuka.

## Contoh Alur Tampilan GUI

Aplikasi menggunakan tab (`JTabbedPane`).
- **Tab 1: Kelola Produk**
  - Bagian Atas: Form isian (ID otomatis, Nama, Harga, Stok, Kategori Makanan/Minuman) dan tombol Tambah, Edit, Hapus.
  - Bagian Tengah: Tabel (`JTable`) berisi daftar produk. Klik tabel untuk memasukkan data ke form secara otomatis.
  - Bagian Bawah: Fitur Cari berdasarkan Nama dan tombol Refresh.
- **Tab 2: Transaksi Kasir**
  - Dropdown (`JComboBox`) berisi daftar nama produk yang ada.
  - Textfield "Jumlah Beli".
  - Textfield "Total Harga" dan tombol "Hitung Total".
  - Tombol "Bayar". Jika diklik akan mengurangi stok produk di DB dan menyimpan riwayat transaksi.
- **Tab 3: Riwayat Transaksi**
  - Hanya menampilkan tabel (`JTable`) berisi riwayat penjualan beserta waktu transaksinya.

> **Tips Presentasi Mahasiswa:**
> 1. Jelaskan `DatabaseConnection.java` pertama kali untuk menunjukkan koneksi sukses.
> 2. Buka Class `Product` lalu `FoodProduct` untuk menjelaskan Inheritance.
> 3. Tunjukkan GUI, klik data tabel (praktekkan *Event Handling*), lakukan update stok, lalu peragakan fitur transaksi.
> 4. Tunjukkan tabel Database berubah setelah tombol Bayar di-klik.
