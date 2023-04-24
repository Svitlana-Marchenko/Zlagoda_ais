package items_forms;

import bd_connection.Check;
import com.toedter.calendar.JDateChooser;
import helpers.*;

import com.toedter.calendar.JTextFieldDateEditor;
import entity.Employee;
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

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.NONE;

/**
 * Форма для відображення, редагування та видалення працівника
 */
public class EmployeeActionForm extends JFrame {
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

    private Employee employee;


    public EmployeeActionForm(Employee employee, DefaultTableModel model, JFrame frame){
        super("Edit employee");
        this.setSize(600,500);
        this.employee = employee;
        start(model,frame);
    }

    /**
     * Початкова ініціалізація графічних об'єктів
     */
    private void init(DefaultTableModel model, JFrame frame){
        backPanel = new JPanel();
        List<JTextField> fields = new ArrayList<>();
        final int[] max_length = {50,13,9,72};
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.PAGE_AXIS));
        JPanel captionPanel = new JPanel();
        captionPanel.setLayout(new BoxLayout(captionPanel, BoxLayout.LINE_AXIS));
        ButtonsPanel buttonPanel = new ButtonsPanel(true);

        GridBagConstraints c = new GridBagConstraints();
        JLabel captionLabel = new JLabel("Employee");
        captionLabel.setHorizontalAlignment(JLabel.CENTER);
        captionLabel.setFont(new Font("TimesRoman", Font.BOLD, 35));
        captionPanel.add(captionLabel);
        backPanel.add(captionPanel);


        JPanel mainPanel = new JPanel(new GridBagLayout());
        List<JLabel> labels = new ArrayList<>();

        nameField = new JTextField();
        nameField.setText(employee.getName());
        fields.add(nameField);
        labels.add(new JLabel("Name: "));

        surnameField = new JTextField();
        surnameField.setText(employee.getSurname());
        fields.add(surnameField);
        labels.add(new JLabel("Surname: "));

        patronymicField = new JTextField();
        patronymicField.setText(employee.getPatronymic());
        fields.add(patronymicField);
        labels.add(new JLabel("Patronymic: "));

        labels.add(new JLabel("Date of birth: "));

        cityField = new JTextField();
        cityField.setText(employee.getCity());
        fields.add(cityField);
        labels.add(new JLabel("City: "));

        streetField = new JTextField();
        streetField.setText(employee.getStreet());
        fields.add(streetField);
        labels.add(new JLabel("Street: "));

        phoneNumberField = new JTextField();
        phoneNumberField.setText(employee.getPhoneNumber());
        fields.add(phoneNumberField);
        labels.add(new JLabel("Phone number: "));

        zipCodeField = new JTextField();
        zipCodeField.setText(employee.getZipCode());
        labels.add(new JLabel("Zip code: "));
        fields.add(zipCodeField);

        labels.add(new JLabel("Role: "));

        salaryField = new JTextField();
        salaryField.setText(String.valueOf(employee.getSalary()));
        labels.add(new JLabel("Salary: "));
        fields.add(salaryField);

        labels.add(new JLabel("Date of start: "));

        passwordField = new JTextField();
        passwordField.setText(employee.getPassword());
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
                dateOfBirth.setDate(employee.getBirthdate());
                dateOfBirth.setEnabled(false);
                ((JTextFieldDateEditor)dateOfBirth.getDateEditor())
                        .setDisabledTextColor(Color.black);
                dateOfBirth.setFont(new Font("TimesRoman",Font.PLAIN, 20));
                mainPanel.add(dateOfBirth,c);
                k++;
            }else if(i==8){
                String[] roles = Stream.of(Employee.Role.values()).map(Employee.Role::name).toArray(String[]::new);
                roleField = new JComboBox();
                roleField.setFont(new Font("TimesRoman",Font.PLAIN, 20));
                roleField.setEnabled(false);
                for(String role: roles){
                    roleField.addItem(role);
                    if(role==employee.getRole().toString())
                        roleField.setSelectedItem(role);
                }
                roleField.setRenderer(new DefaultListCellRenderer() {
                    @Override
                    public void paint(Graphics g) {
                        setForeground(Color.BLACK);
                        super.paint(g);
                    }
                });
                mainPanel.add(roleField,c);
                k++;
            }else if(i==10){
                dateOfStart = new JDateChooser();
                dateOfStart.setEnabled(false);
                dateOfStart.setDate(employee.getStartDate());
                ((JTextFieldDateEditor)dateOfStart.getDateEditor())
                        .setDisabledTextColor(Color.black);
                dateOfStart.setFont(new Font("TimesRoman", Font.PLAIN, 20));
                mainPanel.add(dateOfStart,c);
                k++;
            }else{
                fields.get(i-k).setFont(new Font("TimesRoman",Font.PLAIN, 25));
                fields.get(i-k).setEditable(false);
                mainPanel.add(fields.get(i-k),c);
            }
        }
        CheckForErrors.tFields=new ArrayList<>();
        CheckForErrors.tFields.addAll(fields.subList(0,2));
        CheckForErrors.tFields.addAll(fields.subList(3,fields.size()));

        buttonPanel.getEditButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonPanel.setEnabled(true);
                for(JTextField field: fields){
                    field.setEditable(true);
                }
                dateOfStart.setEnabled(true);
                dateOfBirth.setEnabled(true);
                roleField.setEnabled(true);
            }
        });

        buttonPanel.getSaveButton().addActionListener(new ActionListener() {
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
                    showError(errors.get(0), CheckForErrors.getErrorTextFields(errors));
                }else if(errors2!=null){
                    showError(errors2.get(0), CheckForErrors.getErrorTextFields(errors2));
                }else if(errors3!=null){
                    showError(errors3.get(0), CheckForErrors.getErrorTextFields(errors3));
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
                    showError("Wrong phone number format. Must be +380xxxxxxxxx", new JTextField[]{phoneNumberField});
                }else{
                    updateItem(1);
                    buttonPanel.setEnabled(false);
                    for(JTextField field: fields){
                        field.setEditable(false);
                    }
                    dateOfStart.setEnabled(false);
                    dateOfBirth.setEnabled(false);
                    roleField.setEnabled(false);
                }
            }
        });

        buttonPanel.getDeleteButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null,"Are you sure? Delete this employee?", "Delete category",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if(result == JOptionPane.YES_OPTION){
                    deleteItem(model,frame);
                }
            }
        });

        buttonPanel.getCancelButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwitchFrames.switchFramesForEmployee(frame,model);
                dispose();
            }
        });

        backPanel.add(mainPanel);
        backPanel.add(buttonPanel);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SwitchFrames.switchFramesForEmployee(frame,model);
                dispose();
            }
        });
    }

    /**
     * Видадяєм працівника з бази
     * @param model модель таблички в основному фреймі
     * @param frame основний фрейм з табличкою
     */
    private void deleteItem(DefaultTableModel model, JFrame frame) {
        if(!Check.getAllReceiptWithEmployee(employee).isEmpty()){
            JOptionPane.showMessageDialog(null,"You can't delete employee with checks!","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        bd_connection.Employee.deleteEmployeeById(employee.getId());

        EmployeeTableManager.getEmployee_List().remove(employee);
        SwitchFrames.switchFramesForEmployee(frame,model);
        dispose();
    }

    /**
     * Редагуєм інформацію про працівника
     */
    private void updateItem(int index) {
        Employee temp = new Employee(employee.getId(),surnameField.getText(),nameField.getText(),passwordField.getText(),patronymicField.getText(),Employee.Role.valueOf(roleField.getSelectedItem().toString()), new BigDecimal(salaryField.getText()).setScale(4),new java.sql.Date(dateOfBirth.getDate().getTime()), new java.sql.Date(dateOfStart.getDate().getTime()),phoneNumberField.getText(),cityField.getText(),streetField.getText(),zipCodeField.getText());
        if(!temp.equals(employee)){
            index=EmployeeTableManager.getEmployee_List().indexOf(employee);
            EmployeeTableManager.getEmployee_List().set(index,temp);
            bd_connection.Employee.updateEmployeeById(temp);
            employee=temp;
        }
    }

    /**
     * Відображаєм помилку
     * @param text текст помилки
     * @param fields поля з помилкою
     */
    private void showError(String text, JTextField[] fields){
        for(int i=0;i<fields.length;i++){
            fields[i].setForeground(Color.red);
        }
        JOptionPane.showMessageDialog(null,text,"Error",JOptionPane.ERROR_MESSAGE);
        for(int i=0;i<fields.length;i++){
            fields[i].setForeground(Color.black);
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
