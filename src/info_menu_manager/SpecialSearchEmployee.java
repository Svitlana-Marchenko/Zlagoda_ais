package info_menu_manager;

import bd_connection.Customer_Card;
import create_forms.CreateEmployeeForm;
import entity.Employee;
import items_forms.CustomerCardActionForm;
import items_forms.EmployeeActionForm;
import menu.MainMenuManager;
import menu.Report;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


import static bd_connection.Employee.*;



public class SpecialSearchEmployee {

    public static void display(JFrame frame, Employee role) {

        List<Employee> employee_List = findAllCashierWhoSellProductFromAllCategories();


        JToolBar buttonPanel = new JToolBar();
        JButton homeButton = new JButton("Home");
        buttonPanel.add(homeButton);

        homeButton.addActionListener( s ->{
            frame.getContentPane().removeAll();
            frame.dispose();
            EmployeeTableManager.display(frame, role);
            frame.revalidate();
            frame.repaint();
        });

        JButton sortButton = new JButton("Sort");
        buttonPanel.add(sortButton);

        JPanel tablePanel = new JPanel(new BorderLayout());

        JTable table = new JTable();

        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Surname", "Name", "Patronymic", "Position", "Phone", "Address" ,"Salary"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);

        for (Employee sp : employee_List) {
            model.addRow(new Object[]{sp.getId(), sp.getSurname(), sp.getName(), (sp.getPatronymic() == null ? "" : sp.getPatronymic()), sp.getRole().toString(), sp.getPhoneNumber(), sp.getCity()+", "+sp.getStreet()+", "+sp.getZipCode() ,sp.getSalary()});
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
                Collections.sort(employee_List, (c1, c2) -> c1.getSurname().compareToIgnoreCase(c2.getSurname()));
                sortButton.setText("Sort (Z-A)");
                sortAlph.set(true);
            } else {
                Collections.sort(employee_List, (c1, c2) -> c2.getSurname().compareToIgnoreCase(c1.getSurname()));
                sortButton.setText("Sort (A-Z)");
                sortAlph.set(false);
            }

            model.setRowCount(0);
            for (Employee sp : employee_List) {
                model.addRow(new Object[]{sp.getId(), sp.getSurname(), sp.getName(), (sp.getPatronymic() == null ? "" : sp.getPatronymic()), sp.getRole().toString(), sp.getPhoneNumber(), sp.getCity()+", "+sp.getStreet()+", "+sp.getZipCode() ,sp.getSalary()});
            }
        });

    }


}

