package com.alex.eshop.mapper;

import com.alex.eshop.dto.cartDTOs.CartDTO;
import com.alex.eshop.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface CartMapper {
    @Mapping(source = "item.id", target = "itemId")
    @Mapping(source = "item.price", target = "itemPrice")
    @Mapping(source = "item.name", target = "itemName")
    CartDTO toDto(Cart cart);

    @Mapping(source = "item.id", target = "itemId")
    @Mapping(source = "item.price", target = "itemPrice")
    @Mapping(source = "item.name", target = "itemName")
    List<CartDTO> toDto(List<Cart> cartList);

    @Mapping(source = "itemId", target = "item.id")
    Cart toEntity(CartDTO cartDTO);
}