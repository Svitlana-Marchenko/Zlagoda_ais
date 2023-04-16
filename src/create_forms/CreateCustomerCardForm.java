package create_forms;

import bd_connection.*;
import entity.CustomerCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.NONE;

public class CreateCustomerCardForm extends JFrame {
    private JPanel mainPanel;
    private JPanel backPanel;
    private JPanel captionPanel;

    private JTextField nameField;
    private JTextField surnameField;
    private JTextField patronymicField;
    private JTextField phoneNumberField;
    private JTextField cityField;
    private JTextField streetField;
    private JTextField zipCodeField;
    private JTextField percentField;

    private JButton createButton;
    private JButton cancelButton;

    private static final int[] max_length = {50,13,9};

    public CreateCustomerCardForm(){
        super("Add new client");
        this.setSize(600,500);
        start();
    }

    /**
     * Початкова ініціалізація графічних об'єктів
     */
    private void init(){
        backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.PAGE_AXIS));
        captionPanel = new JPanel();
        captionPanel.setLayout(new BoxLayout(captionPanel, BoxLayout.LINE_AXIS));

        GridBagConstraints c = new GridBagConstraints();
        JLabel captionLabel = new JLabel("New client");
        captionLabel.setHorizontalAlignment(JLabel.CENTER);
        captionLabel.setFont(new Font("TimesRoman", Font.BOLD, 40));
        captionPanel.add(captionLabel);
        backPanel.add(captionPanel);


        mainPanel = new JPanel(new GridBagLayout());
        JLabel nameLabel = new JLabel("Name: ");
        nameLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 0;
        c.gridx = 0;
        mainPanel.add(nameLabel,c);

        nameField = new JTextField();
        nameField.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        c.gridy = 0;
        c.gridx = 1;
        c.weightx = 1;
        c.fill = BOTH;
        mainPanel.add(nameField,c);

        c.fill = NONE;
        JLabel surnameLabel = new JLabel("Surname: ");
        surnameLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 1;
        c.gridx = 0;
        c.weightx = 0;
        mainPanel.add(surnameLabel,c);

        surnameField = new JTextField();
        surnameField.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        c.gridy = 1;
        c.gridx = 1;
        c.weightx = 1;
        c.fill = BOTH;
        mainPanel.add(surnameField,c);

        JLabel patronymicLabel = new JLabel("Patronymic: ");
        patronymicLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 2;
        c.gridx = 0;
        c.weightx = 0;
        c.fill = NONE;
        mainPanel.add(patronymicLabel,c);

        patronymicField = new JTextField();
        patronymicField.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        c.gridy = 2;
        c.gridx = 1;
        c.weightx = 1;
        c.fill = BOTH;
        mainPanel.add(patronymicField,c);

        JLabel phoneNumberLabel = new JLabel("Phone number: ");
        phoneNumberLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 3;
        c.gridx = 0;
        c.weightx = 0;
        c.fill = NONE;
        mainPanel.add(phoneNumberLabel,c);

        phoneNumberField = new JTextField();
        phoneNumberField.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        c.gridy = 3;
        c.gridx = 1;
        c.weightx = 1;
        c.fill = BOTH;
        mainPanel.add(phoneNumberField,c);

        JLabel cityLabel = new JLabel("City: ");
        cityLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 4;
        c.gridx = 0;
        c.weightx = 0;
        c.fill = NONE;
        mainPanel.add(cityLabel,c);

        cityField = new JTextField();
        cityField.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        c.gridy = 4;
        c.gridx = 1;
        c.weightx = 1;
        c.fill = BOTH;
        mainPanel.add(cityField,c);

        JLabel streetLabel = new JLabel("Street: ");
        streetLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 5;
        c.gridx = 0;
        c.weightx = 0;
        c.fill = NONE;
        mainPanel.add(streetLabel,c);

        streetField = new JTextField();
        streetField.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        c.gridy = 5;
        c.gridx = 1;
        c.weightx = 1;
        c.fill = BOTH;
        mainPanel.add(streetField,c);

        JLabel zipCodeLabel = new JLabel("Zip code: ");
        zipCodeLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 6;
        c.gridx = 0;
        c.weightx = 0;
        c.fill = NONE;
        mainPanel.add(zipCodeLabel,c);

        zipCodeField = new JTextField();
        zipCodeField.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        c.gridy = 6;
        c.gridx = 1;
        c.weightx = 1;
        c.fill = BOTH;
        mainPanel.add(zipCodeField,c);

        JLabel percentLabel = new JLabel("Percent: ");
        percentLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 7;
        c.gridx = 0;
        c.weightx = 0;
        c.fill = NONE;
        mainPanel.add(percentLabel,c);

        percentField = new JTextField();
        percentField.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        c.gridy = 7;
        c.gridx = 1;
        c.weightx = 1;
        c.fill = BOTH;
        mainPanel.add(percentField,c);

        c.fill = NONE;
        createButton = new JButton("Create");
        createButton.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<JTextField> fields= new ArrayList<>();
                List<JTextField> checkForInt= new ArrayList<>();
                List<List<JTextField>> fieldsList= new ArrayList<>();
                fields.add(nameField);
                fields.add(surnameField);
                fields.add(patronymicField);
                fields.add(cityField);
                fields.add(streetField);
                fieldsList.add(new ArrayList<>());
                fieldsList.get(0).addAll(fields);
                fields.add(phoneNumberField);
                fieldsList.add(new ArrayList<>());
                fieldsList.get(1).add(phoneNumberField);
                fields.add(zipCodeField);
                fieldsList.add(new ArrayList<>());
                fieldsList.get(2).add(zipCodeField);
                fields.add(percentField);
                checkForInt.add(percentField);
                CheckForErrors.tFields=fields;
                List<String> errors = CheckForErrors.checkForEmptyErrors();
                List<String> errors2 = CheckForErrors.checkForNotNumbersErrors(new ArrayList<>(),checkForInt);
                List<String> errors3 = CheckForErrors.checkForLength(max_length,fieldsList);
                if(errors!=null){
                    showError(errors.get(0),CheckForErrors.getErrorTextFields(errors));
                }else if(errors2!=null){
                    showError(errors2.get(0),CheckForErrors.getErrorTextFields(errors2));
                }else if(errors3!=null){
                    showError(errors3.get(0),CheckForErrors.getErrorTextFields(errors3));
                }else if(Integer.valueOf(percentField.getText())>100){
                    percentField.setBackground(Color.red);
                    JOptionPane.showMessageDialog(null,"Percent must be <=100","Error",JOptionPane.ERROR_MESSAGE);
                    percentField.setBackground(Color.white);
                    percentField.setText("");
                }else if(!CheckForErrors.checkPhoneNumber(phoneNumberField.getText())){
                    phoneNumberField.setBackground(Color.red);
                    JOptionPane.showMessageDialog(null, "Wrong phone number format. Must be +380xxxxxxxxx", "Error", JOptionPane.ERROR_MESSAGE);
                    phoneNumberField.setText("");
                    phoneNumberField.setBackground(Color.white);
                }else{
                    createNewItem();
                    dispose();
                }
            }
        });
        c.gridy = 8;
        c.gridx = 0;
        c.weighty = 0.5;

        c.ipadx = 0;
        c.gridwidth = 0;
        c.insets = new Insets(10,0,10,200);
        createButton.setBackground(Color.green);
        mainPanel.add(createButton,c);
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        c.gridy = 8;
        c.gridx = 1;
        c.insets = new Insets(10,90,10,0);
        cancelButton.setBackground(Color.cyan);
        mainPanel.add(cancelButton,c);
        backPanel.add(mainPanel);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    public String generateNumber() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    private void createNewItem(){
        String number = generateNumber();
        while(Customer_Card.findCustomerCardById(number) != null)
            number = generateNumber();
        CustomerCard customerCard = new CustomerCard(number,nameField.getText(),surnameField.getText(),patronymicField.getText(),phoneNumberField.getText(),cityField.getText(),streetField.getText(),zipCodeField.getText(),Integer.valueOf(percentField.getText()));
        Customer_Card.addCustomer(customerCard);
        System.out.println(Customer_Card.findAll());
    }
    /**
     * Відображаєм помилку
     * @param text текст помилки
     * @param fields поля з помилкою
     */
    private void showError(String text, JTextField[] fields){
        for(int i=0;i<fields.length;i++){
            fields[i].setBackground(Color.red);
        }
        JOptionPane.showMessageDialog(null,text,"Error",JOptionPane.ERROR_MESSAGE);
        for(int i=0;i<fields.length;i++){
            fields[i].setBackground(Color.white);
            fields[i].setText("");
        }
    }
    /**
     * Запускаєм користувацьку форму
     */
    public void start(){
        init();
        add(backPanel);
        this.pack();
        this.setVisible(true);
    }

}
