package menu;

import entity.Employee;
import entity.Product;
import info_menu_common.ProductTable2;
import info_menu_common.StoreProductTable;
import info_menu_manager.*;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;


public class MainMenuManager {
    static List<Product> product_List;
   static JFrame frame = new JFrame("Menu");

    public static void display(JFrame frame, Employee empl){

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel logoPanel = new JPanel();

        JPanel buttonPanel = new JPanel(new GridLayout(2, 3));
        buttonPanel.setLayout(new GridLayout(2, 3, 10, 10)); // set horizontal and vertical gap between components
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JPanel exitPanel = new JPanel(new GridLayout(1, 1));
        //exitPanel.setLayout(new GridLayout(1, 1, 10, 10)); // set horizontal and vertical gap between components
        exitPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        ImageIcon icon = new ImageIcon("C:\\Zlahoda\\Zlagoda\\images\\logo.png");
        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(icon);
        logoPanel.add(logoLabel);

        JButton customer = new JButton("Customer");
        JButton employee = new JButton("Employee");
        JButton category = new JButton("Category");
        JButton product = new JButton("Product");
        JButton sProduct = new JButton("Store Product");
        JButton receipt = new JButton("Receipt");

        buttonPanel.add(customer);
        buttonPanel.add(employee);
        buttonPanel.add(category);
        buttonPanel.add(product);
        buttonPanel.add(sProduct);
        buttonPanel.add(receipt);

        JButton logout = new JButton("Log out");
        exitPanel.add(logout);

        frame.add(logoPanel, BorderLayout.PAGE_START);

        frame.add(buttonPanel, BorderLayout.CENTER);

        frame.add(exitPanel, BorderLayout.PAGE_END);

        frame.setSize(500, 500);
        frame.setVisible(true);


        customer.addActionListener( s ->{
            frame.getContentPane().removeAll();
            CustomerTableManager.display(frame, empl);
            // Repaint the frame
            frame.revalidate();
            frame.repaint();
        });


        employee.addActionListener( s ->{
            frame.getContentPane().removeAll();
            EmployeeTableManager.display(frame, empl);
            // Repaint the frame
            frame.revalidate();
            frame.repaint();
        });

        category.addActionListener( s ->{
            frame.getContentPane().removeAll();
            CategoryTable.display(frame, empl);
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
            ReceiptViewer.display(frame, empl);
            // Repaint the frame
            frame.revalidate();
            frame.repaint();
        });

    }


    public static void main(String[] args) throws SQLException {
        Employee m = new Employee("1", "Smith", "John", "password123", "Adam", Employee.Role.MANAGER, BigDecimal.valueOf(30000), Date.valueOf("1980-01-01"), Date.valueOf("2020-02-20"), "380977282828", "New York", "12", "10001");
//'1', 'Smith', 'John', 'password123', 'Cashier', 'Adam', 3000.0000, '1980-01-01', '2010-01-01', '123-456-7890', 'New York', '1st Avenue', '10001'

        display(new JFrame(), m);
    }

}