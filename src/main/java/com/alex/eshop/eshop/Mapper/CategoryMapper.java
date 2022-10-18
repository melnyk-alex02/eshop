package com.alex.eshop.eshop.Mapper;

import com.alex.eshop.eshop.DTO.CategoryDTO;
import com.alex.eshop.eshop.Entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CategoryMapper {
    CategoryDTO toDto(Category category);
    List<CategoryDTO> toDto(List<Category> category);

    Category toEntity(CategoryDTO categoryDTO);
    List<Category> toEntity(List<CategoryDTO> categoryDto);

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
}
