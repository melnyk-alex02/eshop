package com.alex.eshop.dto.orderDTOs;

import com.alex.eshop.constants.OrderStatus;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public record OrderDTO(
        Long id,
        String number,

        OrderStatus status,
        ZonedDateTime createdDate,
        BigDecimal price,
        List<OrderItemDTO> orderItemDTOList,
        String email,
        String userId,
        ZonedDateTime purchasedDate
) {
}