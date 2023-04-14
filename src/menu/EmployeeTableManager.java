package menu;

import entity.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class EmployeeTableManager {
    static List<Employee> employee_List;



        public static void display() {

            JFrame frame = new JFrame("Employee Table");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JToolBar buttonPanel = new JToolBar();
            JButton homeButton = new JButton("Home");
            buttonPanel.add(homeButton);

            JComboBox<String> positionComboBox = new JComboBox<>(new String[]{"All types", "Only cashier", "Only manager"});
            buttonPanel.add(positionComboBox);

            JButton sortButton = new JButton("Sort");
            buttonPanel.add(sortButton);

            JLabel categoriesLabel = new JLabel("Employee");
            buttonPanel.add(categoriesLabel);

            JPanel tablePanel = new JPanel(new BorderLayout());

            JTable table = new JTable();

            DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Surname", "Name", "Patronymic", "Position", "Phone", "Salary"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            table.setModel(model);

            for (Employee sp : employee_List) {
                model.addRow(new Object[]{sp.getId(), sp.getSurname(), sp.getName(), (sp.getPatronymic() == null ? "" : sp.getPatronymic()), sp.getRole().toString(), sp.getPhoneNumber(), sp.getSalary()});
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
                    model.addRow(new Object[]{sp.getId(), sp.getSurname(), sp.getName(), (sp.getPatronymic() == null ? "" : sp.getPatronymic()), sp.getRole().toString(), sp.getPhoneNumber(), sp.getSalary()});
                }
            });

        }

    public static void main(String[] args) {
        List<Employee> list = new ArrayList<>();
        list.add(new Employee("1", "A", "A", "A", null, Employee.Role.CASHIER, BigDecimal.valueOf(100), Date.valueOf("2000-02-02"), Date.valueOf("2020-02-02"), "", "", "", ""));
        list.add(new Employee("2", "B", "", "", "B", Employee.Role.MANAGER, BigDecimal.valueOf(100), Date.valueOf("2000-02-02"), Date.valueOf("2020-02-02"), "", "", "", ""));
        list.add(new Employee("3", "C", "", "", null, Employee.Role.CASHIER, BigDecimal.valueOf(100), Date.valueOf("2000-02-02"), Date.valueOf("2020-02-02"), "", "", "", ""));
        list.add(new Employee("4", "D", "", "", "D", Employee.Role.MANAGER, BigDecimal.valueOf(100), Date.valueOf("2000-02-02"), Date.valueOf("2020-02-02"), "", "", "", ""));
        list.add(new Employee("5", "E", "", "", null, Employee.Role.CASHIER, BigDecimal.valueOf(100), Date.valueOf("2000-02-02"), Date.valueOf("2020-02-02"), "", "", "", ""));

        employee_List = list;
        EmployeeTableManager.display();
    }


}
