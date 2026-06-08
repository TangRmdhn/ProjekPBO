CREATE DATABASE IF NOT EXISTS kasir_db;
USE kasir_db;

-- Tabel untuk menyimpan data produk
CREATE TABLE IF NOT EXISTS products (
    id_produk INT AUTO_INCREMENT PRIMARY KEY,
    nama_produk VARCHAR(100) NOT NULL,
    harga DOUBLE NOT NULL,
    stok INT NOT NULL,
    kategori VARCHAR(50) NOT NULL -- 'Makanan' atau 'Minuman' untuk contoh Polymorphism/Inheritance
);

-- Tabel untuk menyimpan riwayat transaksi
CREATE TABLE IF NOT EXISTS transactions (
    id_transaksi INT AUTO_INCREMENT PRIMARY KEY,
    nama_produk VARCHAR(100) NOT NULL,
    jumlah_beli INT NOT NULL,
    total_harga DOUBLE NOT NULL,
    tanggal TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Contoh Data Awal
INSERT INTO products (nama_produk, harga, stok, kategori) VALUES 
('Nasi Goreng Spesial', 25000, 50, 'Makanan'),
('Ayam Bakar Madu', 30000, 30, 'Makanan'),
('Es Teh Manis', 5000, 100, 'Minuman'),
('Kopi Hitam', 10000, 50, 'Minuman');
