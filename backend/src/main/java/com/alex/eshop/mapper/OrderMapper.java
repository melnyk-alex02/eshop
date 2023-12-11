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

    List<OrderItem> toOrderItemList(List<OrderItemDTO> orderItemDTOList);


    @Mapping(target = "item.id", source = "itemId")
    @Mapping(target = "order.number", source = "orderNumber")
    OrderItem toOrderItem(OrderItemDTO orderItemDTO);

    @Mapping(target = "orderNumber", source = "order.number")
    @Mapping(target = "itemId", source = "item.id")
    @Mapping(target = "itemName", source = "item.name")
    @Mapping(target = "itemPrice", source = "item.price")
    OrderItemDTO toOrderItemDTO(OrderItem orderItem);
}