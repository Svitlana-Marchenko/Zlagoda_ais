package bd_connection;

import entity.Employee;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Category {

    private static Connection connection;




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
   public static List<entity.Category> getAllCategories() throws SQLException {

       List<entity.Category> answ = new ArrayList<>();
        Statement statement = connection.createStatement();
        String sqlCat = "SELECT * FROM Category";
        ResultSet resultCat = statement.executeQuery(sqlCat);

        while (resultCat.next()) {
            answ.add( new entity.Category(resultCat.getInt("category_number"), resultCat.getString("category_name")));
        }
        return answ;
    }
}
