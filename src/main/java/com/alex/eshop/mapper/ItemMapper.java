package com.alex.eshop.mapper;

import com.alex.eshop.dto.ItemDTO;
import com.alex.eshop.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper
public interface ItemMapper {
    @Mapping(source = "category.id", target = "categoryId")
    ItemDTO toDto(Item item);

    @Mapping(source = "category.id", target = "categoryId")
    List<ItemDTO> toDto(List<Item> item);
    @Mapping(source = "categoryId", target = "category.id")
    Item toEntity(ItemDTO itemDto);

    List<Item> toEntity(List<ItemDTO> itemDto);

    List<ItemDTO> toDto(Page<Item> itemPage);
}