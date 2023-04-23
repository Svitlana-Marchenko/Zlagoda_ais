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
    private static final String UPC = "UPC";
    private static final String UPC_PROM = "UPC_prom";
    private static final String SELLING_PRICE = "selling_price";
    private static final String PRODUCTS_NUMBER = "products_number";
    private static final String PROMOTIONAL_PRODUCT = "promotional_product";
    private static final String ID_PRODUCT = "id_product";
    private static final String PRODUCT_NAME = "product_name";
    private static final String CHARACTERISTICS = "characteristics";
    private static final String CATEGORY_ID = "category_number";
    private static final String CATEGORY_NAME = "category_name";

    //1. Додавати нові дані про товари у магазині;
    public static boolean addProductInStore(ProductInStore product){
        try{
            Statement statement = connection.createStatement();
            String request="";
            if(product.getPromotionalUPC()!=null)
                request= "INSERT INTO `zlagoda`.`store_product` (`UPC`, `UPC_prom`, `id_product`, `selling_price`, `products_number`, `promotional_product`) VALUES ('"+product.getUPC()+"', '"+product.getPromotionalUPC()+"', '"+product.getProduct().getId()+"', '"+product.getPrice()+"', '"+product.getAmount()+"', '"+Boolean.compare(product.isPromotional(),false)+"');";
            else
                request= "INSERT INTO `zlagoda`.`store_product` (`UPC`, `id_product`, `selling_price`, `products_number`, `promotional_product`) VALUES ('"+product.getUPC()+"', '"+product.getProduct().getId()+"', '"+product.getPrice()+"', '"+product.getAmount()+"', '"+Boolean.compare(product.isPromotional(),false)+"');";
            statement.execute(request);
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    //2. Редагувати дані про товари у магазині;
    public static boolean updateProductInStoreById(ProductInStore product){
        try {
            Statement statement = connection.createStatement();
            String request = "UPDATE `zlagoda`.`store_product` SET `selling_price` = '"+product.getPrice()+"', `products_number` = '"+product.getAmount()+"' WHERE (`UPC` = '"+product.getUPC()+"');";
            statement.execute(request);
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    //3. Видаляти дані про товари у магазині;
    public static boolean deleteProductInStoreByUPC(String productUPC){
        try {
            Statement statement = connection.createStatement();
            String request = "DELETE FROM `zlagoda`.`store_product` WHERE (`"+UPC+"` = '"+productUPC+"');";
            statement.execute(request);
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    //4. Видруковувати звіти з інформацією про усі товари у магазині

    public static ArrayList<ProductInStore> findAll(){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT "+UPC+", "+UPC_PROM+",`zlagoda`.`product`."+ID_PRODUCT+", "+SELLING_PRICE+", "+PRODUCTS_NUMBER+", "+PROMOTIONAL_PRODUCT+", "+CHARACTERISTICS+", "+PRODUCT_NAME+", `zlagoda`.`category`."+CATEGORY_ID+", "+CATEGORY_NAME+" FROM `zlagoda`.`store_product` INNER JOIN `zlagoda`.`product` ON `zlagoda`.`product`."+ID_PRODUCT+" = `zlagoda`.`store_product`."+ID_PRODUCT+"" +
                    " INNER JOIN `zlagoda`.`category` ON `zlagoda`.`product`."+CATEGORY_ID+" = `zlagoda`.`category`."+CATEGORY_ID+";";
            ResultSet resultSet = statement.executeQuery(request);
            ArrayList<ProductInStore> products = new ArrayList<>();
            while(resultSet.next()) {
                products.add(new ProductInStore(resultSet.getString(UPC), resultSet.getString(UPC_PROM),
                        bd_connection.Product.findProductById(Integer.valueOf(resultSet.getString(ID_PRODUCT))), new BigDecimal(resultSet.getString(SELLING_PRICE)), Integer.valueOf(resultSet.getString(PRODUCTS_NUMBER)), (Integer.valueOf(resultSet.getString(PROMOTIONAL_PRODUCT)) == 1 ? true : false)));
            }
            return products;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
    }

    //знайти товари на складі для певного товару
    public static ArrayList<ProductInStore> findStoreProductsByProductId(int id){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT "+UPC+", "+UPC_PROM+",`zlagoda`.`product`."+ID_PRODUCT+", "+SELLING_PRICE+", "+PRODUCTS_NUMBER+", "+PROMOTIONAL_PRODUCT+", "+CHARACTERISTICS+", "+PRODUCT_NAME+", `zlagoda`.`category`."+CATEGORY_ID+", "+CATEGORY_NAME+" FROM `zlagoda`.`store_product` INNER JOIN `zlagoda`.`product` ON `zlagoda`.`product`."+ID_PRODUCT+" = `zlagoda`.`store_product`."+ID_PRODUCT+"" +
                    " INNER JOIN `zlagoda`.`category` ON `zlagoda`.`product`."+CATEGORY_ID+" = `zlagoda`.`category`."+CATEGORY_ID+" WHERE (`zlagoda`.`product`."+ID_PRODUCT+" = '"+id+"');";
            ResultSet resultSet = statement.executeQuery(request);
            ArrayList<ProductInStore> products = new ArrayList<>();
            while(resultSet.next()) {
                products.add(new ProductInStore(resultSet.getString(UPC), resultSet.getString(UPC_PROM),
                        bd_connection.Product.findProductById(Integer.valueOf(resultSet.getString(ID_PRODUCT))), new BigDecimal(resultSet.getString(SELLING_PRICE)), Integer.valueOf(resultSet.getString(PRODUCTS_NUMBER)), (Integer.valueOf(resultSet.getString(PROMOTIONAL_PRODUCT)) == 1 ? true : false)));
            }
            return products;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
    }

    //знайти товар на складі за айді
    public static ProductInStore findProductInStoreById(String productUPC){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT "+UPC+", "+UPC_PROM+",`zlagoda`.`product`."+ID_PRODUCT+", "+SELLING_PRICE+", "+PRODUCTS_NUMBER+", "+PROMOTIONAL_PRODUCT+", "+CHARACTERISTICS+", "+PRODUCT_NAME+", `zlagoda`.`category`."+CATEGORY_ID+", "+CATEGORY_NAME+" FROM `zlagoda`.`store_product` INNER JOIN `zlagoda`.`product` ON `zlagoda`.`product`."+ID_PRODUCT+" = `zlagoda`.`store_product`."+ID_PRODUCT+"" +
                    " INNER JOIN `zlagoda`.`category` ON `zlagoda`.`product`."+CATEGORY_ID+" = `zlagoda`.`category`."+CATEGORY_ID+" WHERE (`"+UPC+"` = '"+productUPC+"');";
            ResultSet resultSet = statement.executeQuery(request);
            ProductInStore product = null;
            while(resultSet.next()) {
                product = new ProductInStore(resultSet.getString(UPC), resultSet.getString(UPC_PROM),
                        bd_connection.Product.findProductById(Integer.valueOf(resultSet.getString(ID_PRODUCT))), new BigDecimal(resultSet.getString(SELLING_PRICE)), Integer.valueOf(resultSet.getString(PRODUCTS_NUMBER)), (Integer.valueOf(resultSet.getString(PROMOTIONAL_PRODUCT)) == 1 ? true : false));
            }
            return product;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    //10. Отримати інформацію про усі товари у магазині, відсортовані за кількістю;
    public static ArrayList<ProductInStore> findAllSortedByAmount(){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT "+UPC+", "+UPC_PROM+",`zlagoda`.`product`."+ID_PRODUCT+", "+SELLING_PRICE+", "+PRODUCTS_NUMBER+", "+PROMOTIONAL_PRODUCT+", "+CHARACTERISTICS+", "+PRODUCT_NAME+", `zlagoda`.`category`."+CATEGORY_ID+", "+CATEGORY_NAME+" FROM `zlagoda`.`store_product` INNER JOIN `zlagoda`.`product` ON `zlagoda`.`product`."+ID_PRODUCT+" = `zlagoda`.`store_product`."+ID_PRODUCT+"" +
                    " INNER JOIN `zlagoda`.`category` ON `zlagoda`.`product`."+CATEGORY_ID+" = `zlagoda`.`category`."+CATEGORY_ID+" ORDER BY "+PRODUCTS_NUMBER+";";
            ResultSet resultSet = statement.executeQuery(request);
            ArrayList<ProductInStore> products = new ArrayList<>();
            while(resultSet.next()) {
                products.add(new ProductInStore(resultSet.getString(UPC), resultSet.getString(UPC_PROM),
                        bd_connection.Product.findProductById(Integer.valueOf(resultSet.getString(ID_PRODUCT))), new BigDecimal(resultSet.getString(SELLING_PRICE)), Integer.valueOf(resultSet.getString(PRODUCTS_NUMBER)), (Integer.valueOf(resultSet.getString(PROMOTIONAL_PRODUCT)) == 1 ? true : false)));
            }
            return products;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
    }


    //14 За UPC-товару знайти ціну продажу товару, кількість наявних одиниць товару, назву та характеристики товару;+
    //14. За UPC-товару знайти ціну продажу товару, кількість наявних одиниць товару.
    public static ProductInStore getAllAboutProductsOnUPC(String upc) throws SQLException {

        String sql = "SELECT * FROM Store_Product s LEFT JOIN Product p ON s.id_product=p.id_product WHERE s.UPC = '"+ upc+"'";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                int id = resultSet.getInt("id_product");
                String upc_prom = resultSet.getString("UPC_prom");
                String producer = resultSet.getString("producer");
                BigDecimal price = resultSet.getBigDecimal("selling_price");
                int products_number = resultSet.getInt("products_number");
                boolean prom_products = resultSet.getBoolean("promotional_product");

                String name = resultSet.getString("product_name");
                int categoryN = resultSet.getInt("category_number");
                String characteristic = resultSet.getString("characteristics");

                String sqlCat = "SELECT * FROM Category WHERE category_number = '"+categoryN+"'";
                ResultSet resultCat = statement.executeQuery(sqlCat);
                return new ProductInStore(upc, upc_prom, new Product(id, name, getCategory(categoryN), producer, characteristic), price, products_number, prom_products);

            }
        }
        return null;
    }


    //15, Отримати інформацію про усі акційні товари, відсортовані за кількістю одиниць товару/ за назвою;+
    // 16 Отримати інформацію про усі не акційні товари, відсортовані за кількістю одиниць товару/ за назвою;+
    //12. Отримати інформацію про усі акційні товари, відсортовані за кількістю одиниць товару/ за назвою;
    //13. Отримати інформацію про усі не акційні товарів, відсортовані за кількістю одиниць товару/ за назвою;
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
                String producer = resultSet.getString("producer");
                BigDecimal price = resultSet.getBigDecimal("selling_price");
                int products_number = resultSet.getInt("products_number");
                boolean prom_products = resultSet.getBoolean("promotional_product");

                String name = resultSet.getString("product_name");
                int categoryN = resultSet.getInt("category_number");
                String characteristic = resultSet.getString("characteristics");


                ProductInStore product = new ProductInStore(upc, upc_prom, new Product(id, name, getCategory(categoryN), producer, characteristic), price, products_number, prom_products);
                products.add(product);
            }
        }
        return products;
    }


    //2 Отримати інформацію про усі товари у магазині, відсортовані за назвою; +
    public static List<ProductInStore> getAllProductsInStoreSorted(boolean acs){
        try {
            List<ProductInStore> products = new ArrayList<>();
            String sql = "SELECT * " +
                    "FROM Store_Product sp " +
                    "LEFT JOIN Product p ON p.id_product = sp.id_product " +
                    "ORDER BY p.product_name";
            if (!acs)
                sql += " DESC";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {

                    int id = resultSet.getInt("id_product");
                    String upc = resultSet.getString("upc");
                    String upc_prom = resultSet.getString("upc_prom");
                    String producer = resultSet.getString("producer");
                    BigDecimal price = resultSet.getBigDecimal("selling_price");
                    int products_number = resultSet.getInt("products_number");
                    boolean prom_products = resultSet.getBoolean("promotional_product");

                    String name = resultSet.getString("product_name");
                    int categoryN = resultSet.getInt("category_number");
                    String characteristic = resultSet.getString("characteristics");

                    ProductInStore product = new ProductInStore(upc, upc_prom, new Product(id, name, getCategory(categoryN), producer, characteristic), price, products_number, prom_products);
                    products.add(product);
                }
            }
            return products;
        }catch(SQLException ex){
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }


}
