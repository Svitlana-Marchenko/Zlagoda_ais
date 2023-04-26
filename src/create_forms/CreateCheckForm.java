package create_forms;

import bd_connection.Check;
import bd_connection.Customer_Card;
import bd_connection.Sale;
import bd_connection.Store_Product;
import entity.*;
import info_menu_cashier.CustomerTableCashier;
import info_menu_cashier.ReceiptViewCashier;
import info_menu_cashier.StoreProductCashier;
import info_menu_common.StoreProductTable;
import info_menu_manager.StoreProductManager;
import items_forms.ProductInStoreActionForm;
import menu.LoginMenu;
import menu.MainMenuCashier;
import menu.MainMenuManager;
import menu.Report;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static bd_connection.Customer_Card.getAllCustomersSorted;
import static bd_connection.Customer_Card.getCustomersBySurname;
import static bd_connection.Store_Product.*;
import static entity.Employee.Role.CASHIER;

public class CreateCheckForm{

    private JPanel checkPanel;
    private JFrame frame;
    private Employee employee;
    private CustomerCard card;
    private HashMap<String,SoldProduct> added;
    private static List<CustomerCard> customersList;
    private static List<ProductInStore>  store_productListList;

    public CreateCheckForm(JFrame frame, Employee employee) {
        this.frame = frame;
        this.employee = employee;
        added = new HashMap<>();
        frame.setBounds(800,400,500,350);
        init();
    }

    private void init() {
        checkPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        frame.add(checkPanel);

        JButton backButton = new JButton("Back");
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                frame.getContentPane().removeAll();
                MainMenuCashier.display(frame, employee);
                // Repaint the frame
                frame.revalidate();
                frame.repaint();
            }
        });
        c.gridx = 0;
        c.gridy = 0;
        checkPanel.add(backButton,c);

        JLabel filler1 = new JLabel("");
        c.gridx = 1;
        c.gridy = 0;
        checkPanel.add(filler1,c);
        JLabel filler2 = new JLabel("");
        c.gridx = 2;
        c.gridy = 0;
        checkPanel.add(filler2,c);
        JLabel filler3 = new JLabel("");
        c.gridx = 3;
        c.gridy = 0;
        c.ipadx = frame.getWidth()-270;
        checkPanel.add(filler3,c);

        JLabel addedLabel = new JLabel("Added products:");
        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 0;
        checkPanel.add(addedLabel,c);

        ArrayList<String[]> values = new ArrayList<String[]>();
        String[] columnNames = { "UPC", "Name", "Amount" };

        for (SoldProduct product: added.values()) {
            values.add(new String[] {product.getUPC(),product.getName(), String.valueOf(product.getAmount())});
        }

        // Initializing the JTable
        JTable j = new JTable(values.toArray(new Object[][] {}), columnNames);
        j.setBounds(0, 0, 200, 300);
        JScrollPane scrollPane = new JScrollPane(j);
        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setVisible(true);
        c.gridwidth = 4;
        c.gridx = 0;
        c.gridy = 2;
        c.ipady = 200;
        c.ipadx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        checkPanel.add(scrollPane,c);

        JButton addButton = new JButton("Add product");
        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                frame.setEnabled(false);
                chooseProduct();
            }
        });
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 3;
        c.ipady = 0;
        c.fill = GridBagConstraints.NONE;
        checkPanel.add(addButton,c);

        JButton customerButton = new JButton("Choose customer");
        customerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                frame.setEnabled(false);
                try {
                    chooseCustomer();
                }catch (SQLException ex){
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(new JFrame(), "Error in database occurred", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        c.gridx = 1;
        c.gridy = 3;
        checkPanel.add(customerButton,c);

        JButton createButton = new JButton("Create");
        createButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                try {
                    boolean success = createReceipt();
                    if(success) {
                        JOptionPane.showMessageDialog(new JFrame(), "New receipt created", "Success",
                                JOptionPane.PLAIN_MESSAGE);
                        frame.getContentPane().removeAll();
                        MainMenuCashier.display(frame, employee);
                        // Repaint the frame
                        frame.revalidate();
                        frame.repaint();
                    }
                }catch (SQLException ex){
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(new JFrame(), "Error in database occurred", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        c.gridx = 3;
        c.gridy = 3;
        checkPanel.add(createButton,c);
    }

    private void chooseCustomer() throws SQLException {
        JFrame tempFrame = new JFrame();
        customersList = Customer_Card.getAllCustomersSorted(true);
        JToolBar buttonPanel = new JToolBar();

        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(cancelButton);

        cancelButton.addActionListener(s -> {
            tempFrame.setVisible(false);
        });

        JPanel tablePanel = new JPanel(new BorderLayout());

        JTable table = new JTable();

        DefaultTableModel model = new DefaultTableModel(new Object[]{"Number", "Name", "Surname", "Patronymic", "City", "Street", "ZipCode", "Phone number", "Percent"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // make the cells non-editable
            }
        };
        table.setModel(model);

        for (CustomerCard cc : customersList) {
            model.addRow(new Object[]{cc.getNumber(), cc.getName(), cc.getSurname(), cc.getPatronymic(), cc.getCity(), cc.getStreet(), cc.getZipCode(), cc.getPhoneNumber(), cc.getPercent()});
        }

        JScrollPane scrollPane = new JScrollPane(table);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        String textForJText = "Enter customer surname (optional)";

        JTextField nameField = new JTextField(textForJText);
        nameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (nameField.getText().equals(textForJText)) {
                    nameField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (nameField.getText().isEmpty()) {
                    nameField.setText(textForJText);
                }
            }
        });

        buttonPanel.add(nameField);

        JButton searchUPC = new JButton("Search");
        buttonPanel.add(searchUPC);

        tempFrame.add(buttonPanel, BorderLayout.PAGE_START);

        tempFrame.add(tablePanel, BorderLayout.CENTER);

        tempFrame.setSize(500, 500);
        tempFrame.setVisible(true);
        AtomicBoolean sortAlph = new AtomicBoolean(true);

        searchUPC.addActionListener(e -> {

                //no name
                if(nameField.getText().equals(textForJText)){
                    try {
                        customersList = getAllCustomersSorted(sortAlph.get());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                //with name
                else{
                    try {
                        customersList = getCustomersBySurname(nameField.getText());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }

                if (sortAlph.get()) {
                    Collections.sort(customersList, (c1, c2) -> (c1.getSurname()+c1.getName()).compareToIgnoreCase(c2.getSurname()+c2.getName()));

                } else {
                    Collections.sort(customersList, (c1, c2) -> (c2.getSurname()+c2.getName()).compareToIgnoreCase(c1.getSurname()+c1.getName()));
                }

                model.setRowCount(0);

                for (CustomerCard customer : customersList) {
                    model.addRow(new Object[]{customer.getNumber(), customer.getSurname(), customer.getName(), (customer.getPatronymic()==null?"":customer.getPatronymic()), customer.getPhoneNumber(), customer.getPercent()});
                }
                // Repaint the frame
                tempFrame.revalidate();
                tempFrame.repaint();

        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    tempFrame.setEnabled(false);
                    CustomerCard customer = customersList.get(row);
                    card = customer;
                    tempFrame.setVisible(false);
                    JOptionPane.showMessageDialog(new JFrame(), "Customer was chosen", "Success",
                            JOptionPane.PLAIN_MESSAGE);
                    frame.setEnabled(true);
                }
            }
        });
    }

    private boolean createReceipt() throws SQLException {
        BigDecimal sum = BigDecimal.valueOf(0);
        for (SoldProduct product: added.values()) {
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(product.getAmount())));
        }
        if(card!=null) sum = sum.multiply(BigDecimal.valueOf(1-card.getPercent()/100));
        BigDecimal vat = sum.multiply(BigDecimal.valueOf(0.2));
        sum = sum.add(vat);
        sum = sum.round(new MathContext(2));
        ArrayList<SoldProduct> sold = new ArrayList<>();
        for (String key: added.keySet()) {
            sold.add(added.get(key));
        }
        if(sold.isEmpty()){
            JOptionPane.showMessageDialog(new JFrame(), "No products were added", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        Receipt receipt = new Receipt(generateNumber(), employee, card, Timestamp.valueOf(LocalDateTime.now()), sum, vat, sold);
        Check.AddNewReceipt(receipt);
        return true;
    }

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

    private void chooseProduct() {
        JFrame tempFrame = new JFrame();
        store_productListList = getAllProductsInStoreSorted(true);

        JToolBar buttonPanel = new JToolBar();

        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(cancelButton);

        cancelButton.addActionListener(s -> {
            tempFrame.setVisible(false);
        });

        JPanel tablePanel = new JPanel(new BorderLayout());

        JTable table = new JTable();

        DefaultTableModel model = new DefaultTableModel(new Object[]{"UPC", "Name", "Category", "Price", "Amount", "Promotional", "Producer"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // make the cells non-editable
            }
        };
        table.setModel(model);

        for (ProductInStore sp : store_productListList) {
            model.addRow(new Object[]{sp.getUPC(), sp.getProduct().getName(), sp.getProduct().getCategory().getName(), sp.getPrice(), sp.getAmount(), sp.isPromotional(), sp.getProduct().getProducer()});
        }

        JScrollPane scrollPane = new JScrollPane(table);

        tablePanel.add(scrollPane, BorderLayout.CENTER);


        String textForJText = "Enter product name (optional)";

        JTextField nameField = new JTextField(textForJText);
        nameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (nameField.getText().equals(textForJText)) {
                    nameField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (nameField.getText().isEmpty()) {
                    nameField.setText(textForJText);
                }
            }
        });

        buttonPanel.add(nameField);

        JButton searchName = new JButton("Search");
        buttonPanel.add(searchName);

        tempFrame.add(buttonPanel, BorderLayout.PAGE_START);

        tempFrame.add(tablePanel, BorderLayout.CENTER);

        tempFrame.setSize(500, 500);
        tempFrame.setVisible(true);
        AtomicBoolean sortAlph = new AtomicBoolean(true);

        searchName.addActionListener(e -> {

            //no name
            if(nameField.getText().equals(textForJText)){
                store_productListList = getAllProductsInStoreSorted(sortAlph.get());
            }
            //with name
            else{
                store_productListList = getAllProductsInStoreByName(nameField.getText());
            }

            if(store_productListList==null) store_productListList=new ArrayList<>();
            if (sortAlph.get()) {
                Collections.sort(store_productListList, (c1, c2) -> (c1.getProduct().getName()+c1.getAmount()).compareToIgnoreCase(c2.getProduct().getName()+c2.getAmount()));

            } else {
                Collections.sort(store_productListList, (c1, c2) -> (c2.getProduct().getName()+c2.getAmount()).compareToIgnoreCase(c1.getProduct().getName()+c1.getAmount()));
            }

            model.setRowCount(0);

            for (ProductInStore product : store_productListList) {
                model.addRow(new Object[]{product.getUPC(), product.getProduct().getName(), product.getProduct().getCategory().getName(), product.getPrice(), product.getAmount(), product.isPromotional(), product.getProduct().getProducer()});
            }
            // Repaint the frame
            tempFrame.revalidate();
            tempFrame.repaint();

        });

        tempFrame.add(buttonPanel, BorderLayout.PAGE_START);

        tempFrame.add(tablePanel, BorderLayout.CENTER);

        tempFrame.setSize(500, 500);
        tempFrame.setVisible(true);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    String storeId = (String) model.getValueAt(row, 0);
                    tempFrame.setEnabled(false);
                    ProductInStore product = store_productListList.get(row);
                    int amountThere=0;
                    if(added.containsKey(product.getUPC())) {
                        amountThere = added.get(product.getUPC()).getAmount();
                    }
                    int amount = askAmount(product.getAmount()-amountThere);
                    added.put(product.getUPC(),new SoldProduct(product.getUPC(),product.getProduct().getName(),amount+amountThere,product.getPrice()));

                    ProductInStore inStore = Store_Product.findProductInStoreById(product.getUPC());
                    Store_Product.updateProductInStoreById(new ProductInStore(inStore.getUPC(), inStore.getPromotionalUPC(), inStore.getProduct(),
                            inStore.getPrice(), inStore.getAmount() - amount, inStore.isPromotional()));

                    tempFrame.setVisible(false);
                    frame.getContentPane().removeAll();
                    init();
                    frame.revalidate();
                    frame.repaint();
                    frame.setEnabled(true);
                }
            }
        });
    }

    private int askAmount(int inStore) {
        JFrame tempFrame2 = new JFrame();
        String result = JOptionPane.showInputDialog(tempFrame2, "Enter amount:");
        try {
            int amount = Integer.parseInt(result);
            if(amount<1) throw new NumberFormatException();
            if(amount>inStore) {
                JOptionPane.showMessageDialog(new JFrame(), "Amount can not be bigger than amount in the store", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return askAmount(inStore);
            }
            return amount;
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(new JFrame(), "Amount must be natural number", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return askAmount(inStore);
        }
    }

    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3308/zlagoda",
                    "root",
                    ""
            );
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        Store_Product.setConnection(connection);
        Check.setConnection(connection);
        bd_connection.Category.setConnection(connection);
        Customer_Card.setConnection(connection);
        bd_connection.Product.setConnection(connection);
        bd_connection.Sale.setConnection(connection);
        JFrame frame = new JFrame();
        CreateCheckForm a = new CreateCheckForm(frame,new Employee("1","","","","",CASHIER,BigDecimal.valueOf(0),
                null,null,"","","",""));
        frame.setVisible(true);
    }
}
