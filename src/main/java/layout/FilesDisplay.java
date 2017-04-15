package layout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

class FilesDisplay extends JPanel {
    private JPanel fileButtonsInnerPanel   = new JPanel(new GridLayout(0,1));
    private JPanel fileButtonsPanel        = new JPanel(new BorderLayout());
    private JPanel fileHeaderPanel         = new JPanel(new BorderLayout());
    private Label  fileSystemLocationLabel = new Label("");

    private String filePath;

    private DefaultTableModel tableModel;

    FilesDisplay() throws IOException {
        this.filePath = new File(System.getProperty("user.home")).getCanonicalPath();
        JScrollPane scrollPane = new JScrollPane();

        /* ---------- TABLE ---------- */
        Object[][] tableRowData = new Object[0][0];
        Object[] tableColumnNames = new Object[]{"Nazwa", "Rozmiar", "Data utworzenia"};
        this.tableModel = new MyTableModel(tableRowData, tableColumnNames);
        JTable filesAndDirectoriesTable = new JTable(tableModel);
        filesAndDirectoriesTable.getColumnModel().getColumn(0).setCellRenderer(new CustomTableCellRendered());
        filesAndDirectoriesTable.getColumnModel().getColumn(1).setCellRenderer(new CustomTableCellRendered());
        filesAndDirectoriesTable.getColumnModel().getColumn(2).setCellRenderer(new CustomTableCellRendered());
        scrollPane.setViewportView(filesAndDirectoriesTable);

        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        this.fileHeaderPanel.add(fileSystemLocationLabel, BorderLayout.NORTH);

        this.fileButtonsPanel.add(fileButtonsInnerPanel, BorderLayout.NORTH);

        add(fileHeaderPanel, BorderLayout.NORTH);
        add(fileButtonsPanel, BorderLayout.EAST);
        add(scrollPane, BorderLayout.CENTER);

        filesAndDirectoriesTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JTable table = (JTable)evt.getSource();

                if (evt.getClickCount() == 2) {
                    int row = table.rowAtPoint(evt.getPoint());

                    if (row >= 0) {
                        try {
                            updatePath(row);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        displayFilesAndDirs();
        setFileSystemLocationLabelText(filePath);

    }

    private void updatePath(Integer rowIndex) throws IOException {
        String newDestination = (String) tableModel.getValueAt(rowIndex, 0);
        File selectedFile = new File(newDestination);

        if(selectedFile.isFile()) {
            Desktop.getDesktop().open(selectedFile);
        } else {
            filePath = selectedFile.getCanonicalPath();

            displayFilesAndDirs();
            setFileSystemLocationLabelText(filePath);
        }
    }

    private void displayFilesAndDirs() throws IOException {
        File[] files = new File(filePath).listFiles();

        tableModel.setRowCount(0);
        File levelUp = new File(filePath + File.separator + "..");

        if(!filePath.equals(levelUp.getCanonicalPath())) {
            tableModel.addRow(new Object[]{filePath + File.separator + "..", "-", "-"});
        }

        if (files != null) {
            for (File file : files) {
                BasicFileAttributes fileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                Object[] newRow;

                if(file.isDirectory()) {
                    newRow = new Object[]{file.getCanonicalPath(), "-", fileAttributes.creationTime().toMillis()};
                } else {
                    newRow = new Object[]{file.getCanonicalPath(), file.length(), fileAttributes.creationTime().toMillis()};
                }

                tableModel.addRow(newRow);
            }
        }
    }

    private void setFileSystemLocationLabelText(String arg1) {
        fileSystemLocationLabel.setText(arg1);
    }
}