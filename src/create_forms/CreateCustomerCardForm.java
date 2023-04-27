package create_forms;

import bd_connection.*;
import entity.CustomerCard;
import helpers.*;
import info_menu_manager.CustomerTableManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.awt.GridBagConstraints.*;

/**
 * Форма створення карти нового постійного клієнта
 */
public class CreateCustomerCardForm extends JFrame {

    private JPanel backPanel;

    private JTextField nameField;
    private JTextField surnameField;
    private JTextField patronymicField;
    private JTextField phoneNumberField;
    private JTextField cityField;
    private JTextField streetField;
    private JTextField zipCodeField;
    private JTextField percentField;


    public CreateCustomerCardForm(DefaultTableModel model,JFrame frame){
        super("Add new customer");
        this.setSize(600,500);
        this.setLocation(600,100);
        start(frame,model);
    }

    /**
     * Початкова ініціалізація графічних об'єктів
     */
    private void init(JFrame frame,DefaultTableModel model){
        final int[] max_length = {50,13,9};

        backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.PAGE_AXIS));
        JPanel captionPanel = new JPanel();
        captionPanel.setLayout(new BoxLayout(captionPanel, BoxLayout.LINE_AXIS));

        GridBagConstraints c = new GridBagConstraints();
        JLabel captionLabel = new JLabel("New customer");
        captionLabel.setHorizontalAlignment(JLabel.CENTER);
        captionLabel.setFont(new Font("TimesRoman", Font.BOLD, 40));
        captionPanel.add(captionLabel);
        backPanel.add(captionPanel);

        JPanel buttonPanel = new JPanel(new GridBagLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        List<JLabel> labels = new ArrayList<>();
        ArrayList<JTextField> fields = new ArrayList<>();

        nameField = new JTextField();
        fields.add(nameField);
        labels.add(new JLabel("Name: "));

        surnameField = new JTextField();
        fields.add(surnameField);
        labels.add(new JLabel("Surname: "));

        patronymicField = new JTextField();
        fields.add(patronymicField);
        labels.add(new JLabel("Patronymic: "));

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

        percentField = new JTextField();
        fields.add(percentField);
        labels.add(new JLabel("Percent: "));

        for(int i=0;i<labels.size();i++){
            labels.get(i).setFont(new Font("TimesRoman",Font.PLAIN, 25));
            c.gridy = i;
            c.gridx = 0;
            c.weightx = 0;
            c.ipadx=0;
            c.fill=NONE;
            mainPanel.add(labels.get(i),c);

            fields.get(i).setFont(new Font("TimesRoman",Font.PLAIN, 25));
            c.gridx=1;
            c.weightx = 2;
            c.ipadx=300;
            c.fill = BOTH;
            mainPanel.add(fields.get(i),c);
        }
        CheckForErrors.tFields=new ArrayList<>();
        CheckForErrors.tFields.addAll(fields.subList(0,2));
        CheckForErrors.tFields.add(phoneNumberField);
        CheckForErrors.tFields.addAll(fields.subList(7,fields.size()));

        JButton createButton = new JButton("Create");
        createButton.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<java.util.List<JTextField>> fieldsList= new ArrayList<>();
                fieldsList.add(fields.subList(0,5));
                fieldsList.add(new ArrayList<>(Arrays.asList(phoneNumberField)));
                fieldsList.add(new ArrayList<>(Arrays.asList(zipCodeField)));

                List<String> errors = CheckForErrors.checkForEmptyErrors();
                List<String> errors2 = CheckForErrors.checkForNotNumbersErrors(new ArrayList<>(),new ArrayList<>(Arrays.asList(percentField)));
                List<String> errors3 = CheckForErrors.checkForLength(max_length,fieldsList);
                if(errors!=null){
                    showError(errors.get(0), CheckForErrors.getErrorTextFields(errors));
                }else if(errors2!=null){
                    showError(errors2.get(0), CheckForErrors.getErrorTextFields(errors2));
                }else if(errors3!=null){
                    showError(errors3.get(0), CheckForErrors.getErrorTextFields(errors3));
                }else if(Integer.valueOf(percentField.getText())>100){
                    showError("Percent must be <=100", new JTextField[]{percentField});
                }else if(!CheckForErrors.checkPhoneNumber(phoneNumberField.getText())){
                    showError("Wrong phone number format. Must be +380xxxxxxxxx", new JTextField[]{phoneNumberField});
                }else{
                    createNewItem(frame,model);
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
        cancelButton.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwitchFrames.switchFramesForCustomer(frame, model);
                dispose();
            }
        });
        c.gridx = 1;
        c.insets = new Insets(5,10,5,8);
        cancelButton.setBackground(Color.cyan);
        buttonPanel.add(cancelButton,c);
        c.gridy=8;
        c.gridx=0;
        c.gridwidth = 2;
        mainPanel.add(buttonPanel,c);
        backPanel.add(mainPanel);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SwitchFrames.switchFramesForCustomer(frame, model);
                dispose();
            }
        });
    }

    /**
     * Генеруєм випадковий номер покупця
     * @return випадковий номер
     */
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

    /**
     * Створюєм карту нового клієнта
     * @param model модель таблички в основному фреймі
     * @param frame основний фрейм з табличкою
     */
    private void createNewItem(JFrame frame,DefaultTableModel model){
        String number = generateNumber();
        while(Customer_Card.findCustomerCardById(number) != null)
            number = generateNumber();
        CustomerCard customerCard = new CustomerCard(number,surnameField.getText(),nameField.getText(),patronymicField.getText(),phoneNumberField.getText(),cityField.getText(),streetField.getText(),zipCodeField.getText(),Integer.valueOf(percentField.getText()));
        Customer_Card.addCustomer(customerCard);
        CustomerTableManager.getCustomerList().add(customerCard);
        SwitchFrames.switchFramesForCustomer(frame,model);
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
    public void start(JFrame frame,DefaultTableModel model){
        init(frame,model);
        add(backPanel);
        this.pack();
        this.setVisible(true);
    }

}
