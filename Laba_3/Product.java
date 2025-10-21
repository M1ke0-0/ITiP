package lab3;

public class Product {
    private String name;
    private double price;
    private int quantity;

    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    @Override
    public String toString() {
        return "Товар: " + name + ", цена: " + price + ", количество: " + quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product other = (Product) obj;
        return name.equals(other.name) && price == other.price && quantity == other.quantity;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (int) price;
        result = 31 * result + quantity;
        return result;
    }
}
