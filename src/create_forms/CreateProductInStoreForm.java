package create_forms;

import bd_connection.Category;
import bd_connection.Store_Product;
import entity.Product;
import entity.ProductInStore;
import helpers.*;
import info_menu_common.StoreProductTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;

import java.math.RoundingMode;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static helpers.ComboBoxStructure.*;
import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.NONE;

/**
 * Даний клас реалізує графічний інтерфейс та функціонал створення нового товару на складі
 */
public class CreateProductInStoreForm extends JFrame {
    private JPanel mainPanel;
    private JPanel backPanel;
    private JPanel captionPanel;

    private JTextField countField;
    private JTextField priceField;
    private JComboBox productField;
    private JComboBox categoryField;
    private JCheckBox isPromotional;


    public CreateProductInStoreForm(DefaultTableModel model,JFrame frame){
        super("Add product in store");
        this.setSize(600,500);
        start(model,frame);
    }

    /**
     * Початкова ініціалізація графічних об'єктів
     */
    private void init(DefaultTableModel model,JFrame frame)  {
        DefaultComboBoxModel promotionalCategoriesList = createCategoriesList(true,true,null,false);
        DefaultComboBoxModel notPromotionalCategoriesList=createCategoriesList(false,true,null,false);
        ArrayList<entity.Category> cats = getCategoriesArrayList(true,true);
        DefaultComboBoxModel promotionalProductsList= createProductsList(cats.isEmpty() ? null : cats.get(0),true,null, false);
        cats = getCategoriesArrayList(false,true);
        DefaultComboBoxModel notPromotionalProductsList= createProductsList(cats.isEmpty() ? null : cats.get(0),false,null, false);

        backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.PAGE_AXIS));
        captionPanel = new JPanel();
        captionPanel.setLayout(new BoxLayout(captionPanel, BoxLayout.LINE_AXIS));

        GridBagConstraints c = new GridBagConstraints();
        JLabel captionLabel = new JLabel("New product in store");
        captionLabel.setHorizontalAlignment(JLabel.CENTER);
        captionLabel.setFont(new Font("TimesRoman", Font.BOLD, 35));
        captionPanel.add(captionLabel);
        backPanel.add(captionPanel);

        mainPanel = new JPanel(new GridBagLayout());

        isPromotional = new JCheckBox("promotional");
        isPromotional.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 0;
        c.gridx = 0;
        c.gridwidth=2;
        if(promotionalProductsList==null)
            isPromotional.setEnabled(false);
        if(notPromotionalCategoriesList==null){
            isPromotional.setSelected(true);
            isPromotional.setEnabled(false);
        }
        isPromotional.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    priceField.setText("");
                    priceField.setEditable(false);
                    categoryField.setModel(promotionalCategoriesList);
                    productField.setModel(promotionalProductsList);
                }
                else {
                    priceField.setEditable(true);
                    categoryField.setModel(notPromotionalCategoriesList);
                    productField.setModel(notPromotionalProductsList);
                }
            }
        });

        mainPanel.add(isPromotional,c);
        c.gridwidth=1;

        JLabel groupLabel = new JLabel("Category: ");
        groupLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 1;
        mainPanel.add(groupLabel,c);

        categoryField = new JComboBox();
        if(notPromotionalCategoriesList!=null)
            categoryField.setModel(notPromotionalCategoriesList);
        else
            categoryField.setModel(promotionalCategoriesList);
        categoryField.setFont(new Font("TimesRoman",Font.PLAIN, 20));

        categoryField.setRenderer(new ComboBoxRenderer());

        c.gridx = 1;
        c.fill = BOTH;

        categoryField.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Object obj = categoryField.getSelectedItem();
                Object obj1 = e.getItem();
                if(obj!=obj1 && obj!=null && obj1!=null){
                    int id=-1;
                    if (obj instanceof Object[]) {
                        Object[] rowData = (Object[]) obj;
                        id = Integer.valueOf(rowData[0].toString());
                    }
                    productField.setModel(createProductsList(Category.findCategoryById(id),isPromotional.isSelected(),null,false));
                }
            }
        });
        mainPanel.add(categoryField,c);

        JLabel productLabel = new JLabel("Product: ");
        productLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 2;
        c.gridx = 0;
        c.fill=NONE;
        mainPanel.add(productLabel,c);

        productField = new JComboBox();
        if(notPromotionalProductsList!=null)
            productField.setModel(notPromotionalProductsList);
        else
            productField.setModel(promotionalProductsList);

        productField.setFont(new Font("TimesRoman",Font.PLAIN, 20));

        productField.setRenderer(new ComboBoxRenderer());
        c.gridx = 1;
        c.fill = BOTH;
        mainPanel.add(productField,c);

        List<JLabel> labels = new ArrayList<>();
        List<JTextField> fields = new ArrayList<>();

        countField = new JTextField();
        fields.add(countField);
        labels.add(new JLabel("Quantity: "));

        priceField = new JTextField();
        if(notPromotionalProductsList==null)
            priceField.setEditable(false);
        fields.add(priceField);
        labels.add(new JLabel("Price: "));

        for(int i=0;i<fields.size();i++){
            labels.get(i).setFont(new Font("TimesRoman",Font.PLAIN, 25));
            c.gridy = i+3;
            c.gridx = 0;
            c.fill=NONE;
            c.ipadx=0;
            c.weightx=0;
            mainPanel.add(labels.get(i),c);

            fields.get(i).setFont(new Font("TimesRoman",Font.PLAIN, 25));
            c.gridx=1;
            c.ipadx=300;
            c.weightx=2;
            c.fill = BOTH;
            mainPanel.add(fields.get(i),c);
        }
        CheckForErrors.tFields=fields;

        JButton createButton = new JButton("Create");
        createButton.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> errors = CheckForErrors.checkForEmptyErrors();
                List<String> errors1 = CheckForErrors.checkForNotNumbersErrors(new ArrayList<>(Arrays.asList(priceField)),new ArrayList<>(Arrays.asList(countField)));
                if(errors!=null){
                    showError(errors.get(0), CheckForErrors.getErrorTextFields(errors));
                }else if(errors1!=null){
                    showError(errors1.get(0), CheckForErrors.getErrorTextFields(errors1));
                }else{
                    createNewItem(model,frame);
                    dispose();
                }
            }
        });
        c.fill=NONE;
        c.gridy = 5;
        c.gridx = 0;
        c.weighty = 0.5;
        c.ipadx = 0;
        c.gridwidth = 0;
        c.insets = new Insets(10,0,10,200);
        createButton.setBackground(Color.green);
        mainPanel.add(createButton,c);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwitchFrames.switchFramesForStoreProduct(frame,model);
                dispose();
            }
        });
        c.gridx = 1;
        c.insets = new Insets(10,90,10,0);
        cancelButton.setBackground(Color.cyan);
        mainPanel.add(cancelButton,c);
        backPanel.add(mainPanel);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SwitchFrames.switchFramesForStoreProduct(frame,model);
                dispose();
            }
        });
    }


    /**
     * Генеруєм UPC товару на складі
     * @return UPC
     */
    public String generateUPC() {
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
     * Додаєм новий товар на складі
     * @param model модель таблички в основному фреймі
     * @param frame основний фрейм з табличкою
     */
    private void createNewItem(DefaultTableModel model,JFrame frame){
        String UPC = generateUPC();
        while(Store_Product.findProductInStoreById(UPC) != null)
            UPC = generateUPC();
        int id=getIdOfSelectedValue(productField);
        Product product = bd_connection.Product.findProductById(id);
        ProductInStore productInStore=null;
        if(!isPromotional.isSelected()){
            productInStore= new ProductInStore(UPC, null, product, new BigDecimal(priceField.getText()).setScale(4),Integer.valueOf(countField.getText()),false);
            Store_Product.addProductInStore(productInStore);
        }else{
            ProductInStore mainProduct = Store_Product.findStoreProductsByProductId(id).get(0);
            productInStore = new ProductInStore(UPC, null, product, mainProduct.getPrice().multiply(new BigDecimal(0.8)).setScale(4, RoundingMode.HALF_DOWN),Integer.valueOf(countField.getText()),true);
            mainProduct.setPromotionalUPC(UPC);
            Store_Product.addProductInStore(productInStore);
            Store_Product.updateProductInStoreById(mainProduct);
        }
        StoreProductTable.getStore_productListList().add(productInStore);
        SwitchFrames.switchFramesForStoreProduct(frame,model);
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
    public void start(DefaultTableModel model,JFrame frame) {
        init(model,frame);
        add(backPanel);
        this.pack();
        this.setVisible(true);
    }

}
