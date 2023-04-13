package menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        loginPanel.setBackground(Color.decode("#F7FAA5"));
        add(loginPanel);
        JLabel panelName = new JLabel("Login", SwingConstants.CENTER);
        panelName.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 30));
        panelName.setForeground(Color.decode("#970EAB"));
        loginPanel.add(panelName);

        JLabel filler = new JLabel();
        loginPanel.add(filler);

        //TODO change to what instead username
        JLabel userLabel = new JLabel("username:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 25));
        userLabel.setForeground(Color.decode("#970EAB"));
        loginPanel.add(userLabel);

        JTextField userField = new JTextField();
        userField.setFont(new Font("Arial", Font.PLAIN, 25));
        userField.setForeground(Color.decode("#970EAB"));
        loginPanel.add(userField);

        JLabel passwordLabel = new JLabel("password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 25));
        passwordLabel.setForeground(Color.decode("#970EAB"));
        loginPanel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 25));
        passwordField.setForeground(Color.decode("#970EAB"));
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
