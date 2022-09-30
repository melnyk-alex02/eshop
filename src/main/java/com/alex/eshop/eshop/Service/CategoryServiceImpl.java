package com.alex.eshop.eshop.Service;

import com.alex.eshop.eshop.Entity.Category;
import com.alex.eshop.eshop.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Component
@Transactional
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    public Category getCategory(long id){
        Category category = null;
        Optional<Category> categoryEntityOptional = categoryRepository.findById((int) id);
        if(categoryEntityOptional.isPresent()){
            category = categoryEntityOptional.get();
        }
        return category;
    }


    public CategoryServiceImpl(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }
}
