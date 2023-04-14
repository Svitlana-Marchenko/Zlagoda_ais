package menu;

import entity.Category;
import entity.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static bd_connection.Category.getAllCategories;

public class ProductTable {
    static List<Product> product_List;
    public static void display() throws SQLException {
        JFrame frame = new JFrame("Product Table");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JToolBar buttonPanel = new JToolBar();
        JButton homeButton = new JButton("Home");
        buttonPanel.add(homeButton);

        JComboBox<String> positionComboBox = new JComboBox<>(getAllCategoryList());
        buttonPanel.add(positionComboBox);

        JButton sortButton = new JButton("Sort");
        buttonPanel.add(sortButton);

        JLabel categoriesLabel = new JLabel("Products");
        buttonPanel.add(categoriesLabel);

        JPanel tablePanel = new JPanel(new BorderLayout());

        JTable table = new JTable();

        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Name", "Category", "Characteristic"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);

        for (Product sp : product_List) {
            model.addRow(new Object[]{sp.getId(), sp.getName(), sp.getCategory().getName(), sp.getCharacteristics()});
        }

        JScrollPane scrollPane = new JScrollPane(table);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(buttonPanel, BorderLayout.PAGE_START);

        frame.add(tablePanel, BorderLayout.CENTER);

        frame.setSize(500, 500);
        frame.setVisible(true);

        AtomicBoolean sortAlph = new AtomicBoolean(true);

        sortButton.addActionListener(e -> {

            if (!sortAlph.get()) {
                Collections.sort(product_List, (c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));
                sortButton.setText("Sort (Z-A)");
                sortAlph.set(true);
            } else {
                Collections.sort(product_List, (c1, c2) -> c2.getName().compareToIgnoreCase(c1.getName()));
                sortButton.setText("Sort (A-Z)");
                sortAlph.set(false);

            }


            model.setRowCount(0);
            for (Product sp : product_List) {
                model.addRow(new Object[]{sp.getId(), sp.getName(), sp.getCategory().getName(), sp.getCharacteristics()});
            }
        });

    }

    private static String[] getAllCategoryList() throws  SQLException {
        List<Category> categories = getAllCategories();
        String[] labels = new String[categories.size()+1];
        labels[0] = "All";
        for(int i=1; i< labels.length; i++){
            labels[i] = categories.get(i-1).getId()+" "+ categories.get(i-1).getName();
        }
        return labels;

    }


    public static void main(String[] args) throws SQLException {
        List<Product> list = new ArrayList<>();

        Category a = new Category(1, "Category A");
        Category b = new Category(2, "Category C");
        Category c = new Category(3, "Category B");

        Product aq = new Product(1, "avc", a, "jk", "ss");
        Product aw = new Product(2, "awe", b, "jk", "ss");
        Product ae = new Product(3, "asf", c, "jk", "ss");
        Product ar = new Product(4, "bvc", a, "jk", "ss");
        Product at = new Product(5, "ytr", b, "jk", "ss");
        Product ay = new Product(6, "zsw", c, "jk", "ss");
        Product au = new Product(7, "plo", a, "jk", "ss");

        list.add(aq);
        list.add(aw);
        list.add(ae);
        list.add(ar);
        list.add(at);
        list.add(ay);
        list.add(au);

        product_List = list;
        ProductTable.display();
    }


}

