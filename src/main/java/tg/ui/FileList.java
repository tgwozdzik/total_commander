package tg.ui;

import tg.logic.TableCellRenderer;
import tg.logic.FileListLogic;
import tg.logic.comparators.FileCreationDateComparator;
import tg.logic.comparators.FileNameComparator;
import tg.logic.comparators.FileSizeComparator;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class FileList extends JPanel {
    private FileListLogic fileListLogic;

    private JScrollPane scrollPane;

    private JPanel fileHeaderPanel = new JPanel(new BorderLayout());
    private Label fileSystemLocationLabel = new Label("");
    private Label freeSpaceLabel = new Label("");

    private JComboBox drives;

    private JTable fileTable;

    private Boolean isFocused;

    private TableRowSorter<TableModel> tableRowSorter;

    private void setUpFileList() {
        isFocused = false;
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        fileListLogic = new FileListLogic();
        scrollPane = new JScrollPane();

        fileTable = new JTable(fileListLogic.setDefaultTableModel(new Object[]{"Nazwa", "Rozmiar", "Data utworzenia"}));

        fileTable.setShowGrid(false);
        scrollPane.setViewportView(fileTable);

        scrollPane.getViewport().setBackground(Color.WHITE);
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
                fileListLogic.setCurrentPath(newPath);
                setFreeSpaceLabel();
                setFileSystemLocationLabel(newPath);
                displayFilesAndDirs();
            }
        });
    }

    public FileList() {
        setUpFileList();
        setUpCellsRender();
        setUpColumnsSorter();
        setUpHeader();
        setUpListeners();
        setFileSystemLocationLabel();
        updateDrives();
        displayFilesAndDirs();
    }

    public ArrayList<String> getSelected() {
        ArrayList<String> toReturn = new ArrayList<>();

        if(isFocused) {
            for(int row : fileTable.getSelectedRows()) {
                Object[] element = (Object[]) fileTable.getValueAt(row, 0);

                toReturn.add((String) element[1]);
            }
        } else {
            toReturn.add(fileListLogic.getCurrentPath());
        }

        return toReturn;
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

    public String getCurrentPath() {
        return fileListLogic.getCurrentPath();
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

    public void setFocus(Boolean focus) {
        isFocused = focus;
    }

    public Boolean getFocusStatus() {
        return isFocused;
    }

    public JTable getFileTable() {
        return fileTable;
    }

    public void refresh() {
        displayFilesAndDirs();
    }
}