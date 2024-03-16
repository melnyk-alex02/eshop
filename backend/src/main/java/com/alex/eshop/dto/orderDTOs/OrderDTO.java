package com.alex.eshop.dto.orderDTOs;

import com.alex.eshop.constants.OrderStatus;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public record OrderDTO(
        String number,
        OrderStatus status,
        ZonedDateTime createdDate,
        BigDecimal price,
        Integer count,
        List<OrderItemDTO> orderItemDTOList,
        String userId,
        ZonedDateTime purchasedDate
) {

    public OrderDTO withNumber(String number) {
        return new OrderDTO(number, status, createdDate, price, count, orderItemDTOList, userId, purchasedDate);
    }

    public OrderDTO withStatus(OrderStatus status) {
        return new OrderDTO(number, status, createdDate, price, count, orderItemDTOList, userId, purchasedDate);
    }

    public OrderDTO withCreatedDate(ZonedDateTime createdDate) {
        return new OrderDTO(number, status, createdDate, price, count, orderItemDTOList, userId, purchasedDate);
    }

    public OrderDTO withPrice(BigDecimal price) {
        return new OrderDTO(number, status, createdDate, price, count, orderItemDTOList, userId, purchasedDate);
    }

    public OrderDTO withCount(Integer count) {
        return new OrderDTO(number, status, createdDate, price, count, orderItemDTOList, userId, purchasedDate);
    }

    public OrderDTO withOrderItemDTOList(List<OrderItemDTO> orderItemDTOList) {
        return new OrderDTO(number, status, createdDate, price, count, orderItemDTOList, userId, purchasedDate);
    }

    public OrderDTO withUserId(String userId) {
        return new OrderDTO(number, status, createdDate, price, count, orderItemDTOList, userId, purchasedDate);
    }

    public OrderDTO withPurchasedDate(ZonedDateTime purchasedDate) {
        return new OrderDTO(number, status, createdDate, price, count, orderItemDTOList, userId, purchasedDate);
    }
}
