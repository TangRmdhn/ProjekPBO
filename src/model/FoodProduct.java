package model;

// Subclass dari Product (Penerapan Inheritance)
public class FoodProduct extends Product {

    public FoodProduct(int idProduk, String namaProduk, double harga, int stok) {
        // Memanggil constructor dari superclass (Product)
        super(idProduk, namaProduk, harga, stok, "Makanan");
    }

    // Penerapan Polymorphism (Overriding)
    @Override
    public String getProductInfo() {
        return "[Makanan] " + namaProduk + " seharga Rp " + harga;
    }
}
