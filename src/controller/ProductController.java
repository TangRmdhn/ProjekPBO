package controller;

import database.DatabaseConnection;
import model.DrinkProduct;
import model.FoodProduct;
import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductController {
    
    private Connection conn;

    public ProductController() {
        conn = DatabaseConnection.getConnection();
    }

    // CREATE: Menambah produk ke database
    public boolean addProduct(Product product) {
        String query = "INSERT INTO products (nama_produk, harga, stok, kategori) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, product.getNamaProduk());
            pstmt.setDouble(2, product.getHarga());
            pstmt.setInt(3, product.getStok());
            pstmt.setString(4, product.getKategori());
            
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ: Mengambil semua data produk dari database menggunakan ArrayList
    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> productList = new ArrayList<>();
        String query = "SELECT * FROM products";
        
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("id_produk");
                String nama = rs.getString("nama_produk");
                double harga = rs.getDouble("harga");
                int stok = rs.getInt("stok");
                String kategori = rs.getString("kategori");
                
                // Menerapkan konsep polymorphism dan inheritance
                Product product;
                if (kategori.equalsIgnoreCase("Makanan")) {
                    product = new FoodProduct(id, nama, harga, stok);
                } else {
                    product = new DrinkProduct(id, nama, harga, stok);
                }
                
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return productList;
    }

    // UPDATE: Mengubah data produk di database
    public boolean updateProduct(Product product) {
        String query = "UPDATE products SET nama_produk = ?, harga = ?, stok = ?, kategori = ? WHERE id_produk = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, product.getNamaProduk());
            pstmt.setDouble(2, product.getHarga());
            pstmt.setInt(3, product.getStok());
            pstmt.setString(4, product.getKategori());
            pstmt.setInt(5, product.getIdProduk());
            
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE: Menghapus produk dari database
    public boolean deleteProduct(int idProduk) {
        String query = "DELETE FROM products WHERE id_produk = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, idProduk);
            
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ (Search): Mencari produk berdasarkan nama
    public ArrayList<Product> searchProduct(String keyword) {
        ArrayList<Product> productList = new ArrayList<>();
        String query = "SELECT * FROM products WHERE nama_produk LIKE ?";
        
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("id_produk");
                String nama = rs.getString("nama_produk");
                double harga = rs.getDouble("harga");
                int stok = rs.getInt("stok");
                String kategori = rs.getString("kategori");
                
                Product product;
                if (kategori.equalsIgnoreCase("Makanan")) {
                    product = new FoodProduct(id, nama, harga, stok);
                } else {
                    product = new DrinkProduct(id, nama, harga, stok);
                }
                
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return productList;
    }
}
