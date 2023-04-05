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
}
