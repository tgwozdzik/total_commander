package layout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Tomasz Gwoździk on 09.04.2017.
 */

public class TotalCommanderLayout extends JFrame {
    public TotalCommanderLayout() {
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);
        setTitle("Total Commander v0.1 - Tomasz Gwoździk PUT 2017");
        setPreferredSize(new Dimension(800, 600));

        JMenuBar menuBar;
        JMenu filesMenu, helpMenu;
        JMenuItem exitMenuItem, aboutAppMenuItem;

        menuBar = new JMenuBar();

        filesMenu = new JMenu("Pliki");
        menuBar.add(filesMenu);

        menuBar.add(Box.createHorizontalGlue());

        exitMenuItem = new JMenuItem("Zakończ");
        filesMenu.add(exitMenuItem);

        helpMenu = new JMenu("Pomoc");
        helpMenu.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        menuBar.add(helpMenu);

        aboutAppMenuItem = new JMenuItem("O programie");
        helpMenu.add(aboutAppMenuItem);

        JPanel commandButtonPanel = new JPanel(new GridLayout());
        JButton editButton   = new FlatButton("Edycja", FlatButton.RIGHT);
        JButton copyButton   = new FlatButton("Kopiuj", FlatButton.RIGHT);
        JButton moveButton   = new FlatButton("Przenieś", FlatButton.RIGHT);
        JButton removeButton = new FlatButton("Usuń", FlatButton.RIGHT);
        JButton exitButton   = new FlatButton("Zakończ");

        commandButtonPanel.add(editButton);
        commandButtonPanel.add(copyButton);
        commandButtonPanel.add(moveButton);
        commandButtonPanel.add(removeButton);
        commandButtonPanel.add(exitButton);

        FileDisplay2 localFileDisplay  = new FileDisplay2();
        FileDisplay2 remoteFileDisplay = new FileDisplay2();

        Panel fileDisplayPanel = new Panel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx      = 0;
        gbc.gridy      = 0;
        gbc.gridwidth  = 1;
        gbc.gridheight = 2;
        gbc.fill       = GridBagConstraints.BOTH;
        gbc.anchor     = GridBagConstraints.CENTER;
        gbc.weightx    = 0.5;
        gbc.weighty    = 1.0;
        fileDisplayPanel.add(localFileDisplay, gbc);

        gbc.gridx      = 2;
        gbc.gridy      = 0;
        gbc.gridwidth  = 1;
        gbc.gridheight = 2;
        gbc.fill       = GridBagConstraints.BOTH;
        gbc.anchor     = GridBagConstraints.CENTER;
        gbc.weightx    = 0.5;
        gbc.weighty    = 1.0;
        fileDisplayPanel.add(remoteFileDisplay, gbc);

        gbc.gridx      = 1;
        gbc.gridy      = 0;
        gbc.gridwidth  = 1;
        gbc.gridheight = 1;
        gbc.fill       = GridBagConstraints.NONE;
        gbc.anchor     = GridBagConstraints.SOUTH;
        gbc.weightx    = 0.0;
        gbc.weighty    = 0.5;
        gbc.insets     = new Insets(0, 4, 2, 4);
        fileDisplayPanel.add(new Button(">>"), gbc);

        gbc.gridx      = 1;
        gbc.gridy      = 1;
        gbc.gridwidth  = 1;
        gbc.gridheight = 1;
        gbc.fill       = GridBagConstraints.NONE;
        gbc.anchor     = GridBagConstraints.NORTH;
        gbc.weightx    = 0.0;
        gbc.weighty    = 0.5;
        gbc.insets     = new Insets(2, 4, 0, 4);
        fileDisplayPanel.add(new Button("<<"), gbc);

        JPanel mainBottomSectionPanel = new JPanel(new BorderLayout());

        mainBottomSectionPanel.add(commandButtonPanel, BorderLayout.SOUTH);

        add(fileDisplayPanel, BorderLayout.CENTER);
        add(mainBottomSectionPanel, BorderLayout.SOUTH);

        setJMenuBar(menuBar);
        pack();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exitApp();
            }
        });

        exitButton.addActionListener(e -> {
            exitApp();
        });

        exitMenuItem.addActionListener(e -> {
            exitApp();
        });
    }

    private void exitApp() {
        setVisible(false);
        dispose();
        System.exit(0);
    }
}
