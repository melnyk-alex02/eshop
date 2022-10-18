package com.alex.eshop.mapper;

import com.alex.eshop.dto.CategoryDTO;
import com.alex.eshop.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    CategoryDTO toDto(Category category);

    List<CategoryDTO> toDto(List<Category> category);

    Category toEntity(CategoryDTO categoryDTO);

    List<Category> toEntity(List<CategoryDTO> categoryDto);
}
