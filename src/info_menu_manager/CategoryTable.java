package info_menu_manager;


import create_forms.CreateCategoryForm;
import entity.Category;
import entity.CustomerCard;
import entity.Employee;
import items_forms.CategoryActionForm;
import menu.MainMenuManager;
import menu.Report;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static bd_connection.Category.getAllCategories;
import static bd_connection.Customer_Card.getAllCustomersSorted;

public class CategoryTable {

    static List<Category> categoryList;

    public static List<Category> getCategoryList() {
        return categoryList;
    }

    static {
        categoryList = getAllCategories();
    }

    public static void display(JFrame frame, Employee role) {

        JToolBar buttonPanel = new JToolBar();

        JButton homeButton = new JButton("Home");
        buttonPanel.add(homeButton);

        homeButton.addActionListener( s ->{
            frame.getContentPane().removeAll();
            MainMenuManager.display(frame, role);
            frame.revalidate();
            frame.repaint();
        });

        JButton sortButton = new JButton("Sort");
        buttonPanel.add(sortButton);

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

        JToolBar managerTools = new JToolBar();
        JButton add = new JButton("Add");
        JButton print = new JButton("Print");
        JButton  paramSearch = new JButton("Parametric search");
        managerTools.add(add);
        managerTools.add(print);
        managerTools.add( paramSearch);
        if(role.getRole().toString().equals("MANAGER")){
            frame.add(managerTools, BorderLayout.PAGE_END);
        }

        add.addActionListener( e -> {
                    frame.setEnabled(false);
            CreateCategoryForm categoryForm = new CreateCategoryForm(model,frame);
                }
        );

        print.addActionListener( e -> {
            Report r = new Report(table);
                }
        );

        table.getSelectionModel().addListSelectionListener(e -> {
            if(role.getRole().toString().equals("MANAGER")) {
                if (!e.getValueIsAdjusting()) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        int catId = (int) model.getValueAt(row, 0);
                        frame.setEnabled(false);
                        CategoryActionForm categoryActionForm = new CategoryActionForm(bd_connection.Category.getCategoryById(catId),model,frame);
                    }
                }
            }
        });

        paramSearch.addActionListener( e ->{
            frame.getContentPane().removeAll();
            SpecialSearchCategory.display(frame, role);

            frame.revalidate();
            frame.repaint();
                }

        );
    }

    public static void main(String[] args) {
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category(1, "Category A"));
        categoryList.add(new Category(2, "Category C"));
        categoryList.add(new Category(3, "Category B"));
       // CategoryTable.display(categoryList);
    }

}
