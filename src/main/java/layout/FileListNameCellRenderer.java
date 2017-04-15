package layout;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;

/**
 * Created by Tomasz Gwoździk on 11.04.2017.
 */
public class FileListNameCellRenderer extends DefaultListCellRenderer {
    private FileSystemView fileSystemView;
    private JLabel label;
    private Color textSelectionColor = Color.BLACK;
    private Color backgroundSelectionColor = new Color(135,206,250);
    private Color textNonSelectionColor = Color.BLACK;
    private Color backgroundNonSelectionColor = Color.WHITE;

    FileListNameCellRenderer() {
        label = new JLabel();
        label.setOpaque(true);
        fileSystemView = FileSystemView.getFileSystemView();
    }

    @Override
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean selected,
            boolean expanded) {

        File file = (File)value;

        if(file.getName().contains("..")) {
            label.setIcon(UIManager.getIcon("FileChooser.upFolderIcon"));
        } else {
            label.setIcon(fileSystemView.getSystemIcon(file));
        }

        if(file.isDirectory()) {
            label.setText("[" + fileSystemView.getSystemDisplayName(file) + "]");
        } else {
            label.setText(fileSystemView.getSystemDisplayName(file));
        }

        if (selected) {
            label.setBackground(backgroundSelectionColor);
            label.setForeground(textSelectionColor);
        } else {
            label.setBackground(backgroundNonSelectionColor);
            label.setForeground(textNonSelectionColor);
        }

        return label;
    }
}
