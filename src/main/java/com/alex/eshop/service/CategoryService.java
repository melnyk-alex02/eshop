package com.alex.eshop.service;

import com.alex.eshop.dto.CategoryCreateDTO;
import com.alex.eshop.dto.CategoryDTO;
import com.alex.eshop.dto.CategoryUpdateDTO;
import com.alex.eshop.entity.Category;
import com.alex.eshop.exception.DataNotFound;
import com.alex.eshop.mapper.CategoryCreateMapper;
import com.alex.eshop.mapper.CategoryMapper;
import com.alex.eshop.mapper.CategoryUpdateMapper;
import com.alex.eshop.repository.CategoryRepository;
import org.springframework.data.domain.Pageable;
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

    public List<CategoryDTO> getAllCategories(Pageable pageable) {
        return categoryMapper.toDto(categoryRepository.findAll(pageable));
    }

    public CategoryDTO getCategory(Long id) {
        return categoryMapper.toDto(categoryRepository.findById(id)
                .orElseThrow(()->new DataNotFound("There is no category with id " + id)));
    }
    public Category createCategory(CategoryCreateDTO categoryCreateDTO){
       return categoryRepository.save(categoryCreateMapper.toEntity(categoryCreateDTO));
    }
    public void updateCategory(CategoryUpdateDTO categoryUpdateDTO){
        CategoryUpdateDTO categoryUpdate = categoryUpdateMapper
                .toDto(categoryRepository.findById(categoryUpdateDTO.getId())
                .orElseThrow(()-> new DataNotFound("There is no category with id " + categoryUpdateDTO.getId())));
        categoryUpdate.setName(categoryUpdateDTO.getName());
        categoryUpdate.setDescription(categoryUpdateDTO.getDescription());
        categoryRepository.save(categoryUpdateMapper.toEntity(categoryUpdate));
    }
    public void deleteCategory(Long id){
        categoryRepository.delete(categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFound("There is no category with id" + id)));
    }
}
