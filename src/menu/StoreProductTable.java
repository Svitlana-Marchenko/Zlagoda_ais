package menu;

import entity.Category;
import entity.Product;
import entity.ProductInStore;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static bd_connection.Store_Product.getAllProductsInStoreSaleSorted;
import static bd_connection.Store_Product.getAllProductsInStoreSorted;

public class StoreProductTable {

    static List<ProductInStore> store_productListList;
    public static void display() {
        // Create a JFrame
        JFrame frame = new JFrame("StoreProduct Table");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a JPanel for the buttons
        JToolBar buttonPanel = new JToolBar();

        // Create a "Home" button and add it to the button panel
        JButton homeButton = new JButton("Home");
        buttonPanel.add(homeButton);

        JComboBox<String> saleComboBox = new JComboBox<>(new String[]{"All types", "Only on sale", "Only on regular price"});
        buttonPanel.add(saleComboBox);

        JComboBox<String> sortComboBox = new JComboBox<>(new String[]{"Using name", "Using quantity num"});
        buttonPanel.add(sortComboBox);

        JButton sortButton = new JButton("Sort");
        buttonPanel.add(sortButton);

        JLabel categoriesLabel = new JLabel("Categories");
        buttonPanel.add(categoriesLabel);

        JPanel tablePanel = new JPanel(new BorderLayout());

        JTable table = new JTable();

        DefaultTableModel model = new DefaultTableModel(new Object[]{"UPC", "Name", "Category", "Price", "Amount", "Promotional"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // make the cells non-editable
            }
        };
        table.setModel(model);

        for (ProductInStore sp : store_productListList) {
            model.addRow(new Object[]{sp.getUPC(), sp.getProduct().getName(), sp.getProduct().getCategory().getName(), sp.getPrice(), sp.getAmount(), sp.isPromotional()});
        }

        JScrollPane scrollPane = new JScrollPane(table);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(buttonPanel, BorderLayout.PAGE_START);

        frame.add(tablePanel, BorderLayout.CENTER);

        frame.setSize(500, 500);
        frame.setVisible(true);
AtomicBoolean sortAlph = new AtomicBoolean(true);

sortButton.addActionListener(e -> {
            if(sortComboBox.getSelectedItem().equals("Using name")){
                if (!sortAlph.get() /*sortOrder.get() == 1*/) {
                    Collections.sort(store_productListList, (c1, c2) -> c1.getProduct().getName().compareToIgnoreCase(c2.getProduct().getName()));
                    sortButton.setText("Sort (Z-A)");
                    // sortOrder.set(0);
                    sortAlph.set(true);
                } else {
                    Collections.sort(store_productListList, (c1, c2) -> c2.getProduct().getName().compareToIgnoreCase(c1.getProduct().getName()));
                    sortButton.setText("Sort (A-Z)");
                    sortAlph.set(false);
                }
            }
            else{
                if (!sortAlph.get() /*sortOrder.get() == 1*/) {
                    Collections.sort(store_productListList, (c1, c2) -> Integer.compare(c1.getAmount(),c2.getAmount()));
                    sortButton.setText("Sort (Z-A)");
                    sortAlph.set(true);
                } else {
                    Collections.sort(store_productListList, (c1, c2) -> Integer.compare(c2.getAmount(),c1.getAmount()));
                    sortButton.setText("Sort (A-Z)");
                    sortAlph.set(false);
                }
            }
            model.setRowCount(0);
            for (ProductInStore sp : store_productListList) {
                model.addRow(new Object[]{sp.getUPC(), sp.getProduct().getName(), sp.getProduct().getCategory().getName(), sp.getPrice(), sp.getAmount(), sp.isPromotional()});
            }
        });

        saleComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selected = (String) saleComboBox.getSelectedItem();
                if(selected.equals("All types")){
                    try {
                        store_productListList = getAllProductsInStoreSorted(sortAlph.get());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    model.setRowCount(0);
                    for (ProductInStore sp : store_productListList) {
                        model.addRow(new Object[]{sp.getUPC(), sp.getProduct().getName(), sp.getProduct().getCategory().getName(), sp.getPrice(), sp.getAmount(), sp.isPromotional()});
                    }

                }
                else if(selected.equals("Only on sale")){
                    try {
                        store_productListList = getAllProductsInStoreSaleSorted(sortAlph.get(), sortComboBox.getActionCommand().equals("Using name")? "product_name": "products_number" , true);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    model.setRowCount(0);
                    for (ProductInStore sp : store_productListList) {
                        model.addRow(new Object[]{sp.getUPC(), sp.getProduct().getName(), sp.getProduct().getCategory().getName(), sp.getPrice(), sp.getAmount(), sp.isPromotional()});
                    }

                }
                else{
                    try {
                        store_productListList = getAllProductsInStoreSaleSorted(sortAlph.get(), sortComboBox.getActionCommand().equals("Using name")? "product_name": "products_number" , false);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    model.setRowCount(0);
                    for (ProductInStore sp : store_productListList) {
                        model.addRow(new Object[]{sp.getUPC(), sp.getProduct().getName(), sp.getProduct().getCategory().getName(), sp.getPrice(), sp.getAmount(), sp.isPromotional()});
                    }

                }
            }
        });

        sortComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (sortComboBox.getSelectedItem().equals("Using name")) {
                    if (!sortAlph.get() ) {
                        Collections.sort(store_productListList, (c2, c1) -> c1.getProduct().getName().compareToIgnoreCase(c2.getProduct().getName()));

                    } else {
                        Collections.sort(store_productListList, (c2, c1) -> c2.getProduct().getName().compareToIgnoreCase(c1.getProduct().getName()));

                    }
                } else {
                    if (!sortAlph.get()) {
                        Collections.sort(store_productListList, (c2, c1) -> Integer.compare(c1.getAmount(), c2.getAmount()));

                    } else {
                        Collections.sort(store_productListList, (c2, c1) -> Integer.compare(c2.getAmount(), c1.getAmount()));

                    }
                }
                model.setRowCount(0);
                for (ProductInStore sp : store_productListList) {
                    model.addRow(new Object[]{sp.getUPC(), sp.getProduct().getName(), sp.getProduct().getCategory().getName(), sp.getPrice(), sp.getAmount(), sp.isPromotional()});
                }
            }
        });

    }

    public static void main(String[] args) {
        List<ProductInStore> list = new ArrayList<>();
        list.add(new ProductInStore("123456789", "12345", new Product(1, "Banana", new Category(1, "Fruits"), "Africa", "good fruit"), BigDecimal.valueOf(30), 30, false));
        list.add(new ProductInStore("123456789", "12345", new Product(1, "Mango", new Category(1, "Fruits"), "Africa", "good fruit"), BigDecimal.valueOf(50), 40, false));
        list.add(new ProductInStore("123456789", "12345", new Product(1, "Apple", new Category(1, "Fruits"), "Africa", "good fruit"), BigDecimal.valueOf(35), 35, true));
        list.add(new ProductInStore("123456789", "12345", new Product(1, "Orange", new Category(1, "Fruits"), "Africa", "good fruit"), BigDecimal.valueOf(30), 10, true));
        list.add(new ProductInStore("123456789", "12345", new Product(1, "Aloe", new Category(1, "Fruits"), "Africa", "good fruit"), BigDecimal.valueOf(30), 60, true));
        list.add(new ProductInStore("123456789", "12345", new Product(1, "Cherry", new Category(1, "Fruits"), "Africa", "good fruit"), BigDecimal.valueOf(30), 32, true));
        list.add(new ProductInStore("123456789", "12345", new Product(1, "Rusberry", new Category(1, "Fruits"), "Africa", "good fruit"), BigDecimal.valueOf(30), 35, true));
        list.add(new ProductInStore("123456789", "12345", new Product(1, "Mint", new Category(1, "Fruits"), "Africa", "good fruit"), BigDecimal.valueOf(30), 78, false));
        list.add(new ProductInStore("123456789", "12345", new Product(1, "Onion", new Category(1, "Fruits"), "Africa", "good fruit"), BigDecimal.valueOf(30), 90, false));
        list.add(new ProductInStore("123456789", "12345", new Product(1, "Strawberry", new Category(1, "Fruits"), "Africa", "good fruit"), BigDecimal.valueOf(30), 23, false));
        list.add(new ProductInStore("123456789", "12345", new Product(1, "Ananas", new Category(1, "Fruits"), "Africa", "good fruit"), BigDecimal.valueOf(30), 34, false));
store_productListList = list;
        StoreProductTable.display();
    }


}
