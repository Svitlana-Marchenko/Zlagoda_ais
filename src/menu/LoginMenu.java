package menu;

import additional_libraries.BCrypt;
import bd_connection.*;

import javax.sql.ConnectionEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LoginMenu extends JFrame {
    JPanel loginPanel;
    JFrame loginFrame;

    public LoginMenu(){
        super("Zlagoda");
        loginFrame=this;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init(this);
    }

    private void init(JFrame frame) {
        loginPanel = new JPanel(new GridLayout(4,2));
        add(loginPanel);
        JLabel panelName = new JLabel("Login", SwingConstants.CENTER);
        panelName.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 24));
        loginPanel.add(panelName);

        JLabel filler = new JLabel();
        loginPanel.add(filler);

        JLabel userLabel = new JLabel("mobile number:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        loginPanel.add(userLabel);

        JTextField userField = new JTextField();
        userField.setFont(new Font("Arial", Font.PLAIN, 18));
        loginPanel.add(userField);

        JLabel passwordLabel = new JLabel("password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        loginPanel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 18));
        loginPanel.add(passwordField);

        JButton loginButton = new JButton("log in");
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                logIn(userField.getText(),passwordField.getText());
            }
        });
        loginPanel.add(loginButton);
    }

    private void logIn(String phoneNumber, String password) {
        //TODO create connection
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3308/zlagoda",
                    "root",
                    ""
            );
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        Employee.setConnection(connection);
        entity.Employee employee = checkPassword(phoneNumber, password);
        if( employee != null) {
            loginFrame.getContentPane().removeAll();
            if (employee.getRole().equals(entity.Employee.Role.CASHIER))
                MainMenuCashier.display(loginFrame, employee);
            else MainMenuManager.display(loginFrame, employee);
            loginFrame.revalidate();
            loginFrame.repaint();
        }
    }

    private entity.Employee checkPassword(String phoneNumber, String password) {
        entity.Employee employee = Employee.findEmployeeByPhoneNumber(phoneNumber);
        if(employee == null)
            JOptionPane.showMessageDialog(null, "Wrong phone number", "Eror", JOptionPane.ERROR_MESSAGE);
        else if(BCrypt.checkpw(password,employee.getPassword())){
            return employee;
        }else{
            JOptionPane.showMessageDialog(new JFrame(), "Wrong phone number or password", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }


}
