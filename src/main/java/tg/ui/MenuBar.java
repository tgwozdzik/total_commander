package tg.ui;

import javax.swing.*;

/**
 * Created by tgwozdzik on 16.04.2017.
 */
public class MenuBar {
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
        filesMenu = new JMenu("Pliki");
        helpMenu = new JMenu("Pomoc");
    }

    private void setupSubMenus() {
        aboutAppMenuItem = new JMenuItem("O programie");
        exitMenuItem = new JMenuItem("Zakończ");
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
            JOptionPane.showMessageDialog(null, "<html><center>Projekt zaliczeniowy na TPAL<br />Total Commander<br />Tomasz Gwoździk<br /></br />PUT 2017</center></html>");
        });
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }
}
