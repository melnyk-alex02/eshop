package com.alex.eshop.eshop.Service;


import com.alex.eshop.eshop.Entity.CategoryEntity;

import java.util.List;

public interface CategoryService {
    List<CategoryEntity> getAllCategories();
    CategoryEntity getCategory(int id);
}
