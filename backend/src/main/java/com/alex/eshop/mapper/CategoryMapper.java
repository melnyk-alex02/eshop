package com.alex.eshop.mapper;

import com.alex.eshop.dto.categoryDTOs.CategoryCreateDTO;
import com.alex.eshop.dto.categoryDTOs.CategoryDTO;
import com.alex.eshop.dto.categoryDTOs.CategoryUpdateDTO;
import com.alex.eshop.entity.Category;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper
public interface CategoryMapper {
    CategoryDTO toDto(Category category);

    List<CategoryDTO> toDto(List<Category> category);

    List<CategoryDTO> toDto(Page<Category> categoryPage);

    Category toEntity(CategoryCreateDTO categoryCreateDTO);

    Category toEntity(CategoryUpdateDTO categoryUpdateDTO);

    List<Category> toEntity(List<CategoryCreateDTO> categoryCreateDTOList);

}
