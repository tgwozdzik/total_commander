package tg.ui.fileList;

import tg.logic.TableCellRenderer;
import tg.logic.FileListLogic;
import tg.logic.comparators.FileCreationDateComparator;
import tg.logic.comparators.FileNameComparator;
import tg.logic.comparators.FileSizeComparator;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;

public class FileList extends JPanel {
    private FileListLogic fileListLogic;

    private JScrollPane scrollPane;

    private JPanel fileHeaderPanel = new JPanel(new BorderLayout());
    private Label fileSystemLocationLabel = new Label("");
    private Label freeSpaceLabel = new Label("");

    private JComboBox drives;

    private JTable fileTable;

    private TableRowSorter<TableModel> tableRowSorter;

    private void setUpFileList() {
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        fileListLogic = new FileListLogic();
        scrollPane = new JScrollPane();

        fileTable = new JTable(fileListLogic.setDefaultTableModel(new Object[]{"Nazwa", "Rozmiar", "Data utworzenia"}));
        scrollPane.setViewportView(fileTable);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void setUpColumnsSorter() {
        this.tableRowSorter = new TableRowSorter<>(fileTable.getModel());

        tableRowSorter.setComparator(0, new FileNameComparator(0, fileTable));
        tableRowSorter.setComparator(1, new FileSizeComparator(1, fileTable));
        tableRowSorter.setComparator(2, new FileCreationDateComparator(2, fileTable));

        fileTable.setRowSorter(tableRowSorter);
    }

    private void setUpHeader() {
        this.drives = new JComboBox();

        this.fileHeaderPanel.add(fileSystemLocationLabel, BorderLayout.SOUTH);
        this.fileHeaderPanel.add(drives, BorderLayout.WEST);
        this.fileHeaderPanel.add(freeSpaceLabel, BorderLayout.CENTER);

        add(fileHeaderPanel, BorderLayout.NORTH);
    }

    private void setUpCellsRender() {
        fileTable.getColumnModel().getColumn(0).setCellRenderer(new TableCellRenderer());
        fileTable.getColumnModel().getColumn(1).setCellRenderer(new TableCellRenderer());
        fileTable.getColumnModel().getColumn(2).setCellRenderer(new TableCellRenderer());
    }

    private void setUpListeners() {
        fileTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JTable table = (JTable) evt.getSource();

                if (evt.getClickCount() == 2) {
                    int row = table.rowAtPoint(evt.getPoint());

                    if (row >= 0) updatePath(row);
                }
            }
        });

        drives.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String newPath = (String) e.getItem();

                fileListLogic.setCurrentDrive(newPath);
                setFreeSpaceLabel();
                displayFilesAndDirs();
                setFileSystemLocationLabel(newPath);
            }
        });
    }

    public FileList() {
        setUpFileList();
        setUpCellsRender();
        setUpColumnsSorter();
        setUpHeader();
        setUpListeners();
        updateDrives();
        displayFilesAndDirs();
        setFileSystemLocationLabel();
    }

    private void setFreeSpaceLabel() {
        freeSpaceLabel.setText(fileListLogic.getSpacesLabel());
    }

    private void updateDrives() {
        for(String path : fileListLogic.getDrivesList()) {
            drives.addItem(path);
        }
    }

    private void updatePath(Integer rowIndex) {
        Object[] selectedValue = (Object[]) fileTable.getValueAt(rowIndex, 0);

        fileListLogic.updatePath(selectedValue);

        displayFilesAndDirs();
        setFileSystemLocationLabel();
    }

    private void displayFilesAndDirs() {
        fileListLogic.displayFilesAndDirs();
    }

    private void setFileSystemLocationLabel() {
        fileSystemLocationLabel.setText(fileListLogic.getCurrentPath());
    }

    private void setFileSystemLocationLabel(String labelText) {
        fileSystemLocationLabel.setText(labelText);
    }
}