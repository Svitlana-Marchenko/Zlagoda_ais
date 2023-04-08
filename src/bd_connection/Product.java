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



    //13 Здійснити пошук усіх товарів, що належать певній категорії, відсортованих за назвою;+
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


}
