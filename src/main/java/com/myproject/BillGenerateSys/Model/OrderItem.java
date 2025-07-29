package com.myproject.BillGenerateSys.Model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemId;

    @ManyToOne
    private Product product;

    private Long price;
    private Integer quantity;

    @ManyToOne
    private Bill bill;

    public OrderItem(){}

    public OrderItem(Product product, Long price, Integer quantity) {
        this.product = product;
        this.price = price;
        this.quantity = quantity;

    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return itemId == orderItem.itemId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(itemId);
    }
}
