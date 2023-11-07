package com.alex.eshop.mapper;

import com.alex.eshop.dto.orderDTOs.OrderDTO;
import com.alex.eshop.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface OrderMapper {
    @Mapping(target = "orderItemDTOSet", source = "orderItemsSet")
    OrderDTO toDto(Order order);

    @Mapping(source = "orderItemDTOSet", target = "orderItemsSet")
    Order toEntity(OrderDTO orderDTO);
}