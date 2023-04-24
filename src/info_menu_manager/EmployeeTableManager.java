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



public class EmployeeTableManager {
    static List<Employee> employee_List;

    public static List<Employee> getEmployee_List() {
        return employee_List;
    }


    public static void display(JFrame frame, Employee role) {

           employee_List = findAllEmployee();

            String textForField = "Enter employee surname (optional)";


            JToolBar buttonPanel = new JToolBar();
            JButton homeButton = new JButton("Home");
            buttonPanel.add(homeButton);

            homeButton.addActionListener( s ->{
                frame.getContentPane().removeAll();
                frame.dispose();
                MainMenuManager.display(frame, role);
                // Repaint the frame
                frame.revalidate();
                frame.repaint();
            });

            JComboBox<String> positionComboBox = new JComboBox<>(new String[]{"All types", "Only cashier", "Only manager"});
            buttonPanel.add(positionComboBox);


            JTextField nameField = new JTextField(textForField);
            nameField.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (nameField.getText().equals(textForField)) {
                        nameField.setText("");
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (nameField.getText().isEmpty()) {
                        nameField.setText(textForField);
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


            searchButton.addActionListener(e -> {

                if(positionComboBox.getSelectedItem().toString().equals("All types")){

                    //no name, no category
                    if(nameField.getText().equals(textForField)){
                        employee_List = findAllEmployee();
                    }
                    //with name, no category
                    else{
                        employee_List = findAllEmployeeBySurname(nameField.getText());
                    }
                }
                else{
                    //no name, with category+
                    if(nameField.getText().equals(textForField)){

                        try {
                            employee_List = getAllSpecial(((positionComboBox.getSelectedItem().equals("Only cashier"))?"CASHIER":"MANAGER"));
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }

                    }
                    //with name, with category
                    else{
                        employee_List = findAllEmployeeBySurnameAndRole(nameField.getText(), ((positionComboBox.getSelectedItem().equals("Only cashier"))?"CASHIER":"MANAGER"));
                    }
                }

                if (sortAlph.get()) {
                    Collections.sort(employee_List, (c1, c2) -> c1.getSurname().compareToIgnoreCase(c2.getSurname()));

                } else {
                    Collections.sort(employee_List, (c1, c2) -> c2.getSurname().compareToIgnoreCase(c1.getSurname()));

                }

                model.setRowCount(0);
                for (Employee sp : employee_List) {
                    model.addRow(new Object[]{sp.getId(), sp.getSurname(), sp.getName(), (sp.getPatronymic() == null ? "" : sp.getPatronymic()), sp.getRole().toString(), sp.getPhoneNumber(), sp.getCity()+", "+sp.getStreet()+", "+sp.getZipCode() ,sp.getSalary()});
                }
            });


            table.getSelectionModel().addListSelectionListener(e -> {
                if(role.getRole().toString().equals("MANAGER")) {
                if (!e.getValueIsAdjusting()) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        String employeeId = (String) model.getValueAt(row, 0);
                        frame.setEnabled(false);
                        EmployeeActionForm employeeActionForm = new EmployeeActionForm(employee_List.get(row),model,frame);

                    }
                }
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
                CreateEmployeeForm createEmployeeForm = new CreateEmployeeForm(model,frame);
                    }
            );

            print.addActionListener( e -> {
                        //TODO print panel
                Report r = new Report(table);
                    }
            );

        }


}
