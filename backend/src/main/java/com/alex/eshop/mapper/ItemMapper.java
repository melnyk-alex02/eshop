package com.alex.eshop.mapper;

import com.alex.eshop.dto.ItemCreateDTO;
import com.alex.eshop.dto.ItemDTO;
import com.alex.eshop.dto.ItemUpdateDTO;
import com.alex.eshop.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper
public interface ItemMapper {
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    ItemDTO toDto(Item item);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    List<ItemDTO> toDto(List<Item> item);

    @Mapping(source = "categoryId", target = "category.id")
    Item toEntity(ItemDTO itemDto);

    @Mapping(source = "categoryId", target = "category.id")
    Item toEntity(ItemCreateDTO itemCreateDTO);

    @Mapping(source = "categoryId", target = "category.id")
    Item toEntity(ItemUpdateDTO itemUpdateDTO);

    @Mapping(source = "categoryId", target =  "category.id")
    List<Item> toEntity(List<ItemCreateDTO> itemDto);

    List<ItemDTO> toDto(Page<Item> itemPage);
}