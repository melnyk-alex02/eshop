package com.alex.eshop.entity;

import com.alex.eshop.entity.compositeIds.CartItemId;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CartItem {
    @EmbeddedId
    private CartItemId cartItemId;

    @Column(insertable = false, updatable = false)
    private String userId;

    @ManyToOne
    @JoinColumn(insertable = false, updatable = false)
    private Item item;
    private Integer count;
}