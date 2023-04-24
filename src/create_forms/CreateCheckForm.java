package create_forms;

import bd_connection.Check;
import bd_connection.Store_Product;
import entity.*;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static bd_connection.Store_Product.getAllProductsInStoreSaleSorted;
import static bd_connection.Store_Product.getAllProductsInStoreSorted;
import static entity.Employee.Role.CASHIER;

public class CreateCheckForm extends JFrame {

    private JPanel checkPanel;
    private JFrame frame;
    private Employee employee;
    private CustomerCard card;
    private HashMap<String,SoldProduct> added;

    public CreateCheckForm(JFrame frame, Employee employee, CustomerCard card) {
        this.frame = frame;
        this.employee = employee;
        this.card = card;
        added = new HashMap<>();
        this.setBounds(800,400,500,350);
        init();
    }

    private void init() {
        checkPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        add(checkPanel);

        JButton backButton = new JButton("Back");
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                frame.getContentPane().removeAll();
                ReceiptViewCashier.display(frame, employee);
                // Repaint the frame
                frame.revalidate();
                frame.repaint();
            }
        });
        c.gridx = 0;
        c.gridy = 0;
        checkPanel.add(backButton,c);

        JLabel addedLabel = new JLabel("Added products:");
        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = this.getWidth()-200;
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
        c.gridwidth = 2;
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

        JButton createButton = new JButton("Create");
        createButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                try {
                    createReceipt();
                    JOptionPane.showMessageDialog(new JFrame(), "New receipt created", "Success",
                            JOptionPane.PLAIN_MESSAGE);
                    frame.getContentPane().removeAll();
                    ReceiptViewCashier.display(frame, employee);
                    // Repaint the frame
                    frame.revalidate();
                    frame.repaint();
                }catch (SQLException ex){
                    JOptionPane.showMessageDialog(new JFrame(), "Error in database occurred", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        c.gridx = 1;
        c.gridy = 3;
        checkPanel.add(createButton,c);
    }

    private void createReceipt() throws SQLException {
        BigDecimal sum = BigDecimal.valueOf(0);
        for (SoldProduct product: added.values()) {
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(product.getAmount())));
        }
        BigDecimal vat = sum.multiply(BigDecimal.valueOf(0.2));
        sum = sum.add(vat);
        Receipt receipt = new Receipt(generateNumber(),employee,card, Timestamp.valueOf(LocalDateTime.now()),sum,vat, (List<SoldProduct>) added.values());
        Check.AddNewReceipt(receipt);
        for (SoldProduct product: added.values()) {
            ProductInStore inStore = Store_Product.findProductInStoreById(product.getUPC());
            Store_Product.updateProductInStoreById(new ProductInStore(inStore.getUPC(),inStore.getPromotionalUPC(),inStore.getProduct(),
                    inStore.getPrice(),inStore.getAmount()-product.getAmount(),inStore.isPromotional()));
        }
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
        List<ProductInStore>  store_productListList = getAllProductsInStoreSorted(true);

        JToolBar buttonPanel = new JToolBar();

        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(cancelButton);

        cancelButton.addActionListener(s -> {
            tempFrame.setVisible(false);
        });

        JComboBox<String> sortComboBox = new JComboBox<>(new String[]{"Using name", "Using quantity num"});
        buttonPanel.add(sortComboBox);

        JButton sortButton = new JButton("Sort");
        buttonPanel.add(sortButton);

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


        JToolBar managerTools = new JToolBar();
        JButton searchUPC = new JButton("Search");
        managerTools.add(searchUPC);

        tempFrame.add(buttonPanel, BorderLayout.PAGE_START);

        tempFrame.add(tablePanel, BorderLayout.CENTER);



        tempFrame.setSize(500, 500);
        tempFrame.setVisible(true);
        AtomicBoolean sortAlph = new AtomicBoolean(true);

        sortButton.addActionListener(e -> {
            if (sortComboBox.getSelectedItem().equals("Using name")) {
                if (!sortAlph.get()) {
                    Collections.sort(store_productListList, (c1, c2) -> c1.getProduct().getName().compareToIgnoreCase(c2.getProduct().getName()));
                    sortButton.setText("Sort (Z-A)");
                    // sortOrder.set(0);
                    sortAlph.set(true);
                } else {
                    Collections.sort(store_productListList, (c1, c2) -> c2.getProduct().getName().compareToIgnoreCase(c1.getProduct().getName()));
                    sortButton.setText("Sort (A-Z)");
                    sortAlph.set(false);
                }
            } else {
                if (!sortAlph.get() /*sortOrder.get() == 1*/) {
                    Collections.sort(store_productListList, (c1, c2) -> Integer.compare(c1.getAmount(), c2.getAmount()));
                    sortButton.setText("Sort (Z-A)");
                    sortAlph.set(true);
                } else {
                    Collections.sort(store_productListList, (c1, c2) -> Integer.compare(c2.getAmount(), c1.getAmount()));
                    sortButton.setText("Sort (A-Z)");
                    sortAlph.set(false);
                }
            }
            model.setRowCount(0);
            for (ProductInStore sp : store_productListList) {
                model.addRow(new Object[]{sp.getUPC(), sp.getProduct().getName(), sp.getProduct().getCategory().getName(), sp.getPrice(), sp.getAmount(), sp.isPromotional(), sp.getProduct().getProducer()});
            }
        });

        sortComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (sortComboBox.getSelectedItem().equals("Using name")) {
                    if (!sortAlph.get()) {
                        Collections.sort(store_productListList, (c2, c1) -> c1.getProduct().getName().compareToIgnoreCase(c2.getProduct().getName()));

                    } else {
                        Collections.sort(store_productListList, (c2, c1) -> c2.getProduct().getName().compareToIgnoreCase(c1.getProduct().getName()));

                    }
                } else {
                    if (!sortAlph.get()) {
                        Collections.sort(store_productListList, (c2, c1) -> Integer.compare(c1.getAmount(), c2.getAmount()));

                    } else {
                        Collections.sort(store_productListList, (c2, c1) -> Integer.compare(c2.getAmount(), c1.getAmount()));

                    }
                }
                model.setRowCount(0);
                for (ProductInStore sp : store_productListList) {
                    model.addRow(new Object[]{sp.getUPC(), sp.getProduct().getName(), sp.getProduct().getCategory().getName(), sp.getPrice(), sp.getAmount(), sp.isPromotional(), sp.getProduct().getProducer()});
                }
            }
        });

        searchUPC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tempFrame.getContentPane().removeAll();
                StoreProductCashier.display(tempFrame, employee);
                // Repaint the frame
                tempFrame.revalidate();
                tempFrame.repaint();
            }
        });

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
                    tempFrame.setVisible(false);
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
        CreateCheckForm a = new CreateCheckForm(new JFrame(),new Employee("","","","","",CASHIER,BigDecimal.valueOf(0),
                null,null,"","","",""),null);
        a.setVisible(true);
    }
}
