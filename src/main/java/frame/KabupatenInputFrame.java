package frame;

import com.github.lgooddatepicker.components.DatePicker;
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
    private DatePicker tanggalMulaiDatePicker;

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
            if (nama.equals("")) {
                JOptionPane.showMessageDialog(null,
                        "Isi Nama Kabupaten",
                        "Validasi data kosong", JOptionPane.WARNING_MESSAGE);
                namaTextField.requestFocus();
                return;
            }

            String tanggalMulai = tanggalMulaiDatePicker.getText();
            if (tanggalMulai.equals("")){
                JOptionPane.showMessageDialog(null,
                        "Isi Tanggal Mulai",
                        "Validasi Data Kosong", JOptionPane.WARNING_MESSAGE);
                tanggalMulaiDatePicker.requestFocus();
                return;
            }

            Connection c = koneksi.getConnection();
            PreparedStatement ps;
            try {
                if (id == 0) {
                    String cekSQL = "SELECT * FROM kabupaten WHERE nama = ?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null,
                                "Data sama sudah ada");

                    } else {
                        String insertSQL = "INSERT INTO kabupaten(id, nama, tanggalmulai) " +
                        "VALUES (NULL, ?, ?)";
                        ps = c.prepareStatement(insertSQL);
                        ps.setString(1, nama);
                        ps.setString(2, tanggalMulai);
                        ps.executeUpdate();
                        dispose();
                        }
                } else {
                    String cekSQL = "SELECT * FROM kabupaten WHERE nama = ? AND id != ?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, id);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null,
                                "Data sama sudah ada");
                    } else {
                        String updateSQL = "UPDATE kabupaten SET nama = ?, tanggalmulai = ? WHERE id = ?";
                        ps = c.prepareStatement(updateSQL);
                        ps.setString(1, nama);
                        ps.setString(2, tanggalMulai);
                        ps.setInt(3, id);
                        ps.executeUpdate();
                        dispose();
                        }
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

            tanggalMulaiDatePicker.setText(rs.getString("tanggalMulai"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
