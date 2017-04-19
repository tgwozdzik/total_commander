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
import java.util.ArrayList;

/**
 * Created by tgwozdzik on 16.04.2017.
 */
public class FileCopyOperationDialog extends JFrame {
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
        sourceText.append("<html>");
        for(String sourceObj : source) {
            sourceText.append(sourceObj).append("<br />");
        }
        sourceText.append("</html>");

        JLabel txtSource = new JLabel(sourceText.toString());
        JLabel txtTarget = new JLabel(target);
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

        btnCopy.setText("Anuluj");
    }

    private void runCopy() {
        SwingUtilities.invokeLater(() -> {
            copy = new Copy(source, target, progressAll, txtFile);
            copy.execute();

            copy.addPropertyChangeListener(evt -> {
                if("state".equals(evt.getPropertyName()) && SwingWorker.StateValue.DONE == evt.getNewValue()) {
                    dispose();
                }
            });
        });
    }

    private void runMove() {
        SwingUtilities.invokeLater(() -> {
            move = new Move(source, target, progressAll, txtFile);
            move.execute();

            move.addPropertyChangeListener(evt -> {
                if("state".equals(evt.getPropertyName()) && SwingWorker.StateValue.DONE == evt.getNewValue()) {
                    dispose();
                }
            });
        });
    }

    private void runDelete() {
        SwingUtilities.invokeLater(() -> {
            delete = new Delete(source, progressAll, txtFile);
            delete.execute();

            delete.addPropertyChangeListener(evt -> {
                if("state".equals(evt.getPropertyName()) && SwingWorker.StateValue.DONE == evt.getNewValue()) {
                    dispose();
                }
            });
        });
    }

    public FileCopyOperationDialog(ArrayList<String> source, String target, Integer isCopying) {
        this.source = source;
        this.target = target;
        this.isCopying = isCopying;

        setUpLayout();
        setLabels();
    }
}
