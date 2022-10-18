package com.alex.eshop.eshop.mapper;

import com.alex.eshop.eshop.dto.ItemDTO;
import com.alex.eshop.eshop.entity.Item;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ItemMapper {

    ItemDTO toDto(Item item);

    List<ItemDTO> toDto(List<Item> item);

    Item toEntity(ItemDTO itemDto);

    List<Item> toEntity(List<ItemDTO> itemDto);
}
