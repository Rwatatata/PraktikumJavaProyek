package frame;

import helpers.koneksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class KecamatanViewFrame extends JFrame{
    private JPanel mainPanel;
    private JPanel cariPanel;
    private JTextField cariTextField;
    private JButton cariButton;
    private JScrollPane viewScrollPane;
    private JTable viewTable;
    private JPanel buttonPanel;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton cetakButton;
    private JButton tutupButton;

    public KecamatanViewFrame() {
        tutupButton.addActionListener(e -> {
            dispose();
        });

        batalButton.addActionListener(e -> {
            isiTable();
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                isiTable();
            }
        });

        tambahButton.addActionListener(e -> {
            KecamatanInputFrame inputFrame = new KecamatanInputFrame();
            inputFrame.setVisible(true);
        });

        ubahButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if (barisTerpilih < 0) {
                JOptionPane.showMessageDialog(
                        null,
                        "Pilih data dulu");
                return;
            }
            TableModel tm = viewTable.getModel();
            int id = Integer.parseInt(tm.getValueAt(barisTerpilih, 0).toString());
            KecamatanInputFrame inputFrame = new KecamatanInputFrame();
            inputFrame.setId(id);
            inputFrame.isiKomponen();
            inputFrame.setVisible(true);
        });

        cariButton.addActionListener(e -> {
            if (cariTextField.getText().equals("")){
                JOptionPane.showMessageDialog(null,
                        "Isi kata kunci pencarian",
                        "Validasi kata kunci kosong",
                        JOptionPane.WARNING_MESSAGE);
                cariTextField.requestFocus();
                return;
            }
            Connection c = koneksi.getConnection();
            String keyword = "%" + cariTextField.getText() + "%";
            String searchSQL = "SELECT * FROM kecamatan WHERE nama like ?";
            try {
                PreparedStatement ps = c.prepareStatement(searchSQL);
                ps.setString(1, keyword);
                ResultSet rs = ps.executeQuery();
                DefaultTableModel dtm = (DefaultTableModel) viewTable.getModel();
                dtm.setRowCount(0);
                Object[] row = new Object[2];
                while (rs.next()){
                    row[0] = rs.getInt("id");
                    row[1] = rs.getString("nama");
                    dtm.addRow(row);
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        hapusButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if (barisTerpilih < 0) {
                JOptionPane.showMessageDialog(null, "Pilih Data dulu");
                return;
            }
            int pilihan = JOptionPane.showConfirmDialog(null,
                    "Yakin mau hapus?",
                    "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            if (pilihan == 0){
                TableModel tm = viewTable.getModel();
                int id = Integer.parseInt(tm.getValueAt(barisTerpilih, 0).toString());
                Connection c = koneksi.getConnection();
                String deleteSQL = "DELETE FROM kecamatan WHERE id = ?";
                try {
                    PreparedStatement ps = c.prepareStatement(deleteSQL);
                    ps.setInt(1, id);
                    ps.executeUpdate();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        isiTable();
        init();
    }

    public void init(){
        setContentPane(mainPanel);
        setTitle("Data Kecamatan");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void isiTable(){
        Connection c = koneksi.getConnection();
        String selectSQL = "SELECT * FROM kecamatan";
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);
            String header[] = {"Id", "Nama Kecamatan"};
            DefaultTableModel dtm = new DefaultTableModel(header, 0);
            viewTable.setModel(dtm);
            Object[] row = new Object[2];
            while (rs.next()){
                row[0] = rs.getInt("id");
                row[1] = rs.getString("nama");
                dtm.addRow(row);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}