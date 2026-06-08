package dao;

import database.DatabaseConnection;
import model.Product;
import model.ProductFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// DAO = Data Access Object.
// Tugasnya HANYA satu: berurusan dengan database untuk tabel "products".
// Semua perintah SQL produk berkumpul di sini, jadi Controller tidak perlu tahu soal SQL.
// "implements ProductRepository" = kelas ini berjanji menyediakan semua method di interface itu.
public class ProductDAO implements ProductRepository {

    private Connection conn;

    public ProductDAO() {
        // Ambil koneksi database (database & tabel otomatis dibuat jika belum ada)
        conn = DatabaseConnection.getConnection();
    }

    // CREATE: menyimpan produk baru ke database
    public boolean simpan(Product product) {
        String query = "INSERT INTO products (nama_produk, harga, stok, kategori) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, product.getNamaProduk());
            pstmt.setDouble(2, product.getHarga());
            pstmt.setInt(3, product.getStok());
            pstmt.setString(4, product.getKategori());

            int barisDitambah = pstmt.executeUpdate();
            return barisDitambah > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ: mengambil semua produk dari database
    public ArrayList<Product> ambilSemua() {
        ArrayList<Product> daftarProduk = new ArrayList<>();
        String query = "SELECT * FROM products";

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                daftarProduk.add(bacaSatuBaris(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return daftarProduk;
    }

    // UPDATE: mengubah data produk yang sudah ada
    public boolean ubah(Product product) {
        String query = "UPDATE products SET nama_produk = ?, harga = ?, stok = ?, kategori = ? WHERE id_produk = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, product.getNamaProduk());
            pstmt.setDouble(2, product.getHarga());
            pstmt.setInt(3, product.getStok());
            pstmt.setString(4, product.getKategori());
            pstmt.setInt(5, product.getIdProduk());

            int barisDiubah = pstmt.executeUpdate();
            return barisDiubah > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE: menghapus produk berdasarkan id
    public boolean hapus(int idProduk) {
        String query = "DELETE FROM products WHERE id_produk = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, idProduk);

            int barisDihapus = pstmt.executeUpdate();
            return barisDihapus > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // SEARCH: mencari produk berdasarkan nama
    public ArrayList<Product> cari(String kataKunci) {
        ArrayList<Product> daftarProduk = new ArrayList<>();
        String query = "SELECT * FROM products WHERE nama_produk LIKE ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, "%" + kataKunci + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                daftarProduk.add(bacaSatuBaris(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return daftarProduk;
    }

    // Method bantu: mengubah satu baris hasil database menjadi objek Product.
    // Dibuat terpisah supaya tidak menulis kode yang sama di ambilSemua() dan cari().
    private Product bacaSatuBaris(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_produk");
        String nama = rs.getString("nama_produk");
        double harga = rs.getDouble("harga");
        int stok = rs.getInt("stok");
        String kategori = rs.getString("kategori");

        // Pakai pabrik produk supaya keputusan kategori->kelas hanya di satu tempat
        return ProductFactory.buatProduk(id, nama, harga, stok, kategori);
    }
}
