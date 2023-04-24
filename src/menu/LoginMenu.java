package menu;

import bd_connection.*;

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
        panelName.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 30));
        loginPanel.add(panelName);

        JLabel filler = new JLabel();
        loginPanel.add(filler);

        //TODO change to what instead username
        JLabel userLabel = new JLabel("username:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 25));
        loginPanel.add(userLabel);

        JTextField userField = new JTextField();
        userField.setFont(new Font("Arial", Font.PLAIN, 25));
        loginPanel.add(userField);

        JLabel passwordLabel = new JLabel("password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 25));
        loginPanel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 25));
        loginPanel.add(passwordField);

        JButton loginButton = new JButton("log in");
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                logIn();
            }
        });
        loginPanel.add(loginButton);
    }

    private void logIn() {
        //TODO method for log in
    }


}
