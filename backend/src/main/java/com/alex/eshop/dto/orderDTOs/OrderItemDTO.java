package com.alex.eshop.dto.orderDTOs;

import java.math.BigDecimal;

public record OrderItemDTO(
        Long orderId,
        String orderNumber,
        Long itemId,
        String itemName,
        BigDecimal itemPrice,
        Integer count
) {
}
