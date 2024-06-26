package com.alex.eshop.dto.cartDTOs;

import java.math.BigDecimal;

public record CartItemDTO(
        String userId,
        Long itemId,
        String itemName,
        BigDecimal itemPrice,
        Integer count
) {
}