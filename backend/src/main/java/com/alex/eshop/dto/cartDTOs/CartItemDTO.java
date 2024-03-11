package com.alex.eshop.dto.cartDTOs;

import com.alex.eshop.entity.compositeIds.CartItemId;

import java.math.BigDecimal;

public record CartItemDTO(
        CartItemId cartItemId,
        String userId,
        Long itemId,
        String itemName,
        BigDecimal itemPrice,
        Integer count
) {
    public CartItemDTO withCartItemId(CartItemId cartItemId) {
        return new CartItemDTO(cartItemId, userId, itemId, itemName, itemPrice, count);
    }

    public CartItemDTO withUserId(String userId) {
        return new CartItemDTO(cartItemId, userId, itemId, itemName, itemPrice, count);
    }

    public CartItemDTO withItemId(Long itemId) {
        return new CartItemDTO(cartItemId, userId, itemId, itemName, itemPrice, count);
    }

    public CartItemDTO withItemName(String itemName) {
        return new CartItemDTO(cartItemId, userId, itemId, itemName, itemPrice, count);
    }

    public CartItemDTO withItemPrice(BigDecimal itemPrice) {
        return new CartItemDTO(cartItemId, userId, itemId, itemName, itemPrice, count);
    }

    public CartItemDTO withCount(Integer count) {
        return new CartItemDTO(cartItemId, userId, itemId, itemName, itemPrice, count);
    }
}