package com.alex.eshop.eshop.Mapper;

import com.alex.eshop.eshop.DTO.ItemDTO;
import com.alex.eshop.eshop.Entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    ItemDTO toDto(Item item);

    List<ItemDTO> toDto(List<Item> item);

    Item toEntity(ItemDTO itemDto);

    List<Item> toEntity(List<ItemDTO> itemDto);
}
