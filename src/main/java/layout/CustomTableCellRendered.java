package layout;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

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
            Object values,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {


        Component component = super.getTableCellRendererComponent(table, values, isSelected, hasFocus, row, column);
        JLabel label = (JLabel) component;
        Object[] valuesArray = (Object[]) values;

        if(valuesArray == null) return component;

        //Format file name
        if(column == 0) {
            String filePath = (String) valuesArray[1];
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

        //Format size
        if(column == 1) {
            Long size = (Long) valuesArray[1];

            if(size == -1) {
                label.setText("");
            } else {
                label.setText(size.toString());
            }
        }


        //Format creation date
        if(column == 2) {
            Long creationDate = (Long) valuesArray[1];

            try {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(creationDate);

                label.setText(formatter.format(calendar.getTime()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return component;
    }
}
