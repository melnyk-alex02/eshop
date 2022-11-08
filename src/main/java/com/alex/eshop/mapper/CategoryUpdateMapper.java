package com.alex.eshop.mapper;

import com.alex.eshop.dto.CategoryUpdateDTO;
import com.alex.eshop.entity.Category;
import org.mapstruct.Mapper;

@Mapper
public interface CategoryUpdateMapper {
    Category toEntity(CategoryUpdateDTO categoryUpdateDTO);

    CategoryUpdateDTO toDto(Category category);
}
