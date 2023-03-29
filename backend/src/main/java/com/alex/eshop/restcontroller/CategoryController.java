package com.alex.eshop.restcontroller;

import com.alex.eshop.constants.Role;
import com.alex.eshop.dto.CategoryCreateDTO;
import com.alex.eshop.dto.CategoryDTO;
import com.alex.eshop.dto.CategoryUpdateDTO;
import com.alex.eshop.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @GetMapping("/categories")
    public Page<CategoryDTO> allCategoryList(Pageable pageable) {
        return categoryService.getAllCategories(pageable);
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @GetMapping("/categories/{id}")
    public CategoryDTO getCategory(@PathVariable Long id) {
        return categoryService.getCategory(id);
    }

    @PreAuthorize("hasRole('" + Role.ADMIN + "')")
    @PostMapping("/categories")
    public CategoryDTO createCategory(@RequestBody CategoryCreateDTO categoryCreateDTO) {
        return categoryService.createCategory(categoryCreateDTO);
    }

    @PreAuthorize("hasRole('" + Role.ADMIN + "')")
    @PutMapping("/categories")
    public CategoryDTO updateCategory(@RequestBody CategoryUpdateDTO categoryUpdateDTO) {
        return categoryService.updateCategory(categoryUpdateDTO);
    }
    @PreAuthorize("hasRole('" + Role.ADMIN + "')")
    @DeleteMapping("/categories/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
