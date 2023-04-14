package bd_connection;

import entity.ProductInStore;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Employee {

    private static Connection connection;

    //additional method for getting employee having id_employee
   public static entity.Employee getEmployee(String num) throws SQLException {

        String sqlCast = "SELECT * FROM Employee WHERE id_employee = " + num;
        try (Statement statement = connection.createStatement();
             ResultSet resultEmp = statement.executeQuery(sqlCast)
        ) {
            if(resultEmp.next()){
                String p = resultEmp.getString("empl_patronymic");
                if (p == null)
                    p = "";

                return new entity.Employee(num, resultEmp.getString("empl_surname"), resultEmp.getString("empl_name"), resultEmp.getString("password"), p, entity.Employee.Role.CASHIER, resultEmp.getBigDecimal("salary"), resultEmp.getDate("date_of_birth"), resultEmp.getDate("date_of_start"), resultEmp.getString("phone_number"), resultEmp.getString("city"), resultEmp.getString("street"), resultEmp.getString("zip_code"));

            }
        }
        return null;
    }

    //get all employee with given role
    public static List<entity.Employee> getAllSpecial(String role) throws SQLException {
List<entity.Employee> answ = new ArrayList<>();
        String sqlCast = "SELECT * FROM Employee WHERE role = '" + role + "'";
        try (Statement statement = connection.createStatement();
             ResultSet resultEmp = statement.executeQuery(sqlCast)
        )
        {

            while(resultEmp.next()){
                String p = resultEmp.getString("empl_patronymic");
                if (p == null)
                    p = "";

                answ.add( new entity.Employee(resultEmp.getString("id_employee"), resultEmp.getString("empl_surname"), resultEmp.getString("empl_name"), resultEmp.getString("password"), p, entity.Employee.Role.CASHIER, resultEmp.getBigDecimal("salary"), resultEmp.getDate("date_of_birth"), resultEmp.getDate("date_of_start"), resultEmp.getString("phone_number"), resultEmp.getString("city"), resultEmp.getString("street"), resultEmp.getString("zip_code")));

            }
        }
        return answ;
    }

}
