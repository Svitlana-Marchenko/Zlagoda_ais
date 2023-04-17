package info_menu_manager;

import com.toedter.calendar.JDateChooser;
import entity.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static bd_connection.Check.*;
import static bd_connection.Employee.getAllSpecial;
import static bd_connection.Employee.getEmployee;


public class ReceiptViewer {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    static List<Receipt> receipts;


    public ReceiptViewer() throws SQLException {

        AtomicBoolean sortAlph = new AtomicBoolean(true);
        frame = new JFrame("Receipt Viewer");

        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        JComboBox<String> cashierBox = new JComboBox<>(getCahierStringList());
        toolbar.add(cashierBox);


        toolbar.add(new JLabel("From:"));


        JDateChooser dateFrom = new JDateChooser();
        dateFrom.setDate(new Date());
        toolbar.add(dateFrom);


        toolbar.add(new JLabel("To:"));

        JDateChooser dateTo = new JDateChooser();
        dateFrom.setDate(new Date());
        toolbar.add(dateTo);

        model = new DefaultTableModel(new Object[]{"Receipt ID", "Cashier", "Customer", "Date", "Total", "VAT"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (dateFrom.getDate().getTime() > dateTo.getDate().getTime()) {
                    JOptionPane.showMessageDialog(null, "Please chose the correct date", "Eror", JOptionPane.ERROR_MESSAGE);
                }


                if (cashierBox.getSelectedItem().equals("All")) {
                    try {
                        receipts = getAllReceipt(sortAlph.get(), new java.sql.Date(dateFrom.getDate().getTime()), new java.sql.Date(dateTo.getDate().getTime()));
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    try {

                        receipts = getAllReceiptFromGivenCashier(sortAlph.get(), getEmployee(cashierBox.getSelectedItem().toString().split(" ")[0]), new java.sql.Date(dateFrom.getDate().getTime()), new java.sql.Date(dateTo.getDate().getTime()));
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                model.setRowCount(0);

                for (Receipt receipt : receipts) {
                    model.addRow(new Object[] {receipt.getNumber(), (receipt.getEmployee() == null?"Non authorised":receipt.getEmployee().getId()+" "+receipt.getEmployee().getSurname()+" "+receipt.getEmployee().getName()), (receipt.getCard() == null?"Non authorised":receipt.getCard().getNumber()+" "+receipt.getCard().getSurname()),receipt.getPrintDate(), receipt.getTotalSum(), receipt.getVAT()});
                }

            }
        });
        toolbar.add(searchButton);

        JButton sortButton = new JButton("Sort");
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sortAlph.get()) {
                    Collections.sort(receipts, (c1, c2) -> c1.getPrintDate().compareTo(c2.getPrintDate()));
                    sortButton.setText("Sort (Z-A)");
                    sortAlph.set(false);
                } else {
                    Collections.sort(receipts, (c2, c1) -> c1.getPrintDate().compareTo(c2.getPrintDate()));
                    sortButton.setText("Sort (A-Z)");
                    sortAlph.set(true);

                }
                model.setRowCount(0);
                for (Receipt receipt : receipts) {
                    model.addRow(new Object[] {receipt.getNumber(), (receipt.getEmployee() == null?"Non authorised":receipt.getEmployee().getId()+" "+receipt.getEmployee().getSurname()+" "+receipt.getEmployee().getName()), (receipt.getCard() == null?"Non authorised":receipt.getCard().getNumber()+" "+receipt.getCard().getSurname()),receipt.getPrintDate(), receipt.getTotalSum(), receipt.getVAT()});
                }

            }
        });

        toolbar.add(sortButton);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    String receiptId = (String) model.getValueAt(row, 0);
                    try {
                        displayReceiptProducts(receiptId);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);

        frame.getContentPane().add(toolbar, BorderLayout.PAGE_START);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        for (Receipt receipt : receipts) {
            model.addRow(new Object[] {receipt.getNumber(), (receipt.getEmployee() == null?"Non authorised":receipt.getEmployee().getId()+" "+receipt.getEmployee().getSurname()+" "+receipt.getEmployee().getName()), (receipt.getCard() == null?"Non authorised":receipt.getCard().getNumber()+" "+receipt.getCard().getSurname()),receipt.getPrintDate(), receipt.getTotalSum(), receipt.getVAT()});
        }
    }


    private void displayReceiptProducts(String receiptId) throws Exception {

        Receipt rec = getReceipt(receiptId);
        List<SoldProduct> productL = rec.getProducts();
        JTable table = new JTable();

        DefaultTableModel model = new DefaultTableModel(new Object[]{"UPC", "Name", "Amount", "Price"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);
        model.addRow(new Object[]{"UPC", "Name", "Amount", "Price"});

        for (SoldProduct pr : productL)
            model.addRow(new Object[]{pr.getUPC(), pr.getName(), pr.getAmount(), pr.getPrice()});

        JOptionPane.showMessageDialog(null, table, "Products", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                // create some test data
                List<Receipt> testReceipts = new ArrayList<>();
                testReceipts.add(new Receipt("1", null, null, Timestamp.valueOf("2020-02-20 11:11:11"), BigDecimal.valueOf(100), BigDecimal.valueOf(20), null));
                testReceipts.add(new Receipt("1", null, null, Timestamp.valueOf("2020-02-20 11:11:11"), BigDecimal.valueOf(100), BigDecimal.valueOf(20), null));
                testReceipts.add(new Receipt("1", null, null, Timestamp.valueOf("2020-02-20 11:11:11"), BigDecimal.valueOf(100), BigDecimal.valueOf(20), null));
                testReceipts.add(new Receipt("1", null, null, Timestamp.valueOf("2020-02-20 11:11:11"), BigDecimal.valueOf(100), BigDecimal.valueOf(20), null));


                receipts = testReceipts;
                try {
                    ReceiptViewer viewer = new ReceiptViewer();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    private static String[] getCahierStringList() throws SQLException {
        List<Employee> cashier = getAllSpecial("CASHIER");
        String[] labels = new String[cashier.size() + 1];
        labels[0] = "All";
        for (int i = 1; i < labels.length; i++) {
            labels[i] = cashier.get(i - 1).getId() + " " + cashier.get(i - 1).getSurname() + " " + cashier.get(i - 1).getName() + " " + (cashier.get(i - 1).getPatronymic() == null ? " " : cashier.get(i - 1).getPatronymic());
        }
        return labels;

    }
}
