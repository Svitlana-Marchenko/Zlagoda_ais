package menu;

import entity.CustomerCard;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CustomerTableCashier {

    public static void display(List<CustomerCard> customerList) {

        JFrame frame = new JFrame("CustomerCard Table");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JToolBar buttonPanel = new JToolBar();

        JButton homeButton = new JButton("Home");
        buttonPanel.add(homeButton);

        JButton sortButton = new JButton("Sort");
        buttonPanel.add(sortButton);

        JLabel categoriesLabel = new JLabel("Customers");
        buttonPanel.add(categoriesLabel);

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
    }

    public static void main(String[] args) {
        List<CustomerCard> list = new ArrayList<>();
        list.add(new CustomerCard("", "aa", "cds", "", "", "", "", "", 20));
        list.add(new CustomerCard("", "aa", "cxz", "", "", "", "", "", 20));
        list.add(new CustomerCard("", "qa", "dc", "", "", "", "", "", 20));
        list.add(new CustomerCard("", "wa", "", "", "", "", "", "", 20));
        CustomerTableCashier.display(list);
    }

}

