package menu;


import entity.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CategoryTable {

    public static void display(List<Category> categoryList) {

        JFrame frame = new JFrame("Category Table");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JToolBar buttonPanel = new JToolBar();

        JButton homeButton = new JButton("Home");
        buttonPanel.add(homeButton);

        JButton sortButton = new JButton("Sort");
        buttonPanel.add(sortButton);

        JLabel categoriesLabel = new JLabel("Categories");
        buttonPanel.add(categoriesLabel);

        JPanel tablePanel = new JPanel(new BorderLayout());

        JTable table = new JTable();

        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Name"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);

        for (Category category : categoryList) {
            model.addRow(new Object[]{category.getId(), category.getName()});
        }

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(buttonPanel, BorderLayout.PAGE_START);
        frame.add(tablePanel, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);

        AtomicInteger sortOrder = new AtomicInteger();

        sortButton.addActionListener(e -> {

            if (sortOrder.get() == 1) {
                Collections.sort(categoryList, (c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));
                sortButton.setText("Sort (Z-A)");
                sortOrder.set(0);
            } else {
                Collections.sort(categoryList, (c1, c2) -> c2.getName().compareToIgnoreCase(c1.getName()));
                sortButton.setText("Sort (A-Z)");
                sortOrder.getAndIncrement();
            }

            model.setRowCount(0);
            for (Category category : categoryList) {
                model.addRow(new Object[]{category.getId(), category.getName()});
            }
        });

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                TableColumnModel columnModel = table.getColumnModel();
                columnModel.getColumn(0).setPreferredWidth((int) (table.getWidth() * 0.2));
                columnModel.getColumn(1).setPreferredWidth((int) (table.getWidth() * 0.8));
                table.setPreferredScrollableViewportSize(new Dimension(table.getWidth(), table.getRowHeight() * table.getRowCount()));
                table.revalidate();
            }
        });
    }

    public static void main(String[] args) {
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category(1, "Category A"));
        categoryList.add(new Category(2, "Category C"));
        categoryList.add(new Category(3, "Category B"));
        CategoryTable.display(categoryList);
    }

}
