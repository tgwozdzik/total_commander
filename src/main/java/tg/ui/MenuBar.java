package tg.ui;

import tg.logic.Context;
import tg.logic.ContextChangeListener;

import javax.swing.*;

/**
 * Created by tgwozdzik on 16.04.2017.
 */
public class MenuBar implements ContextChangeListener {
    private JMenuBar menuBar;
    private JMenu filesMenu, helpMenu;
    private JMenuItem exitMenuItem, aboutAppMenuItem;

    public MenuBar() {
        this.menuBar = new JMenuBar();

        setupMenus();
        setupSubMenus();

        glueAllTogether();

        setListeners();
    }

    private void setupMenus() {
        filesMenu = new JMenu(Context.getString("files"));
        helpMenu = new JMenu(Context.getString("help"));
    }

    private void setupSubMenus() {
        aboutAppMenuItem = new JMenuItem(Context.getString("about"));
        exitMenuItem = new JMenuItem(Context.getString("quit"));
    }

    private void glueAllTogether() {
        //FilesMenu
        filesMenu.add(exitMenuItem);

        //HelpMenu
        helpMenu.add(aboutAppMenuItem);

        //MenuBar
        menuBar.add(filesMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(helpMenu);
    }

    private void setListeners() {
        exitMenuItem.addActionListener(e -> {
            System.exit(0);
        });

        aboutAppMenuItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, new JLabel("<html><center>"+ Context.getString("final_project") +"<br />Total Commander<br />Tomasz Gwoździk<br /></br />PUT 2017</center></html>",JLabel.CENTER), Context.getString("about"), JOptionPane.PLAIN_MESSAGE);
        });
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    @Override
    public void contextChanged() {
        filesMenu.setText(Context.getString("files"));
        helpMenu.setText(Context.getString("help"));
        aboutAppMenuItem.setText(Context.getString("about"));
        exitMenuItem.setText(Context.getString("quit"));
    }
}
