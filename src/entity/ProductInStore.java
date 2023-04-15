package entity;

import java.math.BigDecimal;

public class ProductInStore {
    private String UPC;
    private String promotionalUPC;
    private Product product;
    private BigDecimal price;
    private int amount;
    private boolean promotional;

    public ProductInStore(String UPC, String promotionalUPC, Product product, BigDecimal price, int amount, boolean promotional) {
        this.UPC = UPC;
        this.promotionalUPC = promotionalUPC;
        this.product = product;
        this.price = price;
        this.amount = amount;
        this.promotional = promotional;
    }

    public String getUPC() {
        return UPC;
    }

    public String getPromotionalUPC() {
        return promotionalUPC;
    }
    public void setPromotionalUPC(String UPC) {
        promotionalUPC=UPC;
    }

    public Product getProduct() {
        return product;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isPromotional() {
        return promotional;
    }

    @Override
    public String toString() {
        return "ProductInStore{" +
                "UPC='" + UPC + '\'' +
                ", promotionalUPC='" + promotionalUPC + '\'' +
                ", product=" + product +
                ", price=" + price +
                ", amount=" + amount +
                ", promotional=" + promotional +
                '}';
    }
}
