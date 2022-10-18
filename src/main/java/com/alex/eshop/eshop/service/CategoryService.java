package com.alex.eshop.eshop.service;


import com.alex.eshop.eshop.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();

    CategoryDTO getCategory(Long id);
}
