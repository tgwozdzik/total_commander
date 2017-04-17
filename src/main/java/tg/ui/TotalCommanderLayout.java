package tg.ui;

import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Tomasz Gwoździk on 09.04.2017.
 */

public class TotalCommanderLayout extends JFrame {
    private static final long serialVersionUID = 1L;
    private FileList leftFileList;
    private FileList rightFileList;

    private void setUpLayout() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));

        setTitle("Total Commander v0.1 - Tomasz Gwoździk PUT 2017");

        setBackground(Color.LIGHT_GRAY);
    }

    private void setUpLists() {
        JPanel fileDisplayPanel = new JPanel(new GridBagLayout());
        fileDisplayPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        setUpLeftFileList(fileDisplayPanel);
        setUpMiddleActionButtons(fileDisplayPanel);
        setUpRightFileList(fileDisplayPanel);

        add(fileDisplayPanel, BorderLayout.CENTER);
    }

    private void setUpMenuBar() {
        setJMenuBar(new MenuBar().getMenuBar());
    }

    private void setUpLeftFileList(JPanel fileDisplayPanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        leftFileList = new FileList();

        gbc.gridx      = 0;
        gbc.gridy      = 0;
        gbc.gridwidth  = 1;
        gbc.gridheight = 2;
        gbc.fill       = GridBagConstraints.BOTH;
        gbc.anchor     = GridBagConstraints.CENTER;
        gbc.weightx    = 0.5;
        gbc.weighty    = 1.0;
        fileDisplayPanel.add(leftFileList, gbc);
    }

    private void setUpMiddleActionButtons(JPanel fileDisplayPanel) {
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

    private void setUpRightFileList(JPanel fileDisplayPanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        rightFileList = new FileList();

        gbc.gridx      = 2;
        gbc.gridy      = 0;
        gbc.gridwidth  = 1;
        gbc.gridheight = 2;
        gbc.fill       = GridBagConstraints.BOTH;
        gbc.anchor     = GridBagConstraints.CENTER;
        gbc.weightx    = 0.5;
        gbc.weighty    = 1.0;
        fileDisplayPanel.add(rightFileList, gbc);
    }

    private void setUpListeners() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        leftFileList.getFileTable().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                leftFileList.setFocus(true);
                rightFileList.setFocus(false);
            }

            @Override
            public void focusLost(FocusEvent e) {}
        });

        rightFileList.getFileTable().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                rightFileList.setFocus(true);
                leftFileList.setFocus(false);
            }

            @Override
            public void focusLost(FocusEvent e) {}
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

        moveButton.addActionListener(e -> {
            ArrayList<String> leftSelected = leftFileList.getSelected();
            ArrayList<String> rightSelected = rightFileList.getSelected();

            FileCopyOperationDialog fileCopyOperationDialog;

            if(leftFileList.getFocusStatus()) {
                fileCopyOperationDialog = new FileCopyOperationDialog(leftSelected, rightSelected.get(0), 0);

            } else {
                fileCopyOperationDialog = new FileCopyOperationDialog(rightSelected, leftSelected.get(0), 0);
            }

            fileCopyOperationDialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    leftFileList.refresh();
                    rightFileList.refresh();
                }
            });
            fileCopyOperationDialog.setVisible(true);
        });

        copyButton.addActionListener(e -> {
            ArrayList<String> leftSelected = leftFileList.getSelected();
            ArrayList<String> rightSelected = rightFileList.getSelected();

            FileCopyOperationDialog fileCopyOperationDialog;

            if(leftFileList.getFocusStatus()) {
                fileCopyOperationDialog = new FileCopyOperationDialog(leftSelected, rightSelected.get(0), 1);

            } else {
                fileCopyOperationDialog = new FileCopyOperationDialog(rightSelected, leftSelected.get(0), 1);
            }

            fileCopyOperationDialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    leftFileList.refresh();
                    rightFileList.refresh();
                }
            });
            fileCopyOperationDialog.setVisible(true);
        });

        removeButton.addActionListener(e -> {
            ArrayList<String> leftSelected = leftFileList.getSelected();
            ArrayList<String> rightSelected = rightFileList.getSelected();

            FileCopyOperationDialog fileCopyOperationDialog;

            if(leftFileList.getFocusStatus()) {
                fileCopyOperationDialog = new FileCopyOperationDialog(leftSelected, null, 2);

            } else {
                fileCopyOperationDialog = new FileCopyOperationDialog(rightSelected, null, 2);
            }

            fileCopyOperationDialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    leftFileList.refresh();
                    rightFileList.refresh();
                }
            });
            fileCopyOperationDialog.setVisible(true);
        });

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
