package info_menu_manager;

import com.toedter.calendar.JDateChooser;
import entity.Employee;
import entity.ProductInStore;
import info_menu_common.StoreProductTable;
import menu.MainMenuManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static bd_connection.Sale.getNumSold;
import static bd_connection.Store_Product.getAllAboutProductsOnUPC;

public class StoreProductManager {

    public static void display(JFrame frame, Employee role) {

        String textForUPCField = "Enter UPC";


        // Create a JPanel for the buttons
        JToolBar buttonPanel = new JToolBar();

        // Create a "Home" button and add it to the button panel
        JButton homeButton = new JButton("Home");
        buttonPanel.add(homeButton);

        homeButton.addActionListener( s ->{
            frame.getContentPane().removeAll();
            StoreProductTable.display(frame, role);
            // Repaint the frame
            frame.revalidate();
            frame.repaint();
        });

        JTextField upcField = new JTextField(textForUPCField);
        upcField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (upcField.getText().equals(textForUPCField)) {
                    upcField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (upcField.getText().isEmpty()) {
                    upcField.setText(textForUPCField);
                }
            }
        });
        buttonPanel.add(upcField);

        JButton searchButton = new JButton("Search");
        buttonPanel.add(searchButton);

        //JPanel contentPanel = new JPanel(new GridLayout(2, 1));
        JPanel contentPanel = new JPanel(new BorderLayout());





        JPanel infoPanel = new JPanel(new GridLayout(7, 2));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel upcLabel = new JLabel("UPC:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel descrLabel = new JLabel("Description");
        JLabel priceLabel = new JLabel("Price:");
        JLabel amountLabel = new JLabel("Amount:");
        JLabel promoLabel = new JLabel("Is Promo:");


        JLabel upcLabelN = new JLabel("");
        JLabel nameLabelN = new JLabel("");
        JLabel descrLabelN = new JLabel("");
        JLabel priceLabelN = new JLabel("");
        JLabel amountLabelN = new JLabel("");
        JLabel promoLabelN = new JLabel("");

        infoPanel.add(upcLabel);
        infoPanel.add(upcLabelN);

        infoPanel.add(nameLabel);
        infoPanel.add(nameLabelN);


        infoPanel.add(descrLabel);
        infoPanel.add(descrLabelN);

        infoPanel.add(priceLabel);
        infoPanel.add(priceLabelN);

        infoPanel.add(amountLabel);
        infoPanel.add(amountLabelN);

        infoPanel.add(promoLabel);
        infoPanel.add(promoLabelN);

        infoPanel.add(new JLabel("Get number of sold product having dates"));

       // JPanel productPanel = new JPanel(new BorderLayout());
       // productPanel.add(infoPanel, BorderLayout.CENTER);







JPanel specialSearch = new JPanel();
specialSearch.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        JPanel numProductForGivenDate = new JPanel(new GridLayout(1, 3, 10, 10));
        numProductForGivenDate.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 150));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        JDateChooser dateFrom = new JDateChooser();
        dateFrom.setDate(new Date());
        JDateChooser dateTo = new JDateChooser();
        dateTo.setDate(new Date());
        JButton search1 = new JButton("Search");


        numProductForGivenDate.add(dateFrom);
        numProductForGivenDate.add(dateTo);
        numProductForGivenDate.add(search1);

specialSearch.add(numProductForGivenDate);



        contentPanel.add(infoPanel, BorderLayout.CENTER);
        contentPanel.add(specialSearch, BorderLayout.PAGE_END);
        frame.add(buttonPanel, BorderLayout.PAGE_START);
        frame.add(contentPanel, BorderLayout.CENTER);

        frame.setSize(500, 500);
        frame.setVisible(true);


contentPanel.setVisible(false);
       // productPanel.setVisible(false);
        searchButton.addActionListener(e -> {
            String upc = upcField.getText();

            if (!upc.equals(textForUPCField)) {
                ProductInStore pr;
                try {
                    pr = getAllAboutProductsOnUPC(upc);

                    upcLabelN.setText(pr.getUPC());
                    nameLabelN.setText(pr.getProduct().getName());
                    descrLabelN.setText(pr.getProduct().getCharacteristics());
                    priceLabelN.setText(pr.getPrice().toString());
                    amountLabelN.setText(pr.getAmount()+"");
                    promoLabelN.setText(pr.isPromotional()+"");

                  //  productPanel.setVisible(true);
                    contentPanel.setVisible(true);

                } catch (NullPointerException ex){
                        JOptionPane.showMessageDialog(null, "Please enter correct upc", "Eror", JOptionPane.ERROR_MESSAGE);
                       // productPanel.setVisible(false);
                    contentPanel.setVisible(false);
                    }
            } else {
               // productPanel.setVisible(false);
                contentPanel.setVisible(false);
            }
        });







        search1.addActionListener( e->
        {

            if(textForUPCField.equals(upcField.getText())){
                JOptionPane.showMessageDialog(null, "Please chose the upc", "Eror", JOptionPane.ERROR_MESSAGE);
            }
            else{
                ProductInStore pr = getAllAboutProductsOnUPC(upcField.getText());
                if(pr==null){
                    JOptionPane.showMessageDialog(null, "Please enter the correct upc", "Eror", JOptionPane.ERROR_MESSAGE);
                }
                else if(dateFrom.getDate().getTime()>dateTo.getDate().getTime()){
                    JOptionPane.showMessageDialog(null, "Please chose the correct date", "Eror", JOptionPane.ERROR_MESSAGE);
                }
                else{

                    JOptionPane.showMessageDialog(null, "Number of sold product for "+pr.getProduct().getName() + " between "+dateFormat.format(dateFrom.getDate())+" and "+dateFormat.format(dateTo.getDate())+" => "+getNumSold(new java.sql.Date(dateFrom.getDate().getTime()), new java.sql.Date(dateTo.getDate().getTime()), pr));
                }
            }

        });

    }

    public static void main(String[] args) {
        //display();
    }
}

