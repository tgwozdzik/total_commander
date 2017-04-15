package layout;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Tomasz Gwoździk on 11.04.2017.
 */
public class CustomTableCellRendered extends DefaultTableCellRenderer {
    private FileSystemView fileSystemView;

    CustomTableCellRendered() {
        fileSystemView = FileSystemView.getFileSystemView();
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {

        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        JLabel label = (JLabel) component;

        //Format file name
        if(column == 0) {
            File file = new File(label.getText());

            if (label.getName().contains("..")) {
                label.setIcon(UIManager.getIcon("FileChooser.upFolderIcon"));
            } else {
                label.setIcon(fileSystemView.getSystemIcon(file));
            }

            if (file.isDirectory()) {
                label.setText("[" + fileSystemView.getSystemDisplayName(file) + "]");
            } else {
                label.setText(fileSystemView.getSystemDisplayName(file));
            }
        }

        //Format creation date
        if(column == 2 && !label.getText().equals("-") && !label.getText().equals("")) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.parseLong(label.getText()));

                label.setText(formatter.format(calendar.getTime()));
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return component;
    }
}
