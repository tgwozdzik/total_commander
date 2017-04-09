import layout.TotalCommanderLayout;

import javax.swing.*;

public class Main extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TotalCommanderLayout totalCommander = new TotalCommanderLayout();
            totalCommander.setVisible(true);
        });
    }
}
