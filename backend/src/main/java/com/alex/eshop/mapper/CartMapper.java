package com.alex.eshop.mapper;

import com.alex.eshop.dto.cartDTOs.CartItemDTO;
import com.alex.eshop.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface CartMapper {

    @Mapping(source = "item.id", target = "itemId")
    @Mapping(source = "item.price", target = "itemPrice")
    @Mapping(source = "item.name", target = "itemName")
    CartItemDTO toDto(CartItem cartItem);

    @Mapping(source = "item.id", target = "itemId")
    @Mapping(source = "item.price", target = "itemPrice")
    @Mapping(source = "item.name", target = "itemName")
    List<CartItemDTO> toDto(List<CartItem> cartItemList);

    @Mapping(source = "itemId", target = "item.id")
    @Mapping(source = "cartItemId.userId", target = "userId")
    @Mapping(source = "cartItemId.itemId", target = "item")
    CartItem toEntity(CartItemDTO cartItemDTO);
}