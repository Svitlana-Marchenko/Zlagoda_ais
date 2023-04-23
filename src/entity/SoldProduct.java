package entity;

import java.math.BigDecimal;

public class SoldProduct {
    private String UPC;
    private String name;
    private int amount;

    private BigDecimal price;

    public SoldProduct(String UPC, String name, int amount, BigDecimal price) {
        this.UPC = UPC;
        this.name = name;
        this.amount = amount;
        this.price = price;
    }

    public String getUPC() {
        return UPC;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "SoldProduct{" +
                "UPC='" + UPC + '\'' +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", price=" + price +
                '}';
    }
}
