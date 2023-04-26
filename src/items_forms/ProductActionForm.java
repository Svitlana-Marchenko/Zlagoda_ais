package items_forms;


import bd_connection.Store_Product;
import entity.Category;
import helpers.*;
import entity.Product;
import info_menu_common.ProductTable2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

import static helpers.ComboBoxStructure.createCategoriesList;
import static helpers.ComboBoxStructure.getIdOfSelectedValue;
import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.NONE;

/**
 * Форма для відображення, редагування та видалення товару
 */
public class ProductActionForm extends JFrame {

    private JPanel backPanel;

    private JTextField captionField;
    private JTextField descriptionField;
    private JTextField producerField;
    private JComboBox categoryField;

    private Product product;

    public ProductActionForm(Product product, DefaultTableModel model, JFrame frame){
        super("Edit product");
        this.product=product;
        this.setSize(600,500);
        start(model,frame);
    }

    /**
     * Початкова ініціалізація графічних об'єктів
     */
    private void init(DefaultTableModel model, JFrame frame){
        ButtonsPanel buttonsPanel = new ButtonsPanel(true);
        final int[] max_length= {50,100};
        List<JTextField> fields = new ArrayList<>();
        backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.PAGE_AXIS));
        JPanel captionPanel = new JPanel();
        captionPanel.setLayout(new BoxLayout(captionPanel, BoxLayout.LINE_AXIS));

        GridBagConstraints c = new GridBagConstraints();
        JLabel captionLabel = new JLabel("Product");
        captionLabel.setHorizontalAlignment(JLabel.CENTER);
        captionLabel.setFont(new Font("TimesRoman", Font.BOLD, 35));
        captionPanel.add(captionLabel);
        backPanel.add(captionPanel);


        JPanel mainPanel = new JPanel(new GridBagLayout());

        JLabel groupLabel = new JLabel("Category: ");
        groupLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 0;
        c.gridx = 0;
        mainPanel.add(groupLabel,c);

        categoryField = new JComboBox();
        categoryField.setFont(new Font("TimesRoman",Font.PLAIN, 20));
        categoryField.setModel(createCategoriesList(false,false,product.getCategory(),false));
        categoryField.setEnabled(false);
        categoryField.setRenderer(new ComboBoxRenderer());
        c.gridx = 1;
        c.weightx = 2;
        c.fill = BOTH;
        mainPanel.add(categoryField,c);

        List<JLabel> labels = new ArrayList<>();

        captionField = new JTextField();
        captionField.setText(product.getName());
        fields.add(captionField);
        labels.add(new JLabel("Name: "));

        descriptionField = new JTextField();
        descriptionField.setText(product.getCharacteristics());
        fields.add(descriptionField);
        labels.add(new JLabel("Characteristics: "));

        producerField = new JTextField();
        producerField.setText(product.getProducer());
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
            fields.get(i).setEditable(false);
            c.gridx=1;
            c.weightx = 2;
            c.fill = BOTH;
            c.ipadx=300;
            mainPanel.add(fields.get(i),c);
        }
        CheckForErrors.tFields=fields;

        buttonsPanel.getEditButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonsPanel.setEnabled(true);
                for(JTextField field: fields){
                    field.setEditable(true);
                }
                categoryField.setEnabled(true);
            }
        });

        buttonsPanel.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<List<JTextField>> fieldsList = new ArrayList<>();
                fieldsList.add(new ArrayList<>(Arrays.asList(captionField,producerField)));
                fieldsList.add(new ArrayList<>(Arrays.asList(descriptionField)));

                List<String> errors = CheckForErrors.checkForEmptyErrors();
                List<String> errors3 = CheckForErrors.checkForLength(max_length,fieldsList);
                if(errors!=null){
                    showError(errors.get(0), CheckForErrors.getErrorTextFields(errors));
                }else if(errors3!=null){
                    showError(errors3.get(0), CheckForErrors.getErrorTextFields(errors3));
                }else{
                    updateItem();
                    buttonsPanel.setEnabled(false);
                    for(JTextField field: fields){
                        field.setEditable(false);
                    }
                    categoryField.setEnabled(false);
                }
            }
        });

        buttonsPanel.getDeleteButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null,"Are you sure? Delete this product?", "Delete category",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if(result == JOptionPane.YES_OPTION){
                    deleteItem(model,frame);
                }
            }
        });

        buttonsPanel.getCancelButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwitchFrames.switchFramesForProduct(frame,model);
                dispose();
            }
        });

        backPanel.add(mainPanel);
        backPanel.add(buttonsPanel);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SwitchFrames.switchFramesForProduct(frame,model);
                dispose();
            }
        });
    }

    /**
     * Редагуєм товар
     */
    private void updateItem() {
        int catId=getIdOfSelectedValue(categoryField);
        Category category = bd_connection.Category.findCategoryById(catId);
        Product temp = new Product(product.getId(),captionField.getText(), category,producerField.getText(), descriptionField.getText());
        if(!temp.equals(product)){
            ProductTable2.getProduct_List().set(ProductTable2.getProduct_List().indexOf(product),temp);
            product=temp;
            bd_connection.Product.updateProductById(product);
        }
    }

    /**
     * Видадяєм товар
     * @param model модель таблички в основному фреймі
     * @param frame основний фрейм з табличкою
     */
    private void deleteItem(DefaultTableModel model, JFrame frame) {
        if(!Store_Product.findStoreProductsByProductId(product.getId()).isEmpty()){
            JOptionPane.showMessageDialog(null,"You can't delete product with products in store!","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        bd_connection.Product.deleteProductById(product.getId());
        ProductTable2.getProduct_List().remove(product);
        SwitchFrames.switchFramesForProduct(frame,model);
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
        }
        for(int i=0;i<fields.length;i++){
            if(fields[i]==captionField)
                captionField.setText(product.getName());
            else if(fields[i]==descriptionField)
                descriptionField.setText(product.getCharacteristics());
            else if(fields[i]==producerField)
                producerField.setText(product.getProducer());
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

