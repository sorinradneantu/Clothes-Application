package model;

import java.util.Objects;

public class ProductToOrder {

    private Product prod;
    private int quantity;
    private String size;

    public ProductToOrder()
    {

    }

    public ProductToOrder(Product prod,int quantity,String size)
    {
        this.prod=prod;
        this.quantity=quantity;
        this.size=size;
    }

    public Product getProd() {
        return prod;
    }

    public void setProd(Product prod) {
        this.prod = prod;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "ProductToOrder{" +
                "prod=" + prod +
                ", quantity=" + quantity +
                ", size='" + size + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductToOrder that = (ProductToOrder) o;
        return quantity == that.quantity &&
                Objects.equals(prod, that.prod) &&
                Objects.equals(size, that.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prod, quantity, size);
    }
}
