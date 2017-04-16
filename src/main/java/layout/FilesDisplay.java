package layout;

import Comparators.FileCreationDateComparator;
import Comparators.FileNameComparator;
import Comparators.FileSizeComparator;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

class FilesDisplay extends JPanel {
    private JPanel fileButtonsInnerPanel   = new JPanel(new GridLayout(0,1));
    private JPanel fileButtonsPanel        = new JPanel(new BorderLayout());
    private JPanel fileHeaderPanel         = new JPanel(new BorderLayout());
    private Label fileSystemLocationLabel = new Label("");
    private Label freeSpaceLabel = new Label("");

    private JComboBox drivesList;

    private JTable filesAndDirectoriesTable;
    private String filePath;

    private DefaultTableModel tableModel;
    private TableRowSorter<TableModel> tableRowSorter;

    FilesDisplay() throws IOException {
        this.filePath = new File(System.getProperty("user.home")).getCanonicalPath();
        JScrollPane scrollPane = new JScrollPane();

        /* ---------- TABLE ---------- */
        Object[][] tableRowData = new Object[0][0];
        Object[] tableColumnNames = new Object[]{"Nazwa", "Rozmiar", "Data utworzenia"};
        this.tableModel = new MyTableModel(tableRowData, tableColumnNames);
        filesAndDirectoriesTable = new JTable(tableModel);
        filesAndDirectoriesTable.getColumnModel().getColumn(0).setCellRenderer(new CustomTableCellRendered());
        filesAndDirectoriesTable.getColumnModel().getColumn(1).setCellRenderer(new CustomTableCellRendered());
        filesAndDirectoriesTable.getColumnModel().getColumn(2).setCellRenderer(new CustomTableCellRendered());

        this.tableRowSorter = new TableRowSorter<>(filesAndDirectoriesTable.getModel());
        tableRowSorter.setComparator(0, new FileNameComparator(0, filesAndDirectoriesTable));
        tableRowSorter.setComparator(1, new FileSizeComparator(1, filesAndDirectoriesTable));
        tableRowSorter.setComparator(2, new FileCreationDateComparator(2, filesAndDirectoriesTable));
        filesAndDirectoriesTable.setRowSorter(tableRowSorter);

        this.drivesList = new JComboBox();

        scrollPane.setViewportView(filesAndDirectoriesTable);

        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        this.fileHeaderPanel.add(fileSystemLocationLabel, BorderLayout.SOUTH);
        this.fileHeaderPanel.add(drivesList, BorderLayout.WEST);
        this.fileHeaderPanel.add(freeSpaceLabel, BorderLayout.CENTER);

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

        updateDrives();
        setFreeSpaceLabel();
        displayFilesAndDirs();
        setFileSystemLocationLabelText(filePath);
    }

    private void setFreeSpaceLabel() {
        File file = new File((String) drivesList.getSelectedItem());

        Long freeSpace = file.getFreeSpace() / 1000;
        Long totalSpace = file.getTotalSpace() / 1000;

        freeSpaceLabel.setText(freeSpace.toString() + " k z " + totalSpace.toString() + " k wolne");
    }

    private void updateDrives() {
        drivesList.removeAllItems();

        try {
            for(File drive : File.listRoots()) {
                drivesList.addItem(drive.getCanonicalPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updatePath(Integer rowIndex) throws IOException {
        Object[] newDestination = (Object[]) filesAndDirectoriesTable.getValueAt(rowIndex, 0);
        File selectedFile = new File((String) newDestination[1]);

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

        ArrayList<Object[]> fileList = new ArrayList<>();
        ArrayList<Object[]> directoryList = new ArrayList<>();

        if(!filePath.equals(levelUp.getCanonicalPath())) {
            directoryList.add(new Object[]{
                    new Object[]{true, filePath + File.separator + ".."},
                    new Object[]{true, (long) -1},
                    new Object[]{true, (long) -1}
            });
        }

        if (files != null) {
            for (File file : files) {
                BasicFileAttributes fileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);

                if(file.isDirectory()) {
                    directoryList.add(new Object[]{
                            new Object[]{true, file.getCanonicalPath()},
                            new Object[]{true, (long) -1},
                            new Object[]{true, fileAttributes.creationTime().toMillis()}
                    });
                    //directoryList.add(new Object[]{file.getCanonicalPath(), (long) -1, "d" + fileAttributes.creationTime().toMillis()});
                } else {
                    fileList.add(new Object[]{
                            new Object[]{false, file.getCanonicalPath()},
                            new Object[]{false, file.length()},
                            new Object[]{false, fileAttributes.creationTime().toMillis()}
                    });
                    //fileList.add(new Object[]{file.getCanonicalPath(), file.length(), "" + fileAttributes.creationTime().toMillis()});
                }
            }
        }


        for(Object[] directoryObj : directoryList ) {
            tableModel.addRow(directoryObj);
        }

        for(Object[] fileObj : fileList ) {
            tableModel.addRow(fileObj);
        }
    }

    private void setFileSystemLocationLabelText(String arg1) {
        fileSystemLocationLabel.setText(arg1);
    }
}