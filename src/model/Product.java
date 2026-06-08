package model;

// Class Induk (Superclass) yang menerapkan konsep Encapsulation dan Abstraksi
public abstract class Product {
    // ENCAPSULATION: semua data dibungkus dengan modifier "private",
    // jadi tidak bisa diakses langsung dari luar. Akses hanya lewat getter/setter.
    // Class anak (FoodProduct/DrinkProduct) pun harus pakai getter, bukan akses langsung.
    private int idProduk;
    private String namaProduk;
    private double harga;
    private int stok;
    private String kategori;

    // Constructor
    public Product(int idProduk, String namaProduk, double harga, int stok, String kategori) {
        this.idProduk = idProduk;
        this.namaProduk = namaProduk;
        this.harga = harga;
        this.stok = stok;
        this.kategori = kategori;
    }

    // Constructor Kosong
    public Product() {}

    // Getter dan Setter untuk mengakses data yang di-encapsulate
    public int getIdProduk() {
        return idProduk;
    }

    public void setIdProduk(int idProduk) {
        this.idProduk = idProduk;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
    
    // Abstract Method untuk penerapan Abstraksi dan Polymorphism (akan di-override oleh subclass)
    public abstract String getProductInfo();
}
