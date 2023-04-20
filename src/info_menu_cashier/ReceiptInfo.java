package info_menu_cashier;

import entity.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static bd_connection.Check.getReceiptByNumber;

public class ReceiptInfo {

    static Receipt receipt = null;

    public static void display(JFrame frame, Employee cashier){

        String tetxForJText = "Enter receipt number";



        JToolBar buttonPanel = new JToolBar();
        JButton homeButton = new JButton("Home");
        buttonPanel.add(homeButton);

        homeButton.addActionListener( s ->{
            frame.getContentPane().removeAll();
                ReceiptViewCashier.display(frame, cashier);
            // Repaint the frame
            frame.revalidate();
            frame.repaint();
        });


        JTextField numField = new JTextField(tetxForJText);
        numField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (numField.getText().equals(tetxForJText)) {
                    numField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (numField.getText().isEmpty()) {
                    numField.setText(tetxForJText);
                }
            }
        });

        buttonPanel.add(numField);

        JButton searchButton = new JButton("Search");
        buttonPanel.add(searchButton);


        JPanel infoPanel = new JPanel(new GridLayout(6, 2));
        JLabel idLabel = new JLabel("ID:");
        JLabel cashierLabel = new JLabel("Cashier:");
        JLabel customerLabel = new JLabel("Customer:");
        JLabel dateLabel = new JLabel("Date:");
        JLabel sumLabel = new JLabel("Sum:");
        JLabel vatLabel = new JLabel("VAT:");

        JLabel idLabelN = new JLabel("");
        JLabel cashierLabelN = new JLabel("");
        JLabel customerLabelN = new JLabel("");
        JLabel dateLabelN = new JLabel("");
        JLabel sumLabelN = new JLabel("");
        JLabel vatLabelN = new JLabel("");

        infoPanel.add(idLabel);
        infoPanel.add(idLabelN);
        infoPanel.add(cashierLabel);
        infoPanel.add(cashierLabelN);
        infoPanel.add(customerLabel);
        infoPanel.add(customerLabelN);
        infoPanel.add(dateLabel);
        infoPanel.add(dateLabelN);
        infoPanel.add(sumLabel);
        infoPanel.add(sumLabelN);
        infoPanel.add(vatLabel);
        infoPanel.add(vatLabelN);

        JPanel tablePanel = new JPanel(new BorderLayout());

        JTable table = new JTable();

        DefaultTableModel model = new DefaultTableModel(new Object[]{"UPC", "Name", "Amount", "Price"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);

        JScrollPane scrollPane = new JScrollPane(table);

        tablePanel.add(scrollPane, BorderLayout.CENTER);


        JPanel p = new JPanel(new GridLayout(2, 1));

        p.add(infoPanel);
        p.add(tablePanel);
        frame.add(buttonPanel, BorderLayout.PAGE_START);

        frame.add(p, BorderLayout.CENTER);

        frame.setSize(500, 500);
        frame.setVisible(true);



        searchButton.addActionListener(e -> {

            if(!numField.getText().equals(tetxForJText)){
                try {
                    receipt = getReceiptByNumber(numField.getText());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                if(receipt==null){
                    JOptionPane.showMessageDialog(null, "Please enter correct num", "Eror", JOptionPane.ERROR_MESSAGE);
                }
else {
                    List<SoldProduct> productL = receipt.getProducts();

                    model.setRowCount(0);

                    for (SoldProduct pr : productL)
                        model.addRow(new Object[]{pr.getUPC(), pr.getName(), pr.getAmount(), pr.getPrice()});

                    idLabelN.setText(receipt.getNumber());
                    cashierLabelN.setText(receipt.getEmployee().getId() + " " + receipt.getEmployee().getSurname() + " " + receipt.getEmployee().getName());
                    customerLabelN.setText(((receipt.getCard() == null) ? "No authorised" : receipt.getCard().getNumber() + " " + receipt.getCard().getSurname() + " " + receipt.getCard().getName()));
                    dateLabelN.setText(receipt.getPrintDate().toString());
                    sumLabelN.setText(receipt.getTotalSum().toString());
                    vatLabelN.setText(receipt.getVAT().toString());
                }
            }

            else{
                model.setRowCount(0);

                idLabelN.setText("");
                cashierLabelN.setText("");
                customerLabelN.setText("");
                dateLabelN.setText("");
                sumLabelN.setText("");
                vatLabelN.setText("");
            }

        });
    }



    public static void main(String[] args) throws SQLException {

        receipt = new Receipt("00001", new Employee("1", "", "", "", null, Employee.Role.CASHIER, BigDecimal.valueOf(100), Date.valueOf("2000-10-10"), Date.valueOf("2022-10-10"), "", "", "", ""), new CustomerCard("", "", "", "", "", "", "", "", 9), Timestamp.valueOf("2020-02-20 11:11:11"), BigDecimal.valueOf(10.50), BigDecimal.valueOf(1.05), null);


    }


}
