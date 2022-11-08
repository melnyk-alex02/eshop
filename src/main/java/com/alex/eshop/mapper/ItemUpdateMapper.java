package com.alex.eshop.mapper;

import com.alex.eshop.dto.ItemUpdateDTO;
import com.alex.eshop.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ItemUpdateMapper {
    @Mapping(source = "categoryId", target = "category.id")
    Item toEntity(ItemUpdateDTO itemUpdateDTO);

    @Mapping(source = "category.id", target = "categoryId")
    ItemUpdateDTO toDto(Item item);
}
