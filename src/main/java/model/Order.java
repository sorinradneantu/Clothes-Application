package model;

import java.util.ArrayList;
import java.util.Objects;

public class Order {

    private String shopname;
    private String customername;
    private ArrayList<ProductToOrder> productsOrd ;

    public Order()
    {

    }

    public Order(String shopname,String customername,ArrayList<ProductToOrder> productsOrd)
    {
        this.shopname=shopname;
        this.customername=customername;
        this.productsOrd=productsOrd;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public ArrayList<ProductToOrder> getProductsOrd() {
        return productsOrd;
    }

    public void setProductsOrd(ArrayList<ProductToOrder> productsOrd) {
        this.productsOrd = productsOrd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(shopname, order.shopname) &&
                Objects.equals(customername, order.customername) &&
                Objects.equals(productsOrd, order.productsOrd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shopname, customername, productsOrd);
    }

    @Override
    public String toString() {
        return "Order{" +
                "shopname='" + shopname + '\'' +
                ", customername='" + customername + '\'' +
                ", productsOrd=" + productsOrd +
                '}';
    }
}
