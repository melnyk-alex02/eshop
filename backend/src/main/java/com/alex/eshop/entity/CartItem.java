package com.alex.eshop.entity;

import com.alex.eshop.entity.compositeIds.CartItemId;
import jakarta.persistence.*;

@Entity
public class CartItem {
    @EmbeddedId
    private CartItemId cartItemId;

    @Column(insertable = false, updatable = false)
    private String userId;

    @ManyToOne
    @JoinColumn(insertable = false, updatable = false)
    private Item item;
    private Integer count;

    public CartItemId getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(CartItemId cartItemId) {
        this.cartItemId = cartItemId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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