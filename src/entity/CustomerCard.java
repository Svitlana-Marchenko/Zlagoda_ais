package entity;

public class CustomerCard {
    private String number;
    private String surname;
    private String name;
    private String patronymic;
    private String phoneNumber;
    private String city;
    private String street;
    private String zipCode;
    private int percent;

    public CustomerCard(String number, String surname, String name, String patronymic, String phoneNumber, String city, String street, String zipCode, int percent) {
        this.number = number;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
        this.percent = percent;
    }
}
