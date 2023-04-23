package bd_connection;

import entity.Product;
import entity.ProductInStore;
import entity.Receipt;
import entity.SoldProduct;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Sale {

    private static Connection connection;
    public static void setConnection(Connection con){
        connection=con;
    }
   

    //21  Визначити загальну кількість одиниць певного товару, проданого за певний період часу.+
    public static int getNumSold(Date from, Date to, SoldProduct saleP) throws SQLException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


        String sql = "SELECT SUM(s.product_number) AS sumT\n" +
                "FROM Sale s\n" +
                "JOIN `Check` c ON s.check_number = c.check_number\n" +
                "WHERE s.upc = "+saleP.getUPC()+"\n" +
                "AND c.print_date BETWEEN '"+sdf.format(from) +"' AND '"+sdf.format(to)+"'";

        int answ = 0;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if(resultSet.next())
                answ = resultSet.getInt("sumT");
        }

        return answ;
    }


    //additional method for getting list of sold product having check_number from receipt
    static List<SoldProduct> getSoldProductsFromReceipt(String rec_num) throws SQLException {
        List<SoldProduct> products = new ArrayList<>();
        String sql = "SELECT * FROM Sale WHERE check_number = '" + rec_num + "'";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {

                String name = resultSet.getString("UPC");


                int num = resultSet.getInt("product_number");
                BigDecimal price = resultSet.getBigDecimal("selling_price");

                SoldProduct product = new SoldProduct(name, rec_num, num, price);
                products.add(product);
            }
        }
        return products;
    }


}
