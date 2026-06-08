package model;

import java.sql.Timestamp;

public class Transaction {
    private int idTransaksi;
    private String namaProduk;
    private int jumlahBeli;
    private double totalHarga;
    private Timestamp tanggal;

    public Transaction(int idTransaksi, String namaProduk, int jumlahBeli, double totalHarga, Timestamp tanggal) {
        this.idTransaksi = idTransaksi;
        this.namaProduk = namaProduk;
        this.jumlahBeli = jumlahBeli;
        this.totalHarga = totalHarga;
        this.tanggal = tanggal;
    }

    // Getters and Setters
    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public int getJumlahBeli() {
        return jumlahBeli;
    }

    public void setJumlahBeli(int jumlahBeli) {
        this.jumlahBeli = jumlahBeli;
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(double totalHarga) {
        this.totalHarga = totalHarga;
    }

    public Timestamp getTanggal() {
        return tanggal;
    }

    public void setTanggal(Timestamp tanggal) {
        this.tanggal = tanggal;
    }
}
