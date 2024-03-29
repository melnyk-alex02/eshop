package com.alex.eshop.entity;

import com.alex.eshop.entity.compositeIds.OrderItemId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

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

    private BigDecimal itemPrice;

    private Integer count;
}