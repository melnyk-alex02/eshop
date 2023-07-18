package com.alex.eshop.service;

import com.alex.eshop.dto.CategoryCreateDTO;
import com.alex.eshop.dto.CategoryDTO;
import com.alex.eshop.dto.CategoryUpdateDTO;
import com.alex.eshop.exception.DataNotFoundException;
import com.alex.eshop.exception.InvalidDataException;
import com.alex.eshop.mapper.CategoryMapper;
import com.alex.eshop.repository.CategoryRepository;
import com.alex.eshop.utils.FormatChecker;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public List<CategoryDTO> uploadCategoriesFromCsv(MultipartFile file) {
        List<CategoryCreateDTO> categoryCreateDTOList = new ArrayList<>();
        String[] headers = {"name", "description"};

        if (FormatChecker.isCsv(file)) {
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
                    CategoryCreateDTO categoryCreateDTO = new CategoryCreateDTO();

                    categoryCreateDTO.setName(csvRecord.get("name"));
                    categoryCreateDTO.setDescription(csvRecord.get("description"));

                    categoryCreateDTOList.add(categoryCreateDTO);
                }
                return categoryMapper.toDto(categoryRepository.saveAll(categoryMapper.toEntity(categoryCreateDTOList)));
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new InvalidDataException("Unsupported file format");
        }
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