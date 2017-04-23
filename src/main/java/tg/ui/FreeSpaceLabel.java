package tg.ui;

import tg.logic.Context;
import tg.logic.ContextChangeListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Created by Tomasz Gwoździk on 23.04.2017.
 */
public class FreeSpaceLabel extends JLabel implements ContextChangeListener {
    private Long freeSpace;
    private Long totalSpace;

    @Override
    public void contextChanged() {
        setText(Context.formatNumber(freeSpace) +
                " k " +
                Context.getString("of") +
                " " +
                Context.formatNumber(totalSpace) +
                " k " +
                Context.getString("free"));
    }

    public FreeSpaceLabel() {
        super();
        setBorder();
    }

    public FreeSpaceLabel(Long free, Long total) {
        super();
        setBorder();

        this.freeSpace = free;
        this.totalSpace = total;
    }

    private void setBorder() {
        setBorder(new EmptyBorder(0, 10, 0, 10));
    }

    public void changeValues(Long free, Long total) {
        this.freeSpace = free;
        this.totalSpace = total;

        contextChanged();
    }
}
