package tg.ui;

import tg.logic.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by tgwozdzik on 16.04.2017.
 */
public class OperationDialog extends JFrame implements ContextChangeListener {
    private Copy copy;
    private Move move;
    private Delete delete;

    private Integer isCopying;
    private String target;
    private ArrayList<String> source;

    private JProgressBar progressAll;
    private FlatButton actionButton;
    private JLabel txtFile;

    private JLabel lblFile;
    private JLabel lblSource;
    private JLabel lblTarget;
    private JLabel lblProgressAll;

    private void setUpLayout() {
        setPreferredSize(new Dimension(500, 170));
        setResizable(false);

        OperationDialog dialog = this;

        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                cancelAction();
                Context.removeComponentFromList(actionButton);
                Context.removeComponentFromList(dialog);
            }
        });

        lblSource = new JLabel(Context.getString("source")  + " ");
        lblTarget = new JLabel(Context.getString("destination")  + " ");
        lblFile = new JLabel("");

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

        lblProgressAll = new JLabel(Context.getString("total_progress")  + " ");
        progressAll = new JProgressBar(0, 100);
        progressAll.setStringPainted(true);

        actionButton = new FlatButton("");
        actionButton.setFocusPainted(false);

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
        panControls.add(actionButton, BorderLayout.EAST);

        JPanel panUpper = new JPanel(new BorderLayout());
        panUpper.add(panInput, BorderLayout.NORTH);
        panUpper.add(panProgress, BorderLayout.SOUTH);

        contentPane.add(panUpper, BorderLayout.NORTH);
        contentPane.add(panDetails, BorderLayout.CENTER);
        contentPane.add(panControls, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);

        actionButton.addActionListener(e -> {
            if(move != null || copy != null || delete != null) {
                cancelAction();
            } else {
                runAction();
            }
        });

        Context.addContextChangeListener(this);
        Context.addContextChangeListener(actionButton);
    }

    private void setLabels() {
        switch(isCopying) {
            case 0:
                setTitle(Context.getString("moving_title"));
                if((move != null) && move.getIsRunning()){
                    actionButton.changeKey("cancel");
                } else {
                    actionButton.changeKey("move");
                }
                lblFile.setText(Context.getString("moving")  + " ");
                break;
            case 1:
                setTitle(Context.getString("copying_title"));
                if((copy != null) && copy.getIsRunning()){
                    actionButton.changeKey("cancel");
                } else {
                    actionButton.changeKey("copy");
                }
                lblFile.setText(Context.getString("copying")  + " ");
                break;
            case 2:
                setTitle(Context.getString("deleting_title"));
                if((delete != null) && delete.getIsRunning()){
                    actionButton.changeKey("cancel");
                } else {
                    actionButton.changeKey("delete");
                }
                lblFile.setText(Context.getString("deleting")  + " ");
                break;
        }
    }

    private void cancelAction() {
        switch(isCopying) {
            case 0:
                if(move != null && move.getIsRunning()) move.endTask();
                break;
            case 1:
                if(copy != null && copy.getIsRunning()) copy.endTask();
                break;
            case 2:
                if(delete != null && delete.getIsRunning()) delete.endTask();
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
        message.append("<html>" + Context.getString("file_not_exists") + "<br /><br />");
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

            Object[] options = {
                Context.getString("overwrite_all"),
                Context.getString("overwrite"),
                Context.getString("skip")
            };

            if(fileTarget.exists()) {
                int option = JOptionPane.showOptionDialog(this, "<html>" + Context.getString("overwrite_question") + "<br />"+fileTarget.getAbsolutePath() + "</html>", Context.getString("overwrite_title"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);

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
                Object[] options = {
                    Context.getString("delete_all"),
                    Context.getString("delete_this"),
                    Context.getString("skip")
                };

                int option = JOptionPane.showOptionDialog(this, "<html>" + Context.getString("delete_question") + "<br />" + file.getAbsolutePath() + "</html>", Context.getString("delete_ttile"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);

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

                    actionButton.changeKey("cancel");
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

                    actionButton.changeKey("cancel");
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

                actionButton.changeKey("cancel");
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

    @Override
    public void contextChanged() {
        setLabels();

        lblSource.setText(Context.getString("source") + " ");
        lblTarget.setText(Context.getString("destination") + " ");
        lblProgressAll.setText(Context.getString("total_progress") + " ");
    }
}
