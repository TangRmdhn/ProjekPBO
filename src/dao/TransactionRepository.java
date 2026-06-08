package dao;

import model.Transaction;
import java.util.ArrayList;

// Interface khusus urusan data transaksi (Interface Segregation).
// Dipisah dari ProductRepository supaya tiap interface kecil dan fokus pada satu hal.
public interface TransactionRepository {
    boolean prosesTransaksi(int idProduk, String namaProduk, int jumlahBeli, double totalHarga);
    ArrayList<Transaction> ambilSemua();
}
