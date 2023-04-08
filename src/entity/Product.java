package entity;

public class Product {
    private int id;
    private String name;
    private Category category;
    private String producer;
    private String characteristics;

    public Product(int id, String name, Category category, String producer, String characteristics) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.producer = producer;
        this.characteristics = characteristics;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public String getProducer() {
        return producer;
    }

    public String getCharacteristics() {
        return characteristics;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", producer='" + producer + '\'' +
                ", characteristics='" + characteristics + '\'' +
                '}';
    }
}
