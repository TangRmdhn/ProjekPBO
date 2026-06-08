package view;

import controller.ProductController;
import controller.TransactionController;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

// View = bagian tampilan (GUI Swing).
// Tugasnya HANYA: menampilkan data dan menerima klik/ketikan pengguna,
// lalu menyerahkan pekerjaan ke Controller. View tidak menulis SQL
// dan tidak menghitung logika bisnis sendiri.
public class MainView extends JFrame {

    private ProductController productController;
    private TransactionController transactionController;

    // Menyimpan daftar produk yang sedang tampil di combobox transaksi.
    // Dipakai agar saat memilih produk kita ambil objek Product langsung (lewat nomor urut),
    // bukan dengan memotong-motong teks combobox.
    private ArrayList<Product> daftarProdukCombo = new ArrayList<>();

    // Komponen GUI
    private JTabbedPane tabbedPane;

    // -- TAB PRODUK --
    private JPanel panelProduk;
    private DefaultTableModel tableModelProduk;
    private JTable tableProduk;
    private JTextField txtIdProduk, txtNamaProduk, txtHarga, txtStok, txtCari;
    private JComboBox<String> cbKategori;
    private JButton btnTambah, btnEdit, btnHapus, btnCari, btnRefresh;

    // -- TAB TRANSAKSI --
    private JPanel panelTransaksi;
    private JComboBox<String> cbPilihProduk;
    private JTextField txtJumlahBeli, txtTotalHarga;
    private JButton btnHitung, btnBayar;

    // -- TAB RIWAYAT --
    private JPanel panelRiwayat;
    private DefaultTableModel tableModelRiwayat;
    private JTable tableRiwayat;

    // -- HEADER --
    private JLabel lblClock;

    // Constructor
    public MainView() {
        productController = new ProductController();
        transactionController = new TransactionController();

        setTitle("Aplikasi Sistem Kasir (POS) - Project PBO");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Posisi di tengah layar
        setLayout(new BorderLayout());

        // Setup Header (Title & Clock)
        JPanel panelHeader = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("Aplikasi Sistem Kasir (POS)", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblClock = new JLabel("Waktu: --:--:--", SwingConstants.RIGHT);
        lblClock.setFont(new Font("Arial", Font.BOLD, 14));
        panelHeader.add(lblTitle, BorderLayout.CENTER);
        panelHeader.add(lblClock, BorderLayout.EAST);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
        add(panelHeader, BorderLayout.NORTH);

        startClockThread(); // Menjalankan Multithreading Jam

        tabbedPane = new JTabbedPane();

        // Setup Tab
        initTabProduk();
        initTabTransaksi();
        initTabRiwayat();

        tabbedPane.addTab("Kelola Produk", panelProduk);
        tabbedPane.addTab("Transaksi Kasir", panelTransaksi);
        tabbedPane.addTab("Riwayat Transaksi", panelRiwayat);

        add(tabbedPane, BorderLayout.CENTER);

        // Load data awal
        refreshTableProduk();
        refreshComboProduk();
        refreshTableRiwayat();

        // Listener saat tab berubah untuk refresh data
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 1) { // Tab Transaksi
                refreshComboProduk();
            } else if (tabbedPane.getSelectedIndex() == 2) { // Tab Riwayat
                refreshTableRiwayat();
            }
        });
    }

    // ================== INIT TAB PRODUK ==================
    private void initTabProduk() {
        panelProduk = new JPanel(new BorderLayout());

        // Form Input (Atas)
        JPanel panelForm = new JPanel(new GridLayout(6, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelForm.add(new JLabel("ID Produk (Auto):"));
        txtIdProduk = new JTextField();
        txtIdProduk.setEditable(false);
        panelForm.add(txtIdProduk);

        panelForm.add(new JLabel("Nama Produk:"));
        txtNamaProduk = new JTextField();
        panelForm.add(txtNamaProduk);

        panelForm.add(new JLabel("Harga:"));
        txtHarga = new JTextField();
        panelForm.add(txtHarga);

        panelForm.add(new JLabel("Stok:"));
        txtStok = new JTextField();
        panelForm.add(txtStok);

        panelForm.add(new JLabel("Kategori:"));
        cbKategori = new JComboBox<>(new String[]{"Makanan", "Minuman"});
        panelForm.add(cbKategori);

        // Tombol Aksi (CRUD)
        JPanel panelAksi = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnTambah = new JButton("Tambah");
        btnEdit = new JButton("Edit");
        btnHapus = new JButton("Hapus");
        panelAksi.add(btnTambah);
        panelAksi.add(btnEdit);
        panelAksi.add(btnHapus);
        panelForm.add(panelAksi);

        panelProduk.add(panelForm, BorderLayout.NORTH);

        // Tabel Data (Tengah)
        tableModelProduk = new DefaultTableModel(new String[]{"ID", "Nama Produk", "Harga", "Stok", "Kategori"}, 0);
        tableProduk = new JTable(tableModelProduk);
        JScrollPane scrollPane = new JScrollPane(tableProduk);
        panelProduk.add(scrollPane, BorderLayout.CENTER);

        // Pencarian (Bawah)
        JPanel panelCari = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelCari.add(new JLabel("Cari Nama: "));
        txtCari = new JTextField(15);
        btnCari = new JButton("Cari");
        btnRefresh = new JButton("Refresh");
        panelCari.add(txtCari);
        panelCari.add(btnCari);
        panelCari.add(btnRefresh);
        panelProduk.add(panelCari, BorderLayout.SOUTH);

        // Event Listeners (Event Handling)
        btnTambah.addActionListener(e -> tambahProduk());
        btnEdit.addActionListener(e -> editProduk());
        btnHapus.addActionListener(e -> hapusProduk());
        btnCari.addActionListener(e -> cariProduk());
        btnRefresh.addActionListener(e -> refreshTableProduk());

        // Event Klik Tabel: isi form dari baris yang diklik
        tableProduk.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableProduk.getSelectedRow();
                if (row >= 0) {
                    txtIdProduk.setText(tableModelProduk.getValueAt(row, 0).toString());
                    txtNamaProduk.setText(tableModelProduk.getValueAt(row, 1).toString());
                    txtHarga.setText(tableModelProduk.getValueAt(row, 2).toString());
                    txtStok.setText(tableModelProduk.getValueAt(row, 3).toString());
                    cbKategori.setSelectedItem(tableModelProduk.getValueAt(row, 4).toString());
                }
            }
        });
    }

    // ================== INIT TAB TRANSAKSI ==================
    private void initTabTransaksi() {
        panelTransaksi = new JPanel(new BorderLayout());

        JPanel panelForm = new JPanel(new GridLayout(4, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panelForm.add(new JLabel("Pilih Produk:"));
        cbPilihProduk = new JComboBox<>();
        panelForm.add(cbPilihProduk);

        panelForm.add(new JLabel("Jumlah Beli:"));
        txtJumlahBeli = new JTextField("1");
        panelForm.add(txtJumlahBeli);

        panelForm.add(new JLabel("Total Harga:"));
        txtTotalHarga = new JTextField();
        txtTotalHarga.setEditable(false);
        txtTotalHarga.setFont(new Font("Arial", Font.BOLD, 14));
        panelForm.add(txtTotalHarga);

        btnHitung = new JButton("Hitung Total");
        btnBayar = new JButton("Bayar / Proses Transaksi");
        panelForm.add(btnHitung);
        panelForm.add(btnBayar);

        panelTransaksi.add(panelForm, BorderLayout.NORTH);

        // Event Handling
        btnHitung.addActionListener(e -> hitungTotal());
        btnBayar.addActionListener(e -> prosesBayar());
    }

    // ================== INIT TAB RIWAYAT ==================
    private void initTabRiwayat() {
        panelRiwayat = new JPanel(new BorderLayout());
        panelRiwayat.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableModelRiwayat = new DefaultTableModel(new String[]{"ID Transaksi", "Nama Produk", "Jumlah", "Total Harga", "Waktu"}, 0);
        tableRiwayat = new JTable(tableModelRiwayat);
        JScrollPane scrollPane = new JScrollPane(tableRiwayat);

        panelRiwayat.add(new JLabel("Daftar Riwayat Transaksi Penjualan"), BorderLayout.NORTH);
        panelRiwayat.add(scrollPane, BorderLayout.CENTER);
    }

    // ================== LOGIKA CRUD PRODUK ==================
    private void refreshTableProduk() {
        tableModelProduk.setRowCount(0); // Kosongkan tabel
        ArrayList<Product> list = productController.ambilSemuaProduk();
        for (Product p : list) {
            tableModelProduk.addRow(new Object[]{
                    p.getIdProduk(), p.getNamaProduk(), p.getHarga(), p.getStok(), p.getKategori()
            });
        }
        clearFormProduk();
    }

    private void clearFormProduk() {
        txtIdProduk.setText("");
        txtNamaProduk.setText("");
        txtHarga.setText("");
        txtStok.setText("");
        cbKategori.setSelectedIndex(0);
        txtCari.setText("");
    }

    private void tambahProduk() {
        try {
            String nama = txtNamaProduk.getText();
            double harga = Double.parseDouble(txtHarga.getText());
            int stok = Integer.parseInt(txtStok.getText());
            String kategori = cbKategori.getSelectedItem().toString();

            if (nama.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama produk tidak boleh kosong!");
                return;
            }

            // Minta Controller membuat objek produk (id 0 = biar database yang menentukan)
            Product p = productController.buatProduk(0, nama, harga, stok, kategori);

            if (productController.tambahProduk(p)) {
                JOptionPane.showMessageDialog(this, "Produk berhasil ditambahkan!");
                refreshTableProduk();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambah produk.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Input harga/stok harus berupa angka!", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editProduk() {
        try {
            if (txtIdProduk.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit dari tabel!");
                return;
            }

            int id = Integer.parseInt(txtIdProduk.getText());
            String nama = txtNamaProduk.getText();
            double harga = Double.parseDouble(txtHarga.getText());
            int stok = Integer.parseInt(txtStok.getText());
            String kategori = cbKategori.getSelectedItem().toString();

            Product p = productController.buatProduk(id, nama, harga, stok, kategori);

            if (productController.ubahProduk(p)) {
                JOptionPane.showMessageDialog(this, "Produk berhasil diupdate!");
                refreshTableProduk();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal update produk.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Input salah!", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusProduk() {
        if (txtIdProduk.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus!");
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus produk ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtIdProduk.getText());
            if (productController.hapusProduk(id)) {
                JOptionPane.showMessageDialog(this, "Produk berhasil dihapus!");
                refreshTableProduk();
            }
        }
    }

    private void cariProduk() {
        String keyword = txtCari.getText();
        tableModelProduk.setRowCount(0);
        ArrayList<Product> list = productController.cariProduk(keyword);
        for (Product p : list) {
            tableModelProduk.addRow(new Object[]{
                    p.getIdProduk(), p.getNamaProduk(), p.getHarga(), p.getStok(), p.getKategori()
            });
        }
    }

    // ================== LOGIKA TRANSAKSI ==================
    private void refreshComboProduk() {
        cbPilihProduk.removeAllItems();

        // Ambil produk dari controller dan simpan di daftar memori.
        // Urutan item combobox SAMA dengan urutan daftarProdukCombo,
        // jadi nanti produk yang dipilih bisa diambil lewat nomor urut (index).
        daftarProdukCombo = productController.ambilSemuaProduk();
        for (Product p : daftarProdukCombo) {
            cbPilihProduk.addItem(p.getNamaProduk() + " (Stok: " + p.getStok() + ") - Rp" + p.getHarga());
        }
    }

    // Mengambil objek Product yang sedang dipilih di combobox (tanpa memotong teks).
    private Product produkTerpilih() {
        int index = cbPilihProduk.getSelectedIndex();
        if (index < 0 || index >= daftarProdukCombo.size()) {
            return null;
        }
        return daftarProdukCombo.get(index);
    }

    private void hitungTotal() {
        try {
            Product produk = produkTerpilih();
            if (produk == null) return;

            int jumlah = Integer.parseInt(txtJumlahBeli.getText());

            // Perhitungan diserahkan ke Controller (logika bisnis)
            double total = transactionController.hitungTotal(produk, jumlah);
            txtTotalHarga.setText(String.valueOf(total));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Jumlah beli harus angka valid!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void prosesBayar() {
        if (txtTotalHarga.getText().isEmpty()) {
            hitungTotal(); // hitung otomatis jika belum
        }

        try {
            Product produk = produkTerpilih();
            if (produk == null) return;

            int jumlah = Integer.parseInt(txtJumlahBeli.getText());
            double total = Double.parseDouble(txtTotalHarga.getText());

            if (transactionController.prosesTransaksi(produk, jumlah, total)) {
                JOptionPane.showMessageDialog(this, "Transaksi Sukses!");
                txtJumlahBeli.setText("1");
                txtTotalHarga.setText("");
                refreshComboProduk(); // Update sisa stok di combobox
                refreshTableProduk(); // Update stok di tabel tab pertama
            } else {
                JOptionPane.showMessageDialog(this, "Transaksi Gagal! Cek stok barang.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Jumlah beli harus angka valid!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ================== LOGIKA MULTITHREADING ==================
    private void startClockThread() {
        // Membuat Thread terpisah dari Main UI (Penerapan Multithreading)
        Thread clockThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        String timeNow = sdf.format(new java.util.Date());

                        // Update UI menggunakan SwingUtilities agar aman dari thread tabrakan
                        SwingUtilities.invokeLater(() -> {
                            lblClock.setText("Waktu: " + timeNow);
                        });

                        Thread.sleep(1000); // Thread di-pause selama 1 detik
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        clockThread.setDaemon(true); // Thread akan berhenti jika aplikasi utama ditutup
        clockThread.start();
    }

    // ================== LOGIKA RIWAYAT ==================
    private void refreshTableRiwayat() {
        tableModelRiwayat.setRowCount(0);
        ArrayList<Transaction> list = transactionController.ambilSemuaTransaksi();
        for (Transaction t : list) {
            tableModelRiwayat.addRow(new Object[]{
                    t.getIdTransaksi(), t.getNamaProduk(), t.getJumlahBeli(), t.getTotalHarga(), t.getTanggal()
            });
        }
    }
}
