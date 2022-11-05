package com.alex.eshop.mapper;

import com.alex.eshop.dto.ItemDTO;
import com.alex.eshop.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface ItemMapper {
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    ItemDTO toDto(Item item);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    List<ItemDTO> toDto(List<Item> item);

    Item toEntity(ItemDTO itemDto);

    List<Item> toEntity(List<ItemDTO> itemDto);
}