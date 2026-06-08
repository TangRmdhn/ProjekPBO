package dao;

import model.Product;
import java.util.ArrayList;

// INTERFACE = "kontrak" yang berisi daftar kemampuan tanpa isi kode.
// Ini menerapkan prinsip:
// - Interface Segregation (I pada SOLID): interface kecil & fokus, khusus urusan data produk.
// - Dependency Inversion (D pada SOLID): Controller cukup bergantung pada interface ini,
//   bukan pada kelas ProductDAO yang konkret. Jadi kalau nanti ganti cara simpan data
//   (misal ke file), Controller tidak perlu diubah.
public interface ProductRepository {
    boolean simpan(Product product);
    ArrayList<Product> ambilSemua();
    boolean ubah(Product product);
    boolean hapus(int idProduk);
    ArrayList<Product> cari(String kataKunci);
}
