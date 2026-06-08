package model;

// Kelas "pabrik" untuk membuat objek Product.
// Tujuannya: keputusan "kategori apa -> kelas apa" cukup ditulis SATU kali di sini,
// supaya tidak ada kode if-else yang sama berulang di View maupun Controller.
public class ProductFactory {

    // Membuat objek produk yang tepat berdasarkan kategori.
    // Ini contoh Polymorphism: hasilnya bertipe Product,
    // tapi isi sebenarnya bisa FoodProduct atau DrinkProduct.
    public static Product buatProduk(int id, String nama, double harga, int stok, String kategori) {
        if (kategori.equalsIgnoreCase("Makanan")) {
            return new FoodProduct(id, nama, harga, stok);
        } else {
            return new DrinkProduct(id, nama, harga, stok);
        }
    }
}
