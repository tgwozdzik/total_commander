package tg.logic;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Tomasz Gwoździk on 11.04.2017.
 */
public class TableCellRenderer extends DefaultTableCellRenderer {
    private FileSystemView fileSystemView;
    private JLabel label;

    public TableCellRenderer() {
        fileSystemView = FileSystemView.getFileSystemView();
    }

    private void formatFileName(Object[] values) {
        String filePath = (String) values[1];
        File file = new File(filePath);

        if (filePath.contains("..")) {
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

    private void formatFileSize(Object[] values) {
        if(values[1] == null) {
            label.setText("");
        } else {
            Long size = (Long) values[1];

            if (size == -1) {
                label.setText("");
            } else {
                label.setText(size.toString());
            }
        }
    }

    private void formatCreationDate(Object[] values) {
        if(values[1] == null) {
            label.setText("");
        } else {
            Long creationDate = (Long) values[1];

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(creationDate);

            label.setText(formatter.format(calendar.getTime()));
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object values, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, values, isSelected, hasFocus, row, column);

        label = (JLabel) component;

        Object[] valuesArray = (Object[]) values;

        if(valuesArray == null) return component;
        else if(column == 0) formatFileName(valuesArray);
        else if(column == 1) formatFileSize(valuesArray);
        else if(column == 2) formatCreationDate(valuesArray);

        if(isSelected) {
            label.setBackground(new Color(135,206,250));
        } else {
            label.setBackground(null);
        }

        label.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, new Color(0,0,0)));

        return label;
    }
}
