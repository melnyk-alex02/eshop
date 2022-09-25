package com.alex.eshop.eshop.Service;

import com.alex.eshop.eshop.Entity.CategoryEntity;
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
    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }
    public CategoryEntity getCategory(int id){
        CategoryEntity categoryEntity = null;
        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findById(id);
        if(categoryEntityOptional.isPresent()){
            categoryEntity = categoryEntityOptional.get();
        }
        return categoryEntity;
    }


    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }
}
