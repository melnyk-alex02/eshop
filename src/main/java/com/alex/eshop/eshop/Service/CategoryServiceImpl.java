package com.alex.eshop.eshop.Service;

import com.alex.eshop.eshop.Entity.Category;
import com.alex.eshop.eshop.ExceptionHandling.NoSuchCategoryException;
import com.alex.eshop.eshop.Repository.CategoryRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    public CategoryServiceImpl(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    public Category getCategory(Long id){
        Category category;
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            category = categoryOptional.get();
        }
        else{
            throw new NoSuchCategoryException("There is no category with id " + id);
        }

        return category;
    }
}
