package info_menu_manager;

import bd_connection.Customer_Card;
import com.toedter.calendar.JDateChooser;
import entity.CustomerCard;
import entity.Employee;
import entity.Receipt;
import entity.SoldProduct;
import helpers.CheckForErrors;
import menu.MainMenuManager;
import menu.Report;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static bd_connection.Check.*;
import static bd_connection.Check.deleteReceiptById;
import static bd_connection.Employee.getAllSpecial;
import static bd_connection.Employee.getEmployee;
import static bd_connection.Store_Product.getAllAboutProductsOnUPC;

public class CustomerAdditionalSearch {
    private static JTable table;
    private static DefaultTableModel model;
    static ArrayList<CustomerCard> customerCards;
    private static boolean isDefault=true;


    public static void display(JFrame frame, Employee empl){

        JPanel topPanel = new JPanel(new BorderLayout());

        JLabel productsInAllCatsLabel = new JLabel("Find customers who bought products in all categories between these dates:");
        productsInAllCatsLabel.setFont(new Font("TimesRoman", Font.PLAIN, 16));
        productsInAllCatsLabel.setBorder(new CompoundBorder(productsInAllCatsLabel.getBorder(), new EmptyBorder(10,5,5,5)));
        topPanel.add(productsInAllCatsLabel, BorderLayout.PAGE_START);
        JToolBar topToolBar = new JToolBar();
        topToolBar.setFloatable(true);

        JButton home = new JButton("Home");
        topToolBar.add(home);
        JButton all = new JButton("All");
        topToolBar.add(all);
        topToolBar.add(new JLabel("From:"));

        JDateChooser dateFrom = new JDateChooser();
        dateFrom.setDate(new Date());
        topToolBar.add(dateFrom);

        topToolBar.add(new JLabel("To:"));
        JDateChooser dateTo = new JDateChooser();
        dateTo.setDate(new Date());
        topToolBar.add(dateTo);

        JButton searchButton = new JButton("Find");
        topToolBar.add(searchButton);

        topPanel.add(topToolBar, BorderLayout.PAGE_END);
        frame.add(topPanel, BorderLayout.PAGE_START);

        JPanel tablePanel = new JPanel(new BorderLayout());
        customerCards = Customer_Card.findAll();

        model = new DefaultTableModel(new Object[]{"Card num", "Surname", "Name", "Patronymic", "Phone", "Discount"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);

        for (CustomerCard customer : customerCards) {
            model.addRow(new Object[]{customer.getNumber(), customer.getSurname(), customer.getName(), (customer.getPatronymic()==null?"":customer.getPatronymic()), customer.getPhoneNumber(), customer.getPercent()});
        }

        JScrollPane scrollPane = new JScrollPane(table);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.setPreferredSize(new Dimension(500, 400));

        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel lowerPanel = new JPanel(new BorderLayout());
        JButton query2 = new JButton("Find customers who worked with all cashiers");
        query2.setFont(new Font("TimesRoman", Font.PLAIN, 16));
        query2.setBorder(new CompoundBorder(query2.getBorder(), new EmptyBorder(0,100,0,100)));

        lowerPanel.add(query2,BorderLayout.PAGE_START);

        JPanel query3Panel = new JPanel(new BorderLayout());
        JLabel forQuery3 = new JLabel("Find number of receipts for each customer with sum greater than:");
        forQuery3.setHorizontalAlignment(SwingConstants.CENTER);
        forQuery3.setFont(new Font("TimesRoman", Font.PLAIN, 16));
        forQuery3.setBorder(new CompoundBorder(forQuery3.getBorder(), new EmptyBorder(30,5,5,5)));
        query3Panel.add(forQuery3, BorderLayout.PAGE_START);

        JToolBar query3ToolBar = new JToolBar();
        query3ToolBar.setFloatable(true);

        JButton findQuery3 = new JButton("Find");
        findQuery3.setFont(new Font("TimesRoman", Font.PLAIN, 16));
        JLabel sumLabel = new JLabel("Sum: ");
        sumLabel.setFont(new Font("TimesRoman", Font.PLAIN, 16));
        sumLabel.setBorder(new CompoundBorder(sumLabel.getBorder(), new EmptyBorder(5,5,5,5)));
        JTextField sumField = new JTextField();
        sumField.setFont(new Font("TimesRoman", Font.PLAIN, 16));

        query3ToolBar.add(sumLabel);
        query3ToolBar.add(sumField);
        query3ToolBar.add(findQuery3);

        query3Panel.add(query3ToolBar, BorderLayout.PAGE_END);

        lowerPanel.add(query3Panel, BorderLayout.PAGE_END);
        frame.add(lowerPanel,BorderLayout.PAGE_END);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        home.addActionListener( s ->{
            frame.getContentPane().removeAll();
            CustomerTableManager.display(frame, empl);

            frame.revalidate();
            frame.repaint();
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dateFrom.getDate().getTime() > dateTo.getDate().getTime()) {
                    JOptionPane.showMessageDialog(null, "Please chose the correct date", "Error", JOptionPane.ERROR_MESSAGE);
                }else{
                    customerCards=Customer_Card.findCustomersWhoBuyFromAllCategoriesBetweenTwoDates(new java.sql.Date(dateFrom.getDate().getTime()), new java.sql.Date(dateTo.getDate().getTime()));
                    rewriteTable();
                }

            }
        });

        all.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                customerCards = Customer_Card.findAll();
                rewriteTable();
            }
        });

        query2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                customerCards = Customer_Card.findCustomersWhoWorkedWithAllCashiers();
                rewriteTable();
            }
        });

        findQuery3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(sumField.getText().isEmpty()){
                    return;
                }
                if(!CheckForErrors.checkDoubleNumber(sumField.getText())){
                    showError("Sum must a number >0", new JTextField[]{sumField});
                    return;
                }
                BigDecimal sum = new BigDecimal(sumField.getText());
                if(sum.compareTo(new BigDecimal(0))<=0){
                    showError("Sum must >0", new JTextField[]{sumField});
                }else{
                    rearrangeTable(sum);
                }
            }
        });

    }

    /**
     * Відображаєм помилку
     * @param text текст помилки
     * @param fields поля з помилкою
     */
    private static void showError(String text, JTextField[] fields){
        for(int i=0;i<fields.length;i++){
            fields[i].setBackground(Color.red);
        }
        JOptionPane.showMessageDialog(null,text,"Error",JOptionPane.ERROR_MESSAGE);
        for(int i=0;i<fields.length;i++){
            fields[i].setBackground(Color.white);
            fields[i].setText("");
        }
    }

    private static void rearrangeTable(BigDecimal sum) {
        isDefault=false;
        HashMap<CustomerCard, Integer> result = Customer_Card.countReceiptsForCustomersAboveSum(sum);
        model = new DefaultTableModel(new Object[]{"Card num", "Surname", "Name", "Patronymic", "Phone", "Discount", "Number of Receipts"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(model);

        for (HashMap.Entry<CustomerCard, Integer> entry : result.entrySet()) {
            CustomerCard customer = entry.getKey();
            model.addRow(new Object[]{customer.getNumber(), customer.getSurname(), customer.getName(), (customer.getPatronymic()==null?"":customer.getPatronymic()), customer.getPhoneNumber(), customer.getPercent(), entry.getValue()});
        }
    }

    private static void rewriteTable(){
        if(!isDefault){
            model = new DefaultTableModel(new Object[]{"Card num", "Surname", "Name", "Patronymic", "Phone", "Discount"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            table.setModel(model);
        }
        model.setRowCount(0);
        for (CustomerCard customer : customerCards) {
            model.addRow(new Object[]{customer.getNumber(), customer.getSurname(), customer.getName(), (customer.getPatronymic()==null?"":customer.getPatronymic()), customer.getPhoneNumber(), customer.getPercent()});
        }
    }



}
