package bd_connection;

import entity.Category;

import java.sql.*;
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
            String request = "INSERT INTO product (`id_product`, `category_number`, `product_name`, `characteristics`, `producer`) VALUES ('"+product.getId()+"', '"+product.getCategory().getId()+"', '"+product.getName()+"', '"+product.getCharacteristics()+"', '"+product.getProducer()+"');\n";
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
            String request = "UPDATE product SET `category_number` = '"+product.getCategory().getId()+"', `product_name` = '"+product.getName()+"', `characteristics` = '"+product.getCharacteristics()+"', `producer` = '"+product.getProducer()+"' WHERE (`id_product` = '"+product.getId()+"');";
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
            String request = "DELETE FROM product WHERE (`id_product` = '"+productId+"');";
            statement.execute(request);
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    //знайти товар за айді
    public static entity.Product findProductById(int id){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT id_product, product_name,characteristics, producer, category.category_number,category_name FROM product INNER JOIN category ON product.category_number = category.category_number WHERE (`id_product` = '"+id+"');";
            ResultSet resultSet = statement.executeQuery(request);
            entity.Product product = null;
            while(resultSet.next()) {
                product = new entity.Product(Integer.valueOf(resultSet.getString(ID_PRODUCT)),resultSet.getString(PRODUCT_NAME), new Category(Integer.valueOf(resultSet.getString(CATEGORY_ID)),resultSet.getString(CATEGORY_NAME)), resultSet.getString(PRODUCER),resultSet.getString(CHARACTERISTICS));
            }
            return product;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public static entity.Category findCategoryByProductId(int id){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT id_product, product_name,characteristics, producer, category.category_number,category_name FROM product INNER JOIN category ON product.category_number = category.category_number WHERE (`id_product` = '"+id+"');";
            ResultSet resultSet = statement.executeQuery(request);
            entity.Category category = null;
            while(resultSet.next()) {
                category = new Category(resultSet.getInt(CATEGORY_ID),resultSet.getString(CATEGORY_NAME));
            }
            return category;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    //4. Видруковувати звіти з інформацією про усі товари
    public static ArrayList<entity.Product> findAll(){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT id_product, product_name,characteristics,producer,category.category_number, category_name FROM product INNER JOIN category ON product.category_number = category.category_number;";
            ResultSet resultSet = statement.executeQuery(request);
            ArrayList<entity.Product> products = new ArrayList<>();
            while(resultSet.next()) {
                products.add(new entity.Product(resultSet.getInt(ID_PRODUCT),resultSet.getString(PRODUCT_NAME), new Category(resultSet.getInt(CATEGORY_ID),resultSet.getString(CATEGORY_NAME)),resultSet.getString(PRODUCER),  resultSet.getString(CHARACTERISTICS)));
            }
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
            String request = "SELECT id_product, product_name,characteristics, producer, category.category_number, category_name FROM product INNER JOIN category ON product.category_number = category.category_number ORDER BY "+PRODUCT_NAME+";";
            ResultSet resultSet = statement.executeQuery(request);
            ArrayList<entity.Product> products = new ArrayList<>();
            while(resultSet.next()) {
                products.add(new entity.Product(Integer.valueOf(resultSet.getString(ID_PRODUCT)),resultSet.getString(PRODUCT_NAME), new Category(Integer.valueOf(resultSet.getString(CATEGORY_ID)),resultSet.getString(CATEGORY_NAME)),resultSet.getString(PRODUCER),  resultSet.getString(CHARACTERISTICS)));
            }
            return products;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
    }




    //13 Здійснити пошук усіх товарів, що належать певній категорії, відсортованих за назвою;+
    //5. Здійснити пошук товарів, що належать певній категорії, відсортованих за назвою;
    public static List<entity.Product> getAllProductsInCategorySorted(boolean acs, Category cat) {
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
                String producer = resultSet.getString("producer");
                entity.Product product = new entity.Product(id, name, cat,producer, characteristic);
                products.add(product);
            }
            return products;
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }

    }


    //1 Отримати інформацію про усі товари, відсортовані за назвою; +
    public static List<entity.Product> getAllProductsSorted(boolean acs)  {
        try {
            List<entity.Product> products = new ArrayList<>();
            String sql = "SELECT * FROM Product ORDER BY product_name";
            if (!acs)
                sql += " DESC";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id_product");
                    String name = resultSet.getString("product_name");
                    int categoryN = resultSet.getInt("category_number");
                    String producer = resultSet.getString("producer");
                    String characteristic = resultSet.getString("characteristics");
                    entity.Product product = new entity.Product(id, name, getCategory(categoryN), producer, characteristic);
                    products.add(product);
                }
            }

            return products;
        }catch(SQLException ex){
            ex.printStackTrace();
            return new ArrayList<>();
        }

    }

    //4. Здійснити пошук товарів за назвою;
    public static List<entity.Product> getAllProductsByName(String name) throws SQLException {
        List<entity.Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE product_name = '"+name+"'";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id_product");
                int categoryN = resultSet.getInt("category_number");
                String characteristic = resultSet.getString("characteristics");
                String producer = resultSet.getString("producer");
                entity.Product product = new entity.Product(id, name, getCategory(categoryN),producer, characteristic);
                products.add(product);
            }
        }
        return products;
    }

    //additional mehtod. get all product by name and category
    public static List<entity.Product> getAllProductsByNameAndCategory(String name, Category cat) throws SQLException {
        List<entity.Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE product_name = '"+ name+"'"+" AND category_number = "+cat.getId();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id_product");
                int categoryN = resultSet.getInt("category_number");
                String characteristic = resultSet.getString("characteristics");
                String producer = resultSet.getString("producer");
                entity.Product product = new entity.Product(id, name, getCategory(categoryN),producer, characteristic);
                products.add(product);
            }
        }
        return products;
    }


    public static entity.Product getProductById(int id){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT id_product, product_name, characteristics, producer, Category.category_number, category_name FROM product INNER JOIN Category ON Product.category_number = Category.category_number WHERE (`id_product` = '"+id+"');";
            ResultSet resultSet = statement.executeQuery(request);
            entity.Product product = null;
            while(resultSet.next()) {
                product = new entity.Product(Integer.valueOf(resultSet.getString(ID_PRODUCT)),resultSet.getString(PRODUCT_NAME), new Category(Integer.valueOf(resultSet.getString(CATEGORY_ID)),resultSet.getString(CATEGORY_NAME)), resultSet.getString(PRODUCER),resultSet.getString(CHARACTERISTICS));
            }
            return product;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
