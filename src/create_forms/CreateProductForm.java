package create_forms;

import bd_connection.*;
import entity.Category;
import entity.Product;

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

/**
 * Даний клас реалізує графічний інтерфейс та функціонал створення нового товару
 */
public class CreateProductForm extends JFrame {

    private JPanel mainPanel;
    private JPanel backPanel;
    private JPanel captionPanel;

    private JTextField captionField;
    private JTextField descriptionField;
    private JTextField producerField;
    private JComboBox categoryField;
    private JButton createButton;
    private JButton cancelButton;
    private static final int[] max_length= {50,100};

    public CreateProductForm(){
        super("Add new product");
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
        JLabel captionLabel = new JLabel("New product");
        captionLabel.setHorizontalAlignment(JLabel.CENTER);
        captionLabel.setFont(new Font("TimesRoman", Font.BOLD, 35));
        captionPanel.add(captionLabel);
        backPanel.add(captionPanel);


        mainPanel = new JPanel(new GridBagLayout());
        JLabel groupLabel = new JLabel("Category: ");
        groupLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 0;
        c.gridx = 0;

        mainPanel.add(groupLabel,c);

        ArrayList<Category> categoryArrayList = bd_connection.Category.findAll();
        categoryField = new JComboBox();
        categoryField.setFont(new Font("TimesRoman",Font.PLAIN, 20));

        for(Category category: categoryArrayList){
            categoryField.addItem(category.getName());
        }
        c.gridy = 0;
        c.gridx = 1;
        c.weightx = 2;
        c.fill = BOTH;

        mainPanel.add(categoryField,c);

        c.fill = NONE;
        c.gridwidth=1;
        JLabel getCaptionLabel = new JLabel("Name: ");
        getCaptionLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 1;
        c.gridx = 0;
        mainPanel.add(getCaptionLabel,c);

        captionField = new JTextField();
        captionField.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        c.gridy = 1;
        c.gridx = 1;
        c.weightx=2;
        c.ipadx=300;
        c.fill = BOTH;
        mainPanel.add(captionField,c);

        JLabel descriptionLabel = new JLabel("Description: ");
        descriptionLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 2;
        c.gridx = 0;
        c.weightx = 0;
        c.fill = NONE;
        c.ipadx=0;
        mainPanel.add(descriptionLabel,c);

        descriptionField = new JTextField();
        descriptionField.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        c.gridy = 2;
        c.gridx = 1;
        c.weightx = 1;
        c.fill = BOTH;

        mainPanel.add(descriptionField,c);

        JLabel producerLabel = new JLabel("Producer: ");
        producerLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 3;
        c.gridx = 0;
        c.weightx = 0;
        c.fill = NONE;
        mainPanel.add(producerLabel,c);

        producerField = new JTextField();
        producerField.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        c.gridy = 3;
        c.gridx = 1;
        c.weightx = 1;
        c.fill = BOTH;

        mainPanel.add(producerField,c);

        c.fill = NONE;
        createButton = new JButton("Create");
        createButton.setFont(new Font("TimesRoman", Font.PLAIN, 27));
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<List<JTextField>> fieldsList = new ArrayList<>();
                fieldsList.add(new ArrayList<>());
                fieldsList.get(0).add(captionField);
                fieldsList.get(0).add(producerField);
                fieldsList.add(new ArrayList<>());
                fieldsList.get(1).add(descriptionField);
                List<JTextField> fields = new ArrayList<>();
                fields.add(captionField);
                fields.add(descriptionField);
                fields.add(producerField);
                CheckForErrors.tFields=fields;
                List<String> errors = CheckForErrors.checkForEmptyErrors();
                List<String> errors2 = CheckForErrors.checkForLength(max_length,fieldsList);
                if(errors!=null){
                    showError(errors.get(0),CheckForErrors.getErrorTextFields(errors));
                }else if(errors2!=null){
                    showError(errors2.get(0),CheckForErrors.getErrorTextFields(errors2));
                }else{
                    createNewItem();
                    dispose();
                }
            }
        });
        c.gridy = 4;
        c.gridx = 0;
        c.weighty = 0.5;

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
        c.gridy = 4;
        c.gridx = 1;

        c.insets = new Insets(10,60,10,0);
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

    private void createNewItem(){
        Random random = new Random();
        int id=random.nextInt();
        while(bd_connection.Product.findProductById(id) != null)
            id=random.nextInt();
        Category category = bd_connection.Category.findCategoryByName(categoryField.getSelectedItem().toString());
        Product product = new Product(id,captionField.getText(), category,producerField.getText(), descriptionField.getText());
        bd_connection.Product.addProduct(product);
        System.out.println(bd_connection.Product.findAll());
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
