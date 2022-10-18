package com.alex.eshop.service;

import com.alex.eshop.dto.CategoryDTO;
import com.alex.eshop.exception.DataNotFound;
import com.alex.eshop.mapper.CategoryMapper;
import com.alex.eshop.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryMapper.toDto(categoryRepository.findAll());
    }

    public CategoryDTO getCategory(Long id) {
        return categoryMapper.toDto(categoryRepository.findById(id).orElseThrow(()->new DataNotFound("There is no category with id " + id)));
    }
}
