package com.alex.eshop.eshop.service;

import com.alex.eshop.eshop.dto.CategoryDTO;
import com.alex.eshop.eshop.exception.DataNotFound;
import com.alex.eshop.eshop.mapper.CategoryMapper;
import com.alex.eshop.eshop.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryMapper.toDto(categoryRepository.findAll());
    }

    public CategoryDTO getCategory(Long id) {
        return categoryMapper.toDto(categoryRepository.findById(id).orElseThrow(()->new DataNotFound("There is no category with id " + id)));
    }
}
