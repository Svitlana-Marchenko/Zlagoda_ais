package create_forms;

import entity.Category;
import entity.Product;
import helpers.*;
import info_menu_common.ProductTable2;

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

import static helpers.ComboBoxStructure.createCategoriesList;
import static helpers.ComboBoxStructure.getIdOfSelectedValue;
import static java.awt.GridBagConstraints.*;

/**
 * Даний клас реалізує графічний інтерфейс та функціонал створення нового товару
 */
public class CreateProductForm extends JFrame {

    private JPanel backPanel;

    private JTextField captionField;
    private JTextField descriptionField;
    private JTextField producerField;
    private JComboBox categoryField;


    public CreateProductForm(DefaultTableModel model, JFrame frame){
        super("Add new product");
        this.setSize(600,500);
        this.setLocation(600,100);
        start(model,frame);
    }

    /**
     * Початкова ініціалізація графічних об'єктів
     */
    private void init(DefaultTableModel model, JFrame frame){
        final int[] max_length= {50,100};

        backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.PAGE_AXIS));
        JPanel captionPanel = new JPanel();
        captionPanel.setLayout(new BoxLayout(captionPanel, BoxLayout.LINE_AXIS));

        GridBagConstraints c = new GridBagConstraints();
        JLabel captionLabel = new JLabel("New product");
        captionLabel.setHorizontalAlignment(JLabel.CENTER);
        captionLabel.setFont(new Font("TimesRoman", Font.BOLD, 35));
        captionPanel.add(captionLabel);
        backPanel.add(captionPanel);

        JPanel buttonPanel = new JPanel();

        JPanel mainPanel = new JPanel(new GridBagLayout());
        JLabel groupLabel = new JLabel("Category: ");
        groupLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 0;
        c.gridx = 0;

        mainPanel.add(groupLabel,c);

        categoryField = new JComboBox();
        categoryField.setModel(createCategoriesList(false,false,null,false));
        categoryField.setFont(new Font("TimesRoman",Font.PLAIN, 20));

        categoryField.setRenderer(new ComboBoxRenderer());
        c.gridy = 0;
        c.gridx = 1;
        c.weightx = 2;
        c.fill = BOTH;

        mainPanel.add(categoryField,c);

        List<JLabel> labels = new ArrayList<>();
        List<JTextField> fields = new ArrayList<>();

        captionField = new JTextField();
        fields.add(captionField);
        labels.add(new JLabel("Name: "));

        descriptionField = new JTextField();
        fields.add(descriptionField);
        labels.add(new JLabel("Characteristics: "));

        producerField = new JTextField();
        fields.add(producerField);
        labels.add(new JLabel("Producer: "));

        for(int i=0;i<labels.size();i++){
            labels.get(i).setFont(new Font("TimesRoman",Font.PLAIN, 25));
            c.gridy = i+1;
            c.gridx = 0;
            c.weightx = 0;
            c.ipadx=0;
            c.fill=NONE;
            mainPanel.add(labels.get(i),c);

            fields.get(i).setFont(new Font("TimesRoman",Font.PLAIN, 25));
            c.gridx=1;
            c.weightx = 2;
            c.fill = BOTH;
            c.ipadx=300;
            mainPanel.add(fields.get(i),c);
        }
        CheckForErrors.tFields=fields;

        JButton createButton = new JButton("Create");
        createButton.setFont(new Font("TimesRoman", Font.PLAIN, 27));
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<List<JTextField>> fieldsList = new ArrayList<>();
                fieldsList.add(new ArrayList<>(Arrays.asList(captionField,producerField)));
                fieldsList.add(new ArrayList<>(Arrays.asList(descriptionField)));

                List<String> errors = CheckForErrors.checkForEmptyErrors();
                List<String> errors2 = CheckForErrors.checkForLength(max_length,fieldsList);
                if(errors!=null){
                    showError(errors.get(0),CheckForErrors.getErrorTextFields(errors));
                }else if(errors2!=null){
                    showError(errors2.get(0),CheckForErrors.getErrorTextFields(errors2));
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
        c.fill=HORIZONTAL;
        c.insets = new Insets(5,5,5,0);
        createButton.setBackground(Color.green);
        buttonPanel.add(createButton,c);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("TimesRoman", Font.PLAIN, 27));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwitchFrames.switchFramesForProduct(frame,model);
                dispose();
            }
        });
        c.gridx = 1;
        c.insets = new Insets(5,10,5,8);
        cancelButton.setBackground(Color.cyan);
        buttonPanel.add(cancelButton,c);
        c.gridy=4;
        c.gridx=0;
        c.gridwidth = 2;
        mainPanel.add(buttonPanel,c);
        backPanel.add(mainPanel);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SwitchFrames.switchFramesForProduct(frame,model);
                dispose();
            }
        });
    }

    /**
     * Додаєм новий товар
     * @param model модель таблички в основному фреймі
     * @param frame основний фрейм з табличкою
     */
    private void createNewItem(DefaultTableModel model, JFrame frame){
        Random random = new Random();
        int id=random.nextInt();
        while(bd_connection.Product.findProductById(id) != null)
            id=random.nextInt();
        int catId=getIdOfSelectedValue(categoryField);
        Category category = bd_connection.Category.findCategoryById(catId);
        Product product = new Product(id,captionField.getText(), category,producerField.getText(), descriptionField.getText());
        bd_connection.Product.addProduct(product);
        ProductTable2.getProduct_List().add(product);
        SwitchFrames.switchFramesForProduct(frame,model);
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
