package create_forms;

import bd_connection.*;
import entity.Category;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.NONE;

public class CreateCategoryForm extends JFrame {

    private JPanel mainPanel;
    private JPanel backPanel;
    private JPanel captionPanel;

    private JTextField captionField;
    private JButton createButton;
    private JButton cancelButton;

    private static final int max_length=50;

    public CreateCategoryForm(){
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
        JLabel captionLabel = new JLabel("New Category");
        captionLabel.setFont(new Font("TimesRoman", Font.BOLD, 30));
        captionPanel.add(captionLabel);
        backPanel.add(captionPanel);

        mainPanel = new JPanel(new GridBagLayout());

        JLabel getCaptionLabel = new JLabel("Caption: ");
        getCaptionLabel.setFont(new Font("TimesRoman",Font.PLAIN, 20));
        c.gridy = 0;
        c.gridx = 0;
        mainPanel.add(getCaptionLabel,c);

        captionField = new JTextField();
        captionField.setFont(new Font("TimesRoman", Font.PLAIN, 20));

        c.gridy = 0;
        c.gridx = 1;
        c.weightx = 1;
        c.fill = BOTH;
        mainPanel.add(captionField,c);

        c.fill = NONE;
        createButton = new JButton("Create");
        createButton.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<JTextField> textFields = new ArrayList<>();
                textFields.add(captionField);
                CheckForErrors.tFields=textFields;
                List<String> errors = CheckForErrors.checkForEmptyErrors();
                if(errors!=null){
                    showError(errors.get(0),CheckForErrors.getErrorTextFields(errors));
                }else if(!CheckForErrors.checkForLength(max_length,0)){
                    errors= new ArrayList<>();
                    errors.add("Caption is too long. Maximum: 50 symbols.");
                    errors.add("1");
                    showError(errors.get(0),CheckForErrors.getErrorTextFields(errors));
                }else{
                    createNewItem();
                    dispose();
                }
            }

        });
        c.gridy = 1;
        c.gridx = 0;
        c.weighty = 0.5;
        c.ipadx = 0;
        c.gridwidth = 0;
        c.insets = new Insets(10,0,10,200);
        createButton.setBackground(Color.green);
        mainPanel.add(createButton,c);
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        c.gridx = 1;
        c.insets = new Insets(10,60,10,0);
        cancelButton.setBackground(Color.cyan);
        mainPanel.add(cancelButton,c);
        backPanel.add(mainPanel);
    }

    private void createNewItem(){
        Random random = new Random();
        int id=random.nextInt();
        while(bd_connection.Category.findCategoryById(id) != null)
            id=random.nextInt();
        Category category = new Category(id, captionField.getText());
        bd_connection.Category.addCategory(category);
        System.out.println(bd_connection.Category.findAll());
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

}