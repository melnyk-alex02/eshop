package com.alex.eshop.eshop.Service;

import com.alex.eshop.eshop.Entity.Category;
import com.alex.eshop.eshop.Exception.DataNotFound;
import com.alex.eshop.eshop.Repository.CategoryRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new DataNotFound("There is no category with id " + id));

    }
}
