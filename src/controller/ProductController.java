package controller;

import dao.ProductDAO;
import model.Product;
import model.ProductFactory;

import java.util.ArrayList;

// Controller = jembatan antara View (tampilan) dan DAO (database).
// Tugasnya: menerima permintaan dari View, mengatur logika sederhana,
// lalu menyuruh DAO menyimpan/mengambil data.
// Controller TIDAK menulis SQL (itu tugas DAO) dan TIDAK mengurus tampilan (itu tugas View).
public class ProductController {

    private ProductDAO productDAO;

    public ProductController() {
        productDAO = new ProductDAO();
    }

    // Membuat objek Product dari data mentah form.
    // View cukup mengirim data biasa, urusan memilih kelas diserahkan ke pabrik.
    public Product buatProduk(int id, String nama, double harga, int stok, String kategori) {
        return ProductFactory.buatProduk(id, nama, harga, stok, kategori);
    }

    // Menambah produk baru
    public boolean tambahProduk(Product product) {
        return productDAO.simpan(product);
    }

    // Mengambil semua produk
    public ArrayList<Product> ambilSemuaProduk() {
        return productDAO.ambilSemua();
    }

    // Mengubah produk
    public boolean ubahProduk(Product product) {
        return productDAO.ubah(product);
    }

    // Menghapus produk
    public boolean hapusProduk(int idProduk) {
        return productDAO.hapus(idProduk);
    }

    // Mencari produk berdasarkan nama
    public ArrayList<Product> cariProduk(String kataKunci) {
        return productDAO.cari(kataKunci);
    }
}
