import frame.KabupatenViewFrame;
import frame.KecamatanViewFrame;
import helpers.koneksi;

public class Main {
    public static void main(String[] args) {
//        koneksi.getConnection();
//        KabupatenViewFrame viewFrame = new KabupatenViewFrame();
        KecamatanViewFrame viewFrame = new KecamatanViewFrame();
        viewFrame.setVisible(true);
    }
}
