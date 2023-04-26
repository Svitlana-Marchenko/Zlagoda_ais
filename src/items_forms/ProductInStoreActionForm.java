package items_forms;

import bd_connection.Check;
import bd_connection.Store_Product;
import entity.ProductInStore;
import entity.Receipt;
import helpers.*;
import info_menu_common.StoreProductTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

import static helpers.ComboBoxStructure.*;
import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.NONE;

/**
 * Форма для відображення, редагування та видалення товару на складі
 */
public class ProductInStoreActionForm extends JFrame {
    private JPanel backPanel;

    private JTextField countField;
    private JTextField priceField;
    private JComboBox productField;
    private JComboBox categoryField;
    private JCheckBox isPromotional;

    private ProductInStore productInStore;

    public ProductInStoreActionForm(ProductInStore productInStore, DefaultTableModel model, JFrame frame){
        super("Edit product in store");
        this.productInStore = productInStore;
        this.setSize(600,500);
        start(model,frame);
    }

    /**
     * Початкова ініціалізація графічних об'єктів
     */
    private void init(DefaultTableModel model, JFrame frame)  {
        DefaultComboBoxModel promotionalCategoriesList = createCategoriesList(true,false,productInStore.getProduct().getCategory(),productInStore.isPromotional());
        DefaultComboBoxModel promotionalProductsList= createProductsList(productInStore.getProduct().getCategory(),true, productInStore.isPromotional() ? productInStore.getProduct() : null,true );
        DefaultComboBoxModel notPromotionalCategoriesList=createCategoriesList(false,false,productInStore.getProduct().getCategory(),!productInStore.isPromotional());
        DefaultComboBoxModel notPromotionalProductsList= createProductsList(productInStore.getProduct().getCategory(),false, !productInStore.isPromotional() ? productInStore.getProduct() : null, true);

        ButtonsPanel buttonsPanel=new ButtonsPanel(true);
        List<JTextField> fields = new ArrayList<>();
        backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.PAGE_AXIS));
        JPanel captionPanel = new JPanel();
        captionPanel.setLayout(new BoxLayout(captionPanel, BoxLayout.LINE_AXIS));

        GridBagConstraints c = new GridBagConstraints();
        JLabel captionLabel = new JLabel("Product in store");
        captionLabel.setHorizontalAlignment(JLabel.CENTER);
        captionLabel.setFont(new Font("TimesRoman", Font.BOLD, 35));
        captionPanel.add(captionLabel);
        backPanel.add(captionPanel);

        JPanel mainPanel = new JPanel(new GridBagLayout());

        isPromotional = new JCheckBox("promotional");
        isPromotional.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 0;
        c.gridx = 0;
        c.gridwidth=2;
        isPromotional.setSelected(productInStore.isPromotional());
        isPromotional.setEnabled(false);
        mainPanel.add(isPromotional,c);
        c.gridwidth=1;

        JLabel groupLabel = new JLabel("Category: ");
        groupLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 1;
        mainPanel.add(groupLabel,c);

        categoryField = new JComboBox();
        categoryField.setModel(productInStore.isPromotional() ? promotionalCategoriesList : notPromotionalCategoriesList);
        categoryField.setFont(new Font("TimesRoman",Font.PLAIN, 20));

        categoryField.setRenderer(new ComboBoxRenderer());

        c.gridy = 1;
        c.gridx = 1;
        c.fill = BOTH;
        categoryField.setEnabled(false);
        mainPanel.add(categoryField,c);

        JLabel productLabel = new JLabel("Product: ");
        productLabel.setFont(new Font("TimesRoman",Font.PLAIN, 25));
        c.gridy = 2;
        c.gridx = 0;
        c.fill=NONE;
        mainPanel.add(productLabel,c);

        productField = new JComboBox();
        productField.setModel(productInStore.isPromotional() ? promotionalProductsList : notPromotionalProductsList);
        productField.setFont(new Font("TimesRoman",Font.PLAIN, 20));
        productField.setRenderer(new ComboBoxRenderer());
        productField.setEnabled(false);
        c.gridy = 2;
        c.gridx = 1;
        c.fill = BOTH;
        mainPanel.add(productField,c);

        List<JLabel> labels = new ArrayList<>();

        countField = new JTextField();
        countField.setText(String.valueOf(productInStore.getAmount()));
        fields.add(countField);
        labels.add(new JLabel("Quantity: "));

        priceField = new JTextField();
        priceField.setText(String.valueOf(productInStore.getPrice()));
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
            fields.get(i).setEditable(false);
            c.gridx=1;
            c.ipadx=300;
            c.weightx=2;
            c.fill = BOTH;
            mainPanel.add(fields.get(i),c);
        }
        CheckForErrors.tFields=fields;

        buttonsPanel.getEditButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonsPanel.setEnabled(true);

                if(!isPromotional.isSelected())
                    priceField.setEditable(true);
                countField.setEditable(true);
            }
        });

        buttonsPanel.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> errors = CheckForErrors.checkForEmptyErrors();
                List<String> errors1 = CheckForErrors.checkForNotNumbersErrors(new ArrayList<>(Arrays.asList(priceField)),new ArrayList<>(Arrays.asList(countField)));
                if(errors!=null){
                    showError(errors.get(0), CheckForErrors.getErrorTextFields(errors));
                }else if(errors1!=null){
                    showError(errors1.get(0), CheckForErrors.getErrorTextFields(errors1));
                }else{
                    updateItem();
                    buttonsPanel.setEnabled(false);
                    priceField.setEditable(false);
                    countField.setEditable(false);
                }
            }
        });

        buttonsPanel.getDeleteButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null,"Are you sure? Delete this product in store?", "Delete category",
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
                SwitchFrames.switchFramesForStoreProduct(frame,model);
                dispose();
            }
        });

        backPanel.add(mainPanel);
        backPanel.add(buttonsPanel);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SwitchFrames.switchFramesForStoreProduct(frame,model);
                dispose();
            }
        });

    }

    /**
     * Редагуєм товар на складі
     */
    private void updateItem() {
        if(Integer.valueOf(countField.getText()) != productInStore.getAmount() || !(new BigDecimal(priceField.getText())).equals(productInStore.getPrice())){
            ProductInStore temp = new ProductInStore(productInStore.getUPC(),productInStore.getPromotionalUPC(),productInStore.getProduct(),new BigDecimal(priceField.getText()).setScale(4),Integer.valueOf(countField.getText()),productInStore.isPromotional());
            StoreProductTable.getStore_productListList().set(StoreProductTable.getStore_productListList().indexOf(productInStore),temp);
            productInStore=temp;
            Store_Product.updateProductInStoreById(productInStore);
        }

    }

    /**
     * Видадяєм товар на складі
     * @param model модель таблички в основному фреймі
     * @param frame основний фрейм з табличкою
     */
    private void deleteItem(DefaultTableModel model, JFrame frame) {
        if(isInCheck()){
            JOptionPane.showMessageDialog(null,"You can't delete product in store with checks!","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        bd_connection.Store_Product.deleteProductInStoreByUPC(productInStore.getUPC());
        StoreProductTable.getStore_productListList().remove(productInStore);
        SwitchFrames.switchFramesForStoreProduct(frame,model);
        dispose();
    }

    /**
     * Перевіряєм чи є чек із заданим товаром на складі
     * @return чи є чек із заданим товаром на складі
     */
    private boolean isInCheck(){
        ArrayList<Receipt> receipts = Check.findAll();
        for(Receipt r : receipts){
            if(r.includeProductInStore(productInStore))
                return true;
        }
        return false;
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
        }
        for(int i=0;i<fields.length;i++){
            if(fields[i]==countField)
                countField.setText(String.valueOf(productInStore.getAmount()));
            else if(fields[i]==priceField)
                priceField.setText(String.valueOf(productInStore.getPrice()));
        }
    }
    /**
     * Запускаєм користувацьку форму
     */
    public void start(DefaultTableModel model, JFrame frame) {
        init(model,frame);
        add(backPanel);
        this.pack();
        this.setVisible(true);
    }


}
