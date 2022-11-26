package com.alex.eshop.restcontroller;

import com.alex.eshop.dto.CategoryDTO;
import com.alex.eshop.service.CategoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public List<CategoryDTO> allCategoryList(Pageable pageable) {
        return categoryService.getAllCategories(pageable);
    }

    @GetMapping("/categories/{id}")
    public CategoryDTO getCategory(@PathVariable Long id) {
        return categoryService.getCategory(id);
    }
}
