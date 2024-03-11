package com.alex.eshop.entity;

import com.alex.eshop.entity.compositeIds.OrderItemId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class OrderItem {
    @EmbeddedId
    private OrderItemId orderItemId;
    @ManyToOne
    @JoinColumn(insertable = false, updatable = false)
    private Order order;
    @ManyToOne
    @JoinColumn(insertable = false, updatable = false)
    private Item item;
    private Integer count;

    public OrderItemId getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(OrderItemId orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}