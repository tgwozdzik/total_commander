package layout;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by tgwozdzik on 16.04.2017.
 */
public class ChangeRootListener implements ItemListener {
    // This method is called only if a new item has been selected.
    public void itemStateChanged(ItemEvent evt) {
        JComboBox cb = (JComboBox) evt.getSource();

        Object item = evt.getItem();

        if (evt.getStateChange() == ItemEvent.SELECTED) {

        } else if (evt.getStateChange() == ItemEvent.DESELECTED) {
            // Item is no longer selected
        }
    }
}
