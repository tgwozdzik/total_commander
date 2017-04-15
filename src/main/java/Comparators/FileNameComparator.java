package Comparators;

import javax.swing.*;
import java.util.Comparator;

/**
 * Created by tgwozdzik on 15.04.2017.
 */
public class FileNameComparator implements Comparator<Object[]> {
    private Integer columnId;
    private JTable table;

    public FileNameComparator(Integer columnId, JTable table) {
        this.columnId = columnId;
        this.table = table;
    }

    @Override
    public int compare(Object[] comp1, Object[] comp2) {
        SortOrder order = table.getRowSorter().getSortKeys().get(0).getSortOrder();

        Boolean comp1_0 = (Boolean) comp1[0];
        Boolean comp2_0 = (Boolean) comp2[0];

        if(order == SortOrder.DESCENDING) {
            if(comp1_0 && comp2_0) return 0;
            if(comp1_0 && !comp2_0) return 0;
            if(!comp1_0 && comp2_0) return -1;
        }

        if(order == SortOrder.ASCENDING) {
            if(comp1_0 && comp2_0) return 0;
            if(comp1_0 && !comp2_0) return 0;
            if(!comp1_0 && comp2_0) return 1;
        }

        String comp1_1 = (String) comp1[1];
        String comp2_2 = (String) comp2[1];

        return comp1_1.compareTo(comp2_2);
    }
}
