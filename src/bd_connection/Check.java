package bd_connection;

import entity.Employee;
import entity.Receipt;
import entity.SoldProduct;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static bd_connection.Customer_Card.getCustomerCard;
import static bd_connection.Employee.getEmployee;
import static bd_connection.Sale.getSoldProductsFromReceipt;

public class Check {

    private static Connection connection;
    public static void setConnection(Connection con){
        connection=con;
    }

    private static final String CHECK_NUMBER = "check_number";
    private static final String ID_EMPLOYEE = "id_employee";
    private static final String CARD_NUMBER = "card_number";
    private static final String PRINT_DATE = "print_date";
    private static final String SUM_TOTAL = "sum_total";
    private static final String VAT = "vat";
    private static final String UPC = "UPC";
    private static final String PRODUCT_NUMBER = "product_number";
    private static final String SELLING_PRICE = "selling_price";

    //3. Видаляти дані про чеки;
    public static boolean deleteReceiptById(String check_number){
        try {
            Statement statement = connection.createStatement();
            String request = "DELETE FROM `zlagoda`.`check` WHERE (`"+CHECK_NUMBER+"` = '"+check_number+"');";
            statement.execute(request);
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    //4. Видруковувати звіти з інформацією про усі чеки;
    public static ArrayList<Receipt> findAll(){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT * FROM `zlagoda`.`check`;";
            ResultSet resultSet = statement.executeQuery(request);
            ArrayList<Receipt> receipts = new ArrayList<>();
            while(resultSet.next()) {
                receipts.add(new Receipt(resultSet.getString(CHECK_NUMBER), bd_connection.Employee.findEmployeeById(resultSet.getString(ID_EMPLOYEE)),Customer_Card.findCustomerCardById(resultSet.getString(CARD_NUMBER)),Timestamp.valueOf(resultSet.getString(PRINT_DATE)),new BigDecimal(resultSet.getString(SUM_TOTAL)),new BigDecimal(resultSet.getString(VAT)), getSoldProductsFromReceipt(resultSet.getString(CHECK_NUMBER))));
            }
            return receipts;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
    }

    //17 Отримати інформацію про усі чеки, створені певним касиром за певний період часу (з можливістю перегляду куплених товарів у цьому чеку, їх назви, к-сті та ціни);+
    //10. Переглянути список усіх чеків, що створив касир за певний період часу;
    public static List<Receipt> getAllReceiptFromGivenCashier(boolean acs, Employee cashier, Date from, Date to) throws SQLException {
        List<Receipt> receipts = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "SELECT * " +
                "FROM `Check` " +
                "WHERE id_employee = " + cashier.getId() +
                " AND print_date BETWEEN '" + sdf.format(from) + "' AND '" + sdf.format(to) + "'" +
                " ORDER BY print_date ";
        if (!acs)
            sql += " DESC";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                String num = resultSet.getString("check_number");
                String cardnum = resultSet.getString("card_number");
                Timestamp timestamp = resultSet.getTimestamp("print_date");
                BigDecimal sum_total = resultSet.getBigDecimal("sum_total");
                BigDecimal vat = resultSet.getBigDecimal("vat");
                Receipt receipt = new Receipt(num, cashier, getCustomerCard(cardnum), timestamp, sum_total, vat, getSoldProductsFromReceipt(num));
                receipts.add(receipt);

            }
        }
        return receipts;
    }


    //18. Отримати інформацію про усі чеки, створені усіма касирами за певний період часу (з можливістю перегляду куплених товарів у цьому чеку, їх назва, к-сті та ціни);+
    public static List<Receipt> getAllReceipt(boolean acs, Date from, Date to) throws SQLException {
        List<Receipt> receipts = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String sql = "SELECT * " +
                "FROM `Check` " +
                "WHERE print_date BETWEEN '" + sdf.format(from) + "' AND '" + sdf.format(to) + "'" +
                " ORDER BY print_date ";
        if (!acs)
            sql += " DESC";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                String num = resultSet.getString("check_number");
                String cardnum = resultSet.getString("card_number");
                Timestamp timestamp = resultSet.getTimestamp("print_date");
                BigDecimal sum_total = resultSet.getBigDecimal("sum_total");
                BigDecimal vat = resultSet.getBigDecimal("vat");

                String cashier_num = resultSet.getString("id_employee");

                Receipt receipt = new Receipt(num, getEmployee(cashier_num), getCustomerCard(cardnum), timestamp, sum_total, vat, getSoldProductsFromReceipt(num));
                receipts.add(receipt);
            }
        }
        return receipts;
    }


    //19 Визначити загальну суму проданих товарів з чеків, створених певним касиром за певний період часу;+
    public static BigDecimal getSumFromGivenCashier(Employee cashier, Date from, Date to) throws SQLException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "SELECT SUM(sum_total) AS sumT\n" +
                "FROM `Check` " +
                "WHERE id_employee = " + cashier.getId() +
                " AND print_date BETWEEN '" + sdf.format(from) + "' AND '" + sdf.format(to) + "'";

        BigDecimal answ = BigDecimal.valueOf(0);
        ;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next())
                answ = resultSet.getBigDecimal("sumT");
        }

        return answ;
    }


    //20 Визначити загальну суму проданих товарів з чеків, створених усіма касиром за певний період часу;
    public static BigDecimal getSumCheck(Date from, Date to) throws SQLException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "SELECT SUM(sum_total) AS sumT\n" +
                "FROM `Check` " +
                "WHERE print_date BETWEEN '" + sdf.format(from) + "' AND '" + sdf.format(to) + "'";

        BigDecimal answ = BigDecimal.valueOf(0);
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next())
                answ = resultSet.getBigDecimal("sumT");
        }

        return answ;
    }

    //additional method for
    public static Receipt getReceipt(String id) throws SQLException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "SELECT * " +
                "FROM `Check` WHERE check_number = " + "'"+id+"'";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                String num = resultSet.getString("check_number");
                String cardnum = resultSet.getString("card_number");
                Timestamp timestamp = resultSet.getTimestamp("print_date");
                BigDecimal sum_total = resultSet.getBigDecimal("sum_total");
                BigDecimal vat = resultSet.getBigDecimal("vat");

                String cashier_num = resultSet.getString("id_employee");

                return new Receipt(num, getEmployee(cashier_num), getCustomerCard(cardnum), timestamp, sum_total, vat, getSoldProductsFromReceipt(num));
            }

        }

        return null;
    }

    //7. Здійснювати продаж товарів (додавання чеків);
    public static void AddNewReceipt(Receipt receipt) throws SQLException{
        String sql = "INSERT INTO Check (check_number, id_employee, card_number, print_date, sum_total, vat)" +
                "VALUES ("+receipt.getNumber()+", "+receipt.getEmployee().getId()+", "+receipt.getCard().getNumber()+", "+receipt.getPrintDate()+", "+receipt.getVAT()+")";
        Statement statement = connection.createStatement();
        statement.executeQuery(sql);
        for (SoldProduct product:receipt.getProducts()) {
            AddSaleToReceipt(receipt.getNumber(),product);
        }
    }

    //additional method for 7
    public static void AddSaleToReceipt(String check_number,SoldProduct product) throws SQLException{
        String sql = "INSERT INTO Sale (UPC, check_number, product_number, selling_price)" +
                "VALUES ("+product.getUPC()+", "+check_number+", "+product.getAmount()+", "+product.getPrice()+")";
        Statement statement = connection.createStatement();
        statement.executeQuery(sql);
    }

    //9. Переглянути список усіх чеків, що створив касир за цей день;
    public static List<Receipt> getAllReceiptFromGivenCashierToday(boolean acs, Employee cashier) throws SQLException {
        return getAllReceiptFromGivenCashier(acs,cashier, (java.sql.Date)Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()),(java.sql.Date)Date.from(LocalDate.now().atTime(23,59,59).atZone(ZoneId.systemDefault()).toInstant()));
    }

    //11. За номером чеку вивести усю інформацію про даний чек, в тому числі інформацію про назву, к-сть та ціну товарів, придбаних в даному чеку.
    public static Receipt getReceiptByNumber(String number) throws SQLException {
        String sqlCast = "SELECT * FROM `Check` WHERE check_number = '" + number+"'";
        try (Statement statement = connection.createStatement();
             ResultSet resultCheck = statement.executeQuery(sqlCast)
        ) {
            if(resultCheck.next()){
                List<SoldProduct> products = getSoldProductsFromReceipt(number);
                return new Receipt(number, getEmployee(resultCheck.getString("id_employee")),getCustomerCard(resultCheck.getString("card_number")),resultCheck.getTimestamp("print_date"),resultCheck.getBigDecimal("sum_total"),resultCheck.getBigDecimal("vat"),products);
            }
        }
        return null;
    }

}
