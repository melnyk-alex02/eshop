package com.alex.eshop.mapper;

import com.alex.eshop.dto.ItemCreateDTO;
import com.alex.eshop.entity.Item;
import org.mapstruct.Mapper;

@Mapper
public interface ItemCreateMapper {
    Item toEntity(ItemCreateDTO itemCreateDTO);
    ItemCreateDTO toDto(Item item);
}
