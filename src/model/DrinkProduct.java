package model;

// Subclass dari Product (Penerapan Inheritance)
public class DrinkProduct extends Product {

    public DrinkProduct(int idProduk, String namaProduk, double harga, int stok) {
        // Memanggil constructor dari superclass (Product)
        super(idProduk, namaProduk, harga, stok, "Minuman");
    }

    // Penerapan Polymorphism (Overriding)
    @Override
    public String getProductInfo() {
        // Pakai getter (getNamaProduk/getHarga), bukan akses field langsung,
        // karena field di superclass sekarang private (Encapsulation).
        return "[Minuman] " + getNamaProduk() + " seharga Rp " + getHarga();
    }
}
