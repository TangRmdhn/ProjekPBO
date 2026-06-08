package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class DatabaseConnection {
    // Sesuaikan konfigurasi ini dengan database lokal Anda
    private static final String HOST = "jdbc:mysql://localhost:3307/"; // alamat server MySQL (tanpa nama database)
    private static final String NAMA_DATABASE = "kasir_db";            // nama database yang akan dipakai
    private static final String URL = HOST + NAMA_DATABASE;            // alamat lengkap ke database kasir_db
    private static final String USER = "root";                         // Username default XAMPP
    private static final String PASSWORD = "";                         // Password default XAMPP biasanya kosong

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Mendaftarkan driver MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Langkah 1: pastikan database dan tabelnya sudah ada.
                // Kalau belum ada, method ini akan otomatis membuatkannya.
                siapkanDatabase();

                // Langkah 2: buka koneksi ke database kasir_db yang sudah pasti ada.
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Koneksi Database Berhasil!");
            } catch (ClassNotFoundException e) {
                System.out.println("Driver JDBC tidak ditemukan!");
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println("Gagal terhubung ke Database!");
                JOptionPane.showMessageDialog(null, "Gagal terhubung ke database!\nPastikan MySQL (XAMPP) sudah berjalan.", "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
        return connection;
    }

    // Membuat database dan semua tabel kalau belum ada.
    // Dipanggil otomatis sebelum aplikasi memakai database.
    private static void siapkanDatabase() throws SQLException {
        // Koneksi ke server MySQL dulu, BUKAN ke database kasir_db,
        // karena database-nya mungkin belum dibuat.
        Connection koneksiServer = DriverManager.getConnection(HOST, USER, PASSWORD);
        Statement perintah = koneksiServer.createStatement();

        // 1. Buat database kalau belum ada
        perintah.execute("CREATE DATABASE IF NOT EXISTS " + NAMA_DATABASE);

        // 2. Pilih database yang baru/akan dipakai
        perintah.execute("USE " + NAMA_DATABASE);

        // 3. Buat tabel produk kalau belum ada
        perintah.execute(
            "CREATE TABLE IF NOT EXISTS products (" +
            "    id_produk INT AUTO_INCREMENT PRIMARY KEY," +
            "    nama_produk VARCHAR(100) NOT NULL," +
            "    harga DOUBLE NOT NULL," +
            "    stok INT NOT NULL," +
            "    kategori VARCHAR(50) NOT NULL" +
            ")"
        );

        // 4. Buat tabel transaksi kalau belum ada
        perintah.execute(
            "CREATE TABLE IF NOT EXISTS transactions (" +
            "    id_transaksi INT AUTO_INCREMENT PRIMARY KEY," +
            "    nama_produk VARCHAR(100) NOT NULL," +
            "    jumlah_beli INT NOT NULL," +
            "    total_harga DOUBLE NOT NULL," +
            "    tanggal TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ")"
        );

        // 5. Isi data awal HANYA jika tabel products masih kosong,
        //    supaya data contoh tidak dobel setiap kali aplikasi dijalankan.
        var hasil = perintah.executeQuery("SELECT COUNT(*) FROM products");
        hasil.next();
        int jumlahProduk = hasil.getInt(1);

        if (jumlahProduk == 0) {
            perintah.execute(
                "INSERT INTO products (nama_produk, harga, stok, kategori) VALUES " +
                "('Nasi Goreng Spesial', 25000, 50, 'Makanan')," +
                "('Ayam Bakar Madu', 30000, 30, 'Makanan')," +
                "('Es Teh Manis', 5000, 100, 'Minuman')," +
                "('Kopi Hitam', 10000, 50, 'Minuman')"
            );
            System.out.println("Data awal produk berhasil ditambahkan.");
        }

        // Tutup koneksi sementara ke server, kita sudah selesai menyiapkan database.
        hasil.close();
        perintah.close();
        koneksiServer.close();
        System.out.println("Database dan tabel siap digunakan!");
    }
}
