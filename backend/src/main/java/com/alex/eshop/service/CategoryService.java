package com.alex.eshop.service;

import com.alex.eshop.dto.CategoryCreateDTO;
import com.alex.eshop.dto.CategoryDTO;
import com.alex.eshop.dto.CategoryUpdateDTO;
import com.alex.eshop.exception.DataNotFoundException;
import com.alex.eshop.mapper.CategoryMapper;
import com.alex.eshop.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
    }

    public Page<CategoryDTO> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toDto);
    }

    public CategoryDTO getCategory(Long id) {
        return categoryMapper.toDto(categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("There is no category with id " + id)));
    }

    public CategoryDTO createCategory(CategoryCreateDTO categoryCreateDTO) {
        return categoryMapper.toDto(categoryRepository.save(categoryMapper.toEntity(categoryCreateDTO)));
    }

    public CategoryDTO updateCategory(CategoryUpdateDTO categoryUpdateDTO) {
        if (!categoryRepository.existsById(categoryUpdateDTO.getId())) {
            throw new DataNotFoundException("There is no category with id " + categoryUpdateDTO.getId());
        }
        return categoryMapper.toDto(categoryRepository.save(categoryMapper.toEntity(categoryUpdateDTO)));
    }

    public void deleteCategory(Long id) {
        Long numOfItemsInCategory = categoryRepository.countItemsByCategory(id);
        if (!categoryRepository.existsById(id)) {
            throw new DataNotFoundException("There is no category with id " + id);
        }
        if (numOfItemsInCategory > 0) {
            throw new DataNotFoundException("There are " + numOfItemsInCategory + " items in category");
        }
        categoryRepository.deleteById(id);
    }
}