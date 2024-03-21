package com.alex.eshop.entity;

import com.alex.eshop.entity.compositeIds.OrderItemId;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OrderItem {
    @EmbeddedId
    private OrderItemId orderItemId;
    @ManyToOne
    @JoinColumn(name = "order_number")
    private Order order;
    @ManyToOne
    private Item item;
    private Integer count;
}