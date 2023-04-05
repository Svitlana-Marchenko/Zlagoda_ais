package entity;

import java.util.List;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class Receipt {
    private String number;
    private Employee employee;
    private CustomerCard card;
    private Timestamp printDate;
    private BigDecimal totalSum;
    private BigDecimal VAT; // value added tax
    private List<SoldProduct> products;

    public Receipt(String number, Employee employee, CustomerCard card, Timestamp printDate, BigDecimal totalSum, BigDecimal VAT, List<SoldProduct> products) {
        this.number = number;
        this.employee = employee;
        this.card = card;
        this.printDate = printDate;
        this.totalSum = totalSum;
        this.VAT = VAT;
        this.products = products;
    }
}
