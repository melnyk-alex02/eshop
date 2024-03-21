package com.alex.eshop.dto.orderDTOs;

import com.alex.eshop.entity.compositeIds.OrderItemId;

import java.math.BigDecimal;

public record OrderItemDTO(
        OrderItemId orderItemId,
        String orderNumber,
        Long itemId,
        String itemName,
        BigDecimal itemPrice,
        Integer count
) {
}
