package entity;

import java.math.BigDecimal;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductInStore that = (ProductInStore) o;
        return amount == that.amount && promotional == that.promotional && Objects.equals(UPC, that.UPC) && Objects.equals(promotionalUPC, that.promotionalUPC) && Objects.equals(product, that.product) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(UPC, promotionalUPC, product, price, amount, promotional);
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
