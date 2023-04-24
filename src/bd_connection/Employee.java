package bd_connection;

import entity.ProductInStore;

import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Employee {

    private static Connection connection;
    public static void setConnection(Connection con){
        connection=con;
    }


    private static final String ID_EMPLOYEE = "id_employee";
    private static final String SURNAME = "empl_surname";
    private static final String NAME = "empl_name";
    private static final String PATRONYMIC = "empl_patronymic";
    private static final String PASSWORD = "password";
    private static final String ROLE = "role";
    private static final String SALARY = "salary";
    private static final String BIRTH_DATE = "date_of_birth";
    private static final String START_DATE = "date_of_start";
    private static final String PHONE = "phone_number";
    private static final String CITY = "city";
    private static final String STREET = "street";
    private static final String ZIP_CODE = "zip_code";

    //1. Додавати нові дані про працівників
    public static boolean addEmployee(entity.Employee employee){
        try{
            Statement statement = connection.createStatement();
            String request = "INSERT INTO employee (`id_employee`, `empl_surname`, `empl_name`, `password`, `role`, `empl_patronymic`, `salary`, `date_of_birth`, `date_of_start`, `phone_number`, `city`, `street`, `zip_code`) VALUES ('"+employee.getId()+"', '"+employee.getSurname()+"', '"+employee.getName()+"', '"+employee.getPassword()+"', '"+employee.getRole()+"', '"+employee.getPatronymic()+"', '"+employee.getSalary()+"', '"+employee.getBirthdate()+"', '"+employee.getStartDate()+"', '"+employee.getPhoneNumber()+"', '"+employee.getCity()+"', '"+employee.getStreet()+"', '"+employee.getZipCode()+"');";
            statement.execute(request);
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    //2. Редагувати дані про працівників
    public static boolean updateEmployeeById(entity.Employee employee){
        try {
            Statement statement = connection.createStatement();
            String request = "UPDATE employee SET `empl_surname` = '"+employee.getSurname()+"', `empl_name` = '"+employee.getName()+"', `password` = '"+employee.getPassword()+"', `role` = '"+employee.getRole()+"', `empl_patronymic` = '"+employee.getRole()+"', `salary` = '"+employee.getSalary()+"', `date_of_birth` = '"+employee.getBirthdate()+"', `date_of_start` = '"+employee.getStartDate()+"', `phone_number` = '"+employee.getPhoneNumber()+"', `city` = '"+employee.getCity()+"', `street` = '"+employee.getStreet()+"', `zip_code` = '"+employee.getZipCode()+"' WHERE (`id_employee` = '"+employee.getId()+"');\n";
            statement.execute(request);
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    //3. Видаляти дані про працівників
    public static boolean deleteEmployeeById(String employeeId){
        try {
            Statement statement = connection.createStatement();
            String request = "DELETE FROM employee WHERE (`id_employee` = '"+employeeId+"');";
            statement.execute(request);
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    //4. Видруковувати звіти з інформацією про усіх працівників
    public static ArrayList<entity.Employee> findAll(){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT * FROM employee;";
            ResultSet resultSet = statement.executeQuery(request);
            ArrayList<entity.Employee> employees = new ArrayList<>();
            while(resultSet.next()) {
                employees.add(new entity.Employee(resultSet.getString(ID_EMPLOYEE),resultSet.getString(SURNAME),resultSet.getString(NAME),resultSet.getString(PASSWORD),resultSet.getString(PATRONYMIC), entity.Employee.Role.valueOf(resultSet.getString(ROLE)),new BigDecimal(resultSet.getString(SALARY)),Date.valueOf(resultSet.getString(BIRTH_DATE)),Date.valueOf(resultSet.getString(START_DATE)),resultSet.getString(PHONE),resultSet.getString(CITY),resultSet.getString(STREET),resultSet.getString(ZIP_CODE)));
            }
            return employees;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
    }


    //знайти працівника за айді
    public static entity.Employee findEmployeeById(String id){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT * FROM employee WHERE (`id_employee` = '"+id+"');";
            ResultSet resultSet = statement.executeQuery(request);
            entity.Employee employee = null;
            while(resultSet.next()) {
                employee=new entity.Employee(resultSet.getString(ID_EMPLOYEE),resultSet.getString(SURNAME),resultSet.getString(NAME),resultSet.getString(PASSWORD),resultSet.getString(PATRONYMIC), entity.Employee.Role.valueOf(resultSet.getString(ROLE)),new BigDecimal(resultSet.getString(SALARY)),Date.valueOf(resultSet.getString(BIRTH_DATE)),Date.valueOf(resultSet.getString(START_DATE)),resultSet.getString(PHONE),resultSet.getString(CITY),resultSet.getString(STREET),resultSet.getString(ZIP_CODE));
            }
            return employee;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public static entity.Employee findEmployeeByPhoneNumber(String phoneNumber){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT * FROM employee WHERE (`phone_number` = '"+phoneNumber+"');";
            ResultSet resultSet = statement.executeQuery(request);
            entity.Employee employee = null;
            while(resultSet.next()) {
                employee=new entity.Employee(resultSet.getString(ID_EMPLOYEE),resultSet.getString(SURNAME),resultSet.getString(NAME),resultSet.getString(PASSWORD),resultSet.getString(PATRONYMIC), entity.Employee.Role.valueOf(resultSet.getString(ROLE)),new BigDecimal(resultSet.getString(SALARY)),Date.valueOf(resultSet.getString(BIRTH_DATE)),Date.valueOf(resultSet.getString(START_DATE)),resultSet.getString(PHONE),resultSet.getString(CITY),resultSet.getString(STREET),resultSet.getString(ZIP_CODE));
            }
            return employee;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }


    //5. Отримати інформацію про усіх працівників, відсортованих за прізвищем;
    public static ArrayList<entity.Employee> findAllSortedBySurname(){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT * FROM employee ORDER BY empl_surname;";
            ResultSet resultSet = statement.executeQuery(request);
            ArrayList<entity.Employee> employees = new ArrayList<>();
            while(resultSet.next()) {
                employees.add(new entity.Employee(resultSet.getString(ID_EMPLOYEE),resultSet.getString(SURNAME),resultSet.getString(NAME),resultSet.getString(PASSWORD),resultSet.getString(PATRONYMIC), entity.Employee.Role.valueOf(resultSet.getString(ROLE)),new BigDecimal(resultSet.getString(SALARY)),Date.valueOf(resultSet.getString(BIRTH_DATE)),Date.valueOf(resultSet.getString(START_DATE)),resultSet.getString(PHONE),resultSet.getString(CITY),resultSet.getString(STREET),resultSet.getString(ZIP_CODE)));
            }
            return employees;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
    }
    //6. Отримати інформацію про усіх працівників, що займають посаду касира, відсортованих за прізвищем;
    public static ArrayList<entity.Employee> findAllCashiersSortedBySurname(){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT * FROM employee WHERE (`role` = 'CASHIER') ORDER BY empl_surname;";
            ResultSet resultSet = statement.executeQuery(request);
            ArrayList<entity.Employee> employees = new ArrayList<>();
            while(resultSet.next()) {
                employees.add(new entity.Employee(resultSet.getString(ID_EMPLOYEE),resultSet.getString(SURNAME),resultSet.getString(NAME),resultSet.getString(PASSWORD),resultSet.getString(PATRONYMIC), entity.Employee.Role.valueOf(resultSet.getString(ROLE)),new BigDecimal(resultSet.getString(SALARY)),Date.valueOf(resultSet.getString(BIRTH_DATE)),Date.valueOf(resultSet.getString(START_DATE)),resultSet.getString(PHONE),resultSet.getString(CITY),resultSet.getString(STREET),resultSet.getString(ZIP_CODE)));
            }
            return employees;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
    }
    //11. За прізвищем працівника знайти його телефон та адресу;
    public static ArrayList<String> findPhoneAndAddressBySurname(String surname){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT "+PHONE+","+CITY+","+STREET+","+ZIP_CODE+" FROM employee WHERE (`"+SURNAME+"` = '"+surname+"');";
            ArrayList<String> rez= new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(request);
            while(resultSet.next()) {
                rez.add(resultSet.getString(PHONE) + ", " + resultSet.getString(CITY) + ", " + resultSet.getString(STREET) + ", " + resultSet.getString(ZIP_CODE));
            }
            return rez;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
    }

    //15. Можливість отримати усю інформацію про себе.
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

                answ.add( new entity.Employee(resultEmp.getString("id_employee"), resultEmp.getString("empl_surname"), resultEmp.getString("empl_name"), resultEmp.getString("password"), p, (role=="CASHIER"? entity.Employee.Role.CASHIER: entity.Employee.Role.MANAGER), resultEmp.getBigDecimal("salary"), resultEmp.getDate("date_of_birth"), resultEmp.getDate("date_of_start"), resultEmp.getString("phone_number"), resultEmp.getString("city"), resultEmp.getString("street"), resultEmp.getString("zip_code")));

            }
        }
        return answ;
    }

    //find all employee
    public static ArrayList<entity.Employee> findAllEmployee() {
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT * FROM `Employee`;";
            ResultSet resultSet = statement.executeQuery(request);
            ArrayList<entity.Employee> employees = new ArrayList<>();
            while (resultSet.next()) {
                employees.add(new entity.Employee(resultSet.getString(ID_EMPLOYEE), resultSet.getString(SURNAME), resultSet.getString(NAME), resultSet.getString(PASSWORD), resultSet.getString(PATRONYMIC), entity.Employee.Role.valueOf(resultSet.getString(ROLE)), new BigDecimal(resultSet.getString(SALARY)), Date.valueOf(resultSet.getString(BIRTH_DATE)), Date.valueOf(resultSet.getString(START_DATE)), resultSet.getString(PHONE), resultSet.getString(CITY), resultSet.getString(STREET), resultSet.getString(ZIP_CODE)));
            }
            return employees;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
    }
        //find all employee with given surname
        public static ArrayList<entity.Employee> findAllEmployeeBySurname(String surname){
            try {
                Statement statement = connection.createStatement();
                String request = "SELECT * FROM `Employee` WHERE "+SURNAME+" = '"+surname+"'";
                ResultSet resultSet = statement.executeQuery(request);
                ArrayList<entity.Employee> employees = new ArrayList<>();
                while(resultSet.next()) {
                    employees.add(new entity.Employee(resultSet.getString(ID_EMPLOYEE),resultSet.getString(SURNAME),resultSet.getString(NAME),resultSet.getString(PASSWORD),resultSet.getString(PATRONYMIC), entity.Employee.Role.valueOf(resultSet.getString(ROLE)),new BigDecimal(resultSet.getString(SALARY)),Date.valueOf(resultSet.getString(BIRTH_DATE)),Date.valueOf(resultSet.getString(START_DATE)),resultSet.getString(PHONE),resultSet.getString(CITY),resultSet.getString(STREET),resultSet.getString(ZIP_CODE)));
                }
                return employees;
            }catch (SQLException ex){
                System.out.println(ex.getMessage());
                return new ArrayList<>();
            }
    }

    //find all employee with given surname and role
    public static ArrayList<entity.Employee> findAllEmployeeBySurnameAndRole(String surname,String role){
        try {
            Statement statement = connection.createStatement();
            String request = "SELECT * FROM `Employee` WHERE "+SURNAME+" = '"+surname+"' AND "+ROLE+" = '"+role+"'";
            ResultSet resultSet = statement.executeQuery(request);
            ArrayList<entity.Employee> employees = new ArrayList<>();
            while(resultSet.next()) {
                employees.add(new entity.Employee(resultSet.getString(ID_EMPLOYEE),resultSet.getString(SURNAME),resultSet.getString(NAME),resultSet.getString(PASSWORD),resultSet.getString(PATRONYMIC), entity.Employee.Role.valueOf(resultSet.getString(ROLE)),new BigDecimal(resultSet.getString(SALARY)),Date.valueOf(resultSet.getString(BIRTH_DATE)),Date.valueOf(resultSet.getString(START_DATE)),resultSet.getString(PHONE),resultSet.getString(CITY),resultSet.getString(STREET),resultSet.getString(ZIP_CODE)));
            }
            return employees;
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        }
    }

}
