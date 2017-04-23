package tg.ui;

import tg.logic.Context;
import tg.logic.ContextChangeListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

/**
 * Created by Tomasz Gwoździk on 09.04.2017.
 */
public class FlatButton extends JButton implements ContextChangeListener {
    private String key;

    @Override
    public void contextChanged() {
        setText(Context.getString(key));
    }

    public enum ElementBorderPosition {
        RIGHT
    };

    public final static ElementBorderPosition RIGHT = ElementBorderPosition.RIGHT;

    public FlatButton(String key) {
        this.key = key;

        setBorderPainted(false);
        setText(Context.getString(key));
        setItFlat();
    }

    public FlatButton(String key, ElementBorderPosition position) {
        this.key = key;

        switch (position) {
            case RIGHT:
                setBorder(BorderFactory.createMatteBorder(0,0,0,1, Color.BLACK));

                break;
        }

        setText(Context.getString(key));
        setItFlat();
    }

    public void changeKey(String key) {
        this.key = key;

        contextChanged();
    }

    private void setItFlat() {
        setFocusPainted(false);
        setBackground(new Color(0,0,0));
        setOpaque(false);

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(new Color(135,206,250));
                setOpaque(true);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(new Color(0,0,0));
                setOpaque(false);
            }
        });
    }
}
