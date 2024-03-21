package com.alex.eshop.service;

import com.alex.eshop.dto.categoryDTOs.CategoryCreateDTO;
import com.alex.eshop.dto.categoryDTOs.CategoryDTO;
import com.alex.eshop.dto.categoryDTOs.CategoryUpdateDTO;
import com.alex.eshop.entity.Category;
import com.alex.eshop.exception.DataNotFoundException;
import com.alex.eshop.filterSpecifications.CategorySpecification;
import com.alex.eshop.mapper.CategoryMapper;
import com.alex.eshop.repository.CategoryRepository;
import com.alex.eshop.utils.CsvHeaderChecker;
import com.alex.eshop.utils.FormatChecker;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public Page<CategoryDTO> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toDto);
    }

    public Page<CategoryDTO> searchCategories(Pageable pageable, String name) {
        Specification<Category> categorySpecification = CategorySpecification.hasNameContaining(name);

        return categoryRepository.findAll(categorySpecification, pageable)
                .map(categoryMapper::toDto);
    }

    public CategoryDTO getCategory(Long id) {
        return categoryMapper.toDto(categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("There is no category with id " + id)));
    }

    public CategoryDTO createCategory(CategoryCreateDTO categoryCreateDTO) {
        return categoryMapper.toDto(categoryRepository.save(categoryMapper.toEntity(categoryCreateDTO)));
    }

    public List<CategoryDTO> uploadCategoriesFromCsv(MultipartFile file) {
        FormatChecker.isCsv(file);

        String[] headers = {"name", "description"};

        CsvHeaderChecker.checkHeaders(file, headers);

        List<CategoryCreateDTO> categoryCreateDTOList = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(
                     fileReader,
                     CSVFormat.DEFAULT.builder()
                             .setHeader(headers)
                             .setSkipHeaderRecord(true)
                             .build())
        ) {
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                CategoryCreateDTO categoryCreateDTO = new CategoryCreateDTO(csvRecord.get("name"),
                        csvRecord.get("description"));

                categoryCreateDTOList.add(categoryCreateDTO);
            }
            return categoryMapper.toDto(categoryRepository.saveAll(categoryMapper.toEntity(categoryCreateDTOList)));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public CategoryDTO updateCategory(CategoryUpdateDTO categoryUpdateDTO) {
        if (!categoryRepository.existsById(categoryUpdateDTO.id())) {
            throw new DataNotFoundException("There is no category with id " + categoryUpdateDTO.id());
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