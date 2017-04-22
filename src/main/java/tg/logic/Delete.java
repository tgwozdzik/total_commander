package tg.logic;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tgwozdzik on 16.04.2017.
 */
public class Delete extends SwingWorker<Void, List<Object>> {
    private ArrayList<File> source;
    private long totalBytes = 0L;
    private long removedBytes = 0L;
    private JProgressBar progressAll;
    private JLabel currentlyCopiedFile;

    private Boolean isRunning;

    public Delete(ArrayList<String> source, JProgressBar progressAll, JLabel txtFile) {
        this.source = new ArrayList<>();
        for(String sourceObj : source) {
            this.source.add(new File(sourceObj));
        }

        this.progressAll = progressAll;

        this.progressAll.setValue(0);

        this.currentlyCopiedFile = txtFile;
    }

    @Override
    public Void doInBackground() throws Exception {
        isRunning = true;

        publish(createPublishArray(null,"Retrieving some info ..."));

        for(File sourceObj : source) {
            retrieveTotalBytes(sourceObj);
        }

        publish(createPublishArray(null,"Start task"));

        for(File sourceObj : source) {
            removeFiles(sourceObj);
        }

        isRunning = false;

        return null;
    }

    @Override
    public void process(List<List<Object>> chunks) {
        for(List<Object> i : chunks)
        {
            if(i.get(0) != null) {
                //Total
                progressAll.setValue((Integer) i.get(0));
            }

            if(i.get(1) != null) {
                //Current file
                currentlyCopiedFile.setText((String) i.get(1));
            }
        }
    }

    @Override
    public void done() {
        publish(createPublishArray(100,"\nDone!\n"));
    }

    private void retrieveTotalBytes(File sourceFile) {
        if(sourceFile.isDirectory()) {

            File[] files = sourceFile.listFiles();
            for(File file : files)
            {
                if(file.isDirectory()) retrieveTotalBytes(file);
                else totalBytes += file.length();
            }
        } else {
            totalBytes += sourceFile.length();
        }
    }

    private ArrayList<Object> createPublishArray(Object allProgress, Object text) {
        ArrayList<Object> array = new ArrayList<>();
        array.add(allProgress);
        array.add(text);

        return array;
    }

    private void removeFiles(File sourceFile) throws IOException {
        if(!isRunning) return;

        if(sourceFile.isDirectory())
        {
            String[] filePaths = sourceFile.list();

            for(String filePath : filePaths)
            {
                File srcFile = new File(sourceFile, filePath);

                removeFiles(srcFile);
            }

            if(!sourceFile.delete()) {
                publish(createPublishArray(null, "Nie usunięto folderu!"));
            }
        }
        else
        {
            publish(createPublishArray(null, sourceFile.getName()));

            Long tempFileSize = sourceFile.length();

            if(!sourceFile.delete()) {
                publish(createPublishArray(null, "Nie usunięto pliku!"));
            } else {
                removedBytes += tempFileSize;
                publish(createPublishArray((int) (removedBytes * 100 / totalBytes), null));
            }

            publish(createPublishArray(null, "..."));
        }
    }

    public Boolean getIsRunning() {
        return isRunning;
    }

    public void endTask() {
        this.isRunning = false;
    }
}


