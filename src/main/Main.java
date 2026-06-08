package main;

import view.MainView;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Menjalankan GUI di Event Dispatch Thread agar thread-safe
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Membuat object MainView dan menampilkannya
                MainView view = new MainView();
                view.setVisible(true);
            }
        });
    }
}
