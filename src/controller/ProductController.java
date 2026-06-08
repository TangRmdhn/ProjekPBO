package controller;

import dao.ProductDAO;
import dao.ProductRepository;
import model.Product;
import model.ProductFactory;

import java.util.ArrayList;

// Controller = jembatan antara View (tampilan) dan DAO (database).
// Tugasnya: menerima permintaan dari View, mengatur logika sederhana,
// lalu menyuruh DAO menyimpan/mengambil data.
// Controller TIDAK menulis SQL (itu tugas DAO) dan TIDAK mengurus tampilan (itu tugas View).
public class ProductController {

    // DEPENDENCY INVERSION: tipe field-nya interface (ProductRepository),
    // bukan kelas konkret (ProductDAO). Controller tidak peduli datanya disimpan di mana.
    private ProductRepository productDAO;

    // Constructor biasa: pakai ProductDAO (database) secara default.
    public ProductController() {
        this.productDAO = new ProductDAO();
    }

    // OVERLOADING constructor (Polymorphism): nama sama, parameter beda.
    // Berguna untuk mengganti sumber data, misal saat testing.
    public ProductController(ProductRepository repository) {
        this.productDAO = repository;
    }

    // Membuat objek Product dari data mentah form.
    // View cukup mengirim data biasa, urusan memilih kelas diserahkan ke pabrik.
    public Product buatProduk(int id, String nama, double harga, int stok, String kategori) {
        return ProductFactory.buatProduk(id, nama, harga, stok, kategori);
    }

    // Menambah produk baru
    public boolean tambahProduk(Product product) {
        // Jaring pengaman: jangan simpan kalau data produk tidak masuk akal
        if (!produkMasukAkal(product)) {
            return false;
        }
        return productDAO.simpan(product);
    }

    // Mengambil semua produk
    public ArrayList<Product> ambilSemuaProduk() {
        return productDAO.ambilSemua();
    }

    // Mengubah produk
    public boolean ubahProduk(Product product) {
        if (!produkMasukAkal(product)) {
            return false;
        }
        return productDAO.ubah(product);
    }

    // Jaring pengaman logika bisnis: harga harus > 0, stok tidak boleh negatif,
    // nama tidak boleh kosong. Dipakai sebelum menyimpan/mengubah ke database.
    private boolean produkMasukAkal(Product product) {
        if (product.getNamaProduk() == null || product.getNamaProduk().trim().isEmpty()) {
            return false;
        }
        if (product.getHarga() <= 0) {
            return false;
        }
        return product.getStok() >= 0;
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
