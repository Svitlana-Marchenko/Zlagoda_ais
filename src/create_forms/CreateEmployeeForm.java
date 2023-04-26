package create_forms;

import additional_libraries.BCrypt;
import com.toedter.calendar.JDateChooser;
import entity.Employee;
import helpers.*;
import info_menu_manager.EmployeeTableManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

import static java.awt.GridBagConstraints.*;

/**
 * Форма додавання нового працівника
 */
public class CreateEmployeeForm extends JFrame {

    private JPanel backPanel;

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

    public CreateEmployeeForm(DefaultTableModel model, JFrame frame){
        super("Create new employee");
        this.setSize(600,500);
        start(model,frame);
    }

    /**
     * Початкова ініціалізація графічних об'єктів
     */
    private void init(DefaultTableModel model, JFrame frame){
        backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.PAGE_AXIS));
        JPanel captionPanel = new JPanel();
        captionPanel.setLayout(new BoxLayout(captionPanel, BoxLayout.LINE_AXIS));

        final int[] max_length = {50,13,9,72};

        GridBagConstraints c = new GridBagConstraints();
        JLabel captionLabel = new JLabel("New employee");
        captionLabel.setHorizontalAlignment(JLabel.CENTER);
        captionLabel.setFont(new Font("TimesRoman", Font.BOLD, 35));
        captionPanel.add(captionLabel);
        backPanel.add(captionPanel);
        JPanel buttonPanel = new JPanel();


        JPanel mainPanel = new JPanel(new GridBagLayout());
        List<JLabel> labels = new ArrayList<>();
        List<JTextField> fields = new ArrayList<>();

        nameField = new JTextField();
        fields.add(nameField);
        labels.add(new JLabel("Name: "));

        surnameField = new JTextField();
        fields.add(surnameField);
        labels.add(new JLabel("Surname: "));

        patronymicField = new JTextField();
        fields.add(patronymicField);
        labels.add(new JLabel("Patronymic: "));

        labels.add(new JLabel("Date of birth: "));

        cityField = new JTextField();
        fields.add(cityField);
        labels.add(new JLabel("City: "));

        streetField = new JTextField();
        fields.add(streetField);
        labels.add(new JLabel("Street: "));

        phoneNumberField = new JTextField();
        fields.add(phoneNumberField);
        labels.add(new JLabel("Phone number: "));

        zipCodeField = new JTextField();
        labels.add(new JLabel("Zip code: "));
        fields.add(zipCodeField);

        labels.add(new JLabel("Role: "));

        salaryField = new JTextField();
        labels.add(new JLabel("Salary: "));
        fields.add(salaryField);

        labels.add(new JLabel("Date of start: "));

        passwordField = new JTextField();
        labels.add(new JLabel("Password: "));
        fields.add(passwordField);

        for(int i=0,k=0;i<labels.size();i++){
            labels.get(i).setFont(new Font("TimesRoman",Font.PLAIN, 25));
            c.gridy = i;
            c.gridx = 0;
            c.weightx = 0;
            c.ipadx=0;
            c.fill=NONE;
            mainPanel.add(labels.get(i),c);

            c.gridx=1;
            c.weightx = 2;
            c.ipadx=300;
            c.fill = BOTH;

            if(i==3){
                dateOfBirth = new JDateChooser();
                dateOfBirth.setFont(new Font("TimesRoman",Font.PLAIN, 20));
                dateOfBirth.setDate(new Date());
                mainPanel.add(dateOfBirth,c);
                k++;
            }else if(i==8){
                String[] roles = Stream.of(Employee.Role.values()).map(Employee.Role::name).toArray(String[]::new);
                roleField = new JComboBox();
                roleField.setFont(new Font("TimesRoman",Font.PLAIN, 20));
                for(String role: roles){
                    roleField.addItem(role);
                }
                mainPanel.add(roleField,c);
                k++;
            }else if(i==10){
                dateOfStart = new JDateChooser();
                dateOfStart.setFont(new Font("TimesRoman", Font.PLAIN, 20));
                dateOfStart.setDate(new Date());
                mainPanel.add(dateOfStart,c);
                k++;
            }else{
                fields.get(i-k).setFont(new Font("TimesRoman",Font.PLAIN, 25));
                mainPanel.add(fields.get(i-k),c);
            }
        }
        CheckForErrors.tFields=new ArrayList<>();
        CheckForErrors.tFields.addAll(fields.subList(0,2));
        CheckForErrors.tFields.addAll(fields.subList(3,fields.size()));

        JButton createButton = new JButton("Create");
        createButton.setFont(new Font("TimesRoman", Font.PLAIN, 27));
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<List<JTextField>> fieldsList = new ArrayList<>();
                fieldsList.add(fields.subList(0,5));
                fieldsList.add(new ArrayList<>(Arrays.asList(phoneNumberField)));
                fieldsList.add(new ArrayList<>(Arrays.asList(zipCodeField)));
                fieldsList.add(new ArrayList<>(Arrays.asList(passwordField)));

                List<String> errors = CheckForErrors.checkForEmptyErrors();
                List<String> errors2 = CheckForErrors.checkForNotNumbersErrors(new ArrayList<>(Arrays.asList(salaryField)), new ArrayList<>());
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
                }else if(dateOfStart.getDate().before(new java.sql.Date(dateOfBirth.getDate().getYear()+18,dateOfBirth.getDate().getMonth(),dateOfBirth.getDate().getDay()))){
                    JOptionPane.showMessageDialog(null, "Date of Start error!\nEmployee must be >=18 y.o at the date of start", "Error", JOptionPane.ERROR_MESSAGE);
                    dateOfStart.setDate(new Date());
                }else if(!CheckForErrors.checkPhoneNumber(phoneNumberField.getText())){
                    phoneNumberField.setBackground(Color.red);
                    JOptionPane.showMessageDialog(null, "Wrong phone number format. Must be +380xxxxxxxxx", "Error", JOptionPane.ERROR_MESSAGE);
                    phoneNumberField.setText("");
                    phoneNumberField.setBackground(Color.white);
                }else if(!CheckForErrors.checkForUniquePhoneNumber(phoneNumberField.getText(),null)){
                    phoneNumberField.setBackground(Color.red);
                    JOptionPane.showMessageDialog(null, "This phone number is already registered\nPhone number must be unique!", "Error", JOptionPane.ERROR_MESSAGE);
                    phoneNumberField.setText("");
                    phoneNumberField.setBackground(Color.white);
                }else if(passwordField.getText().length()<4){
                    showError("Password min length is 4 symbols!", new JTextField[]{passwordField});
                }else{
                    createNewItem(model,frame);
                    dispose();
                }
            }
        });
        c=new GridBagConstraints();
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 0.5;
        c.weighty=0.5;
        c.fill=HORIZONTAL;
        c.insets = new Insets(5,5,5,0);
        createButton.setBackground(Color.green);
        buttonPanel.add(createButton,c);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("TimesRoman", Font.PLAIN, 27));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwitchFrames.switchFramesForEmployee(frame,model);
                dispose();
            }
        });
        c.gridx = 1;
        c.insets = new Insets(5,10,5,8);
        cancelButton.setBackground(Color.cyan);
        buttonPanel.add(cancelButton,c);
        c.gridy=12;
        c.gridx=0;
        c.gridwidth = 2;
        mainPanel.add(buttonPanel,c);
        backPanel.add(mainPanel);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SwitchFrames.switchFramesForEmployee(frame,model);
                dispose();
            }
        });
    }

    /**
     * Генеруєм випадковий номер працівника
     * @return випадковий номер
     */
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

    /**
     * Додаєм нового працівника
     * @param model модель таблички в основному фреймі
     * @param frame основний фрейм з табличкою
     */
    private void createNewItem(DefaultTableModel model, JFrame frame){
        String number = generateNumber();
        while(bd_connection.Employee.findEmployeeById(number) != null)
            number = generateNumber();
        Employee employee = new Employee(number,surnameField.getText(),nameField.getText(), BCrypt.hashpw(passwordField.getText(), BCrypt.gensalt()),patronymicField.getText(),Employee.Role.valueOf(roleField.getSelectedItem().toString()), new BigDecimal(salaryField.getText()).setScale(4),new java.sql.Date(dateOfBirth.getDate().getTime()), new java.sql.Date(dateOfStart.getDate().getTime()),phoneNumberField.getText(),cityField.getText(),streetField.getText(),zipCodeField.getText());
        bd_connection.Employee.addEmployee(employee);
        EmployeeTableManager.getEmployee_List().add(employee);
        SwitchFrames.switchFramesForEmployee(frame,model);
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
    public void start(DefaultTableModel model, JFrame frame){
        init(model,frame);
        add(backPanel);
        this.pack();
        this.setVisible(true);
    }

}
