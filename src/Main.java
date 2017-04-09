import java.awt.EventQueue;
import javax.swing.JFrame;

public class Main extends JFrame {

    Main() {
        initUI();
    }

    private void initUI() {
        setTitle("Simple example");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Main ex = new Main();
            ex.setVisible(true);
        });
    }
}
