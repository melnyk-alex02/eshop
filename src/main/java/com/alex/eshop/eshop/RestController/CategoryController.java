package com.alex.eshop.eshop.RestController;

import com.alex.eshop.eshop.Entity.CategoryEntity;
import com.alex.eshop.eshop.Repository.CategoryRepository;
import com.alex.eshop.eshop.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private final  CategoryService categoryService;
    @GetMapping("/categories")
    public List<CategoryEntity> allCategoryList(){
       return categoryService.getAllCategories();
    }
    @GetMapping("/categories/{id}")
    public  CategoryEntity getCategory(int id){
        return categoryService.getCategory(id);
    }
    public CategoryController (CategoryService categoryService){
        this.categoryService = categoryService;
    }
}
