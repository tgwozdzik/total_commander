package tg.logic;

import javax.swing.table.DefaultTableModel;

/**
 * Created by tgwozdzik on 15.04.2017.
 */
public class MyTableModel extends DefaultTableModel {
    public MyTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column){
        return false;
    }
}
