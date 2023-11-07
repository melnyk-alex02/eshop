package com.alex.eshop.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(name = "order_table")
public class Order {
    @Id
    @Column(name = "number")
    private String number;
    private String status;
    private ZonedDateTime createdDate;
    private BigDecimal price;
    private Integer count;
    private String userId;
    @OneToMany(mappedBy = "order")
    private Set<OrderItem> orderItemsSet;
    private ZonedDateTime purchasedDate;


    public Set<OrderItem> getOrderItemsSet() {
        return orderItemsSet;
    }

    public void setOrderItemsSet(Set<OrderItem> orderItemsSet) {
        this.orderItemsSet = orderItemsSet;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getPurchasedDate() {
        return purchasedDate;
    }

    public void setPurchasedDate(ZonedDateTime purchasedDate) {
        this.purchasedDate = purchasedDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}