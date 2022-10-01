package com.alex.eshop.eshop.Service;

import com.alex.eshop.eshop.DTO.CategoryDTO;
import com.alex.eshop.eshop.Exception.DataNotFound;
import com.alex.eshop.eshop.Mapper.CategoryMapper;
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
    public List<CategoryDTO> getAllCategories() {
        return CategoryMapper.INSTANCE.toDto(categoryRepository.findAll());
    }

    public CategoryDTO getCategory(Long id) {
        return CategoryMapper.INSTANCE.toDto(categoryRepository.findById(id).orElseThrow(()->new DataNotFound("There is no category with id " + id)));
    }
}
