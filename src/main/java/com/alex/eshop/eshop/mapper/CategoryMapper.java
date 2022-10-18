package com.alex.eshop.eshop.mapper;

import com.alex.eshop.eshop.dto.CategoryDTO;
import com.alex.eshop.eshop.entity.Category;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",  injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CategoryMapper {

    CategoryDTO toDto(Category category);

    List<CategoryDTO> toDto(List<Category> category);

    Category toEntity(CategoryDTO categoryDTO);

    List<Category> toEntity(List<CategoryDTO> categoryDto);
}
