package info_menu_common;

import bd_connection.Store_Product;
import create_forms.CreateProductInStoreForm;
import entity.Category;
import entity.Employee;
import entity.Product;
import entity.ProductInStore;
import info_menu_cashier.StoreProductCashier;
import info_menu_manager.CustomerTableManager;
import info_menu_manager.StoreProductManager;
import items_forms.ProductInStoreActionForm;
import menu.MainMenuCashier;
import menu.MainMenuManager;
import menu.Report;


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

    public static List<ProductInStore> getStore_productListList() {
        return store_productListList;
    }

    public static void display(JFrame frame, Employee role) {
        store_productListList = getAllProductsInStoreSorted(true);

        JToolBar buttonPanel = new JToolBar();

        JButton homeButton = new JButton("Home");
        buttonPanel.add(homeButton);

        homeButton.addActionListener(s -> {
            frame.getContentPane().removeAll();
            frame.dispose();
            if (role.getRole().toString().equals("MANAGER"))
                MainMenuManager.display(frame, role);
            else
                MainMenuCashier.display(frame, role);
            // Repaint the frame
            frame.revalidate();
            frame.repaint();
        });

        JComboBox<String> saleComboBox = new JComboBox<>(new String[]{"All types", "Only on sale", "Only on regular price"});
        buttonPanel.add(saleComboBox);

        JComboBox<String> sortComboBox = new JComboBox<>(new String[]{"Using name", "Using quantity num"});
        buttonPanel.add(sortComboBox);

        JButton sortButton = new JButton("Sort");
        buttonPanel.add(sortButton);

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


        JToolBar managerTools = new JToolBar();
        JButton searchUPC = new JButton("Search");
        managerTools.add(searchUPC);

        frame.add(buttonPanel, BorderLayout.PAGE_START);

        frame.add(tablePanel, BorderLayout.CENTER);



        frame.setSize(500, 500);
        frame.setVisible(true);
        AtomicBoolean sortAlph = new AtomicBoolean(true);

        sortButton.addActionListener(e -> {
            if (sortComboBox.getSelectedItem().equals("Using name")) {
                if (!sortAlph.get()) {
                    Collections.sort(store_productListList, (c1, c2) -> c1.getProduct().getName().compareToIgnoreCase(c2.getProduct().getName()));
                    sortButton.setText("Sort (Z-A)");
                    // sortOrder.set(0);
                    sortAlph.set(true);
                } else {
                    Collections.sort(store_productListList, (c1, c2) -> c2.getProduct().getName().compareToIgnoreCase(c1.getProduct().getName()));
                    sortButton.setText("Sort (A-Z)");
                    sortAlph.set(false);
                }
            } else {
                if (!sortAlph.get() /*sortOrder.get() == 1*/) {
                    Collections.sort(store_productListList, (c1, c2) -> Integer.compare(c1.getAmount(), c2.getAmount()));
                    sortButton.setText("Sort (Z-A)");
                    sortAlph.set(true);
                } else {
                    Collections.sort(store_productListList, (c1, c2) -> Integer.compare(c2.getAmount(), c1.getAmount()));
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
                if (selected.equals("All types")) {
                    store_productListList = getAllProductsInStoreSorted(sortAlph.get());

                    model.setRowCount(0);
                    for (ProductInStore sp : store_productListList) {
                        model.addRow(new Object[]{sp.getUPC(), sp.getProduct().getName(), sp.getProduct().getCategory().getName(), sp.getPrice(), sp.getAmount(), sp.isPromotional()});
                    }

                } else if (selected.equals("Only on sale")) {
                    try {
                        store_productListList = getAllProductsInStoreSaleSorted(sortAlph.get(), sortComboBox.getActionCommand().equals("Using name") ? "product_name" : "products_number", true);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    model.setRowCount(0);
                    for (ProductInStore sp : store_productListList) {
                        model.addRow(new Object[]{sp.getUPC(), sp.getProduct().getName(), sp.getProduct().getCategory().getName(), sp.getPrice(), sp.getAmount(), sp.isPromotional()});
                    }

                } else {
                    try {
                        store_productListList = getAllProductsInStoreSaleSorted(sortAlph.get(), sortComboBox.getActionCommand().equals("Using name") ? "product_name" : "products_number", false);
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
                    if (!sortAlph.get()) {
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

        searchUPC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (role.toString().equals("MANAGER")) {
                    frame.getContentPane().removeAll();
                    StoreProductManager.display(frame, role);
                    // Repaint the frame
                    frame.revalidate();
                    frame.repaint();
                } else {
                    frame.getContentPane().removeAll();
                    StoreProductCashier.display(frame, role);
                    // Repaint the frame
                    frame.revalidate();
                    frame.repaint();
                }
            }
        });



        JButton add = new JButton("Add");
        JButton print = new JButton("Print");


        if(role.getRole().toString().equals("MANAGER")){
            managerTools.add(add);
            managerTools.add(print);
        }
        frame.add(managerTools, BorderLayout.PAGE_END);
        add.addActionListener( e -> {
                    frame.setEnabled(false);
            CreateProductInStoreForm createProductInStoreForm = new CreateProductInStoreForm(model,frame);
                }
        );

        print.addActionListener( e -> {
            Report r = new Report(table);
                }
        );

        table.getSelectionModel().addListSelectionListener(e -> {
            if (role.getRole().toString().equals("MANAGER")) {
                if (!e.getValueIsAdjusting()) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        String storeId = (String) model.getValueAt(row, 0);
                        frame.setEnabled(false);
                        ProductInStoreActionForm productInStoreActionForm = new ProductInStoreActionForm(Store_Product.findProductInStoreById(storeId),model,frame);
                    }
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
        // StoreProductTable.display();
    }


}
