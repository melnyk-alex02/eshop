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
}