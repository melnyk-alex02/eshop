package com.alex.eshop.entity.compositeIds;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderItemId implements Serializable {
    @Column(name = "order_number", insertable = false, updatable = false)
    private String orderNumber;

    @Column(name = "item_id", insertable = false, updatable = false)
    private Long itemId;

    public OrderItemId() {}

    public OrderItemId(String orderNumber, Long itemId) {
        this.orderNumber = orderNumber;
        this.itemId = itemId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemId that = (OrderItemId) o;
        return Objects.equals(orderNumber, that.orderNumber) && Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber, itemId);
    }
}