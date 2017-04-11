package layout;

import com.sun.deploy.util.StringUtils;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;

public class FilesDisplay extends JPanel {
    private JPanel fileButtonsInnerPanel   = new JPanel(new GridLayout(0,1));
    private JPanel fileButtonsPanel        = new JPanel(new BorderLayout());
    private JPanel fileHeaderPanel         = new JPanel(new BorderLayout());
    private Label  fileSystemLocationLabel = new Label("");

    private File filePath;
    private DefaultListModel<File> model;
    private JScrollPane scrollPane;
    private JList<File> fileList;

    public FilesDisplay() throws IOException {
        this.filePath = new File(System.getProperty("user.home"));
        this.model = new DefaultListModel<>();
        this.scrollPane = new JScrollPane();
        this.fileList = new JList<>(model);

        FileListNameCellRenderer renderer = new FileListNameCellRenderer();
        fileList.setCellRenderer(renderer);

        this.scrollPane.setViewportView(fileList);

        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        this.fileHeaderPanel.add(fileSystemLocationLabel, BorderLayout.NORTH);

        this.fileButtonsPanel.add(fileButtonsInnerPanel, BorderLayout.NORTH);

        this.fileList.setBackground(Color.WHITE);

        add(fileHeaderPanel, BorderLayout.NORTH);
        add(fileButtonsPanel, BorderLayout.EAST);
        add(scrollPane, BorderLayout.CENTER);

        fileList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();

                if (evt.getClickCount() == 2) {
                    try {
                        updatePath(list.locationToIndex(evt.getPoint()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        displayFilesAndDirs();
        setFileSystemLocationLabelText(filePath.getCanonicalPath());

    }

    private void updatePath(Integer index) throws IOException {
        File selectedFile = model.get(index);

        if(selectedFile.isFile()) {
            Desktop.getDesktop().open(selectedFile);
        } else {
            filePath = selectedFile;
            displayFilesAndDirs();
            setFileSystemLocationLabelText(filePath.getCanonicalPath());
        }
    }

    private void displayFilesAndDirs() throws IOException {
        File[] files = new File(filePath.getCanonicalPath()).listFiles();

        model.clear();
        File levelUp = new File(filePath.getCanonicalPath() + File.separator + "..");

        if(!filePath.getCanonicalPath().equals(levelUp.getCanonicalPath())) {
            model.addElement(levelUp);
        }

        for (File file : files) {
            model.addElement(file);
        }
    }

    public void setFileSystemLocationLabelText(String arg1) {
        fileSystemLocationLabel.setText(arg1);
    }
}