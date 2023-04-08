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

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public Role getRole() {
        return role;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", role=" + role +
                ", salary=" + salary +
                ", birthdate=" + birthdate +
                ", startDate=" + startDate +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }
}
