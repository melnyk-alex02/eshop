package com.alex.eshop.mapper;

import com.alex.eshop.dto.ItemCreateDTO;
import com.alex.eshop.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ItemCreateMapper {
    @Mapping(target = "category.id", source = "categoryId")
    Item toEntity(ItemCreateDTO itemCreateDTO);
}
