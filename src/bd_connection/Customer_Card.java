package bd_connection;

import entity.CustomerCard;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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


    //6. Здійснити пошук постійних клієнтів за прізвищем;
    public  static List<CustomerCard> getCustomersBySurname(String surname) throws SQLException {
        List<CustomerCard> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customer_Card WHERE cust_surname="+ surname;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                String id = resultSet.getString("card_number");
                String name = resultSet.getString("cust_name");
                String patronymic = resultSet.getString("cust_patronymic");
                if(patronymic==null)
                    patronymic ="";
                String phone = resultSet.getString("phone_number");
                String city = resultSet.getString("city");
                if(city==null)
                    city ="";
                String street = resultSet.getString("street");
                if(street==null)
                    street ="";
                String code = resultSet.getString("zip_code");
                if(code==null)
                    code ="";
                int percent = resultSet.getInt("percent");
                CustomerCard cust = new CustomerCard(id, surname, name, patronymic, phone, city, street, code, percent);
                customers.add(cust);
            }
        }
        return customers;
    }

    //8.1. Додавати інформацію про постійних клієнтів;
    public static void AddCustomer(CustomerCard customer) throws SQLException{
        String sql = "INSERT INTO Customer_Card (card_number, cust_surname, cust_name, cust_patronymic, phone_number, city, street, zip_code, percent)" +
                "VALUES ("+customer.getNumber()+", "+customer.getSurname()+", "+customer.getName()+", "+customer.getPatronymic()+", "+customer.getPhoneNumber()+", "+customer.getCity()+", "+customer.getStreet()+", "+customer.getZipCode()+", "+customer.getPercent()+")";
        Statement statement = connection.createStatement();
        statement.executeQuery(sql);
    }

    //8.2. редагувати інформацію про постійних клієнтів;
    public static void UpdateCustomer(CustomerCard customer) throws SQLException{
        String sql = "UPDATE Customer_Card" +
                "SET  (cust_surname = " +customer.getSurname()+
                ", cust_name = "+customer.getName()+
                ", cust_patronymic = "+customer.getPatronymic()+
                ", phone_number = "+customer.getPhoneNumber()+
                ", city = "+customer.getCity()+
                ", street = "+customer.getStreet()+
                ", zip_code = "+customer.getZipCode()+
                ", percent = "+customer.getPercent()+" " +
                "WHERE card_number = "+customer.getNumber()+")";
        Statement statement = connection.createStatement();
        statement.executeQuery(sql);
    }
}
