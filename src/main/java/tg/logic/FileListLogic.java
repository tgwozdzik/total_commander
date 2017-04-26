package tg.logic;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

/**
 * Created by tgwozdzik on 16.04.2017.
 */
public class FileListLogic {
    private Object[][] fileList;
    private MyTableModel tableModel;

    private ArrayList<String> drivesList;

    private String currentPath;
    private String currentDrive;

    private Long freeSpace;
    private Long totalSpace;

    public FileListLogic() {
        this.fileList = new Object[0][0];
        this.currentPath = "";
        this.drivesList = new ArrayList<>();

        setDrivesList();
    }

    public MyTableModel setDefaultTableModel(Object[] header) {
        tableModel = new MyTableModel(fileList, header);

        return tableModel;
    }

    private void calculateSpaces() {
        File file = new File(currentDrive);

        freeSpace = file.getFreeSpace() / 1000;
        totalSpace = file.getTotalSpace() / 1000;
    }

    private void setDrivesList() {
        for(File file : File.listRoots()) {
            try {
                drivesList.add(file.getCanonicalPath());
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        if(drivesList.isEmpty()) {
            //Set home location
        } else {
            setCurrentPath(drivesList.get(0));
        }
    }

    public ArrayList<String> getDrivesList() {
        return drivesList;
    }

    public Long getFreeSpace() {
        return freeSpace;
    }

    public Long getTotalSpace() {
        return totalSpace;
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(String newPath) {
        currentPath = newPath;
    }

    public void setCurrentDrive(String newDrive) {
        currentDrive = newDrive;

        calculateSpaces();
    }

    public void updatePath(Object[] selectedValue) {
        File selectedFile = new File((String) selectedValue[1]);

        if(!(Boolean) selectedValue[0]) {
            try {
                Desktop.getDesktop().open(selectedFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                currentPath = selectedFile.getCanonicalPath();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void displayFilesAndDirs() {
        File[] files = new File[]{};
        try {
            files = new File(currentPath).listFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }

        tableModel.setRowCount(0);
        ArrayList<Object[]> fileList = new ArrayList<>();
        ArrayList<Object[]> directoryList = new ArrayList<>();

        if(!currentPath.equals(currentDrive)) {
            directoryList.add(new Object[]{
                    new Object[]{true, currentPath + File.separator + ".."},
                    new Object[]{true, null},
                    new Object[]{true, null}
            });
        }

        if (files != null) {
            for (File file : files) {
                try {
                    BasicFileAttributes fileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);

                    if(file.isDirectory()) {
                        directoryList.add(new Object[]{
                                new Object[]{true, file.getCanonicalPath()},
                                new Object[]{true, null},
                                new Object[]{true, fileAttributes.creationTime().toMillis()}
                        });
                    } else {
                        fileList.add(new Object[]{
                                new Object[]{false, file.getCanonicalPath()},
                                new Object[]{false, file.length()},
                                new Object[]{false, fileAttributes.creationTime().toMillis()}
                        });
                    }
                } catch(Exception e) {
                    e.printStackTrace();
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
}
