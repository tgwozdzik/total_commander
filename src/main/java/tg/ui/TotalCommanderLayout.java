package tg.ui;

import tg.ui.fileList.FileList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Tomasz Gwoździk on 09.04.2017.
 */

public class TotalCommanderLayout extends JFrame {
    private void setUpLayout() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));

        setTitle("Total Commander v0.1 - Tomasz Gwoździk PUT 2017");

        setBackground(Color.LIGHT_GRAY);
    }

    private void setUpLists() {
        Panel fileDisplayPanel = new Panel(new GridBagLayout());

        setUpLeftFileList(fileDisplayPanel);
        setUpMiddleActionButtons(fileDisplayPanel);
        setUpRightFileList(fileDisplayPanel);

        add(fileDisplayPanel, BorderLayout.CENTER);
    }

    private void setUpMenuBar() {
        setJMenuBar(new MenuBar().getMenuBar());
    }

    private void setUpLeftFileList(Panel fileDisplayPanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        FileList fileList = new FileList();

        gbc.gridx      = 0;
        gbc.gridy      = 0;
        gbc.gridwidth  = 1;
        gbc.gridheight = 2;
        gbc.fill       = GridBagConstraints.BOTH;
        gbc.anchor     = GridBagConstraints.CENTER;
        gbc.weightx    = 0.5;
        gbc.weighty    = 1.0;
        fileDisplayPanel.add(fileList, gbc);
    }

    private void setUpMiddleActionButtons(Panel fileDisplayPanel) {
        GridBagConstraints gbc = new GridBagConstraints();

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
    }

    private void setUpRightFileList(Panel fileDisplayPanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        FileList fileList = new FileList();

        gbc.gridx      = 2;
        gbc.gridy      = 0;
        gbc.gridwidth  = 1;
        gbc.gridheight = 2;
        gbc.fill       = GridBagConstraints.BOTH;
        gbc.anchor     = GridBagConstraints.CENTER;
        gbc.weightx    = 0.5;
        gbc.weighty    = 1.0;
        fileDisplayPanel.add(fileList, gbc);
    }

    private void setUpListeners() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void setUpFooterButtons() {
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

        exitButton.addActionListener(e -> {
            System.exit(0);
        });

        add(commandButtonPanel, BorderLayout.SOUTH);
    }

    public TotalCommanderLayout() {
        setUpLayout();

        setUpMenuBar();
        setUpLists();
        setUpFooterButtons();

        setUpListeners();

        pack();
    }
}
