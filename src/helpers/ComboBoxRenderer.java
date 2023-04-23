package helpers;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;

public class ComboBoxRenderer extends BasicComboBoxRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected,
                cellHasFocus);
        if (value instanceof Object[]) {
            Object[] rowData = (Object[]) value;
            setText(rowData[0].toString() + "   " + rowData[1].toString());
        }
        return this;
    }
    @Override
    public void paint(Graphics g) {
        setForeground(Color.BLACK);
        super.paint(g);
    }
}