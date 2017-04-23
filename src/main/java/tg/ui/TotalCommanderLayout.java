package tg.ui;

import tg.logic.Context;
import tg.logic.ContextChangeListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by Tomasz Gwoździk on 09.04.2017.
 */

public class TotalCommanderLayout extends JFrame {
    private static final long serialVersionUID = 1L;
    private FileList leftFileList;
    private FileList rightFileList;

    private String locale = "PL";

    private void setUpLayout() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));

        setTitle("Total Commander v0.3 - Tomasz Gwoździk PUT 2017");

        setBackground(Color.LIGHT_GRAY);

        Context.setLocale(new Locale(locale));
    }

    private void setUpLists() {
        JPanel fileDisplayPanel = new JPanel(new GridBagLayout());
        fileDisplayPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel headerDisplayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        setUpLeftFileList(fileDisplayPanel);
        setUpMiddleActionButtons(fileDisplayPanel);
        setUpRightFileList(fileDisplayPanel);

        setUpHeader(headerDisplayPanel);

        add(headerDisplayPanel, BorderLayout.NORTH);
        add(fileDisplayPanel, BorderLayout.CENTER);
    }

    private void setUpMenuBar() {
        setJMenuBar(new MenuBar().getMenuBar());
    }

    private void setUpHeader(JPanel headerDisplayPanel) {
        FlatButton refresh = new FlatButton("");

        Image copyIcon = null;
        try {
            copyIcon = ImageIO.read(getClass().getResource("refresh.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(copyIcon != null) {
            refresh.setIcon(new ImageIcon(copyIcon));
        }

        refresh.setBorder(null);
        refresh.setBorderPainted(false);
        refresh.setMargin(new Insets(0,0,0,0));

        refresh.addActionListener(e -> {
            leftFileList.refresh();
            rightFileList.refresh();
        });

        headerDisplayPanel.add(refresh);

        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        Dimension d = separator.getPreferredSize();
        d.height = refresh.getPreferredSize().height;
        separator.setPreferredSize(d);
        headerDisplayPanel.add(separator);

        FlatButton changeLanguage = new FlatButton(locale);

        Context.addContextChangeListener(changeLanguage);

        changeLanguage.addActionListener(e -> {
            if(locale.equals("EN")) {
                locale = "PL";
            } else {
                locale = "EN";
            }

            changeLanguage.changeKey(locale);
            Context.setLocale(new Locale(locale));
        });

        headerDisplayPanel.add(changeLanguage);
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

        FlatButton copy = new FlatButton("");
        copy.setBorder(new EmptyBorder(5, 5, 5,5));
        copy.setBorderPainted(false);
        copy.setMargin(new Insets(0,0,0,0));
        Image copyIcon = null;
        try {
            copyIcon = ImageIO.read(getClass().getResource("copy.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(copyIcon != null) {
            copy.setIcon(new ImageIcon(copyIcon));
        }

        FlatButton move = new FlatButton("");
        move.setBorder(new EmptyBorder(5, 5, 5,5));
        move.setBorderPainted(false);
        move.setMargin(new Insets(0,0,0,0));
        Image moveIcon = null;
        try {
            moveIcon = ImageIO.read(getClass().getResource("move.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(moveIcon != null) {
            move.setIcon(new ImageIcon(moveIcon));
        }
        move.setSize(16,16 );

        gbc.gridx      = 1;
        gbc.gridy      = 0;
        gbc.gridwidth  = 1;
        gbc.gridheight = 1;
        gbc.fill       = GridBagConstraints.NONE;
        gbc.anchor     = GridBagConstraints.SOUTH;
        gbc.weightx    = 0.0;
        gbc.weighty    = 0.5;
        gbc.insets     = new Insets(0, 4, 2, 4);
        fileDisplayPanel.add(copy, gbc);

        gbc.gridx      = 1;
        gbc.gridy      = 1;
        gbc.gridwidth  = 1;
        gbc.gridheight = 1;
        gbc.fill       = GridBagConstraints.NONE;
        gbc.anchor     = GridBagConstraints.NORTH;
        gbc.weightx    = 0.0;
        gbc.weighty    = 0.5;
        gbc.insets     = new Insets(2, 4, 0, 4);
        fileDisplayPanel.add(move, gbc);

        move.addActionListener(e -> {
            moveOperation();
        });

        copy.addActionListener(e -> {
            copyOperation();
        });
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

                rightFileList.getFileTable().clearSelection();
            }

            @Override
            public void focusLost(FocusEvent e) {}
        });

        rightFileList.getFileTable().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                rightFileList.setFocus(true);
                leftFileList.setFocus(false);

                leftFileList.getFileTable().clearSelection();
            }

            @Override
            public void focusLost(FocusEvent e) {}
        });
    }

    private void moveOperation() {
        ArrayList<String> leftSelected = leftFileList.getSelected();
        ArrayList<String> rightSelected = rightFileList.getSelected();

        if(leftFileList.getFileTable().getSelectedRows().length == 0
                && rightFileList.getFileTable().getSelectedRows().length == 0) {
            JOptionPane.showMessageDialog(null, Context.getString("no_selected"), Context.getString("message"), JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if(leftFileList.getCurrentPath().equals(rightFileList.getCurrentPath())) {
            JOptionPane.showMessageDialog(null, Context.getString("copy_to_itself"), Context.getString("message"), JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        OperationDialog operationDialog;

        if(leftFileList.getFocusStatus()) {
            operationDialog = new OperationDialog(leftSelected, rightSelected.get(0), 0);

        } else {
            operationDialog = new OperationDialog(rightSelected, leftSelected.get(0), 0);
        }

        operationDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                leftFileList.refresh();
                rightFileList.refresh();
            }
        });
        operationDialog.setVisible(true);
    }

    private void copyOperation() {
        ArrayList<String> leftSelected = leftFileList.getSelected();
        ArrayList<String> rightSelected = rightFileList.getSelected();

        if(leftFileList.getFileTable().getSelectedRows().length == 0
                && rightFileList.getFileTable().getSelectedRows().length == 0) {
            JOptionPane.showMessageDialog(null, Context.getString("no_selected"), Context.getString("message"), JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if(leftFileList.getCurrentPath().equals(rightFileList.getCurrentPath())) {
            JOptionPane.showMessageDialog(null, Context.getString("copy_to_itself"), Context.getString("message"), JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        OperationDialog operationDialog;

        if(leftFileList.getFocusStatus()) {
            operationDialog = new OperationDialog(leftSelected, rightSelected.get(0), 1);

        } else {
            operationDialog = new OperationDialog(rightSelected, leftSelected.get(0), 1);
        }

        operationDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                leftFileList.refresh();
                rightFileList.refresh();
            }
        });
        operationDialog.setVisible(true);
    }

    private void setUpFooterButtons() {
        JPanel commandButtonPanel = new JPanel(new GridLayout());

        FlatButton copyButton   = new FlatButton("copy", FlatButton.RIGHT);
        FlatButton moveButton   = new FlatButton("move", FlatButton.RIGHT);
        FlatButton removeButton = new FlatButton("delete", FlatButton.RIGHT);
        FlatButton exitButton   = new FlatButton("quit");

        Context.addContextChangeListener(copyButton);
        Context.addContextChangeListener(moveButton);
        Context.addContextChangeListener(removeButton);
        Context.addContextChangeListener(exitButton);

        commandButtonPanel.add(copyButton);
        commandButtonPanel.add(moveButton);
        commandButtonPanel.add(removeButton);
        commandButtonPanel.add(exitButton);

        moveButton.addActionListener(e -> {
            moveOperation();
        });

        copyButton.addActionListener(e -> {
            copyOperation();
        });

        removeButton.addActionListener(e -> {
            ArrayList<String> leftSelected = leftFileList.getSelected();
            ArrayList<String> rightSelected = rightFileList.getSelected();

            if(leftFileList.getFileTable().getSelectedRows().length == 0
                    && rightFileList.getFileTable().getSelectedRows().length == 0) {
                JOptionPane.showMessageDialog(null, Context.getString("no_selected"), Context.getString("message"), JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            OperationDialog operationDialog;

            if(leftFileList.getFocusStatus()) {
                operationDialog = new OperationDialog(leftSelected, null, 2);

            } else {
                operationDialog = new OperationDialog(rightSelected, null, 2);
            }

            operationDialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    leftFileList.refresh();
                    rightFileList.refresh();
                }
            });
            operationDialog.setVisible(true);
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
