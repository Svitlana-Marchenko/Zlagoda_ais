package bd_connection;

import entity.Category;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static bd_connection.Category.getCategory;

public class Product {

    private static Connection connection;
    private static final String ID_PRODUCT = "id_product";
    private static final String PRODUCT_NAME = "product_name";
    private static final String CHARACTERISTICS = "characteristics";
    private static final String CATEGORY_ID = "category_number";
    private static final String CATEGORY_NAME = "category_name";
    private static final String PRODUCER = "producer";

    public static void setConnection(Connection con){
        connection=con;
    }

    //1. Додавати нові дані про товари
    public static boolean addProduct(entity.Product product){
        try{
            Statement statement = connection.createStatement();
            String request = "INSERT INTO `zlagoda`.`product` (`id_product`, `category_number`, `product_name`, `characteristics`, `producer`) VALUES ('"+product.getId()+"', '"+product.getCategory().getId()+"', '"+product.getName()+"', '"+product.getCharacteristics()+"', '"+product.getProducer()+"');\n";
            statement.execute(request);
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    //2. Редагувати дані про товари
    public static boolean updateProductById(entity.Product product){
        try {
            Statement statement = connection.createStatement();
            //Запитати за айді
            String request = "UPDATE `zlagoda`.`product` SET `category_number` = '"+product.getCategory().getId()+"', `product_name` = '"+product.getName()+"', `characteristics` = '"+product.getCharacteristics()+"', `producer` = '"+product.getProducer()+"' WHERE (`id_product` = '"+product.getId()+"');";
            statement.execute(request);
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    //3. Видаляти дані про товари
    public static boolean deleteProductById(int productId){
        try {
            Statement statement = connection.createStatement();
            String request = "DELETE FROM `zlagoda`.`product` WHERE (`id_product` = '"+productId+"');";
            statement.execute(request);
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    public static entity.Product findProductById(int id){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT id_product, product_name,characteristics, producer, `zlagoda`.`category`.category_number,category_name FROM `zlagoda`.`product` INNER JOIN `zlagoda`.`category` ON `zlagoda`.`product`.category_number = `zlagoda`.`category`.category_number WHERE (`id_product` = '"+id+"');";
            ResultSet resultSet = statement.executeQuery(request);
            entity.Product product = null;
            while(resultSet.next()) {
                product = new entity.Product(Integer.valueOf(resultSet.getString(ID_PRODUCT)),resultSet.getString(PRODUCT_NAME), new Category(Integer.valueOf(resultSet.getString(CATEGORY_ID)),resultSet.getString(CATEGORY_NAME)), resultSet.getString(PRODUCER),resultSet.getString(CHARACTERISTICS));
            }
            //System.out.println(product);
            return product;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public static entity.Product findProductByName(String name){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT id_product, product_name,characteristics,producer, `zlagoda`.`category`.category_number,category_name FROM `zlagoda`.`product` INNER JOIN `zlagoda`.`category` ON `zlagoda`.`product`.category_number = `zlagoda`.`category`.category_number WHERE (`"+PRODUCT_NAME+"` = '"+name+"');";
            ResultSet resultSet = statement.executeQuery(request);
            entity.Product product = null;
            while(resultSet.next()) {
                product = new entity.Product(Integer.valueOf(resultSet.getString(ID_PRODUCT)),resultSet.getString(PRODUCT_NAME), new Category(Integer.valueOf(resultSet.getString(CATEGORY_ID)),resultSet.getString(CATEGORY_NAME)),resultSet.getString(PRODUCER), resultSet.getString(CHARACTERISTICS));
            }
            //System.out.println(product);
            return product;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    //4. Видруковувати звіти з інформацією про усі товари
    public static ArrayList<entity.Product> findAll(){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT id_product, product_name,characteristics,producer,`zlagoda`.`category`.category_number, category_name FROM `zlagoda`.`product` INNER JOIN `zlagoda`.`category` ON `zlagoda`.`product`.category_number = `zlagoda`.`category`.category_number;";
            ResultSet resultSet = statement.executeQuery(request);
            ArrayList<entity.Product> products = new ArrayList<>();
            while(resultSet.next()) {
                products.add(new entity.Product(Integer.valueOf(resultSet.getString(ID_PRODUCT)),resultSet.getString(PRODUCT_NAME), new Category(Integer.valueOf(resultSet.getString(CATEGORY_ID)),resultSet.getString(CATEGORY_NAME)),resultSet.getString(PRODUCER),  resultSet.getString(CHARACTERISTICS)));
            }
            //System.out.println(products);
            return products;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
    }

    //9. Отримати інформацію про усі товари, відсортовані за назвою;
    public static ArrayList<entity.Product> findAllSortedByName(){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT id_product, product_name,characteristics, producer,`zlagoda`.`category`.category_number, category_name FROM `zlagoda`.`product` INNER JOIN `zlagoda`.`category` ON `zlagoda`.`product`.category_number = `zlagoda`.`category`.category_number ORDER BY "+PRODUCT_NAME+";";
            ResultSet resultSet = statement.executeQuery(request);
            ArrayList<entity.Product> products = new ArrayList<>();
            while(resultSet.next()) {
                products.add(new entity.Product(Integer.valueOf(resultSet.getString(ID_PRODUCT)),resultSet.getString(PRODUCT_NAME), new Category(Integer.valueOf(resultSet.getString(CATEGORY_ID)),resultSet.getString(CATEGORY_NAME)),resultSet.getString(PRODUCER),  resultSet.getString(CHARACTERISTICS)));
            }
            System.out.println(products);
            return products;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
    }


    //13 Здійснити пошук усіх товарів, що належать певній категорії, відсортованих за назвою;+
    //5. Здійснити пошук товарів, що належать певній категорії, відсортованих за назвою;
    public static List<entity.Product> getAllProductsInCategorySorted(boolean acs, Category cat) throws SQLException {
        List<entity.Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE category_number="+ cat.getId()+" ORDER BY product_name";
        if(!acs)
            sql+=" DESC";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id_product");
                String name = resultSet.getString("product_name");
                String characteristic = resultSet.getString("characteristics");
                entity.Product product = new entity.Product(id, name, cat,"", characteristic);
                products.add(product);
            }
        }
        return products;
    }


    //1 Отримати інформацію про усі товари, відсортовані за назвою; +
    public static List<entity.Product> getAllProductsSorted(boolean acs) throws SQLException {
        List<entity.Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Product ORDER BY product_name";
        if(!acs)
            sql+=" DESC";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id_product");
                String name = resultSet.getString("product_name");
                int categoryN = resultSet.getInt("category_number");
                String characteristic = resultSet.getString("characteristics");
                entity.Product product = new entity.Product(id, name, getCategory(categoryN), "", characteristic);
                products.add(product);
            }
        }

        return products;
    }

    //4. Здійснити пошук товарів за назвою;
    public static List<entity.Product> getAllProductsByName(String name) throws SQLException {
        List<entity.Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE product_name="+ name;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id_product");
                int categoryN = resultSet.getInt("category_number");
                String characteristic = resultSet.getString("characteristics");
                entity.Product product = new entity.Product(id, name, getCategory(categoryN),"", characteristic);
                products.add(product);
            }
        }
        return products;
    }

}
