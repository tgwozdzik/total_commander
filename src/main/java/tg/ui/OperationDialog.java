package tg.ui;

import tg.logic.Copy;
import tg.logic.Delete;
import tg.logic.Move;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by tgwozdzik on 16.04.2017.
 */
public class OperationDialog extends JFrame {
    private Copy copy;
    private Move move;
    private Delete delete;

    private Integer isCopying;
    private String target;
    private ArrayList<String> source;

    private JProgressBar progressAll;
    private JButton btnCopy;
    private JLabel txtFile;

    private void setUpLayout() {
        setPreferredSize(new Dimension(500, 170));
        setResizable(false);

        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                cancelAction();
            }
        });

        JLabel lblSource = new JLabel("Zródło: ");
        JLabel lblTarget = new JLabel("Cel: ");
        JLabel lblFile = new JLabel("Trwa kopiowanie: ");

        StringBuilder sourceText = new StringBuilder();
        StringBuilder sourceTextFlat = new StringBuilder();
        sourceText.append("<html>");
        for(String sourceObj : source) {
            sourceTextFlat.append(sourceObj).append(";");
            sourceText.append(sourceObj).append("<br />");
        }
        sourceText.append("</html>");

        JLabel txtSource = new JLabel(sourceTextFlat.toString());
        txtSource.setToolTipText(sourceText.toString());
        JLabel txtTarget = new JLabel(target);
        txtTarget.setToolTipText(target);
        txtFile  = new JLabel("...");

        JLabel lblProgressAll = new JLabel("Całkowity postęp: ");
        progressAll = new JProgressBar(0, 100);
        progressAll.setStringPainted(true);

        btnCopy = new JButton();
        btnCopy.setFocusPainted(false);

        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel panInputFields = new JPanel(new BorderLayout());
        panInputFields.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel panInputLabels = new JPanel(new BorderLayout());
        panInputLabels.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JPanel panProgressLabels = new JPanel(new BorderLayout());
        panProgressLabels.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JPanel panProgressBars = new JPanel(new BorderLayout());
        panProgressBars.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        panInputLabels.add(lblSource, BorderLayout.NORTH);
        panInputLabels.add(lblTarget, BorderLayout.CENTER);
        panInputLabels.add(lblFile, BorderLayout.SOUTH);

        panInputFields.add(txtSource, BorderLayout.NORTH);
        panInputFields.add(txtTarget, BorderLayout.CENTER);
        panInputFields.add(txtFile, BorderLayout.SOUTH);

        panProgressLabels.add(lblProgressAll, BorderLayout.NORTH);
        panProgressBars.add(progressAll, BorderLayout.NORTH);

        JPanel panInput = new JPanel(new BorderLayout());
        JPanel panProgress = new JPanel(new BorderLayout());
        JPanel panDetails = new JPanel(new BorderLayout());
        JPanel panControls = new JPanel(new BorderLayout());
        JPanel panFileName = new JPanel(new BorderLayout());

        panInput.add(panInputLabels, BorderLayout.LINE_START);
        panInput.add(panInputFields, BorderLayout.CENTER);
        panProgress.add(panProgressLabels, BorderLayout.LINE_START);
        panProgress.add(panProgressBars, BorderLayout.CENTER);
        panDetails.add(panFileName, BorderLayout.CENTER);
        panControls.add(btnCopy, BorderLayout.EAST);

        JPanel panUpper = new JPanel(new BorderLayout());
        panUpper.add(panInput, BorderLayout.NORTH);
        panUpper.add(panProgress, BorderLayout.SOUTH);

        contentPane.add(panUpper, BorderLayout.NORTH);
        contentPane.add(panDetails, BorderLayout.CENTER);
        contentPane.add(panControls, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);

        btnCopy.addActionListener(e -> {
            if(move != null || copy != null || delete != null) {
                cancelAction();
            } else {
                runAction();
            }
        });
    }

    private void setLabels() {
        switch(isCopying) {
            case 0:
                setTitle("Zmiana nazwy/Przenoszenie");
                btnCopy.setText("Zmień nazwę/Przenieś");
                break;
            case 1:
                setTitle("Kopiowanie");
                btnCopy.setText("Kopiuj");
                break;
            case 2:
                setTitle("Usuwanie");
                btnCopy.setText("Usuń");
                break;
        }
    }

    private void cancelAction() {
        switch(isCopying) {
            case 0:
                if(move != null && move.getIsRunning() != null) move.cancel(true);
                break;
            case 1:
                if(copy != null && copy.getIsRunning() != null) copy.cancel(true);
                break;
            case 2:
                if(delete != null && delete.getIsRunning() != null) delete.cancel(true);
                break;
        }

        dispose();
    }

    private void runAction() {
        switch(isCopying) {
            case 0:
                runMove();
                break;
            case 1:
                runCopy();
                break;
            case 2:
                runDelete();
                break;
        }
    }

    private Object[] canRunOperation() {
        Boolean canMakeOperation = true;
        StringBuilder message = new StringBuilder();
        message.append("<html>The source file/directory does not exist!<br /><br />");
        for(String sourceObj : source) {
            File file = new File(sourceObj);
            if(!file.exists()) {
                canMakeOperation = false;

                message.append(sourceObj).append("<br />");
            }
        }
        message.append("</html>");

        return new Object[]{canMakeOperation, message.toString()};
    }

    private ArrayList<String> getSourceElements() {
        ArrayList<String> newSourceList = new ArrayList<>();
        for(String sourceObj : source) {
            File file = new File(sourceObj);
            File fileTarget = new File (target + File.separator + file.getName());

            Object[] options = {"Nadpisz wszystkie",
                    "Nadpisz bieżący",
                    "Pomiń"};

            if(fileTarget.exists()) {
                int option = JOptionPane.showOptionDialog(this, "<html>The target file/directory already exists, do you want to overwrite it?<br />"+fileTarget.getAbsolutePath() + "</html>", "Overwrite the target", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);

                if(option == JOptionPane.YES_OPTION) {
                    newSourceList = source;

                    break;
                }

                if(option == JOptionPane.NO_OPTION) {
                    newSourceList.add(sourceObj);
                }
            } else {
                newSourceList.add(sourceObj);
            }
        }

        return newSourceList;
    }

    private ArrayList<String> checkElementsToDelete() {
        ArrayList<String> newSourceList = new ArrayList<>();
        for(String sourceObj : source) {
            File file = new File(sourceObj);

            if(file.isDirectory() && file.listFiles().length > 0) {
                Object[] options = {"Usuń wszystkie",
                        "Usuń bieżący",
                        "Pomiń"};

                int option = JOptionPane.showOptionDialog(this, "<html>The directory is not empty, do you want to delete it anyway?<br />"+file.getAbsolutePath() + "</html>", "Delete directory", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);

                if(option == JOptionPane.YES_OPTION) {
                    newSourceList = source;

                    break;
                }

                if(option == JOptionPane.NO_OPTION) {
                    newSourceList.add(sourceObj);
                }
            } else {
                newSourceList.add(sourceObj);
            }
        }

        return newSourceList;
    }

    private void runCopy() {
        SwingUtilities.invokeLater(() -> {
            Object[] canRunOperation = canRunOperation();

            if(!((boolean) canRunOperation[0])) {
                JOptionPane.showMessageDialog(this, canRunOperation[1], "ERROR", JOptionPane.ERROR_MESSAGE);
            } else {
                ArrayList<String> newSourceList = getSourceElements();

                if(newSourceList.size() > 0) {
                    copy = new Copy(newSourceList, target, progressAll, txtFile);
                    copy.execute();

                    copy.addPropertyChangeListener(evt -> {
                        if ("state".equals(evt.getPropertyName()) && SwingWorker.StateValue.DONE == evt.getNewValue()) {
                            dispose();
                        }
                    });

                    btnCopy.setText("Anuluj");
                }
            }
        });
    }

    private void runMove() {
        SwingUtilities.invokeLater(() -> {
            Object[] canRunOperation = canRunOperation();

            if(!((boolean) canRunOperation[0])) {
                JOptionPane.showMessageDialog(this, canRunOperation[1], "ERROR", JOptionPane.ERROR_MESSAGE);
            } else {
                ArrayList<String> newSourceList = getSourceElements();

                if(newSourceList.size() > 0) {
                    move = new Move(newSourceList, target, progressAll, txtFile);
                    move.execute();

                    move.addPropertyChangeListener(evt -> {
                        if("state".equals(evt.getPropertyName()) && SwingWorker.StateValue.DONE == evt.getNewValue()) {
                            dispose();
                        }
                    });

                    btnCopy.setText("Anuluj");
                }
            }
        });
    }

    private void runDelete() {
        SwingUtilities.invokeLater(() -> {
            ArrayList<String> newSourceList = checkElementsToDelete();

            if(newSourceList.size() > 0) {
                delete = new Delete(newSourceList, progressAll, txtFile);
                delete.execute();

                delete.addPropertyChangeListener(evt -> {
                    if ("state".equals(evt.getPropertyName()) && SwingWorker.StateValue.DONE == evt.getNewValue()) {
                        dispose();
                    }
                });

                btnCopy.setText("Anuluj");
            }
        });
    }

    public OperationDialog(ArrayList<String> source, String target, Integer isCopying) {
        this.source = source;
        this.target = target;
        this.isCopying = isCopying;

        setUpLayout();
        setLabels();
    }
}
