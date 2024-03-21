package com.alex.eshop.entity;

import com.alex.eshop.entity.compositeIds.CartItemId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
public class CartItem {
    @EmbeddedId
    private CartItemId cartItemId;

    @Column(insertable = false, updatable = false)
    private String userId;

    @MapsId("itemId")
    @ManyToOne
    private Item item;

    private Integer count;
}