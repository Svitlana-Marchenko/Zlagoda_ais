package bd_connection;

import entity.Employee;

import java.awt.*;
import java.sql.*;
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
            String request = "INSERT INTO `zlagoda`.`category` (`category_number`, `category_name`) VALUES ('"+category.getId()+"', '"+category.getName()+"'); ";
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
            String request = "UPDATE `zlagoda`.`category` SET `category_name` = '"+category.getName()+"' WHERE (`category_number` = '"+category.getId()+"');";
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
            String request = "DELETE FROM `zlagoda`.`category` WHERE (`category_number` = '"+categoryId+"');";
            statement.execute(request);
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }


    public static entity.Category findCategoryById(int id){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT * FROM `zlagoda`.`category` WHERE (`category_number` = '"+id+"');";
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

    public static entity.Category findCategoryByName(String name){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT * FROM `zlagoda`.`category` WHERE (`category_name` = '"+name+"');";
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

    //4. Видруковувати звіти з інформацією про усі категорії товарів
    public static ArrayList<entity.Category> findAll(){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT * FROM `zlagoda`.`category`;";
            ResultSet resultSet = statement.executeQuery(request);
            ArrayList<entity.Category> categories = new ArrayList<>();
            while(resultSet.next()) {
                categories.add(new entity.Category(Integer.valueOf(resultSet.getString(CATEGORY_NUMBER)),resultSet.getString(CATEGORY_NAME)));
            }
            //System.out.println(categories);
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
            String request = "SELECT * FROM `zlagoda`.`category` ORDER BY `category_name`;";
            ResultSet resultSet = statement.executeQuery(request);
            ArrayList<entity.Category> categories = new ArrayList<>();
            while(resultSet.next()) {
                categories.add(new entity.Category(Integer.valueOf(resultSet.getString(CATEGORY_NUMBER)),resultSet.getString(CATEGORY_NAME)));
            }
            //System.out.println(categories);
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
}
