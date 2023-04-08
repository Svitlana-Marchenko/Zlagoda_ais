package bd_connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
}
