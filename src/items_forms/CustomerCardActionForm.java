package items_forms;

import bd_connection.Check;
import bd_connection.Customer_Card;
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


import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.NONE;

/**
 * Форма для відображення, редагування та видалення карти клієнта
 */
public class CustomerCardActionForm extends JFrame {
    private JPanel backPanel;

    private JTextField nameField;
    private JTextField surnameField;
    private JTextField patronymicField;
    private JTextField phoneNumberField;
    private JTextField cityField;
    private JTextField streetField;
    private JTextField zipCodeField;
    private JTextField percentField;

    private CustomerCard customerCard;

    public CustomerCardActionForm(CustomerCard customerCard, DefaultTableModel model, JFrame frame, boolean shouldIncludeDelete){
        super("Edit customer");
        this.setSize(600,500);
        this.customerCard = customerCard;
        start(model,frame, shouldIncludeDelete);
    }

    /**
     * Початкова ініціалізація графічних об'єктів
     */
    private void init(DefaultTableModel model,JFrame frame,boolean shouldIncludeDelete){
        final int[] max_length = {50,13,9};

        List<JTextField> fields = new ArrayList<>();
        backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.PAGE_AXIS));
        JPanel captionPanel = new JPanel();
        captionPanel.setLayout(new BoxLayout(captionPanel, BoxLayout.LINE_AXIS));

        GridBagConstraints c = new GridBagConstraints();
        JLabel captionLabel = new JLabel("Customer");
        captionLabel.setHorizontalAlignment(JLabel.CENTER);
        captionLabel.setFont(new Font("TimesRoman", Font.BOLD, 40));
        captionPanel.add(captionLabel);
        backPanel.add(captionPanel);
        ButtonsPanel buttonPanel = new ButtonsPanel(shouldIncludeDelete);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        List<JLabel> labels = new ArrayList<>();

        nameField = new JTextField();
        nameField.setText(customerCard.getName());
        fields.add(nameField);
        labels.add(new JLabel("Name: "));

        surnameField = new JTextField();
        surnameField.setText(customerCard.getSurname());
        fields.add(surnameField);
        labels.add(new JLabel("Surname: "));

        patronymicField = new JTextField();
        patronymicField.setText(customerCard.getPatronymic());
        fields.add(patronymicField);
        labels.add(new JLabel("Patronymic: "));

        cityField = new JTextField();
        cityField.setText(customerCard.getCity());
        fields.add(cityField);
        labels.add(new JLabel("City: "));

        streetField = new JTextField();
        streetField.setText(customerCard.getStreet());
        fields.add(streetField);
        labels.add(new JLabel("Street: "));

        phoneNumberField = new JTextField();
        phoneNumberField.setText(customerCard.getPhoneNumber());
        fields.add(phoneNumberField);
        labels.add(new JLabel("Phone number: "));

        zipCodeField = new JTextField();
        zipCodeField.setText(customerCard.getZipCode());
        labels.add(new JLabel("Zip code: "));
        fields.add(zipCodeField);

        percentField = new JTextField();
        percentField.setText(String.valueOf(customerCard.getPercent()));
        fields.add(percentField);
        labels.add(new JLabel("Percent: "));

        for(int i=0;i<labels.size();i++){
            labels.get(i).setFont(new Font("TimesRoman",Font.PLAIN, 25));
            c.gridy = i;
            c.gridx = 0;
            c.weightx = 0;
            c.fill=NONE;
            mainPanel.add(labels.get(i),c);

            fields.get(i).setFont(new Font("TimesRoman",Font.PLAIN, 25));
            fields.get(i).setEditable(false);
            c.gridx=1;
            c.weightx = 1;
            c.fill = BOTH;
            mainPanel.add(fields.get(i),c);
        }
        CheckForErrors.tFields=fields;

        buttonPanel.getEditButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonPanel.setEnabled(true);
                for(JTextField field: fields){
                    field.setEditable(true);
                }
            }
        });

        buttonPanel.getSaveButton().addActionListener(new ActionListener() {
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
                    updateItem();
                    buttonPanel.setEnabled(false);
                    for(JTextField field: fields){
                        field.setEditable(false);
                    }
                }
            }
        });

        if(shouldIncludeDelete){
            buttonPanel.getDeleteButton().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int result = JOptionPane.showConfirmDialog(null,"Are you sure? Delete this customer?", "Delete category",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if(result == JOptionPane.YES_OPTION){
                        deleteItem(model,frame);
                    }
                }
            });
        }

        buttonPanel.getCancelButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwitchFrames.switchFramesForCustomer(frame, model);
                dispose();
            }
        });

        backPanel.add(mainPanel);
        backPanel.add(buttonPanel);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SwitchFrames.switchFramesForCustomer(frame, model);
                dispose();
            }
        });
    }

    /**
     * Редагуєм карту клієнта
     */
    private void updateItem() {
        CustomerCard temp = new CustomerCard(customerCard.getNumber(),nameField.getText(),surnameField.getText(),patronymicField.getText(),phoneNumberField.getText(),cityField.getText(),streetField.getText(),zipCodeField.getText(),Integer.valueOf(percentField.getText()));
        if(!temp.equals(customerCard)){
            Customer_Card.updateCustomerById(temp);
            CustomerTableManager.getCustomerList().set(CustomerTableManager.getCustomerList().indexOf(customerCard),temp);
            customerCard=temp;
        }
    }

    /**
     * Видадяєм карту клієнта
     * @param model модель таблички в основному фреймі
     * @param frame основний фрейм з табличкою
     */
    private void deleteItem(DefaultTableModel model,JFrame frame) {
        if(!Check.getAllReceiptWithCustomer(customerCard).isEmpty()){
            JOptionPane.showMessageDialog(null,"You can't delete customer with checks!","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        Customer_Card.deleteCustomerById(customerCard.getNumber());

        CustomerTableManager.getCustomerList().remove(customerCard);
        SwitchFrames.switchFramesForCustomer(frame,model);
        dispose();
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
    public void start(DefaultTableModel model,JFrame frame,boolean shouldIncludeDelete){
        init(model,frame,shouldIncludeDelete);
        add(backPanel);
        this.pack();
        this.setVisible(true);
    }

}
