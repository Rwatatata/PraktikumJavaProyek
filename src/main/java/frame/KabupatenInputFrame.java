package frame;

import helpers.koneksi;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class KabupatenInputFrame extends JFrame {
    private JPanel mainPanel;
    private JTextField idTextField;
    private JTextField namaTextField;
    private JPanel buttonPanel;
    private JButton simpanButton;
    private JButton batalButton;

    public KabupatenInputFrame() {
        batalButton.addActionListener(e -> {
            dispose();
        });
        init();

        simpanButton.addActionListener(e -> {
            String nama = namaTextField.getText();
            Connection c = koneksi.getConnection();
            PreparedStatement ps;
            try {
                String insertSQL = "INSERT INTO kabupaten VALUES (NULL, ?)";
                ps = c.prepareStatement(insertSQL);
                ps.setString(1, nama);
                ps.executeUpdate();
                dispose();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void init() {
        setContentPane(mainPanel);
        setTitle("Input Kabupaten");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
