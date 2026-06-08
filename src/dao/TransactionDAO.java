package dao;

import database.DatabaseConnection;
import model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

// DAO khusus tabel "transactions".
// Tugasnya hanya berurusan dengan database untuk data transaksi.
// "implements TransactionRepository" = kelas ini memenuhi kontrak interface tersebut.
public class TransactionDAO implements TransactionRepository {

    private Connection conn;

    public TransactionDAO() {
        conn = DatabaseConnection.getConnection();
    }

    // Memproses 1 transaksi: kurangi stok produk + catat riwayat transaksi.
    // Dipakai "database transaction" supaya kedua langkah berhasil semua, atau dibatalkan semua.
    public boolean prosesTransaksi(int idProduk, String namaProduk, int jumlahBeli, double totalHarga) {
        try {
            // 1. Matikan autocommit agar bisa dibatalkan (rollback) jika ada yang gagal
            conn.setAutoCommit(false);

            // 2. Kurangi stok, tapi hanya jika stok masih cukup (stok >= jumlah beli)
            String queryStok = "UPDATE products SET stok = stok - ? WHERE id_produk = ? AND stok >= ?";
            PreparedStatement pstmtStok = conn.prepareStatement(queryStok);
            pstmtStok.setInt(1, jumlahBeli);
            pstmtStok.setInt(2, idProduk);
            pstmtStok.setInt(3, jumlahBeli);

            int barisDiubah = pstmtStok.executeUpdate();

            // Jika tidak ada baris berubah berarti stok kurang / produk tidak ada -> batalkan
            if (barisDiubah == 0) {
                conn.rollback();
                return false;
            }

            // 3. Catat ke tabel riwayat transaksi
            String queryCatat = "INSERT INTO transactions (nama_produk, jumlah_beli, total_harga) VALUES (?, ?, ?)";
            PreparedStatement pstmtCatat = conn.prepareStatement(queryCatat);
            pstmtCatat.setString(1, namaProduk);
            pstmtCatat.setInt(2, jumlahBeli);
            pstmtCatat.setDouble(3, totalHarga);
            pstmtCatat.executeUpdate();

            // 4. Semua berhasil -> simpan permanen
            conn.commit();
            return true;

        } catch (SQLException e) {
            // Ada error -> batalkan semua perubahan
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            // Kembalikan ke mode normal (autocommit nyala lagi)
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Mengambil semua riwayat transaksi, terbaru di atas
    public ArrayList<Transaction> ambilSemua() {
        ArrayList<Transaction> daftarTransaksi = new ArrayList<>();
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

                daftarTransaksi.add(new Transaction(id, nama, jumlah, total, tgl));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return daftarTransaksi;
    }
}
