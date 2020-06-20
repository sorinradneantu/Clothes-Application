package model;

import java.util.Objects;

public class OrderStatus {
    private Order o;
    private String status;

    public OrderStatus(){

    }
    public OrderStatus(Order o, String status) {
        this.o = o;
        this.status = status;
    }

    public Order getO() {
        return o;
    }

    public String getStatus() {
        return status;
    }

    public void setO(Order o) {
        this.o = o;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o1) {
        if (this == o1) return true;
        if (o1 == null || getClass() != o1.getClass()) return false;
        OrderStatus that = (OrderStatus) o1;
        return Objects.equals(o, that.o) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(o, status);
    }

    @Override
    public String toString() {
        return "OrderStatus{" +
                "o=" + o +
                ", status='" + status + '\'' +
                '}';
    }


}
