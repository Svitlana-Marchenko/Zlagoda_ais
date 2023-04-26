package info_menu_manager;

import bd_connection.Customer_Card;
import create_forms.CreateCustomerCardForm;
import entity.CustomerCard;
import entity.Employee;
import items_forms.CustomerCardActionForm;
import menu.MainMenuManager;
import menu.Report;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    public static List<CustomerCard> getCustomerList() {
        return customerList;
    }

    static {
        try {
            customerList = getAllCustomersSorted(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void display(JFrame frame, Employee role)  {

        String tetxForJText = "Enter customer discount (optional)";

       // JFrame frame = new JFrame("CustomerCard Table");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JToolBar buttonPanel = new JToolBar();

        JButton homeButton = new JButton("Home");
        buttonPanel.add(homeButton);

        homeButton.addActionListener( s ->{
            frame.getContentPane().removeAll();
            frame.dispose();
            MainMenuManager.display(frame, role);
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

        JToolBar managerTools = new JToolBar();
        JButton add = new JButton("Add");
        JButton print = new JButton("Print");
        JButton additionalSearch = new JButton("Additional search");

        managerTools.add(add);
        managerTools.add(print);
        managerTools.add(Box.createGlue());
        managerTools.add(additionalSearch);
        if(role.getRole().toString().equals("MANAGER")){
            frame.add(managerTools, BorderLayout.PAGE_END);
        }

        additionalSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                CustomerAdditionalSearch.display(frame, role);
                // Repaint the frame
                frame.revalidate();
                frame.repaint();
            }
        });

        add.addActionListener( e -> {
            frame.setEnabled(false);
            CreateCustomerCardForm createCustomerCardForm=new CreateCustomerCardForm(model,frame);

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
                    String customerId = (String) model.getValueAt(row, 0);
                    frame.setEnabled(false);
                    CustomerCardActionForm customerCardActionForm = new CustomerCardActionForm(Customer_Card.findCustomerCardById(customerId),model,frame,true);
                }
            }
            }
        });
    }


    private static boolean checkNum(String text){

        Pattern pattern = Pattern.compile("^\\d+$");
        Matcher matcher = pattern.matcher(text);

       return matcher.matches();

    }

}

