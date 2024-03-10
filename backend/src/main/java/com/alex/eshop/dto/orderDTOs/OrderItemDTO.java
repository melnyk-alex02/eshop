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
    public OrderItemDTO withOrderItemId(OrderItemId orderItemId) {
        return new OrderItemDTO(orderItemId, orderNumber, itemId, itemName, itemPrice, count);
    }

    public OrderItemDTO withOrderNumber(String orderNumber) {
        return new OrderItemDTO(orderItemId, orderNumber, itemId, itemName, itemPrice, count);
    }

    public OrderItemDTO withItemId(Long itemId) {
        return new OrderItemDTO(orderItemId, orderNumber, itemId, itemName, itemPrice, count);
    }

    public OrderItemDTO withItemName(String itemName) {
        return new OrderItemDTO(orderItemId, orderNumber, itemId, itemName, itemPrice, count);
    }

    public OrderItemDTO withItemPrice(BigDecimal itemPrice) {
        return new OrderItemDTO(orderItemId, orderNumber, itemId, itemName, itemPrice, count);
    }

    public OrderItemDTO withCount(Integer count) {
        return new OrderItemDTO(orderItemId, orderNumber, itemId, itemName, itemPrice, count);
    }
}
