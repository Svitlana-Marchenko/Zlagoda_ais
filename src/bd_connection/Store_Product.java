package bd_connection;

import entity.Product;
import entity.ProductInStore;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static bd_connection.Category.getCategory;

public class Store_Product {


    private static Connection connection;



    //14 За UPC-товару знайти ціну продажу товару, кількість наявних одиниць товару, назву та характеристики товару;+
    public static ProductInStore getAllAboutProductsOnUPC(String upc) throws SQLException {

        String sql = "SELECT * FROM Store_Product s LEFT JOIN Product p ON s.id_product=p.id_product WHERE s.UPC="+ upc;

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                int id = resultSet.getInt("id_product");
                String upc_prom = resultSet.getString("UPC_prom");
                BigDecimal price = resultSet.getBigDecimal("selling_price");
                int products_number = resultSet.getInt("products_number");
                boolean prom_products = resultSet.getBoolean("promotional_product");

                String name = resultSet.getString("product_name");
                int categoryN = resultSet.getInt("category_number");
                String characteristic = resultSet.getString("characteristics");

                String sqlCat = "SELECT * FROM Сategory WHERE category_number = "+categoryN;
                ResultSet resultCat = statement.executeQuery(sqlCat);
                return new ProductInStore(upc, upc_prom, new Product(id, name, getCategory(categoryN), "", characteristic), price, products_number, prom_products);

            }
        }
        return null;
    }


    //15, Отримати інформацію про усі акційні товари, відсортовані за кількістю одиниць товару/ за назвою;+
    // 16 Отримати інформацію про усі не акційні товари, відсортовані за кількістю одиниць товару/ за назвою;+
    public static List<ProductInStore> getAllProductsInStoreSaleSorted(boolean acs, String sortM, boolean onSale) throws SQLException {
        List<ProductInStore> products = new ArrayList<>();
        String sql = "SELECT *\n" +
                "FROM Store_product sp\n" +
                " LEFT JOIN product p ON p.id_product = sp.id_product\n" +
                " WHERE sp.promotional_product="+onSale+"\n" +
                " ORDER BY ";
        if(sortM.equals("product_name"))
            sql+=" p."+sortM;
        else
            sql+=" sp."+sortM;
        if(!acs)
            sql+=" DESC";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {

                int id = resultSet.getInt("id_product");
                String upc = resultSet.getString("upc");
                String upc_prom = resultSet.getString("upc_prom");
                BigDecimal price = resultSet.getBigDecimal("selling_price");
                int products_number = resultSet.getInt("products_number");
                boolean prom_products = resultSet.getBoolean("promotional_product");

                String name = resultSet.getString("product_name");
                int categoryN = resultSet.getInt("category_number");
                String characteristic = resultSet.getString("characteristics");


                ProductInStore product = new ProductInStore(upc, upc_prom, new Product(id, name, getCategory(categoryN), "", characteristic), price, products_number, prom_products);
                products.add(product);
            }
        }
        return products;
    }


    //2 Отримати інформацію про усі товари у магазині, відсортовані за назвою; +
    public static List<ProductInStore> getAllProductsInStoreSorted(boolean acs) throws SQLException {
        List<ProductInStore> products = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM Store_Product sp " +
                "LEFT JOIN Product p ON p.id_product = sp.id_product " +
                "ORDER BY p.product_name";
        if(!acs)
            sql+=" DESC";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {

                int id = resultSet.getInt("id_product");
                String upc = resultSet.getString("upc");
                String upc_prom = resultSet.getString("upc_prom");
                BigDecimal price = resultSet.getBigDecimal("selling_price");
                int products_number = resultSet.getInt("products_number");
                boolean prom_products = resultSet.getBoolean("promotional_product");

                String name = resultSet.getString("product_name");
                int categoryN = resultSet.getInt("category_number");
                String characteristic = resultSet.getString("characteristics");

                ProductInStore product = new ProductInStore(upc, upc_prom, new Product(id, name, getCategory(categoryN), "", characteristic), price, products_number, prom_products);
                products.add(product);
            }
        }
        return products;
    }



}
