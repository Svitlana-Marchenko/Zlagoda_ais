package info_menu_manager;

import com.toedter.calendar.JDateChooser;
import entity.*;
import menu.MainMenuManager;
import menu.Report;

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

    private static JTable table;
    private static DefaultTableModel model;
    static List<Receipt> receipts;


    public static void display(JFrame frame, Employee empl){


        AtomicBoolean sortAlph = new AtomicBoolean(true);


        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(true);

        JButton home = new JButton("Home");
        toolbar.add(home);

        JComboBox<String> cashierBox = new JComboBox<>(getCahierStringList());
        toolbar.add(cashierBox);


        toolbar.add(new JLabel("From:"));


        JDateChooser dateFrom = new JDateChooser();
        dateFrom.setDate(new Date());
        toolbar.add(dateFrom);


        toolbar.add(new JLabel("To:"));

        JDateChooser dateTo = new JDateChooser();

        dateTo.setDate(new Date());
        toolbar.add(dateTo);

       // receipts = getAllReceipt(true, new java.sql.Date(dateFrom.getDate().getTime()), new java.sql.Date(dateTo.getDate().getTime()));
        receipts = getAllReceipt(true, new java.sql.Date(dateFrom.getDate().getTime()), new java.sql.Date(dateTo.getDate().getTime()));


        model = new DefaultTableModel(new Object[]{"Receipt ID", "Cashier", "Customer", "Date", "Total", "VAT"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);

        JToolBar statPanel = new JToolBar()/*(new GridLayout(1, 2))*/;
        JLabel statL = new JLabel("Total sum of receipts: ");
        JLabel statT = new JLabel();

            BigDecimal b = (getSumCheck(new java.sql.Date(dateFrom.getDate().getTime()), new java.sql.Date(dateTo.getDate().getTime())));
            statT = new JLabel((b==null?"0":b.toString()));


        JButton searchButton = new JButton("Search");
        JLabel finalStatT = statT;
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (dateFrom.getDate().getTime() > dateTo.getDate().getTime()) {
                    JOptionPane.showMessageDialog(null, "Please chose the correct date", "Eror", JOptionPane.ERROR_MESSAGE);
                }


                if (cashierBox.getSelectedItem().equals("All")) {
                    receipts = getAllReceipt(sortAlph.get(), new java.sql.Date(dateFrom.getDate().getTime()), new java.sql.Date(dateTo.getDate().getTime()));

                        BigDecimal b = (getSumCheck(new java.sql.Date(dateFrom.getDate().getTime()), new java.sql.Date(dateTo.getDate().getTime())));
                        finalStatT.setText((b==null?"0":b.toString()));

                } else {
                    try {
                        BigDecimal b = (getSumFromGivenCashier(getEmployee(cashierBox.getSelectedItem().toString().split(" ")[0]),new java.sql.Date(dateFrom.getDate().getTime()), new java.sql.Date(dateTo.getDate().getTime())));
                        finalStatT.setText((b==null?"0":b.toString()));
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

        frame.add(toolbar, BorderLayout.PAGE_START);
        frame.add(scrollPane, BorderLayout.CENTER);

JButton print = new JButton("Print");
statPanel.add(print);
        statPanel.add(statL);
        statPanel.add(statT);

        frame.add(statPanel, BorderLayout.PAGE_END);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        for (Receipt receipt : receipts) {
            model.addRow(new Object[] {receipt.getNumber(), (receipt.getEmployee() == null?"Non authorised":receipt.getEmployee().getId()+" "+receipt.getEmployee().getSurname()+" "+receipt.getEmployee().getName()), (receipt.getCard() == null?"Non authorised":receipt.getCard().getNumber()+" "+receipt.getCard().getSurname()),receipt.getPrintDate(), receipt.getTotalSum(), receipt.getVAT()});
        }

        home.addActionListener( s ->{
            frame.getContentPane().removeAll();
            MainMenuManager.display(frame, empl);
            // Repaint the frame
            frame.revalidate();
            frame.repaint();
        });

        print.addActionListener( e -> {
                    Report r = new Report(table);
                }
        );


    }


    private static void displayReceiptProducts(String receiptId){

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



    private static String[] getCahierStringList() {
        try {
            List<Employee> cashier = getAllSpecial("CASHIER");
            String[] labels = new String[cashier.size() + 1];
            labels[0] = "All";
            for (int i = 1; i < labels.length; i++) {
                labels[i] = cashier.get(i - 1).getId() + " " + cashier.get(i - 1).getSurname() + " " + cashier.get(i - 1).getName() + " " + (cashier.get(i - 1).getPatronymic() == null ? " " : cashier.get(i - 1).getPatronymic());
            }
            return labels;
        }catch(SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }
}
