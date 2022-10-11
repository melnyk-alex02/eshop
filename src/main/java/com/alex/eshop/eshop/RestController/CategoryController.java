package com.alex.eshop.eshop.RestController;

import com.alex.eshop.eshop.Entity.Category;
import com.alex.eshop.eshop.Service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public List<Category> allCategoryList() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/categories/{id}")
    public Category getCategory(@PathVariable Long id, Category category) {
        category = categoryService.getCategory(id);
        return category;
    }
}
