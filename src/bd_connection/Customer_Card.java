package bd_connection;

import entity.CustomerCard;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Customer_Card {

    private static Connection connection;


    //3 Отримати інформацію про усіх постійних клієнтів, відсортованих за прізвищем; +
    public static List<CustomerCard> getAllCustomersSorted(boolean acs) throws SQLException {
        List<CustomerCard> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customer_Card ORDER BY cust_surname";
        if(!acs)
            sql+=" DESC";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                String id = resultSet.getString("card_number");
                String surname = resultSet.getString("cust_surname");
                String name = resultSet.getString("cust_name");
                String patronymic = resultSet.getString("cust_patronymic");
                if(patronymic==null)
                    patronymic ="";
                String phone = resultSet.getString("phone_number");
                String city = resultSet.getString("city");
                String street = resultSet.getString("street");
                String code = resultSet.getString("zip_code");
                int percent = resultSet.getInt("percent");
                //String number, String surname, String name, String patronymic, String phoneNumber, String city, String street, String zipCode, int percent
                CustomerCard cust = new CustomerCard(id, surname, name, patronymic, phone, city, street, code, percent);
                customers.add(cust);
            }
        }
        return customers;
    }


    //additional method for getting customer card having card_number
    static CustomerCard getCustomerCard(String cardnum) throws SQLException {
        CustomerCard card = null;

        String sql = "SELECT * FROM Customer_Card WHERE card_number = "+cardnum;
        try (Statement statement = connection.createStatement();
             ResultSet resultCast = statement.executeQuery(sql)) {
            if (resultCast.next()) {
                String p = resultCast.getString("cust_patronymic");
                if(p==null)
                    p="";
                return  new CustomerCard(cardnum, resultCast.getString("cust_surname"), resultCast.getString("cust_name"), p, resultCast.getString("phone_number"), resultCast.getString("city"), resultCast.getString("street"), resultCast.getString("zip_code"),
                        resultCast.getInt("percent"));
            }
        }


        return card;


    }

}
