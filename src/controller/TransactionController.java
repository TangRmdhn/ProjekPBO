package controller;

import dao.TransactionDAO;
import model.Product;
import model.Transaction;

import java.util.ArrayList;

// Controller transaksi = jembatan antara View dan TransactionDAO.
// Selain meneruskan ke DAO, di sini juga ditaruh logika bisnis sederhana
// seperti menghitung total harga.
public class TransactionController {

    private TransactionDAO transactionDAO;

    public TransactionController() {
        transactionDAO = new TransactionDAO();
    }

    // Logika bisnis: menghitung total harga = harga produk x jumlah beli.
    // Diletakkan di Controller, bukan di View, supaya View hanya mengurus tampilan.
    public double hitungTotal(Product produk, int jumlahBeli) {
        return produk.getHarga() * jumlahBeli;
    }

    // Memproses pembayaran / transaksi kasir
    public boolean prosesTransaksi(Product produk, int jumlahBeli, double totalHarga) {
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
