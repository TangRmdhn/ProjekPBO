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
        return "[Minuman] " + namaProduk + " seharga Rp " + harga;
    }
}
