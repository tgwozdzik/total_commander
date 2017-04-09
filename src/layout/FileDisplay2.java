package layout;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Label;
import java.awt.Panel;
import java.awt.List;
import java.awt.TextField;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;

/** This class represents a small pane which will list the files present
 *  on a given platform.  This pane was made into its own class to allow
 *  easy reuse as both the local and remote file displays
 *
 *  This version of the solution uses the "blob"
 *    technique of writing the GUI code
 *  All GUI creation is done in a single big blob
 *    of code
 */
public class FileDisplay2 extends Panel {
    private Choice       dirChoice               = new Choice();
    private Panel        fileButtonsInnerPanel   = new Panel(new GridLayout(0,1));
    private Panel        fileButtonsPanel        = new Panel(new BorderLayout());
    private Panel        fileHeaderPanel         = new Panel(new BorderLayout());
    private List         fileList                = new List();
    private Label        fileSystemLocationLabel = new Label("");

    public FileDisplay2() {
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        fileHeaderPanel.add("North", fileSystemLocationLabel);
        dirChoice.setBackground(Color.white);
        fileHeaderPanel.add("South", dirChoice);

        fileButtonsPanel.add("North", fileButtonsInnerPanel);

        fileList.setBackground(Color.white);

        add("North",  fileHeaderPanel);
        add("East",   fileButtonsPanel);
        add("Center", fileList);
    }

    public void setFileSystemLocationLabelText(String arg1) {
        fileSystemLocationLabel.setText(arg1);
    }
}