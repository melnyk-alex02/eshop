package com.alex.eshop.eshop.Service;


import com.alex.eshop.eshop.Entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategory(long id);
}
