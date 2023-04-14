package bd_connection;

import entity.Employee;
import entity.Receipt;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static bd_connection.Customer_Card.getCustomerCard;
import static bd_connection.Employee.getEmployee;
import static bd_connection.Sale.getSoldProductsFromReceipt;

public class Check {

    private static Connection connection;

    //17 Отримати інформацію про усі чеки, створені певним касиром за певний період часу (з можливістю перегляду куплених товарів у цьому чеку, їх назви, к-сті та ціни);+
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
}
