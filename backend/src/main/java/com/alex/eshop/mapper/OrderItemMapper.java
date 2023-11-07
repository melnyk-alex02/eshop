package com.alex.eshop.mapper;

import com.alex.eshop.dto.orderDTOs.OrderItemDTO;
import com.alex.eshop.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashSet;

@Mapper
public interface OrderItemMapper {
    @Mapping(target = "itemId", source = "item.id")
    @Mapping(target = "orderId", source = "order.number")
    OrderItemDTO toDto(OrderItem orderItem);

    @Mapping(target = "item.id", source = "itemId")
    @Mapping(target = "order.number", source = "orderId")
    OrderItem toEntity(OrderItemDTO orderItemDTO);

    @Mapping(target = "item.id", source = "itemId")
    @Mapping(target = "order.number", source = "orderId")
    HashSet<OrderItem> toEntity(HashSet<OrderItemDTO> orderItemDTOSet);

}