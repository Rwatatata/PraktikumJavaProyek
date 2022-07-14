package frame;

import helpers.koneksi;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class KabupatenInputFrame extends JFrame {
    private JPanel mainPanel;
    private JTextField idTextField;
    private JTextField namaTextField;
    private JPanel buttonPanel;
    private JButton simpanButton;
    private JButton batalButton;

    private int id;

    public void setId(int id) {
        this.id = id;
    }

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
                if (id == 0) {
                    String insertSQL = "INSERT INTO kabupaten VALUES (NULL, ?)";
                    ps = c.prepareStatement(insertSQL);
                    ps.setString(1, nama);
                    ps.executeUpdate();
                    dispose();
                } else {
                    String updateSQL = "UPDATE kabupaten SET nama =? WHERE id = ?";
                    ps = c.prepareStatement(updateSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, id);
                    ps.executeUpdate();
                    dispose();
                }
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

    public void isiKomponen() {
        Connection c = koneksi.getConnection();
        String findSQL = "SELECT * FROM kabupaten WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idTextField.setText(String.valueOf(rs.getInt("id")));
                namaTextField.setText(rs.getString("nama"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
