package com.alex.eshop.service;

import com.alex.eshop.dto.categoryDTOs.CategoryCreateDTO;
import com.alex.eshop.dto.categoryDTOs.CategoryDTO;
import com.alex.eshop.dto.categoryDTOs.CategoryUpdateDTO;
import com.alex.eshop.entity.Category;
import com.alex.eshop.exception.DataNotFoundException;
import com.alex.eshop.exception.InvalidDataException;
import com.alex.eshop.mapper.CategoryMapper;
import com.alex.eshop.repository.CategoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    public void testGetAllCategories() {
        List<Category> categoryList = createCategoryList();

        List<CategoryDTO> expectedCategoryDTOList = createCategoryDTOList();

        Pageable pageable = PageRequest.of(0, 10);

        Page<Category> categoryPage = new PageImpl<>(categoryList);

        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(categoryPage);
        when(categoryMapper.toDto(any(Category.class))).thenAnswer(invocationOnMock -> {
            Category category = invocationOnMock.getArgument(0);
            Long id = category.getId();
            return expectedCategoryDTOList.stream()
                    .filter(dto -> dto.id().equals(id))
                    .findFirst()
                    .orElse(null);
        });

        Page<CategoryDTO> result = categoryService.getAllCategories(pageable);

        verify(categoryRepository).findAll(any(Pageable.class));
        verify(categoryMapper, times(2)).toDto(any(Category.class));

        assertEquals(2, result.getTotalElements());
        assertEquals(expectedCategoryDTOList.get(0).id(), result.getContent().get(0).id());
        assertEquals(expectedCategoryDTOList.get(0).name(), result.getContent().get(0).name());
        assertEquals(expectedCategoryDTOList.get(0).description(), result.getContent().get(0).description());

        assertEquals(expectedCategoryDTOList.get(1).id(), result.getContent().get(1).id());
        assertEquals(expectedCategoryDTOList.get(1).name(), result.getContent().get(1).name());
        assertEquals(expectedCategoryDTOList.get(1).description(), result.getContent().get(1).description());
    }

    @Test
    public void testSearchCategories() {
        Pageable pageable = PageRequest.of(0, 5);
        String searchName = "Category 1";

        List<Category> categoryList = List.of(createCategoryList().get(0));

        List<CategoryDTO> expectedCategoryDTOList = List.of(createCategoryDTOList().get(0));

        Page<Category> categoryPage = new PageImpl<>(categoryList, pageable, 1);

        when(categoryRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(categoryPage);
        when(categoryMapper.toDto(any(Category.class))).thenAnswer(invocationOnMock -> {
            Category category1 = invocationOnMock.getArgument(0);
            Long id = category1.getId();
            return expectedCategoryDTOList.stream()
                    .filter(dto -> dto.id().equals(id))
                    .findFirst()
                    .orElse(null);
        });

        Page<CategoryDTO> result = categoryService.searchCategories(pageable, searchName);

        verify(categoryRepository).findAll(any(Specification.class), eq(pageable));
        verify(categoryMapper).toDto(any(Category.class));

        assertEquals(1, result.getTotalElements());

        assertEquals(expectedCategoryDTOList.get(0).id(), result.getContent().get(0).id());
        assertEquals(expectedCategoryDTOList.get(0).name(), result.getContent().get(0).name());
        assertEquals(expectedCategoryDTOList.get(0).description(), result.getContent().get(0).description());
    }

    @Test
    public void testGetCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Category 1");
        category.setDescription("Description 2");

        CategoryDTO expectedDto = new CategoryDTO(1L, "Category 1", "Description 1");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(any(Category.class))).thenReturn(expectedDto);

        CategoryDTO result = categoryService.getCategory(1L);

        verify(categoryRepository).findById(1L);
        verify(categoryMapper).toDto(any(Category.class));

        assertNotNull(result);

        assertEquals(expectedDto.id(), result.id());
        assertEquals(expectedDto.name(), result.name());
        assertEquals(expectedDto.description(), result.description());
    }

    @Test
    public void testCreateCategory() {
        CategoryCreateDTO categoryCreateDTO = new CategoryCreateDTO("Category 1", "Description 1");

        Category categoryToSave = new Category();
        categoryToSave.setId(null);
        categoryToSave.setName("Category 1");
        categoryToSave.setDescription("Description 1");

        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName("Category 1");
        savedCategory.setDescription("Description 1");

        CategoryDTO expectedCategoryDTO = new CategoryDTO(1L, "Category 1", "Description 1");

        when(categoryMapper.toEntity(categoryCreateDTO)).thenReturn(categoryToSave);
        when(categoryRepository.save(categoryToSave)).thenReturn(savedCategory);
        when(categoryMapper.toDto(savedCategory)).thenReturn(expectedCategoryDTO);

        CategoryDTO result = categoryService.createCategory(categoryCreateDTO);

        verify(categoryMapper).toEntity(categoryCreateDTO);
        verify(categoryRepository).save(categoryToSave);
        verify(categoryMapper).toDto(savedCategory);

        assertEquals(expectedCategoryDTO.id(), result.id());
        assertEquals(expectedCategoryDTO.name(), result.name());
        assertEquals(expectedCategoryDTO.description(), result.description());
    }

    @Test
    public void testUploadCategoriesFromCsv() {
        String csvContent = """
                name,description
                Category 1,Description 1
                Category 2,Description 2""";


        MultipartFile csvFile = new MockMultipartFile("csvFile",
                "valid.csv",
                "text/csv",
                csvContent.getBytes());

        List<Category> categoryList = createCategoryList();

        List<CategoryDTO> expectedCategoryDTOList = createCategoryDTOList();

        when(categoryRepository.saveAll(any())).thenReturn(categoryList);
        when(categoryMapper.toDto(categoryList)).thenReturn(expectedCategoryDTOList);

        List<CategoryDTO> result = categoryService.uploadCategoriesFromCsv(csvFile);

        verify(categoryRepository).saveAll(any());
        verify(categoryMapper).toDto(categoryList);

        assertEquals(expectedCategoryDTOList.size(), result.size());

        assertEquals(expectedCategoryDTOList.get(0).id(), result.get(0).id());
        assertEquals(expectedCategoryDTOList.get(0).name(), result.get(0).name());
        assertEquals(expectedCategoryDTOList.get(0).description(), result.get(0).description());

        assertEquals(expectedCategoryDTOList.get(1).id(), result.get(1).id());
        assertEquals(expectedCategoryDTOList.get(1).name(), result.get(1).name());
        assertEquals(expectedCategoryDTOList.get(1).description(), result.get(1).description());
    }

    @Test
    public void testUpdateCategory() {
        CategoryUpdateDTO categoryUpdateDTO = new CategoryUpdateDTO(1L, "Updated Category 1", "Updated Description 1");

        Category updatedCategory = new Category();
        updatedCategory.setId(1L);
        updatedCategory.setName("Updated Category 1");
        updatedCategory.setDescription("Updated Description 1");

        CategoryDTO expectedCategoryDTO = new CategoryDTO(1L, "Updated Category 1", "Updated Description 1");


        when(categoryRepository.existsById(categoryUpdateDTO.id())).thenReturn(true);
        when(categoryRepository.save(any())).thenReturn(updatedCategory);
        when(categoryMapper.toEntity(categoryUpdateDTO)).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(expectedCategoryDTO);

        CategoryDTO result = categoryService.updateCategory(categoryUpdateDTO);

        verify(categoryRepository).existsById(categoryUpdateDTO.id());
        verify(categoryRepository).save(any());
        verify(categoryMapper).toEntity(categoryUpdateDTO);
        verify(categoryMapper).toDto(updatedCategory);

        assertNotNull(result);
        assertEquals(expectedCategoryDTO.id(), result.id());
        assertEquals(expectedCategoryDTO.name(), result.name());
        assertEquals(expectedCategoryDTO.description(), result.description());
    }

    @Test
    public void testDeleteCategory() {
        Long categoryIdToDelete = 1L;

        when(categoryRepository.existsById(categoryIdToDelete)).thenReturn(true);

        categoryService.deleteCategory(categoryIdToDelete);

        verify(categoryRepository).deleteById(categoryIdToDelete);
    }

    @Test
    public void testGetCategory_WhenCategoryDoesNotExist_ShouldThrowException() {
        Long id = 1L;

        when(categoryRepository.findById(id)).thenThrow(new DataNotFoundException("There is no category with id" + id));

        assertThrows(DataNotFoundException.class, () -> categoryService.getCategory(id));

        verify(categoryRepository).findById(id);
    }

    @Test
    public void testUploadCategoriesFromCsv_WhenHeaderIsMissing_ShouldThrowException() {
        String csvContent = """
                name,
                Category1,Description1,
                Category2,Description2
                """;

        MultipartFile headerIsMissingFile = new MockMultipartFile("csvFile",
                "headerIsMissingFile.csv",
                "text/csv",
                csvContent.getBytes());

        assertThrows(InvalidDataException.class, () -> categoryService.uploadCategoriesFromCsv(headerIsMissingFile));
    }

    @Test
    public void testUploadCategoriesFromCsv_WhenHeaderIsExtra_ShouldThrowException() {
        String csvContent = """
                name,description,extraColumn,
                Category1,Description1,Extra1,
                Category2,Description2,Extra2
                """;

        MultipartFile extraHeaderFile = new MockMultipartFile("csvFile",
                "extraHeaderFile.csv",
                "text/csv",
                csvContent.getBytes());

        assertThrows(InvalidDataException.class, () -> categoryService.uploadCategoriesFromCsv(extraHeaderFile));
    }

    @Test
    public void testUploadItems_WhenFileIsEmpty_ShouldThrowException() {
        MultipartFile emptyFile = new MockMultipartFile("csvFile", new byte[0]);

        assertThrows(InvalidDataException.class, () -> categoryService.uploadCategoriesFromCsv(emptyFile));
    }

    @Test
    public void testUpdateCategory_WhenCategoryDoesNotExist_ShouldThrowException() {
        CategoryUpdateDTO categoryUpdateDTO = new CategoryUpdateDTO(123L, null, null);

        when(categoryRepository.existsById(categoryUpdateDTO.id())).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> categoryService.updateCategory(categoryUpdateDTO));

        verify(categoryRepository).existsById(categoryUpdateDTO.id());
    }

    @Test
    public void testDeleteCategory_WhenCategoryDoesNotExist_ShouldThrowException() {
        Long id = 123L;

        when(categoryRepository.existsById(id)).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> categoryService.deleteCategory(id));

        verify(categoryRepository).existsById(id);
    }

    @Test
    public void testDeleteCategory_WhenItemsInCategory_ShouldThrowException() {
        Long id = 1L;
        Long itemsInCategory = 5L;

        when(categoryRepository.existsById(id)).thenReturn(true);
        when(categoryRepository.countItemsByCategory(id)).thenReturn(itemsInCategory);

        assertThrows(DataNotFoundException.class, () -> categoryService.deleteCategory(id));

        verify(categoryRepository).existsById(id);
        verify(categoryRepository).countItemsByCategory(id);
    }

    @Test
    public void testSearchCategory_WhenCategoryDoesNotFound_ShouldReturnEmptyList() {
        String searchName = "test";

        Pageable pageable = PageRequest.of(0, 5);

        when(categoryRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(Page.empty());

        Page<CategoryDTO> result = categoryService.searchCategories(pageable, searchName);

        verify(categoryRepository).findAll(any(Specification.class), eq(pageable));

        assertEquals(Page.empty(), result);
    }

    private List<Category> createCategoryList() {
        List<Category> categoryList = new ArrayList<>();

        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Category 1");
        category1.setDescription("Description 1");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Category 2");
        category2.setDescription("Description 2");

        categoryList.add(category1);
        categoryList.add(category2);

        return categoryList;
    }

    private List<CategoryDTO> createCategoryDTOList() {
        List<CategoryDTO> categoryDTOList = new ArrayList<>();

        CategoryDTO categoryDTO1 = new CategoryDTO(1L,
                "Category 1",
                "Description 1");

        CategoryDTO categoryDTO2 = new CategoryDTO(2L,
                "Category 2",
                "Description 2");


        categoryDTOList.add(categoryDTO1);
        categoryDTOList.add(categoryDTO2);

        return categoryDTOList;
    }
}