package entity;

import java.math.BigDecimal;
import java.sql.Date;

public class Employee {
    public enum Role {
        CASHIER,
        MANAGER
    }

    private String id;
    private String surname;
    private String name;
    private String password;
    private String patronymic;
    private Role role;
    private BigDecimal salary;
    private Date birthdate;
    private Date startDate;
    private String phoneNumber;
    private String city;
    private String street;
    private String zipCode;

    public Employee(String id, String surname, String name, String password, String patronymic, Role role, BigDecimal salary, Date birthdate, Date startDate, String phoneNumber, String city, String street, String zipCode) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.password = password;
        this.patronymic = patronymic;
        this.role = role;
        this.salary = salary;
        this.birthdate = birthdate;
        this.startDate = startDate;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
    }
}
