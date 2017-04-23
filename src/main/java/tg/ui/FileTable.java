package tg.ui;

import tg.logic.Context;
import tg.logic.ContextChangeListener;
import tg.logic.FileListLogic;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * Created by Tomasz Gwoździk on 23.04.2017.
 */
public class FileTable extends JTable implements ContextChangeListener {
    private FileListLogic fileListLogic;

    @Override
    public void contextChanged() {
        JTableHeader th = getTableHeader();
        TableColumnModel tcm = th.getColumnModel();

        TableColumn tc = tcm.getColumn(0);
        tc.setHeaderValue( Context.getString("name") );

        tc = tcm.getColumn(1);
        tc.setHeaderValue( Context.getString("size") );

        tc = tcm.getColumn(2);
        tc.setHeaderValue( Context.getString("creation_date") );

        th.repaint();
        repaint();
    }

    public FileTable(FileListLogic fileListLogic) {
        super(fileListLogic.setDefaultTableModel(new Object[]{Context.getString("name"), Context.getString("size"), Context.getString("creation_date")}));
        this.fileListLogic = fileListLogic;
    }
}
