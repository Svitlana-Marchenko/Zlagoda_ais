package info_menu_cashier;

import com.toedter.calendar.JDateChooser;
import entity.*;
import info_menu_cashier.ReceiptInfo;
import info_menu_cashier.StoreProductCashier;
import info_menu_common.StoreProductTable;
import info_menu_manager.ReceiptViewer;
import menu.MainMenuCashier;

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
import static bd_connection.Store_Product.getAllAboutProductsOnUPC;


public class ReceiptViewCashier {

    private static JTable table;
    private DefaultTableModel model;
    private JPanel panel;
    static List<Receipt> receipts;


    public static void display(JFrame frame, Employee CASHIER){

       // Employee CASHIER = new Employee("1", null, null, null, null, null, null, null, null, null, null, null, null);

        AtomicBoolean sortAlph = new AtomicBoolean(true);

        DefaultTableModel model = new DefaultTableModel(new Object[]{"Receipt ID", "Customer", "Date", "Total", "VAT"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // make the cells non-editable
            }
        };

        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        // Create a "Home" button and add it to the button panel
        JButton homeButton = new JButton("Home");
        toolbar.add(homeButton);

        homeButton.addActionListener(s -> {
            frame.getContentPane().removeAll();
            MainMenuCashier.display(frame, CASHIER);
            // Repaint the frame
            frame.revalidate();
            frame.repaint();
        });




        toolbar.add(new JLabel("From:"));

        JDateChooser dateFrom = new JDateChooser();
        dateFrom.setDate(new Date());
        toolbar.add(dateFrom);


        toolbar.add(new JLabel("To:"));

        JDateChooser dateTo = new JDateChooser();
        dateTo.setDate(new Date());
        toolbar.add(dateTo);

        try {

            receipts = getAllReceiptFromGivenCashier(sortAlph.get(), CASHIER, new java.sql.Date(dateFrom.getDate().getTime()), new java.sql.Date(dateTo.getDate().getTime()));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        model.setRowCount(0);
        for (Receipt receipt : receipts) {
            model.addRow(new Object[] {receipt.getNumber(), (receipt.getCard()==null?"Non authorised":receipt.getCard().getNumber()+" "+receipt.getCard().getSurname()+" "+receipt.getCard().getName()), receipt.getPrintDate(),receipt.getTotalSum(),receipt.getVAT()});
        }
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                   if(dateFrom.getDate().getTime()>dateTo.getDate().getTime()){
                       JOptionPane.showMessageDialog(null, "Please chose the correct date", "Error", JOptionPane.ERROR_MESSAGE);
                   }

                    try {

                        receipts = getAllReceiptFromGivenCashier(sortAlph.get(), CASHIER, new java.sql.Date(dateFrom.getDate().getTime()), new java.sql.Date(dateTo.getDate().getTime()));
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                model.setRowCount(0);
                for (Receipt receipt : receipts) {
                    model.addRow(new Object[] {receipt.getNumber(), (receipt.getCard()==null?"Non authorised":receipt.getCard().getNumber()+" "+receipt.getCard().getSurname()+" "+receipt.getCard().getName()), receipt.getPrintDate(),receipt.getTotalSum(),receipt.getVAT()});
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
                    model.addRow(new Object[] {receipt.getNumber(), (receipt.getCard()==null?"Non authorised":receipt.getCard().getNumber()+" "+receipt.getCard().getSurname()+" "+receipt.getCard().getName()), receipt.getPrintDate(),receipt.getTotalSum(),receipt.getVAT()});
                }

            }
        });

        toolbar.add(sortButton);

        table = new JTable(model);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    String receiptId = (String) model.getValueAt(row, 0);

                        displayReceiptProducts(receiptId);

                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);

        frame.getContentPane().add(toolbar, BorderLayout.PAGE_START);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);



        JToolBar endMenu = new JToolBar();
        JButton searchNum= new JButton("Search");
        endMenu.add(searchNum);

        frame.add(endMenu, BorderLayout.PAGE_END);

        searchNum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                frame.getContentPane().removeAll();
                ReceiptInfo.display(frame, CASHIER);
                // Repaint the frame
                frame.revalidate();
                frame.repaint();

            }
        });



        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

if(receipts!=null) {
    for (Receipt receipt : receipts) {
        model.addRow(new Object[]{receipt.getNumber(), receipt.getEmployee().getId() + " " + receipt.getEmployee().getSurname() + " " + receipt.getEmployee().getName(), receipt.getCard().getNumber() + " " + receipt.getCard().getSurname(), receipt.getPrintDate(), receipt.getTotalSum(), receipt.getVAT()});
    }
}
    }


    private static void displayReceiptProducts(String receiptId){

        Receipt rec = getReceipt(receiptId);
        List<SoldProduct> productL = rec.getProducts();

        JTable table = new JTable();

        // Create a table model and set it on the table
        DefaultTableModel model = new DefaultTableModel(new Object[]{"UPC", "Name", "Amount", "Price"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // make the cells non-editable
            }
        };
        table.setModel(model);
        model.addRow(new Object[]{"UPC", "Name", "Amount", "Price"});
            if(productL!=null) {
                for (SoldProduct pr : productL) {
                    model.addRow(new Object[]{pr.getUPC(), (getAllAboutProductsOnUPC(pr.getUPC())==null?"":getAllAboutProductsOnUPC(pr.getUPC()).getProduct().getName()), pr.getAmount(), pr.getPrice()});
                }
            }

        JOptionPane.showMessageDialog(null, table, "Products", JOptionPane.INFORMATION_MESSAGE);

    }

    public static void main(String[] args) {
       /*
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
        List<Receipt> testReceipts = new ArrayList<>();
                testReceipts.add(new Receipt("1'", null, null, Timestamp.valueOf("2020-02-20 11:11:11"), BigDecimal.valueOf(100), BigDecimal.valueOf(20), null));
                testReceipts.add(new Receipt("1'", null, null, Timestamp.valueOf("2020-02-20 11:11:11"), BigDecimal.valueOf(100), BigDecimal.valueOf(20), null));
                testReceipts.add(new Receipt("1'", null, null, Timestamp.valueOf("2020-02-20 11:11:11"), BigDecimal.valueOf(100), BigDecimal.valueOf(20), null));
                testReceipts.add(new Receipt("1'", null, null, Timestamp.valueOf("2020-02-20 11:11:11"), BigDecimal.valueOf(100), BigDecimal.valueOf(20), null));


                receipts = testReceipts;
                try {
                    ReceiptViewer viewer = new ReceiptViewer();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        */
    }

}

