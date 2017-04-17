package tg.logic;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tgwozdzik on 16.04.2017.
 */
public class Move extends SwingWorker<Void, List<Object>> {
    private ArrayList<File> source;
    private File target;
    private long totalBytes = 0L;
    private long copiedBytes = 0L;
    private JProgressBar progressAll;
    private JLabel currentlyCopiedFile;

    private Boolean isRunning;

    public Move(ArrayList<String> source, String target, JProgressBar progressAll, JLabel txtFile) {
        this.source = new ArrayList<>();
        for(String sourceObj : source) {
            this.source.add(new File(sourceObj));
        }

        this.target = new File(target);

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
            moveFiles(sourceObj, new File(target.getCanonicalPath() + File.separator + sourceObj.getName()));
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

    private void moveFiles(File sourceFile, File targetFile) throws IOException {
        if(sourceFile.isDirectory())
        {
            if(!targetFile.exists()) targetFile.mkdirs();

            String[] filePaths = sourceFile.list();

            for(String filePath : filePaths)
            {
                File srcFile = new File(sourceFile, filePath);
                File destFile = new File(targetFile, filePath);

                moveFiles(srcFile, destFile);
            }

            if(!sourceFile.delete()) {
                publish(createPublishArray(null, "Nie usunięto folderu!"));
            }
        }
        else
        {
            publish(createPublishArray(null, sourceFile.getName()));

            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile));
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetFile));

            int theByte;

            while((theByte = bis.read()) != -1)
            {
                bos.write(theByte);
                publish(createPublishArray((int) (copiedBytes++ * 100 / totalBytes), null));
            }

            bis.close();
            bos.close();

            if(!sourceFile.delete()) {
                publish(createPublishArray(null, "Nie usunięto pliku!"));
            }

            publish(createPublishArray(null, "..."));
        }
    }

    public Boolean getIsRunning() {
        return isRunning;
    }
}

