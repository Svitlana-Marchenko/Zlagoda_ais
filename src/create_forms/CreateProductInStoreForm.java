package create_forms;

import bd_connection.Category;
import bd_connection.Store_Product;
import entity.Product;
import entity.ProductInStore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;

import java.util.ArrayList;

import java.util.List;
import java.util.Random;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.NONE;

public class CreateProductInStoreForm extends JFrame {
    private JPanel mainPanel;
    private JPanel backPanel;
    private JPanel captionPanel;

    private JTextField countField;
    private JTextField priceField;
    private JComboBox productField;
    private JComboBox categoryField;
    private JCheckBox isPromotional;
    private JButton createButton;
    private JButton cancelButton;
    private DefaultComboBoxModel promotionalCategoriesList;
    private DefaultComboBoxModel promotionalProductsList;
    private DefaultComboBoxModel notPromotionalCategoriesList;
    private DefaultComboBoxModel notPromotionalProductsList;

    public CreateProductInStoreForm(){
        super("Add product in store");
        this.setSize(600,500);
        start();
    }

    /**
     * Початкова ініціалізація графічних об'єктів
     */
    private void init()  {
        promotionalCategoriesList = createCategoriesList(true);
        notPromotionalCategoriesList=createCategoriesList(false);

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
        isPromotional.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    priceField.setText("");
                    priceField.setEnabled(false);
                    categoryField.setModel(promotionalCategoriesList);
                    productField.setModel(promotionalProductsList);
                }
                else {
                    priceField.setEnabled(true);
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
        c.gridx = 0;
        mainPanel.add(groupLabel,c);

        categoryField = new JComboBox();
        categoryField.setModel(notPromotionalCategoriesList);
        categoryField.setFont(new Font("TimesRoman",Font.PLAIN, 20));

        categoryField.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Object[]) {
                    Object[] rowData = (Object[]) value;
                    setText(rowData[0].toString() + "   " + rowData[1].toString());
                }
                return this;
            }
        });

        c.gridy = 1;
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
                    productField.setModel(createProductsList(Category.findCategoryById(id),isPromotional.isSelected()));
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
        productField.setModel(notPromotionalProductsList);
        productField.setFont(new Font("TimesRoman",Font.PLAIN, 20));

        productField.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Object[]) {
                    Object[] rowData = (Object[]) value;
                    setText(rowData[0].toString() + "   " + rowData[1].toString());
                }
                return this;
            }
        });
        c.gridy = 2;
        c.gridx = 1;
        c.fill = BOTH;
        mainPanel.add(productField,c);

        JLabel countLabel = new JLabel("Quantity: ");
        countLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 3;
        c.fill = NONE;
        c.gridx = 0;
        mainPanel.add(countLabel,c);

        countField = new JTextField();
        c.fill=BOTH;
        countField.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        c.gridy = 3;
        c.gridx = 1;
        mainPanel.add(countField,c);

        JLabel priceLabel = new JLabel("Price: ");
        priceLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 4;
        c.fill = NONE;
        c.gridx = 0;
        mainPanel.add(priceLabel,c);

        priceField = new JTextField();
        priceField.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        c.gridy = 4;
        c.gridx = 1;
        c.fill = BOTH;
        c.weightx = 1;
        c.ipadx = 300;
        mainPanel.add(priceField,c);

        c.fill = NONE;
        createButton = new JButton("Create");
        createButton.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<JTextField> fields = new ArrayList<>();
                List<JTextField> checkForInt = new ArrayList<>();
                List<JTextField> checkForDouble = new ArrayList<>();
                fields.add(priceField);
                fields.add(countField);
                checkForDouble.add(priceField);
                checkForInt.add(countField);
                CheckForErrors.tFields=fields;
                List<String> errors = CheckForErrors.checkForEmptyErrors();
                List<String> errors1 = CheckForErrors.checkForNotNumbersErrors(checkForDouble,checkForInt);
                if(errors!=null){
                    showError(errors.get(0),CheckForErrors.getErrorTextFields(errors));
                }else if(errors1!=null){
                    showError(errors1.get(0),CheckForErrors.getErrorTextFields(errors1));
                }else{
                    createNewItem();
                    dispose();
                }
            }
        });
        c.gridy = 5;
        c.gridx = 0;
        c.weighty = 0.5;
        c.ipadx = 0;
        c.gridwidth = 0;
        c.insets = new Insets(10,0,10,200);
        createButton.setBackground(Color.green);
        mainPanel.add(createButton,c);
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        c.gridy = 5;
        c.gridx = 1;
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

    private DefaultComboBoxModel createProductsList(entity.Category category, boolean condition)  {
        ArrayList<Product> productArrayList = (ArrayList<Product>) bd_connection.Product.getAllProductsInCategorySorted(true, category);
        for(int j=0;j<productArrayList.size();j++){
            List<ProductInStore> productInStores = Store_Product.findStoreProductsByProductId(productArrayList.get(j).getId());
            if(condition ? (productInStores.size()==2 || productInStores.size()==0) : productInStores.size()==2 || productInStores.size()==1) {
                productArrayList.remove(j--);
            }
        }
        return createComboBoxModelForProduct(productArrayList);
    }
    private DefaultComboBoxModel createComboBoxModelForProduct(ArrayList<Product> productArrayList){
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (int i = 0; i < productArrayList.size(); i++) {
            Object[] rowData = new Object[4];
            for (int j = 0; j < rowData.length; j++) {
                rowData[j] = createProductRow(productArrayList.get(i)).getValueAt(0, j);
            }
            model.addElement(rowData);
        }
        return model;
    }

    private DefaultComboBoxModel createCategoriesList(boolean condition) {
        ArrayList<entity.Category> categoryArrayList = Category.findAll();
        for(int i=0;i<categoryArrayList.size();i++){
            int count=0;
            List<Product> products = bd_connection.Product.getAllProductsInCategorySorted(true,categoryArrayList.get(i));
            for(int j=0;j<products.size();j++){
                List<ProductInStore> productInStores = Store_Product.findStoreProductsByProductId(products.get(j).getId());
                if(condition ? (productInStores.size()==2 || productInStores.size()==0) : productInStores.size()==2 || productInStores.size()==1)
                    count++;
            }
            if(count==products.size()){
                categoryArrayList.remove(i--);
            }
        }
        DefaultComboBoxModel model1 = new DefaultComboBoxModel();
        for (int i = 0; i < categoryArrayList.size(); i++) {
            Object[] rowData = new Object[2];
            for (int j = 0; j < rowData.length; j++) {
                rowData[j] = createCategoryRow(categoryArrayList.get(i)).getValueAt(0, j);
            }
            model1.addElement(rowData);
        }
        if(condition)
            promotionalProductsList= createProductsList(categoryArrayList.get(0),condition);
        else
            notPromotionalProductsList= createProductsList(categoryArrayList.get(0),condition);
        return model1;
    }

    private JTable createProductRow(Product product){
        JTable table = new JTable();
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Id", "Name", "Characteristics", "Producer"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);
        model.addRow(new Object[]{product.getId(), product.getName(), product.getCharacteristics(), product.getProducer()});
        return table;
    }

    private JTable createCategoryRow(entity.Category category){
        JTable table = new JTable();
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Id", "Name"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);
        model.addRow(new Object[]{category.getId(), category.getName()});
        return table;
    }

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

    private void createNewItem(){
        String UPC = generateUPC();
        while(Store_Product.findProductInStoreById(UPC) != null)
            UPC = generateUPC();
        int id=-1;
        if (productField.getSelectedItem() instanceof Object[]) {
            Object[] rowData = (Object[]) productField.getSelectedItem();
            id = Integer.valueOf(rowData[0].toString());
        }
        Product product = bd_connection.Product.findProductById(id);
        ProductInStore productInStore=null;
        if(!isPromotional.isSelected()){
            productInStore= new ProductInStore(UPC, null, product, new BigDecimal(Double.valueOf(priceField.getText())),Integer.valueOf(countField.getText()),false);
            Store_Product.addProductInStore(productInStore);
        }else{
            ProductInStore mainProduct = Store_Product.findStoreProductsByProductId(id).get(0);
            productInStore = new ProductInStore(UPC, null, product, mainProduct.getPrice().multiply(new BigDecimal(0.8)),Integer.valueOf(countField.getText()),true);
            mainProduct.setPromotionalUPC(UPC);
            Store_Product.addProductInStore(productInStore);
            Store_Product.updateProductInStoreById(mainProduct);
        }
        System.out.println(Store_Product.findAll());
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
    public void start() {
        init();
        add(backPanel);
        this.pack();
        this.setVisible(true);
    }


}
