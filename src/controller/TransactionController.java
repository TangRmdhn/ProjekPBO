package controller;

import dao.TransactionDAO;
import dao.TransactionRepository;
import model.Product;
import model.Transaction;

import java.util.ArrayList;

// Controller transaksi = jembatan antara View dan TransactionDAO.
// Selain meneruskan ke DAO, di sini juga ditaruh logika bisnis sederhana
// seperti menghitung total harga.
public class TransactionController {

    // DEPENDENCY INVERSION: bergantung pada interface, bukan kelas konkret.
    private TransactionRepository transactionDAO;

    // Constructor biasa: pakai TransactionDAO (database) secara default.
    public TransactionController() {
        this.transactionDAO = new TransactionDAO();
    }

    // OVERLOADING constructor (Polymorphism): bisa diberi sumber data lain.
    public TransactionController(TransactionRepository repository) {
        this.transactionDAO = repository;
    }

    // Logika bisnis: menghitung total harga = harga produk x jumlah beli.
    // Diletakkan di Controller, bukan di View, supaya View hanya mengurus tampilan.
    public double hitungTotal(Product produk, int jumlahBeli) {
        // Jaring pengaman: jumlah tidak masuk akal -> total 0 (jangan hitung negatif)
        if (jumlahBeli <= 0) {
            return 0;
        }
        return produk.getHarga() * jumlahBeli;
    }

    // Memproses pembayaran / transaksi kasir
    public boolean prosesTransaksi(Product produk, int jumlahBeli, double totalHarga) {
        // Jaring pengaman: tolak transaksi kalau jumlah beli <= 0,
        // supaya stok tidak malah bertambah karena angka negatif.
        if (jumlahBeli <= 0) {
            return false;
        }
        return transactionDAO.prosesTransaksi(
                produk.getIdProduk(),
                produk.getNamaProduk(),
                jumlahBeli,
                totalHarga
        );
    }

    // Mengambil semua riwayat transaksi
    public ArrayList<Transaction> ambilSemuaTransaksi() {
        return transactionDAO.ambilSemua();
    }
}
