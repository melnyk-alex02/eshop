package com.alex.eshop.eshop.Service;


import com.alex.eshop.eshop.DTO.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();

    CategoryDTO getCategory(Long id);
}
