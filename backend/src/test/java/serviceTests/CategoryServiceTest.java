package serviceTests;

import com.alex.eshop.dto.categoryDTOs.CategoryCreateDTO;
import com.alex.eshop.dto.categoryDTOs.CategoryDTO;
import com.alex.eshop.dto.categoryDTOs.CategoryUpdateDTO;
import com.alex.eshop.entity.Category;
import com.alex.eshop.exception.DataNotFoundException;
import com.alex.eshop.mapper.CategoryMapper;
import com.alex.eshop.repository.CategoryRepository;
import com.alex.eshop.service.CategoryService;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        List<CategoryDTO> expectedCategoryDTOList = new ArrayList<>();

        CategoryDTO categoryDTO1 = new CategoryDTO();
        categoryDTO1.setId(1L);
        categoryDTO1.setName("Category 1");
        categoryDTO1.setDescription("Description 1");

        CategoryDTO categoryDTO2 = new CategoryDTO();
        categoryDTO2.setId(2L);
        categoryDTO2.setName("Category 2");
        categoryDTO2.setDescription("Description 2");

        expectedCategoryDTOList.add(categoryDTO1);
        expectedCategoryDTOList.add(categoryDTO2);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Category> categoryPage = new PageImpl<>(categoryList);

        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(categoryPage);
        when(categoryMapper.toDto(any(Category.class))).thenAnswer(invocationOnMock -> {
            Category category = invocationOnMock.getArgument(0);
            Long id = category.getId();
            return expectedCategoryDTOList.stream()
                    .filter(dto -> dto.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        });

        Page<CategoryDTO> result = categoryService.getAllCategories(pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(expectedCategoryDTOList.get(0).getId(), result.getContent().get(0).getId());
        assertEquals(expectedCategoryDTOList.get(0).getName(), result.getContent().get(0).getName());
        assertEquals(expectedCategoryDTOList.get(0).getDescription(), result.getContent().get(0).getDescription());

        assertEquals(expectedCategoryDTOList.get(0).getId(), result.getContent().get(0).getId());
        assertEquals(expectedCategoryDTOList.get(0).getName(), result.getContent().get(1).getName());
        assertEquals(expectedCategoryDTOList.get(0).getDescription(), result.getContent().get(1).getDescription());
    }

    @Test
    public void testSearchCategories() {
        Pageable pageable = PageRequest.of(0, 5);
        String searchName = "test";

        List<Category> categoryList = new ArrayList<>();
        Category category = new Category();
        category.setId(1L);
        category.setName("test");
        category.setDescription("Description");

        categoryList.add(category);

        List<CategoryDTO> expectedCategoryDTOList = new ArrayList<>();
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("test");
        categoryDTO.setDescription("Description");

        expectedCategoryDTOList.add(categoryDTO);

        Page<Category> categoryPage = new PageImpl<>(categoryList, pageable, 1);

        when(categoryRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(categoryPage);
        when(categoryMapper.toDto(any(Category.class))).thenAnswer(invocationOnMock -> {
            Category category1 = invocationOnMock.getArgument(0);
            Long id = category1.getId();
            return expectedCategoryDTOList.stream()
                    .filter(dto -> dto.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        });

        Page<CategoryDTO> result = categoryService.searchCategories(pageable, searchName);

        assertEquals(1, result.getTotalElements());

        assertEquals(expectedCategoryDTOList.get(0).getId(), result.getContent().get(0).getId());
        assertEquals(expectedCategoryDTOList.get(0).getName(), result.getContent().get(0).getName());
        assertEquals(expectedCategoryDTOList.get(0).getDescription(), result.getContent().get(0).getDescription());
    }

    @Test
    public void testGetCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Category 1");
        category.setDescription("Description 2");

        CategoryDTO expectedDto = new CategoryDTO();
        expectedDto.setId(1L);
        expectedDto.setName("Category 1");
        expectedDto.setDescription("Description 1");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expectedDto);

        CategoryDTO result = categoryService.getCategory(1L);

        assertNotNull(result);

        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(expectedDto.getName(), result.getName());
        assertEquals(expectedDto.getDescription(), result.getDescription());
    }

    @Test
    public void testCreateCategory() {
        CategoryCreateDTO categoryCreateDTO = new CategoryCreateDTO();
        categoryCreateDTO.setName("Category 1");
        categoryCreateDTO.setDescription("Description 1");

        Category categoryToSave = new Category();
        categoryToSave.setId(null);
        categoryToSave.setName("Category 1");
        categoryToSave.setDescription("Description 1");

        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName("Category 1");
        savedCategory.setDescription("Description 1");

        CategoryDTO expectedCategoryDTO = new CategoryDTO();
        expectedCategoryDTO.setId(1L);
        expectedCategoryDTO.setName("Category 1");
        expectedCategoryDTO.setDescription("Description 1");

        when(categoryMapper.toEntity(categoryCreateDTO)).thenReturn(categoryToSave);
        when(categoryRepository.save(categoryToSave)).thenReturn(savedCategory);
        when(categoryMapper.toDto(savedCategory)).thenReturn(expectedCategoryDTO);

        CategoryDTO result = categoryService.createCategory(categoryCreateDTO);

        assertEquals(expectedCategoryDTO.getId(), result.getId());
        assertEquals(expectedCategoryDTO.getName(), result.getName());
        assertEquals(expectedCategoryDTO.getDescription(), result.getDescription());
    }

    @Test
    public void testUploadCategoriesFromCsv() {
        String csvContent = """
                name,description
                Category1,Description1
                Category2,Description2
                Category3,Description3""";


        MultipartFile csvFile = new MockMultipartFile("csvFile",
                "valid.csv",
                "text/csv",
                csvContent.getBytes());

        List<Category> categoryList = new ArrayList<>();

        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Category1");
        category1.setDescription("Description1");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Category2");
        category2.setDescription("Description2");

        Category category3 = new Category();
        category3.setId(3L);
        category3.setName("Category3");
        category3.setDescription("Description3");

        categoryList.add(category1);
        categoryList.add(category2);
        categoryList.add(category3);

        List<CategoryCreateDTO> categoryCreateDTOList = new ArrayList<>();

        CategoryCreateDTO categoryCreateDTO1 = new CategoryCreateDTO();
        categoryCreateDTO1.setName("Category1");
        categoryCreateDTO1.setDescription("Description1");

        CategoryCreateDTO categoryCreateDTO2 = new CategoryCreateDTO();
        categoryCreateDTO2.setName("Category2");
        categoryCreateDTO2.setDescription("Description2");

        CategoryCreateDTO categoryCreateDTO3 = new CategoryCreateDTO();
        categoryCreateDTO3.setName("Category2");
        categoryCreateDTO3.setDescription("Description2");

        categoryCreateDTOList.add(categoryCreateDTO1);
        categoryCreateDTOList.add(categoryCreateDTO2);
        categoryCreateDTOList.add(categoryCreateDTO3);

        List<CategoryDTO> expectedCategoryDTOList = new ArrayList<>();

        CategoryDTO categoryDTO1 = new CategoryDTO();
        categoryDTO1.setId(1L);
        categoryDTO1.setName("Category1");
        categoryDTO1.setDescription("Description1");

        CategoryDTO categoryDTO2 = new CategoryDTO();
        categoryDTO2.setId(2L);
        categoryDTO2.setName("Category2");
        categoryDTO2.setDescription("Description2");

        CategoryDTO categoryDTO3 = new CategoryDTO();
        categoryDTO3.setId(3L);
        categoryDTO3.setName("Category3");
        categoryDTO3.setDescription("Description3");

        expectedCategoryDTOList.add(categoryDTO1);
        expectedCategoryDTOList.add(categoryDTO2);
        expectedCategoryDTOList.add(categoryDTO3);

        when(categoryRepository.saveAll(any())).thenReturn(categoryList);
        when(categoryMapper.toDto(categoryList)).thenReturn(expectedCategoryDTOList);

        List<CategoryDTO> result = categoryService.uploadCategoriesFromCsv(csvFile);

        assertEquals(expectedCategoryDTOList.size(), result.size());

        assertEquals(expectedCategoryDTOList.get(0).getId(), result.get(0).getId());
        assertEquals(expectedCategoryDTOList.get(0).getName(), result.get(0).getName());
        assertEquals(expectedCategoryDTOList.get(0).getDescription(), result.get(0).getDescription());

        assertEquals(expectedCategoryDTOList.get(1).getId(), result.get(1).getId());
        assertEquals(expectedCategoryDTOList.get(1).getName(), result.get(1).getName());
        assertEquals(expectedCategoryDTOList.get(1).getDescription(), result.get(1).getDescription());

        assertEquals(expectedCategoryDTOList.get(2).getId(), result.get(2).getId());
        assertEquals(expectedCategoryDTOList.get(2).getName(), result.get(2).getName());
        assertEquals(expectedCategoryDTOList.get(2).getDescription(), result.get(2).getDescription());
    }

    @Test
    public void testUpdateCategory() {
        CategoryUpdateDTO categoryUpdateDTO = new CategoryUpdateDTO();
        categoryUpdateDTO.setId(1L);
        categoryUpdateDTO.setName("Updated Category 1");
        categoryUpdateDTO.setDescription("Update Category Description 1");

        Category updatedCategory = new Category();
        updatedCategory.setId(1L);
        updatedCategory.setName("Updated Category 1");
        updatedCategory.setDescription("Updated Description 1");

        CategoryDTO expectedCategoryDTO = new CategoryDTO();
        expectedCategoryDTO.setId(1L);
        expectedCategoryDTO.setName("Updated Category 1");
        expectedCategoryDTO.setDescription("Updated Description 1");

        when(categoryRepository.existsById(categoryUpdateDTO.getId())).thenReturn(true);
        when(categoryRepository.save(any())).thenReturn(updatedCategory);
        when(categoryMapper.toEntity(categoryUpdateDTO)).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(expectedCategoryDTO);

        CategoryDTO result = categoryService.updateCategory(categoryUpdateDTO);

        assertNotNull(result);
        assertEquals(expectedCategoryDTO.getId(), result.getId());
        assertEquals(expectedCategoryDTO.getName(), result.getName());
        assertEquals(expectedCategoryDTO.getDescription(), result.getDescription());
    }

    @Test
    public void testDeleteCategory() {
        Long categoryIdToDelete = 1L;

        Category existingCategory = new Category();
        existingCategory.setId(categoryIdToDelete);
        existingCategory.setName("Category 1");
        existingCategory.setDescription("Description 1");

        when(categoryRepository.existsById(categoryIdToDelete)).thenReturn(true);

        categoryService.deleteCategory(categoryIdToDelete);

        verify(categoryRepository).deleteById(categoryIdToDelete);
    }

    @Test
    public void testGetCategory_Exception() {
        Long id = 1L;

        when(categoryRepository.findById(id)).thenThrow(new DataNotFoundException("There is no category with id" + id));

        assertThrows(DataNotFoundException.class, () -> categoryService.getCategory(id));
    }

    @Test
    public void testUpdateCategory_Exception() {
        CategoryUpdateDTO categoryUpdateDTO = new CategoryUpdateDTO();
        categoryUpdateDTO.setId(123L);

        when(categoryRepository.existsById(categoryUpdateDTO.getId())).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> categoryService.updateCategory(categoryUpdateDTO));
    }

    @Test
    public void testDeleteCategory_ExceptionOfCategoryId() {
        Long id = 123L;

        when(categoryRepository.existsById(id)).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> categoryService.deleteCategory(id));
    }

    @Test
    public void testDeleteCategory_ExceptionOfItemsInCategory() {
        Long id = 1L;
        Long itemsInCategory = 5L;

        when(categoryRepository.existsById(id)).thenReturn(true);
        when(categoryRepository.countItemsByCategory(id)).thenReturn(itemsInCategory);

        assertThrows(DataNotFoundException.class, () -> categoryService.deleteCategory(id));
    }

    @Test
    public void testSearchCategory_Exception() {
        Pageable pageable = PageRequest.of(0, 5);

        String searchName = "test";

        Page<Category> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(categoryRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(emptyPage);

        assertThrows(DataNotFoundException.class, () -> categoryService.searchCategories(pageable, searchName));
    }
}