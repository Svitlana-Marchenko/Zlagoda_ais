package bd_connection;

import entity.CustomerCard;
import entity.ProductInStore;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Customer_Card {

    private static Connection connection;

    public static void setConnection(Connection con){
        connection=con;
    }
    static{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/zlagoda",
                    "zhenia",
                    "happydog"
            );
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }


    private static final String CARD_NUMBER = "card_number";
    private static final String CUSTOMER_SURNAME = "cust_surname";
    private static final String CUSTOMER_NAME = "cust_name";
    private static final String CUSTOMER_PATRONYMIC = "cust_patronymic";
    private static final String PHONE_NUMBER = "phone_number";
    private static final String CITY = "city";
    private static final String STREET = "street";
    private static final String ZIP_CODE = "zip_code";
    private static final String PERCENT = "percent";

    //1. Додавати нові дані про постійних клієнтів
    public static boolean addCustomer(CustomerCard customer){
        try{
            Statement statement = connection.createStatement();
            String request="INSERT INTO customer_card (`card_number`, `cust_surname`, `cust_name`, `cust_patronymic`, `phone_number`, `city`, `street`, `zip_code`, `percent`) VALUES ('"+customer.getNumber()+"', '"+customer.getSurname()+"', '"+customer.getName()+"', '"+customer.getPatronymic()+"', '"+customer.getPhoneNumber()+"', '"+customer.getCity()+"', '"+customer.getStreet()+"', '"+customer.getZipCode()+"', '"+customer.getPercent()+"');";
            statement.execute(request);
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    //2. Редагувати дані про постійних клієнтів
    public static boolean updateCustomerById(CustomerCard customer){
        try {
            Statement statement = connection.createStatement();
            String request="UPDATE customer_card SET `cust_surname` = '"+customer.getSurname()+"', `cust_name` = '"+customer.getName()+"', `cust_patronymic` = '"+customer.getPatronymic()+"', `phone_number` = '"+customer.getPhoneNumber()+"', `city` = '"+customer.getCity()+"', `street` = '"+customer.getStreet()+"', `zip_code` = '"+customer.getZipCode()+"', `percent` = '"+customer.getPercent()+"' WHERE (`card_number` = '"+customer.getNumber()+"');";
            statement.execute(request);
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    //3. Видаляти дані про постійних клієнтів
    public static boolean deleteCustomerById(String id){
        try {
            Statement statement = connection.createStatement();
            String request = "DELETE FROM customer_card WHERE (`"+CARD_NUMBER+"` = '"+id+"');";
            statement.execute(request);
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    //4. Видруковувати звіти з інформацією про усіх постійних клієнтів
    public static ArrayList<CustomerCard> findAll(){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT * FROM customer_card;";
            ResultSet resultSet = statement.executeQuery(request);
            ArrayList<CustomerCard> customerCards = new ArrayList<>();
            while(resultSet.next()) {
                customerCards.add(new CustomerCard(resultSet.getString(CARD_NUMBER),resultSet.getString(CUSTOMER_SURNAME),resultSet.getString(CUSTOMER_NAME),resultSet.getString(CUSTOMER_PATRONYMIC),resultSet.getString(PHONE_NUMBER),resultSet.getString(CITY),resultSet.getString(STREET),resultSet.getString(ZIP_CODE),resultSet.getInt(PERCENT)));
            }
            return customerCards;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
    }


    //знайти покупця за айді
    public static CustomerCard findCustomerCardById(String id){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT * FROM customer_card WHERE (`"+CARD_NUMBER+"` = '"+id+"');";
            ResultSet resultSet = statement.executeQuery(request);
            CustomerCard customerCard = null;
            while(resultSet.next()) {
                customerCard=new CustomerCard(resultSet.getString(CARD_NUMBER),resultSet.getString(CUSTOMER_SURNAME),resultSet.getString(CUSTOMER_NAME),resultSet.getString(CUSTOMER_PATRONYMIC),resultSet.getString(PHONE_NUMBER),resultSet.getString(CITY),resultSet.getString(STREET),resultSet.getString(ZIP_CODE),resultSet.getInt(PERCENT));
            }
            return customerCard;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

//7. Отримати інформацію про усіх постійних клієнтів, відсортованих за прізвищем;

    public static ArrayList<CustomerCard> findAllSortedBySurname(){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT * FROM customer_card ORDER BY "+CUSTOMER_SURNAME+";";
            ResultSet resultSet = statement.executeQuery(request);
            ArrayList<CustomerCard> customerCards = new ArrayList<>();
            while(resultSet.next()) {
                customerCards.add(new CustomerCard(resultSet.getString(CARD_NUMBER),resultSet.getString(CUSTOMER_SURNAME),resultSet.getString(CUSTOMER_NAME),resultSet.getString(CUSTOMER_PATRONYMIC),resultSet.getString(PHONE_NUMBER),resultSet.getString(CITY),resultSet.getString(STREET),resultSet.getString(ZIP_CODE),resultSet.getInt(PERCENT)));
            }
            return customerCards;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
    }

    //12. Отримати інформацію про усіх постійних клієнтів, що мають карту клієнта із певним відсотком, посортованих за прізвищем;
    public static ArrayList<CustomerCard> findAllSortedBySurnameWithPercent(){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT * FROM customer_card WHERE (`"+PERCENT+"` >0) ORDER BY "+CUSTOMER_SURNAME+";";
            ResultSet resultSet = statement.executeQuery(request);
            ArrayList<CustomerCard> customerCards = new ArrayList<>();
            while(resultSet.next()) {
                customerCards.add(new CustomerCard(resultSet.getString(CARD_NUMBER),resultSet.getString(CUSTOMER_SURNAME),resultSet.getString(CUSTOMER_NAME),resultSet.getString(CUSTOMER_PATRONYMIC),resultSet.getString(PHONE_NUMBER),resultSet.getString(CITY),resultSet.getString(STREET),resultSet.getString(ZIP_CODE),resultSet.getInt(PERCENT)));
            }
            return customerCards;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
    }

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
        String sql = "SELECT * FROM Customer_Card WHERE cust_surname = '"+ surname+"'";
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

    //12. Отримати інформацію про усіх постійних клієнтів, що мають карту клієнта із певним відсотком, посортованих за прізвищем;
    public static ArrayList<CustomerCard> findAllCustomersSortedBySurnameWithPercent(int percent){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT * FROM Customer_card WHERE (`"+PERCENT+"` = "+percent+") ORDER BY "+CUSTOMER_SURNAME+";";
            ResultSet resultSet = statement.executeQuery(request);
            ArrayList<CustomerCard> customerCards = new ArrayList<>();
            while(resultSet.next()) {
                customerCards.add(new CustomerCard(resultSet.getString(CARD_NUMBER),resultSet.getString(CUSTOMER_SURNAME),resultSet.getString(CUSTOMER_NAME),resultSet.getString(CUSTOMER_PATRONYMIC),resultSet.getString(PHONE_NUMBER),resultSet.getString(CITY),resultSet.getString(STREET),resultSet.getString(ZIP_CODE),Integer.valueOf(resultSet.getString(PERCENT))));
            }
            return customerCards;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
    }


    public static ArrayList<CustomerCard> findCustomersWhoBuyFromAllCategoriesBetweenTwoDates(Date from, Date to){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Statement statement = connection.createStatement();
            String request = "SELECT * FROM customer_card CC " +
                    "where not exists (select * from category C1" +
                    "                  where not exists( select * from zlagoda.sale" +
                    "                 inner join `check` ON `check`.check_number=sale.check_number" +
                    "                 Inner join store_product On sale.UPC=store_product.UPC" +
                    "                                   Inner join product ON product.id_product=store_product.id_product" +
                    "                                   Inner join category C2 On C2.category_number=product.category_number" +
                    "                                   WHERE `check`.card_number = CC.card_number AND C2.category_number = C1.category_number" +
                    "                                   AND DATE(print_date) >= '" + sdf.format(from) + "' AND DATE(print_date) <= '" + sdf.format(to) + "'));";


            ResultSet resultSet = statement.executeQuery(request);
            ArrayList<CustomerCard> customerCards = new ArrayList<>();
            while(resultSet.next()) {
                customerCards.add(new CustomerCard(resultSet.getString(CARD_NUMBER),resultSet.getString(CUSTOMER_SURNAME),resultSet.getString(CUSTOMER_NAME),resultSet.getString(CUSTOMER_PATRONYMIC),resultSet.getString(PHONE_NUMBER),resultSet.getString(CITY),resultSet.getString(STREET),resultSet.getString(ZIP_CODE),resultSet.getInt(PERCENT)));
            }
            return customerCards;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
    }
    //find customers who worked with all cashiers
    public static ArrayList<CustomerCard> findCustomersWhoWorkedWithAllCashiers(){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT * " +
                    "FROM Customer_Card cc " +
                    "WHERE NOT EXISTS " +
                    "(SELECT id_employee " +
                    "FROM Employee e " +
                    "WHERE e.role=CASHIER " +
                    "AND e.id_employee NOT IN " +
                    "(SELECT id_employee " +
                    "FROM `Check` ch " +
                    "WHERE ch.card_number=cc.card_number));";
            ResultSet resultSet = statement.executeQuery(request);
            ArrayList<CustomerCard> customerCards = new ArrayList<>();
            while(resultSet.next()) {
                customerCards.add(new CustomerCard(resultSet.getString(CARD_NUMBER),resultSet.getString(CUSTOMER_SURNAME),resultSet.getString(CUSTOMER_NAME),resultSet.getString(CUSTOMER_PATRONYMIC),resultSet.getString(PHONE_NUMBER),resultSet.getString(CITY),resultSet.getString(STREET),resultSet.getString(ZIP_CODE),Integer.valueOf(resultSet.getString(PERCENT))));
            }
            return customerCards;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
    }

    //count number of checks that have sum above certain value for each customer
    public static HashMap<CustomerCard, Integer> countReceiptsForCustomersAboveSum(BigDecimal sum){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT cc.card_number, cust_surname, cust_name, cust_patronymic, phone_number, city, street, zip_code, percent, COUNT(DISTINCT check_number) AS amount " +
                    "FROM `check`" +
                    "RIGHT JOIN Customer_Card cc ON cc.card_number = `check`.card_number " +
                    "WHERE sum_total >= '"+sum+"' " +
                    "GROUP BY card_number;";
            ResultSet resultSet = statement.executeQuery(request);
            HashMap<CustomerCard, Integer> customerCards = new HashMap<CustomerCard, Integer>();
            while(resultSet.next()) {
                customerCards.put(new CustomerCard(resultSet.getString(CARD_NUMBER),resultSet.getString(CUSTOMER_SURNAME),resultSet.getString(CUSTOMER_NAME),resultSet.getString(CUSTOMER_PATRONYMIC),resultSet.getString(PHONE_NUMBER),resultSet.getString(CITY),resultSet.getString(STREET),resultSet.getString(ZIP_CODE),Integer.valueOf(resultSet.getString(PERCENT))),Integer.valueOf(resultSet.getString("amount")));
            }
            return customerCards;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return new HashMap<>();
        }
    }
}
