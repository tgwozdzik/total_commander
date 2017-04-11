package layout;

import javax.swing.*;
import java.awt.Choice;
import java.awt.Label;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;

public class FileDisplay2 extends JPanel {
    private Choice dirChoice               = new Choice();
    private JPanel fileButtonsInnerPanel   = new JPanel(new GridLayout(0,1));
    private JPanel fileButtonsPanel        = new JPanel(new BorderLayout());
    private JPanel fileHeaderPanel         = new JPanel(new BorderLayout());
    private Label  fileSystemLocationLabel = new Label("");

    private String filePath;
    private DefaultListModel<String> model;
    private JScrollPane scrollPane;
    private JList<String> fileList;

    public FileDisplay2() {
        this.filePath = System.getProperty("user.home");
        this.model = new DefaultListModel<>();
        this.scrollPane = new JScrollPane();
        this.fileList = new JList<>(model);
        this.scrollPane.setViewportView(fileList);

        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        this.fileHeaderPanel.add(fileSystemLocationLabel, BorderLayout.NORTH);
        this.dirChoice.setBackground(Color.WHITE);
        this.fileHeaderPanel.add(dirChoice, BorderLayout.SOUTH);

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
        setFileSystemLocationLabelText(filePath);
    }

    private void updatePath(Integer index) throws IOException {
        String selectedFile = model.get(index);


        filePath = new File(filePath + File.separator + selectedFile).getCanonicalPath();
        displayFilesAndDirs();
        setFileSystemLocationLabelText(filePath);
    }

    private void displayFilesAndDirs() {
        File[] files = new File(filePath).listFiles();

        model.clear();
        model.addElement("..");

        for (File file : files) {
            model.addElement(file.getName());
        }
    }

    public void setFileSystemLocationLabelText(String arg1) {
        fileSystemLocationLabel.setText(arg1);
    }
}