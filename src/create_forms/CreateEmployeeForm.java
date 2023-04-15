package create_forms;

import bd_connection.*;
import com.toedter.calendar.JDateChooser;
import entity.Employee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.NONE;


public class CreateEmployeeForm extends JFrame {
    private JPanel mainPanel;
    private JPanel backPanel;
    private JPanel captionPanel;

    private JTextField nameField;
    private JTextField surnameField;
    private JTextField patronymicField;
    private JComboBox roleField;
    private JDateChooser dateOfBirth;
    private JDateChooser dateOfStart;
    private JTextField passwordField;
    private JTextField salaryField;


    private JTextField phoneNumberField;
    private JTextField cityField;
    private JTextField streetField;
    private JTextField zipCodeField;


    private JButton createButton;
    private JButton cancelButton;
    private static final int[] max_length = {50,13,9,72};


    public CreateEmployeeForm(){
        super("Create new employee");
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
        JLabel captionLabel = new JLabel("New employee");
        captionLabel.setHorizontalAlignment(JLabel.CENTER);
        captionLabel.setFont(new Font("TimesRoman", Font.BOLD, 35));
        captionPanel.add(captionLabel);
        backPanel.add(captionPanel);


        mainPanel = new JPanel(new GridBagLayout());
        JLabel nameLabel = new JLabel("Name: ");
        nameLabel.setHorizontalAlignment(JLabel.LEFT);
        nameLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 0;
        c.gridx = 0;
        mainPanel.add(nameLabel,c);

        nameField = new JTextField();
        nameField.setSize(150,90);
        nameField.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        c.gridy = 0;
        c.gridx = 1;
        c.weightx = 2;
        c.fill = BOTH;
        c.ipadx=300;
        mainPanel.add(nameField,c);

        c.ipadx=0;
        JLabel surnameLabel = new JLabel("Surname: ");
        surnameLabel.setHorizontalAlignment(JLabel.LEFT);
        surnameLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 1;
        c.gridx = 0;
        c.weightx = 0;
        c.fill = NONE;
        mainPanel.add(surnameLabel,c);

        surnameField = new JTextField();
        surnameField.setSize(150,90);
        surnameField.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        c.gridy = 1;
        c.gridx = 1;
        c.weightx = 1;
        c.fill = BOTH;
        mainPanel.add(surnameField,c);

        JLabel patronymicLabel = new JLabel("Patronymic: ");
        patronymicLabel.setHorizontalAlignment(JLabel.LEFT);
        patronymicLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 2;
        c.gridx = 0;
        c.weightx = 0;
        c.fill = NONE;
        mainPanel.add(patronymicLabel,c);

        patronymicField = new JTextField();
        patronymicField.setSize(150,90);
        patronymicField.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        c.gridy = 2;
        c.gridx = 1;
        c.weightx = 1;
        c.fill = BOTH;
        mainPanel.add(patronymicField,c);

        JLabel birthDayLabel = new JLabel("Date of birth: ");
        birthDayLabel.setHorizontalAlignment(JLabel.LEFT);
        birthDayLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 3;
        c.gridx = 0;
        c.weightx = 0;
        c.fill = NONE;
        mainPanel.add(birthDayLabel,c);

        dateOfBirth = new JDateChooser();
        dateOfBirth.setDate(new Date());
        dateOfBirth.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        c.gridy = 3;
        c.gridx = 1;
        c.fill = BOTH;
        c.weightx = 1;
        mainPanel.add(dateOfBirth,c);

        c.fill = NONE;
        JLabel phoneNumberLabel = new JLabel("Phone number: ");
        phoneNumberLabel.setHorizontalAlignment(JLabel.LEFT);
        phoneNumberLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 4;
        c.gridx = 0;
        c.weightx = 0;
        mainPanel.add(phoneNumberLabel,c);

        phoneNumberField = new JTextField();
        phoneNumberField.setSize(150,90);
        phoneNumberField.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        c.gridy = 4;
        c.gridx = 1;
        c.weightx = 1;
        c.fill = BOTH;
        mainPanel.add(phoneNumberField,c);

        JLabel cityLabel = new JLabel("City: ");
        cityLabel.setHorizontalAlignment(JLabel.LEFT);
        cityLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 5;
        c.gridx = 0;
        c.weightx = 0;
        c.fill = NONE;
        mainPanel.add(cityLabel,c);

        cityField = new JTextField();
        cityField.setSize(150,90);
        cityField.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        c.gridy = 5;
        c.gridx = 1;
        c.weightx = 1;
        c.fill = BOTH;
        mainPanel.add(cityField,c);

        JLabel streetLabel = new JLabel("Street: ");
        streetLabel.setHorizontalAlignment(JLabel.LEFT);
        streetLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 6;
        c.gridx = 0;
        c.weightx = 0;
        c.fill = NONE;
        mainPanel.add(streetLabel,c);

        streetField = new JTextField();
        streetField.setSize(150,90);
        streetField.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        c.gridy = 6;
        c.gridx = 1;
        c.weightx = 1;
        c.fill = BOTH;
        mainPanel.add(streetField,c);

        JLabel zipCodeLabel = new JLabel("Zip code: ");
        zipCodeLabel.setHorizontalAlignment(JLabel.LEFT);
        zipCodeLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 7;
        c.gridx = 0;
        c.weightx = 0;
        c.fill = NONE;
        mainPanel.add(zipCodeLabel,c);

        zipCodeField = new JTextField();
        zipCodeField.setSize(150,90);
        zipCodeField.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        c.gridy = 7;
        c.gridx = 1;
        c.weightx = 1;
        c.fill = BOTH;
        mainPanel.add(zipCodeField,c);

        JLabel roleLabel = new JLabel("Role: ");
        roleLabel.setHorizontalAlignment(JLabel.LEFT);
        roleLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 8;
        c.gridx = 0;
        c.weightx = 0;
        c.fill = NONE;
        mainPanel.add(roleLabel,c);

        String[] roles = Stream.of(Employee.Role.values()).map(Employee.Role::name).toArray(String[]::new);
        roleField = new JComboBox();
        roleField.setFont(new Font("TimesRoman",Font.PLAIN, 20));

        for(String role: roles){
            roleField.addItem(role);
        }
        c.weightx = 2;
        c.fill = BOTH;
        c.gridy = 8;
        c.gridx = 1;
        mainPanel.add(roleField,c);

        JLabel salaryLabel = new JLabel("Salary: ");
        salaryLabel.setHorizontalAlignment(JLabel.LEFT);
        salaryLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.fill = NONE;
        c.weightx = 0;
        c.gridy = 9;
        c.gridx = 0;
        mainPanel.add(salaryLabel,c);

        salaryField = new JTextField();
        salaryField.setSize(150,90);
        salaryField.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        c.gridy = 9;
        c.gridx = 1;
        c.weightx = 1;
        c.fill = BOTH;
        mainPanel.add(salaryField,c);

        JLabel startDateLabel = new JLabel("Date of start: ");
        startDateLabel.setHorizontalAlignment(JLabel.LEFT);
        startDateLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 10;
        c.gridx = 0;
        c.weightx = 0;
        c.fill = NONE;
        mainPanel.add(startDateLabel,c);

        dateOfStart = new JDateChooser();
        dateOfStart.setDate(new Date());
        dateOfStart.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        c.gridy = 10;
        c.gridx = 1;
        c.fill = BOTH;
        c.weightx = 1;
        mainPanel.add(dateOfStart,c);

        c.fill = NONE;
        JLabel passwordLabel = new JLabel("Password: ");
        passwordLabel.setHorizontalAlignment(JLabel.LEFT);
        passwordLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 11;
        c.gridx = 0;

        c.weightx = 0;

        mainPanel.add(passwordLabel,c);

        passwordField = new JTextField();
        passwordField.setSize(150,90);
        passwordField.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        c.gridy = 11;
        c.gridx = 1;
        c.weightx = 1;
        c.fill = BOTH;
        mainPanel.add(passwordField,c);

        c.fill = NONE;
        createButton = new JButton("Create");
        createButton.setFont(new Font("TimesRoman", Font.PLAIN, 27));
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<JTextField> fields = new ArrayList<>();
                List<JTextField> checkForDouble = new ArrayList<>();
                List<List<JTextField>> fieldsList = new ArrayList<>();
                fields.add(nameField);
                fields.add(surnameField);
                fields.add(patronymicField);
                fields.add(cityField);
                fields.add(streetField);
                fieldsList.add(new ArrayList<>());
                fieldsList.get(0).addAll(fields);
                fields.add(zipCodeField);
                fields.add(passwordField);
                fields.add(phoneNumberField);
                fieldsList.add(new ArrayList<>());
                fieldsList.get(1).add(phoneNumberField);
                fieldsList.add(new ArrayList<>());
                fieldsList.get(2).add(zipCodeField);
                fieldsList.add(new ArrayList<>());
                fieldsList.get(3).add(passwordField);
                fields.add(salaryField);
                checkForDouble.add(salaryField);
                CheckForErrors.tFields=fields;
                List<String> errors = CheckForErrors.checkForEmptyErrors();
                List<String> errors2 = CheckForErrors.checkForNotNumbersErrors(checkForDouble, new ArrayList<>());
                List<String> errors3 = CheckForErrors.checkForLength(max_length,fieldsList);
                if(errors!=null){
                    showError(errors.get(0),CheckForErrors.getErrorTextFields(errors));
                }else if(errors2!=null){
                    showError(errors2.get(0),CheckForErrors.getErrorTextFields(errors2));
                }else if(errors3!=null){
                    showError(errors3.get(0),CheckForErrors.getErrorTextFields(errors3));
                }else if(dateOfBirth.getDate().after(new java.sql.Date(2005-1900,3,25))){
                    JOptionPane.showMessageDialog(null, "Employee must be >=18 years old", "Error", JOptionPane.ERROR_MESSAGE);
                    dateOfBirth.setDate(new Date());
                }else if(dateOfStart.getDate().after(new Date())){
                    JOptionPane.showMessageDialog(null, "Employee can't start in the future", "Error", JOptionPane.ERROR_MESSAGE);
                    dateOfStart.setDate(new Date());
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
        c.gridy = 12;
        c.gridx = 0;
        c.weighty = 0.5;

        c.ipadx = 0;
        c.gridwidth = 0;
        c.insets = new Insets(10,0,10,200);
        createButton.setBackground(Color.green);
        mainPanel.add(createButton,c);
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("TimesRoman", Font.PLAIN, 27));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        c.gridy = 12;
        c.gridx = 1;
        c.weighty = 0.5;
        c.ipadx = 0;
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


    private String generateNumber() {
        int leftLimit = 97;
        int rightLimit = 122;
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
        while(bd_connection.Employee.findEmployeeById(number) != null)
            number = generateNumber();
        Employee employee = new Employee(number,surnameField.getText(),nameField.getText(),passwordField.getText(),patronymicField.getText(),Employee.Role.valueOf(roleField.getSelectedItem().toString()), new BigDecimal(Double.valueOf(salaryField.getText())),new java.sql.Date(dateOfBirth.getDate().getTime()), new java.sql.Date(dateOfStart.getDate().getTime()),phoneNumberField.getText(),cityField.getText(),streetField.getText(),zipCodeField.getText());
        bd_connection.Employee.addEmployee(employee);
        System.out.println(bd_connection.Employee.findAll());
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
