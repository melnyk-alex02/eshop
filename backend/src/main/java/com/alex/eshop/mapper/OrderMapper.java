package com.alex.eshop.mapper;

import com.alex.eshop.dto.orderDTOs.OrderDTO;
import com.alex.eshop.dto.orderDTOs.OrderItemDTO;
import com.alex.eshop.entity.Order;
import com.alex.eshop.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;


@Mapper
public interface OrderMapper {
    @Mapping(target = "orderItemDTOList", source = "orderItemList")
    OrderDTO toDto(Order order);

    List<OrderDTO> toDto(Page<Order> orderPage);

    List<OrderDTO> toDto(List<Order> orderLis);

    @Mapping(source = "orderItemDTOList", target = "orderItemList")
    Order toEntity(OrderDTO orderDTO);

    List<OrderItemDTO> toOrderItemDTOList(List<OrderItem> orderItems);

    @Mapping(target = "orderNumber", source = "order.number")
    @Mapping(source = "item.id", target = "itemId")
    @Mapping(source = "item.name", target = "itemName")
    OrderItemDTO toOrderItemDTO(OrderItem orderItem);

    List<OrderItem> toOrderItemList(List<OrderItemDTO> orderItemList);

}