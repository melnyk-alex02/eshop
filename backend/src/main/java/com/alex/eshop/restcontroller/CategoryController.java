package com.alex.eshop.restcontroller;

import com.alex.eshop.constants.Role;
import com.alex.eshop.dto.categoryDTOs.CategoryCreateDTO;
import com.alex.eshop.dto.categoryDTOs.CategoryDTO;
import com.alex.eshop.dto.categoryDTOs.CategoryUpdateDTO;
import com.alex.eshop.service.CategoryService;
import com.alex.eshop.wrapper.PageWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @GetMapping("/categories")
    public PageWrapper<CategoryDTO> getAllCategories(Pageable pageable) {
        Page<CategoryDTO> categoryPage = categoryService.getAllCategories(pageable);

        return new PageWrapper<>(categoryPage.getContent(), categoryPage.getTotalElements());
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @GetMapping("/categories/search")
    public PageWrapper<CategoryDTO> searchCategories(Pageable pageable,
                                                     @RequestParam("name") String name) {
        Page<CategoryDTO> categoryPage = categoryService.searchCategories(pageable, name);

        return new PageWrapper<>(categoryPage.getContent(), categoryPage.getTotalElements());
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
    @PostMapping("/upload-categories")
    public List<CategoryDTO> uploadCategoriesFromCsv(MultipartFile file) {
        return categoryService.uploadCategoriesFromCsv(file);
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