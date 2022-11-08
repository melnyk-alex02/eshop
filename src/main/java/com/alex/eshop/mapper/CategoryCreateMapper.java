package com.alex.eshop.mapper;

import com.alex.eshop.dto.CategoryCreateDTO;
import com.alex.eshop.entity.Category;
import org.mapstruct.Mapper;

@Mapper
public interface CategoryCreateMapper {
    Category toEntity(CategoryCreateDTO categoryCreateDTO);

}
