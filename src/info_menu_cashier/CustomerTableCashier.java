package info_menu_cashier;

import entity.CustomerCard;
import entity.Employee;
import menu.MainMenuCashier;
import menu.MainMenuManager;

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

import static bd_connection.Customer_Card.getAllCustomersSorted;
import static bd_connection.Customer_Card.getCustomersBySurname;

public class CustomerTableCashier {
    static List<CustomerCard> customerList;

    static {
        try {
            customerList = getAllCustomersSorted(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CustomerTableCashier() throws SQLException {
    }

    public static void display(JFrame frame, Employee role)  {

        String tetxForJText = "Enter customer surname (optional)";

        JToolBar buttonPanel = new JToolBar();

        JButton homeButton = new JButton("Home");
        buttonPanel.add(homeButton);

        homeButton.addActionListener( s ->{
            frame.getContentPane().removeAll();

                MainMenuCashier.display(frame, role);
            // Repaint the frame
            frame.revalidate();
            frame.repaint();
        });

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

        DefaultTableModel model = new DefaultTableModel(new Object[]{"Card num", "Surname", "Name", "Patronymic", "Phone"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);

        for (CustomerCard customer : customerList) {
            model.addRow(new Object[]{customer.getNumber(), customer.getSurname(), customer.getName(), (customer.getPatronymic()==null?"":customer.getPatronymic()), customer.getPhoneNumber()});
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
                model.addRow(new Object[]{customer.getNumber(), customer.getSurname(), customer.getName(), (customer.getPatronymic()==null?"":customer.getPatronymic()), customer.getPhoneNumber()});
            }
        });

        searchButton.addActionListener(e -> {

            //no name
            if(nameField.getText().equals(tetxForJText)){
                try {
                   customerList = getAllCustomersSorted(sortAlph.get());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            //with name
            else{
                try {
                    customerList = getCustomersBySurname(nameField.getText());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            if (sortAlph.get()) {
                Collections.sort(customerList, (c1, c2) -> (c1.getSurname()+c1.getName()).compareToIgnoreCase(c2.getSurname()+c2.getName()));

            } else {
                Collections.sort(customerList, (c1, c2) -> (c2.getSurname()+c2.getName()).compareToIgnoreCase(c1.getSurname()+c1.getName()));
            }

            model.setRowCount(0);

            for (CustomerCard customer : customerList) {
                model.addRow(new Object[]{customer.getNumber(), customer.getSurname(), customer.getName(), (customer.getPatronymic()==null?"":customer.getPatronymic()), customer.getPhoneNumber()});
            }
        });

        JToolBar managerTools = new JToolBar();
        JButton add = new JButton("Add");

        managerTools.add(add);
        if(role.getRole().toString().equals("CASHIER")){
            frame.add(managerTools, BorderLayout.PAGE_END);
        }

        add.addActionListener( e -> {
                    //TODO add panel
                }
        );



        table.getSelectionModel().addListSelectionListener(e -> {

                if (!e.getValueIsAdjusting()) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        String custId = (String) model.getValueAt(row, 0);
                        System.out.println("You have clicked on " + custId + " cust");
                        // TODO add customer editor
                    }

            }
        });
    }

    public static void main(String[] args) throws SQLException {
        List<CustomerCard> list = new ArrayList<>();
        list.add(new CustomerCard("", "aa", "cds", "", "", "", "", "", 20));
        list.add(new CustomerCard("", "aa", "cxz", "", "", "", "", "", 20));
        list.add(new CustomerCard("", "qa", "dc", "", "", "", "", "", 20));
        list.add(new CustomerCard("", "wa", "", "", "", "", "", "", 20));
        //CustomerTableCashier.display();
    }

}

