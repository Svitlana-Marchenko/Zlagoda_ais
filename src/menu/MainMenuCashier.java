package menu;

import entity.Employee;
import entity.Product;
import info_menu_cashier.CustomerTableCashier;
import info_menu_cashier.ReceiptViewCashier;
import info_menu_common.ProductTable2;
import info_menu_common.StoreProductTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;


public class MainMenuCashier {


   public static void display(JFrame frame, Employee empl){

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel logoPanel = new JPanel();

        JPanel buttonPanel = new JPanel(new GridLayout(3, 2));
        buttonPanel.setLayout(new GridLayout(2, 3, 10, 10)); // set horizontal and vertical gap between components
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JPanel exitPanel = new JPanel(new GridLayout(1, 1));
        exitPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        ImageIcon icon = new ImageIcon("C:\\Zlahoda\\Zlagoda\\images\\logo.png");
        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(icon);
        logoPanel.add(logoLabel);

        JButton customer = new JButton("Customer");
        JButton receipt = new JButton("Receipt");
        JButton product = new JButton("Product");
        JButton sProduct = new JButton("Store Product");
        JButton about = new JButton("About me");

        buttonPanel.add(customer);
        buttonPanel.add(receipt);
        buttonPanel.add(product);
        buttonPanel.add(sProduct);
        buttonPanel.add(about);


        JButton logout = new JButton("Log out");
        buttonPanel.add(logout);

        frame.add(logoPanel, BorderLayout.PAGE_START);

        frame.add(buttonPanel, BorderLayout.CENTER);

        frame.add(exitPanel, BorderLayout.PAGE_END);

        frame.setSize(500, 500);
        frame.setVisible(true);


       // add resize listener to update logo label size
       frame.addComponentListener(new ComponentAdapter() {
           @Override
           public void componentResized(ComponentEvent e) {
               Image image = icon.getImage().getScaledInstance(-1, frame.getHeight() / 2, Image.SCALE_SMOOTH);
               ImageIcon scaledIcon = new ImageIcon(image);
               logoLabel.setIcon(scaledIcon);
           }
       });

        customer.addActionListener( s ->{
            frame.getContentPane().removeAll();
            CustomerTableCashier.display(frame, empl);
            // Repaint the frame
            frame.revalidate();
            frame.repaint();
        });


        product.addActionListener( s ->{
            frame.getContentPane().removeAll();
            ProductTable2.display(frame, empl);
            // Repaint the frame
            frame.revalidate();
            frame.repaint();
        });


        sProduct.addActionListener( s ->{
            frame.getContentPane().removeAll();
            StoreProductTable.display(frame, empl);
            // Repaint the frame
            frame.revalidate();
            frame.repaint();
        });


        receipt.addActionListener( s ->{
            frame.getContentPane().removeAll();
            ReceiptViewCashier.display(frame, empl);
            // Repaint the frame
            frame.revalidate();
            frame.repaint();
        });

    }


    public static void main(String[] args) throws SQLException {
        Employee cashier = new Employee("1", "Smith", "John", "password123", "Adam", Employee.Role.CASHIER, BigDecimal.valueOf(30000), Date.valueOf("1980-01-01"), Date.valueOf("2020-02-20"), "380977282828", "New York", "12", "10001");
 display(new JFrame(), cashier);
    }

}
