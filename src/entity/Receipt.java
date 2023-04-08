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

    public String getNumber() {
        return number;
    }

    public Employee getEmployee() {
        return employee;
    }

    public CustomerCard getCard() {
        return card;
    }

    public Timestamp getPrintDate() {
        return printDate;
    }

    public BigDecimal getTotalSum() {
        return totalSum;
    }

    public BigDecimal getVAT() {
        return VAT;
    }

    public List<SoldProduct> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "number='" + number + '\'' +
                ", employee=" + employee +
                ", card=" + card +
                ", printDate=" + printDate +
                ", totalSum=" + totalSum +
                ", VAT=" + VAT +
                ", products=" + products +
                '}';
    }
}
