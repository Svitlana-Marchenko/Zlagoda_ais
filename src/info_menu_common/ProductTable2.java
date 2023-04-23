package info_menu_common;

import create_forms.CreateProductForm;
import entity.Category;
import entity.Employee;
import entity.Product;
import items_forms.ProductActionForm;
import menu.MainMenuCashier;
import menu.MainMenuManager;
import menu.Report;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static bd_connection.Category.*;
import static bd_connection.Product.*;

public class ProductTable2 {
    static List<Product> product_List;

    public static List<Product> getProduct_List() {
        return product_List;
    }

    public static void display(JFrame frame, Employee role) {

        product_List = getAllProductsSorted(true);

        String tetxForJText = "Enter product name (optional)";

        JToolBar buttonPanel = new JToolBar();
        JButton homeButton = new JButton("Home");
        buttonPanel.add(homeButton);

        homeButton.addActionListener( s ->{
            frame.getContentPane().removeAll();
            frame.dispose();
            if(role.getRole().toString().equals("MANAGER"))
            MainMenuManager.display(frame, role);
            else
                MainMenuCashier.display(frame, role);
            // Repaint the frame
            frame.revalidate();
            frame.repaint();
        });

        JComboBox<String> positionComboBox = new JComboBox<>(getAllCategoryList());
        buttonPanel.add(positionComboBox);

        JTextField nameField = new JTextField(tetxForJText);
        nameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (nameField.getText().equals(tetxForJText)) {
                    nameField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (nameField.getText().isEmpty()) {
                    nameField.setText(tetxForJText);
                }
            }
        });

        buttonPanel.add(nameField);

        JButton searchButton = new JButton("Search");
        buttonPanel.add(searchButton);

        JButton sortButton = new JButton("Sort");
        buttonPanel.add(sortButton);

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

        searchButton.addActionListener(e -> {

            if(positionComboBox.getSelectedItem().toString().equals("All")){

                //no name, no category
                if(nameField.getText().equals(tetxForJText)){

                        product_List = getAllProductsSorted(sortAlph.get());

                }
                //with name, no category
                else{
                    try {
                        product_List = getAllProductsByName(nameField.getText());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }

            }
            else{
                //no name, with category
                if(nameField.getText().equals(tetxForJText)){

                    product_List = getAllProductsInCategorySorted(sortAlph.get(), getCategoryById(Integer.parseInt(positionComboBox.getSelectedItem().toString().split(" ")[0])));

                }
                //with name, with category
                else{
                    try {
                        product_List = getAllProductsByNameAndCategory(nameField.getText(), getCategoryById(Integer.parseInt(positionComboBox.getSelectedItem().toString().split(" ")[0])));
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }


            if (sortAlph.get()) {
                Collections.sort(product_List, (c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));
            } else {
                Collections.sort(product_List, (c1, c2) -> c2.getName().compareToIgnoreCase(c1.getName()));
            }
            model.setRowCount(0);
            for (Product sp : product_List) {
                model.addRow(new Object[]{sp.getId(), sp.getName(), sp.getCategory().getName(), sp.getCharacteristics()});
            }
        });

        JToolBar managerTools = new JToolBar();
        JButton add = new JButton("Add");
        JButton print = new JButton("Print");

        managerTools.add(add);
        managerTools.add(print);
        if(role.getRole().toString().equals("MANAGER")){
            frame.add(managerTools, BorderLayout.PAGE_END);
        }

        add.addActionListener( e -> {
            frame.setEnabled(false);
            CreateProductForm createProductForm = new CreateProductForm(model,frame);
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
                        int prId = (int) model.getValueAt(row, 0);
                        frame.setEnabled(false);
                        ProductActionForm productActionForm = new ProductActionForm(bd_connection.Product.findProductById(prId),model,frame);
                    }
                }
            }
        });
    }

    private static String[] getAllCategoryList(){
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

        //product_List = list;
      //  ProductTable2.display();
    }


}


