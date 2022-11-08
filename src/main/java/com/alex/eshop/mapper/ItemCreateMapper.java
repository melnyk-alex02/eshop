package com.alex.eshop.mapper;

import com.alex.eshop.dto.ItemCreateDTO;
import com.alex.eshop.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ItemCreateMapper {
    @Mapping(source = "categoryId", target = "category.id")
    Item toEntity(ItemCreateDTO itemCreateDTO);
    ItemCreateDTO toDto(Item item);
}
