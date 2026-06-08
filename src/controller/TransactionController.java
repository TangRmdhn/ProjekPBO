package controller;

import database.DatabaseConnection;
import model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class TransactionController {
    
    private Connection conn;

    public TransactionController() {
        conn = DatabaseConnection.getConnection();
    }

    // Melakukan transaksi kasir
    public boolean processTransaction(int idProduk, String namaProduk, int jumlahBeli, double totalHarga) {
        // Exception Handling untuk transaksi
        try {
            // 1. Matikan autocommit agar transaksi aman
            conn.setAutoCommit(false);
            
            // 2. Kurangi stok produk
            String updateStokQuery = "UPDATE products SET stok = stok - ? WHERE id_produk = ? AND stok >= ?";
            PreparedStatement pstmtUpdate = conn.prepareStatement(updateStokQuery);
            pstmtUpdate.setInt(1, jumlahBeli);
            pstmtUpdate.setInt(2, idProduk);
            pstmtUpdate.setInt(3, jumlahBeli);
            
            int rowsUpdated = pstmtUpdate.executeUpdate();
            
            // Jika stok tidak cukup atau produk tidak ditemukan
            if (rowsUpdated == 0) {
                conn.rollback();
                return false; 
            }
            
            // 3. Catat ke tabel riwayat transaksi
            String insertTransQuery = "INSERT INTO transactions (nama_produk, jumlah_beli, total_harga) VALUES (?, ?, ?)";
            PreparedStatement pstmtInsert = conn.prepareStatement(insertTransQuery);
            pstmtInsert.setString(1, namaProduk);
            pstmtInsert.setInt(2, jumlahBeli);
            pstmtInsert.setDouble(3, totalHarga);
            pstmtInsert.executeUpdate();
            
            // 4. Commit transaksi jika semua berhasil
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            try {
                // Rollback jika terjadi error
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                // Kembalikan ke autocommit true
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Mengambil semua riwayat transaksi
    public ArrayList<Transaction> getAllTransactions() {
        ArrayList<Transaction> transactionList = new ArrayList<>();
        String query = "SELECT * FROM transactions ORDER BY tanggal DESC";
        
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("id_transaksi");
                String nama = rs.getString("nama_produk");
                int jumlah = rs.getInt("jumlah_beli");
                double total = rs.getDouble("total_harga");
                Timestamp tgl = rs.getTimestamp("tanggal");
                
                Transaction trans = new Transaction(id, nama, jumlah, total, tgl);
                transactionList.add(trans);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return transactionList;
    }
}
