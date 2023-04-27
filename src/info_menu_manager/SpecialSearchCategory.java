package info_menu_manager;


import bd_connection.Category;
import com.toedter.calendar.JDateChooser;
import entity.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class SpecialSearchCategory {
    static List<CategorySpecSearch> categoryList;

    public static void display(JFrame frame, Employee role) {

        JToolBar buttonPanel = new JToolBar();

        JButton homeButton = new JButton("Home");
        buttonPanel.add(homeButton);

        homeButton.addActionListener(s -> {
            frame.getContentPane().removeAll();
            CategoryTable.display(frame, role);
            frame.revalidate();
            frame.repaint();
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        JDateChooser dateFrom = new JDateChooser();
        dateFrom.setDate(new Date());
        JDateChooser dateTo = new JDateChooser();
        dateTo.setDate(new Date());

       categoryList = createCommonList(dateFrom.getDate(), dateTo.getDate());

        buttonPanel.add(dateFrom);
        buttonPanel.add(dateTo);

        JButton search = new JButton("Search");
        buttonPanel.add(search);

        JButton sortButton = new JButton("Sort");
        buttonPanel.add(sortButton);

        JPanel tablePanel = new JPanel(new BorderLayout());

        JTable table = new JTable();

        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Name", "Quantity of sold product", "Num of NO sold product"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);

        for (CategorySpecSearch category : categoryList) {
            model.addRow(new Object[]{category.id, category.name, category.numSP, category.numNotSP});
        }

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(buttonPanel, BorderLayout.PAGE_START);
        frame.add(tablePanel, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);

        AtomicBoolean sortOrder = new AtomicBoolean(true);

        sortButton.addActionListener(e -> {

            if (sortOrder.get()) {
                Collections.sort(categoryList, (c1, c2) -> c1.name.compareToIgnoreCase(c2.name));
                sortButton.setText("Sort (Z-A)");
                sortOrder.set(false);
            } else {
                Collections.sort(categoryList, (c1, c2) -> c2.name.compareToIgnoreCase(c1.name));
                sortButton.setText("Sort (A-Z)");
                sortOrder.set(true);
            }

            model.setRowCount(0);
            for (CategorySpecSearch category : categoryList) {
                model.addRow(new Object[]{category.id, category.name, category.numSP, category.numNotSP});
            }
        });


        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dateFrom.getDate().getTime() > dateTo.getDate().getTime()) {
                    JOptionPane.showMessageDialog(null, "Please chose the correct date", "Eror", JOptionPane.ERROR_MESSAGE);
                } else {
                    categoryList = createCommonList(dateFrom.getDate(), dateTo.getDate());
                }
                if (sortOrder.get()) {
                    Collections.sort(categoryList, (c1, c2) -> c1.name.compareToIgnoreCase(c2.name));
                } else {
                    Collections.sort(categoryList, (c1, c2) -> c2.name.compareToIgnoreCase(c1.name));
                }
                model.setRowCount(0);
                for (CategorySpecSearch category : categoryList) {
                    model.addRow(new Object[]{category.id, category.name, category.numSP, category.numNotSP});
                }
            }
        });
    }

    private static class CategorySpecSearch {
        int id;
        String name;
        int numSP;
        int numNotSP;

        public CategorySpecSearch(int id, String name, int numSP, int numNotSP) {
            this.id = id;
            this.name = name;
            this.numSP = numSP;
            this.numNotSP = numNotSP;
        }

        @Override
        public String toString() {
            return "CategorySpecSearch{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", numSP=" + numSP +
                    ", numNotSP=" + numNotSP +
                    '}';
        }
    }


    private static List<CategorySpecSearch> createCommonList(Date from, Date to) {
        List<String[]> notSp = Category.countNumOfProductsThatHasNotBeenSold();
        List<String[]> soldSP = Category.countSoldProductInGivenCategoryHavingDate(new java.sql.Date(from.getTime()), new java.sql.Date(to.getTime()));
        ArrayList<CategorySpecSearch> answ = new ArrayList<>();
        for (int i = 0; i < notSp.size(); i++) {
            answ.add(new CategorySpecSearch(Integer.parseInt(notSp.get(i)[0]), soldSP.get(i)[1], Integer.parseInt(soldSP.get(i)[2]), Integer.parseInt(notSp.get(i)[1])));
        }
        return answ;
    }

}

