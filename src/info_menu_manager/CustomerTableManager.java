package info_menu_manager;

import entity.CustomerCard;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bd_connection.Customer_Card.*;

public class CustomerTableManager {
    static List<CustomerCard> customerList;

    static {
        try {
            customerList = getAllCustomersSorted(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CustomerTableManager() throws SQLException {
    }

    public static void display() throws SQLException {

        String tetxForJText = "Enter customer discount (optional)";

        JFrame frame = new JFrame("CustomerCard Table");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JToolBar buttonPanel = new JToolBar();

        JButton homeButton = new JButton("Home");
        buttonPanel.add(homeButton);



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

        DefaultTableModel model = new DefaultTableModel(new Object[]{"Card num", "Surname", "Name", "Patronymic", "Phone", "Discount"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);

        for (CustomerCard customer : customerList) {
            model.addRow(new Object[]{customer.getNumber(), customer.getSurname(), customer.getName(), (customer.getPatronymic()==null?"":customer.getPatronymic()), customer.getPhoneNumber(), customer.getPercent()});
        }

        JScrollPane scrollPane = new JScrollPane(table);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.setPreferredSize(new Dimension(500, 500));

        frame.add(buttonPanel, BorderLayout.PAGE_START);

        frame.add(tablePanel, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);

        AtomicBoolean sortAlph = new AtomicBoolean(true);

        sortButton.addActionListener(e -> {

            if (!sortAlph.get()) {
                Collections.sort(customerList, (c1, c2) -> (c1.getSurname()+c1.getName()).compareToIgnoreCase(c2.getSurname()+c2.getName()));
                sortButton.setText("Sort (Z-A)");
                sortAlph.set(true);
            } else {
                Collections.sort(customerList, (c1, c2) -> (c2.getSurname()+c2.getName()).compareToIgnoreCase(c1.getSurname()+c1.getName()));
                sortButton.setText("Sort (A-Z)");
                sortAlph.set(false);

            }

            model.setRowCount(0);

            for (CustomerCard customer : customerList) {
                model.addRow(new Object[]{customer.getNumber(), customer.getSurname(), customer.getName(), (customer.getPatronymic()==null?"":customer.getPatronymic()), customer.getPhoneNumber(), customer.getPercent()});
            }
        });

        searchButton.addActionListener(e -> {

            //no discount
            if(nameField.getText().equals(tetxForJText)){
                try {
                    customerList = getAllCustomersSorted(sortAlph.get());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            //with discount
            else{
                if(checkNum(nameField.getText()))
                customerList = findAllCustomersSortedBySurnameWithPercent(Integer.parseInt(nameField.getText()));
                else{
                    JOptionPane.showMessageDialog(null, "Please, enter an int num or erase everything", "Error", JOptionPane.ERROR_MESSAGE);
                    nameField.setText(tetxForJText);
                    try {
                        customerList = getAllCustomersSorted(sortAlph.get());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }





            if (sortAlph.get()) {
                Collections.sort(customerList, (c1, c2) -> (c1.getSurname()+c1.getName()).compareToIgnoreCase(c2.getSurname()+c2.getName()));

            } else {
                Collections.sort(customerList, (c1, c2) -> (c2.getSurname()+c2.getName()).compareToIgnoreCase(c1.getSurname()+c1.getName()));
            }

            model.setRowCount(0);

            for (CustomerCard customer : customerList) {
                model.addRow(new Object[]{customer.getNumber(), customer.getSurname(), customer.getName(), (customer.getPatronymic()==null?"":customer.getPatronymic()), customer.getPhoneNumber(), customer.getPercent()});
            }
        });
    }

    public static void main(String[] args) throws SQLException {
        List<CustomerCard> list = new ArrayList<>();
        list.add(new CustomerCard("", "aa", "cds", "", "", "", "", "", 20));
        list.add(new CustomerCard("", "aa", "cxz", "", "", "", "", "", 20));
        list.add(new CustomerCard("", "qa", "dc", "", "", "", "", "", 20));
        list.add(new CustomerCard("", "wa", "", "", "", "", "", "", 20));
        CustomerTableManager.display();
    }



    private static boolean checkNum(String text){

// create a pattern to match integers
        Pattern pattern = Pattern.compile("^\\d+$");

// create a matcher for the text
        Matcher matcher = pattern.matcher(text);

// check if the text matches the pattern
       return matcher.matches();

    }

}




/*
package menu;


import entity.CustomerCard;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CustomerTableManager {

    public static void display(List<CustomerCard> customerList) {

        JFrame frame = new JFrame("CustomerCard Table");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JToolBar buttonPanel = new JToolBar();

        JButton homeButton = new JButton("Home");
        buttonPanel.add(homeButton);


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

        JButton sortButton = new JButton("Sort");
        buttonPanel.add(sortButton);


       // JLabel categoriesLabel = new JLabel("Customers");
        //buttonPanel.add(categoriesLabel);

        JPanel label = new JPanel();
        JLabel name = new JLabel("Customers in store");
        label.add(name);


        JPanel tablePanel = new JPanel(new BorderLayout());

        JTable table = new JTable();

        DefaultTableModel model = new DefaultTableModel(new Object[]{"Card num", "Surname", "Name", "Patronymic", "Phone"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // make the cells non-editable
            }
        };
        table.setModel(model);

        for (CustomerCard customer : customerList) {
            model.addRow(new Object[]{customer.getNumber(), customer.getSurname(), customer.getName(), (customer.getPatronymic()==null?"":customer.getPatronymic()), customer.getPhoneNumber()});
        }

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(buttonPanel, BorderLayout.PAGE_START);

        frame.add(label);
        frame.add(tablePanel, BorderLayout.CENTER);

        tablePanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = tablePanel.getWidth();
                int height = tablePanel.getHeight();

                scrollPane.setPreferredSize(new Dimension(width, height));
                scrollPane.revalidate();
            }
        });

        frame.setSize(500, 500);
        frame.setVisible(true);

        AtomicBoolean sortAlph = new AtomicBoolean(true);

        sortButton.addActionListener(e -> {

            if (!sortAlph.get()) {
                Collections.sort(customerList, (c1, c2) -> c1.getSurname().compareToIgnoreCase(c2.getSurname()));
                sortButton.setText("Sort (Z-A)");
                sortAlph.set(true);
            } else {
                Collections.sort(customerList, (c1, c2) -> c2.getSurname().compareToIgnoreCase(c1.getSurname()));
                sortButton.setText("Sort (A-Z)");
                sortAlph.set(false);

            }

            model.setRowCount(0);
            for (CustomerCard customer : customerList) {
                model.addRow(new Object[]{customer.getNumber(), customer.getSurname(), customer.getName(), (customer.getPatronymic()==null?"":customer.getPatronymic()), customer.getPhoneNumber()});
            }
        });

        textField.addActionListener(e -> {

            if(!textField.getText().matches("^[0-9]*$"))
            JOptionPane.showMessageDialog(null, "Please, enter an int num or erase everything", "Error", JOptionPane.ERROR_MESSAGE);

            System.out.println("changed");

        });
    }

    public static void main(String[] args) {
        List<CustomerCard> list = new ArrayList<>();
        list.add(new CustomerCard("", "aa", "", "", "", "", "", "", 20));
        list.add(new CustomerCard("", "ab", "", "", "", "", "", "", 20));
        list.add(new CustomerCard("", "qa", "", "", "", "", "", "", 20));
        list.add(new CustomerCard("", "wa", "", "", "", "", "", "", 20));
        CustomerTableManager.display(list);
    }

}


 */


