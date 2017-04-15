package layout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

/**
 * Created by Tomasz Gwoździk on 09.04.2017.
 */
public class FlatButton extends JButton {
    public enum ElementBorderPosition {
        RIGHT
    };

    public final static ElementBorderPosition RIGHT = ElementBorderPosition.RIGHT;

    FlatButton(String label) {
        setBorderPainted(false);
        setText(label);
        setItFlat();
    }

    FlatButton(String label, ElementBorderPosition position) {
        switch (position) {
            case RIGHT:
                setBorder(BorderFactory.createMatteBorder(0,0,0,1, Color.BLACK));

                break;
        }

        setText(label);
        setItFlat();
    }

    private void setItFlat() {
        setFocusPainted(false);
        setBackground(Color.LIGHT_GRAY);

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(new Color(135,206,250));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(Color.LIGHT_GRAY);
            }
        });
    }
}
