package com.alex.eshop.entity;

import com.alex.eshop.entity.compositeIds.OrderItemId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderItem {
    @EmbeddedId
    private OrderItemId orderItemId;

    @MapsId("orderId")
    @ManyToOne
    private Order order;

    @MapsId("itemId")
    @ManyToOne
    private Item item;

    private Integer count;
}