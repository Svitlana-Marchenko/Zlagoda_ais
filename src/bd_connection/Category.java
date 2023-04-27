package bd_connection;

import entity.Employee;

import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Category {

    private static Connection connection;

    public static void setConnection(Connection con){
        connection=con;
    }

    private static final String CATEGORY_NUMBER = "category_number";
    private static final String CATEGORY_NAME = "category_name";


    //1. Додавати нові дані про категорії товарів
    public static boolean addCategory(entity.Category category){
        try{
            Statement statement = connection.createStatement();
            String request = "INSERT INTO category (`category_number`, `category_name`) VALUES ('"+category.getId()+"', '"+category.getName()+"'); ";
            statement.execute(request);
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    //2. Редагувати дані про категорії товарів
    public static boolean updateCategoryById(entity.Category category){
        try {
            Statement statement = connection.createStatement();
            //Запитати за айді
            String request = "UPDATE category SET `category_name` = '"+category.getName()+"' WHERE (`category_number` = '"+category.getId()+"');";
            statement.execute(request);
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    //3. Видаляти дані про категорії товарів
    public static boolean deleteCategoryById(int categoryId){
        try {
            Statement statement = connection.createStatement();
            String request = "DELETE FROM category WHERE (`category_number` = '"+categoryId+"');";
            statement.execute(request);
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

//знайти категорії товарів за айді
    public static entity.Category findCategoryById(int id){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT * FROM category WHERE (`category_number` = '"+id+"');";
            ResultSet resultSet = statement.executeQuery(request);
            entity.Category category =null;
            while(resultSet.next()) {
                category = new entity.Category(resultSet.getInt(CATEGORY_NUMBER),resultSet.getString(CATEGORY_NAME));
            }
            return category;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    //4. Видруковувати звіти з інформацією про усі категорії товарів
    public static ArrayList<entity.Category> findAll(){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT * FROM category;";
            ResultSet resultSet = statement.executeQuery(request);
            ArrayList<entity.Category> categories = new ArrayList<>();
            while(resultSet.next()) {
                categories.add(new entity.Category(resultSet.getInt(CATEGORY_NUMBER),resultSet.getString(CATEGORY_NAME)));
            }
            return categories;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
    }


    //8. Отримати інформацію про усі категорії, відсортовані за назвою;
    public static ArrayList<entity.Category> findAllSortedByName(){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT * FROM category ORDER BY `category_name`;";
            ResultSet resultSet = statement.executeQuery(request);
            ArrayList<entity.Category> categories = new ArrayList<>();
            while(resultSet.next()) {
                categories.add(new entity.Category(resultSet.getInt(CATEGORY_NUMBER),resultSet.getString(CATEGORY_NAME)));
            }
            return categories;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
    }


    //additional method for getting category having category_number
     static entity.Category getCategory(int categoryN) throws SQLException {
        Statement statement = connection.createStatement();
        String sqlCat = "SELECT * FROM Category WHERE category_number = "+categoryN;
        ResultSet resultCat = statement.executeQuery(sqlCat);

        if (resultCat.next()) {
            return new entity.Category(categoryN, resultCat.getString("category_name"));
        } else {
            return null;
        }
    }


    //additional method for getting category having category_number
   public static List<entity.Category> getAllCategories() {
try{
       List<entity.Category> answ = new ArrayList<>();
        Statement statement = connection.createStatement();
        String sqlCat = "SELECT * FROM Category";
        ResultSet resultCat = statement.executeQuery(sqlCat);

        while (resultCat.next()) {
            answ.add( new entity.Category(resultCat.getInt("category_number"), resultCat.getString("category_name")));
        }
        return answ;
}catch (SQLException ex){
    System.out.println(ex.getMessage());
    return new ArrayList<>();
}
    }

    //additional method to get category by id
    public static entity.Category getCategoryById(int id){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT * FROM Category WHERE (`category_number` = "+id+");";
            ResultSet resultSet = statement.executeQuery(request);
            entity.Category category =null;
            while(resultSet.next()) {
                category = new entity.Category(Integer.valueOf(resultSet.getString(CATEGORY_NUMBER)),resultSet.getString(CATEGORY_NAME));
            }
            //System.out.println(categories);
            return category;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    //Підрахувати кількістьпроданих товарів для кожної категорії за заданий період часу
    public static ArrayList <String[]> countSoldProductInGivenCategoryHavingDate(Date from, Date to){
        ArrayList <String[]> answ = new ArrayList<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Statement statement = connection.createStatement();
            String request =
                    "    SELECT cat.category_number, cat.category_name , IFNULL(s.total_sale, 0) AS total\n" +
                            "FROM Category cat\n" +
                            "LEFT JOIN (\n" +
                            "    SELECT p.category_number, SUM(sa.product_number) AS total_sale\n" +
                            "    FROM Product p\n" +
                            "    LEFT JOIN Store_Product s ON p.id_product = s.id_product\n" +
                            "    LEFT JOIN Sale sa ON s.UPC = sa.UPC\n" +
                            "    LEFT JOIN `Check` ch ON sa.check_number = ch.check_number\n" +
                            "    WHERE DATE( ch.print_date) >= '" + sdf.format(from) + "' AND DATE( ch.print_date) <= '" + sdf.format(to) + "'" +
                            "    GROUP BY p.category_number\n" +
                            ") s ON cat.category_number = s.category_number;";
            ResultSet resultSet = statement.executeQuery(request);

            while(resultSet.next()) {
                answ.add(new String[]{Integer.valueOf(resultSet.getString(CATEGORY_NUMBER))+"",String.valueOf(resultSet.getString(CATEGORY_NAME)), ""+Integer.valueOf(resultSet.getString("total"))});
              }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return answ;
    }


    public static ArrayList <String[]> countNumOfProductsThatHasNotBeenSold(){
        ArrayList <String[]> answ = new ArrayList<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Statement statement = connection.createStatement();
            String request =
                    "Select C.category_number, C.category_name, IFNULL(J.number_of_products,0) AS number_of_products\n" +
                            "from category C\n" +
                            "LEFT JOIN (\n" +
                            "Select category_number, COUNT(id_product) AS number_of_products\n" +
                            "from product P\n" +
                            "WHERE NOT exists (select *\n" +
                            "                  from store_product S\n" +
                            "                  where S.id_product=P.id_product \n" +
                            "                  AND S.UPC in ( select UPC\n" +
                            "                                 from sale)\n" +
                            "         )\n" +
                            "group by category_number\n" +
                            ") J ON C.category_number = J.category_number;";
            ResultSet resultSet = statement.executeQuery(request);

            while(resultSet.next()) {
                answ.add(new String[]{Integer.valueOf(resultSet.getString(CATEGORY_NUMBER))+"", ""+Integer.valueOf(resultSet.getString("number_of_products"))});
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return answ;
    }

}
