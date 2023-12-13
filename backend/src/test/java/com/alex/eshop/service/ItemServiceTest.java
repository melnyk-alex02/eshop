package com.alex.eshop.service;

import com.alex.eshop.dto.itemDTOs.ItemCreateDTO;
import com.alex.eshop.dto.itemDTOs.ItemDTO;
import com.alex.eshop.dto.itemDTOs.ItemUpdateDTO;
import com.alex.eshop.entity.Category;
import com.alex.eshop.entity.Item;
import com.alex.eshop.exception.DataNotFoundException;
import com.alex.eshop.exception.InvalidDataException;
import com.alex.eshop.mapper.ItemMapper;
import com.alex.eshop.repository.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemService itemService;

    @Test
    public void testGetAllItems() {
        List<Item> itemList = createItemList();

        List<ItemDTO> expectedItemDTOList = createItemDTOList();

        Pageable pageable = PageRequest.of(0, 10);

        Page<Item> itemPage = new PageImpl<>(itemList);

        when(itemRepository.findAll(any(Pageable.class))).thenReturn(itemPage);
        when(itemMapper.toDto(any(Item.class))).thenAnswer(invocationOnMock -> {
            Item item = invocationOnMock.getArgument(0);
            Long itemId = item.getId();
            return expectedItemDTOList.stream()
                    .filter(dto -> dto.getId().equals(itemId))
                    .findFirst()
                    .orElse(null);
        });

        Page<ItemDTO> result = itemService.getAllItems(pageable);

        verify(itemRepository).findAll(any(Pageable.class));
        verify(itemMapper, times(2)).toDto(any(Item.class));


        assertEquals(expectedItemDTOList.size(), result.getTotalElements());

        assertEquals(expectedItemDTOList.get(0).getId(), result.getContent().get(0).getId());
        assertEquals(expectedItemDTOList.get(0).getName(), result.getContent().get(0).getName());
        assertEquals(expectedItemDTOList.get(0).getDescription(), result.getContent().get(0).getDescription());
        assertEquals(expectedItemDTOList.get(0).getImageSrc(), result.getContent().get(0).getImageSrc());
        assertEquals(expectedItemDTOList.get(0).getCategoryId(), result.getContent().get(0).getCategoryId());
        assertEquals(expectedItemDTOList.get(0).getCategoryName(), result.getContent().get(0).getCategoryName());

        assertEquals(expectedItemDTOList.get(1).getId(), result.getContent().get(1).getId());
        assertEquals(expectedItemDTOList.get(1).getName(), result.getContent().get(1).getName());
        assertEquals(expectedItemDTOList.get(1).getDescription(), result.getContent().get(1).getDescription());
        assertEquals(expectedItemDTOList.get(1).getImageSrc(), result.getContent().get(1).getImageSrc());
        assertEquals(expectedItemDTOList.get(1).getCategoryId(), result.getContent().get(1).getCategoryId());
        assertEquals(expectedItemDTOList.get(1).getCategoryName(), result.getContent().get(1).getCategoryName());
    }

    @Test
    public void testSearchItems() {
        Pageable pageable = PageRequest.of(0, 10);
        String searchName = "test";
        Boolean hasImage = true;
        Long categoryId = 1L;

        Category category = new Category();
        category.setId(1L);
        category.setName("Category");
        category.setDescription("Description");

        List<Item> itemList = new ArrayList<>();
        Item item = new Item();
        item.setId(1L);
        item.setName("test");
        item.setDescription("Description");
        item.setImageSrc("ImgSrc");
        item.setCategory(category);

        itemList.add(item);

        List<ItemDTO> expectedItemDTOList = new ArrayList<>();
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(1L);
        itemDTO.setName("test");
        itemDTO.setDescription("Description");
        itemDTO.setCategoryId(1L);
        itemDTO.setCategoryName("Category");
        itemDTO.setImageSrc("ImgSrc");

        expectedItemDTOList.add(itemDTO);

        Page<Item> itemPage = new PageImpl<>(itemList, pageable, 1);

        when(itemRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(itemPage);
        when(itemMapper.toDto(any(Item.class))).thenAnswer(invocationOnMock -> {
            Item item1 = invocationOnMock.getArgument(0);
            Long id = item1.getId();
            return expectedItemDTOList.stream()
                    .filter(dto -> dto.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        });

        Page<ItemDTO> result = itemService.searchItems(pageable, searchName, hasImage, categoryId);

        verify(itemRepository).findAll(any(Specification.class), eq(pageable));
        verify(itemMapper).toDto(any(Item.class));

        assertEquals(expectedItemDTOList.size(), result.getTotalElements());
        assertEquals(expectedItemDTOList.get(0).getId(), result.getContent().get(0).getId());
        assertEquals(expectedItemDTOList.get(0).getName(), result.getContent().get(0).getName());
        assertEquals(expectedItemDTOList.get(0).getDescription(), result.getContent().get(0).getDescription());
        assertEquals(expectedItemDTOList.get(0).getImageSrc(), result.getContent().get(0).getImageSrc());
        assertEquals(expectedItemDTOList.get(0).getCategoryId(), result.getContent().get(0).getCategoryId());
        assertEquals(expectedItemDTOList.get(0).getCategoryName(), result.getContent().get(0).getCategoryName());
    }

    @Test
    public void testGetOneCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Category");
        category.setDescription("Description");

        Item item = new Item();
        item.setId(1L);
        item.setName("Item 1");
        item.setDescription("Description 1");
        item.setImageSrc("Image src 1");
        item.setCategory(category);

        ItemDTO expectedDto = new ItemDTO();
        expectedDto.setId(1L);
        expectedDto.setName("Item 1");
        expectedDto.setDescription("Description 1");
        expectedDto.setImageSrc("Image src 1");
        expectedDto.setCategoryId(1L);
        expectedDto.setCategoryName("Category");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemMapper.toDto(item)).thenReturn(expectedDto);

        ItemDTO result = itemService.getItemWithCategoryInfo(1L);

        verify(itemRepository).findById(1L);
        verify(itemMapper).toDto(any(Item.class));

        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(expectedDto.getName(), result.getName());
        assertEquals(expectedDto.getDescription(), result.getDescription());
        assertEquals(expectedDto.getCategoryId(), result.getCategoryId());
        assertEquals(expectedDto.getImageSrc(), result.getImageSrc());
        assertEquals(expectedDto.getCategoryName(), result.getCategoryName());
    }

    @Test
    public void testGetLastFiveItems() {
        List<Item> itemList = createItemList();

        Page<Item> itemsPage = new PageImpl<>(itemList);

        List<ItemDTO> expectedItemDTOList = createItemDTOList();

        when(itemRepository.findAll(PageRequest.of(
                0, 5, Sort.Direction.DESC, "id"))).thenReturn(itemsPage);
        when(itemMapper.toDto(itemList)).thenReturn(expectedItemDTOList);

        List<ItemDTO> result = itemService.getLastFiveItems();

        verify(itemRepository).findAll(any(Pageable.class));
        verify(itemMapper).toDto(itemList);

        assertEquals(expectedItemDTOList.get(0).getId(), result.get(0).getId());
        assertEquals(expectedItemDTOList.get(0).getName(), result.get(0).getName());
        assertEquals(expectedItemDTOList.get(0).getDescription(), result.get(0).getDescription());
        assertEquals(expectedItemDTOList.get(0).getImageSrc(), result.get(0).getImageSrc());
        assertEquals(expectedItemDTOList.get(0).getCategoryId(), result.get(0).getCategoryId());
        assertEquals(expectedItemDTOList.get(0).getCategoryName(), result.get(0).getCategoryName());

        assertEquals(expectedItemDTOList.get(1).getId(), result.get(1).getId());
        assertEquals(expectedItemDTOList.get(1).getName(), result.get(1).getName());
        assertEquals(expectedItemDTOList.get(1).getDescription(), result.get(1).getDescription());
        assertEquals(expectedItemDTOList.get(1).getImageSrc(), result.get(1).getImageSrc());
        assertEquals(expectedItemDTOList.get(1).getCategoryId(), result.get(1).getCategoryId());
        assertEquals(expectedItemDTOList.get(1).getCategoryName(), result.get(1).getCategoryName());
    }

    @Test
    public void testGetItemWithCategoryInfo() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Category 1");
        category.setDescription("Description 1");

        Item item = new Item();
        item.setId(1L);
        item.setName("Item 1");
        item.setDescription("Description 1");
        item.setImageSrc("Img Src 1");
        item.setCategory(category);

        ItemDTO expectedItemDTO = new ItemDTO();
        expectedItemDTO.setId(1L);
        expectedItemDTO.setName("Item 1");
        expectedItemDTO.setDescription("Description 1");
        expectedItemDTO.setCategoryId(1L);
        expectedItemDTO.setCategoryName("Category 1");
        expectedItemDTO.setImageSrc("Img Src 1");

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemMapper.toDto(any(Item.class))).thenReturn(expectedItemDTO);

        ItemDTO result = itemService.getItemWithCategoryInfo(1L);

        verify(itemRepository).findById(item.getId());
        verify(itemMapper).toDto(any(Item.class));

        assertEquals(expectedItemDTO.getId(), result.getId());
        assertEquals(expectedItemDTO.getName(), result.getName());
        assertEquals(expectedItemDTO.getDescription(), result.getDescription());
        assertEquals(expectedItemDTO.getImageSrc(), result.getImageSrc());
        assertEquals(expectedItemDTO.getCategoryId(), result.getCategoryId());
        assertEquals(expectedItemDTO.getCategoryName(), result.getCategoryName());
    }

    @Test
    public void testCreateItem() {
        ItemCreateDTO itemCreateDTO = new ItemCreateDTO();
        itemCreateDTO.setName("Item 1");
        itemCreateDTO.setDescription("Description 1");
        itemCreateDTO.setImageSrc("Img Src 1");
        itemCreateDTO.setCategoryId(1L);

        Category category = new Category();
        category.setId(1L);
        category.setName("Category 1");
        category.setDescription("Description 1");

        Item itemToSave = new Item();
        itemToSave.setId(1L);
        itemToSave.setName("Item 1");
        itemToSave.setDescription("Description 1");
        itemToSave.setCategory(category);
        itemToSave.setImageSrc("Img Src 1");

        Item savedItem = new Item();
        savedItem.setId(1L);
        savedItem.setName("Item 1");
        savedItem.setDescription("Description 1");
        savedItem.setCategory(category);
        savedItem.setImageSrc("Image src 1");

        ItemDTO expectedItemDTO = new ItemDTO();
        expectedItemDTO.setId(1L);
        expectedItemDTO.setName("Item 1");
        expectedItemDTO.setDescription("Description 1");
        expectedItemDTO.setCategoryId(1L);
        expectedItemDTO.setCategoryName("Category 1");
        expectedItemDTO.setImageSrc("Img src 1");

        when(itemMapper.toEntity(itemCreateDTO)).thenReturn(itemToSave);
        when(itemRepository.existsByCategoryId(category.getId())).thenReturn(true);
        when(itemRepository.save(itemToSave)).thenReturn(savedItem);
        when(itemMapper.toDto(savedItem)).thenReturn(expectedItemDTO);

        ItemDTO result = itemService.createItem(itemCreateDTO);

        verify(itemMapper).toEntity(itemCreateDTO);
        verify(itemRepository).existsByCategoryId(category.getId());
        verify(itemRepository).save(itemToSave);
        verify(itemMapper).toDto(savedItem);

        assertEquals(expectedItemDTO.getId(), result.getId());
        assertEquals(expectedItemDTO.getName(), result.getName());
        assertEquals(expectedItemDTO.getDescription(), result.getDescription());
        assertEquals(expectedItemDTO.getCategoryId(), result.getCategoryId());
        assertEquals(expectedItemDTO.getCategoryName(), result.getCategoryName());
        assertEquals(expectedItemDTO.getImageSrc(), result.getImageSrc());
    }

    @Test
    public void testUpdateItem() {
        ItemUpdateDTO itemUpdateDTO = new ItemUpdateDTO();
        itemUpdateDTO.setId(1L);
        itemUpdateDTO.setName("Updated Item");
        itemUpdateDTO.setDescription("Updated Description");
        itemUpdateDTO.setImageSrc("Updated ImgSrc");
        itemUpdateDTO.setCategoryId(1L);

        Category category = new Category();
        category.setId(1L);
        category.setName("Category");
        category.setDescription("Description");

        Item updatedItem = new Item();
        updatedItem.setId(1L);
        updatedItem.setName("Updated Item");
        updatedItem.setDescription("Updated Description");
        updatedItem.setImageSrc("Updated ImgSrc");
        updatedItem.setCategory(category);

        ItemDTO expectedItemDTO = new ItemDTO();
        expectedItemDTO.setId(1L);
        expectedItemDTO.setName("Updated Item");
        expectedItemDTO.setDescription("Updated Description");
        expectedItemDTO.setCategoryId(1L);
        expectedItemDTO.setCategoryName("Category");
        expectedItemDTO.setImageSrc("Updated ImgSrc");

        when(itemRepository.existsById(itemUpdateDTO.getId())).thenReturn(true);
        when(itemRepository.existsByCategoryId(category.getId())).thenReturn(true);
        when(itemRepository.save(any())).thenReturn(updatedItem);
        when(itemMapper.toEntity(itemUpdateDTO)).thenReturn(updatedItem);
        when(itemMapper.toDto(updatedItem)).thenReturn(expectedItemDTO);

        ItemDTO result = itemService.updateItem(itemUpdateDTO);

        verify(itemRepository).existsById(itemUpdateDTO.getId());
        verify(itemRepository).existsByCategoryId(category.getId());
        verify(itemRepository).save(any());
        verify(itemMapper).toEntity(itemUpdateDTO);
        verify(itemMapper).toDto(updatedItem);

        assertEquals(expectedItemDTO.getId(), result.getId());
        assertEquals(expectedItemDTO.getName(), result.getName());
        assertEquals(expectedItemDTO.getDescription(), result.getDescription());
        assertEquals(expectedItemDTO.getCategoryId(), result.getCategoryId());
        assertEquals(expectedItemDTO.getCategoryName(), result.getCategoryName());
        assertEquals(expectedItemDTO.getImageSrc(), result.getImageSrc());
    }

    @Test
    public void testDeleteItem() {
        Long itemIdToDelete = 1L;

        when(itemRepository.existsById(itemIdToDelete)).thenReturn(true);

        itemService.deleteItem(itemIdToDelete);

        verify(itemRepository).deleteById(itemIdToDelete);
    }

    @Test
    public void testUploadItemsFromCsv() {
        String csvContent = """
                name,description,categoryId,imageSrc
                Item1,Description1,1,Image src 1
                Item2,Description2,1,Image src 2
                """;

        MultipartFile csvFile = new MockMultipartFile("csvFile",
                "valid.csv",
                "text/csv",
                csvContent.getBytes());

        List<Item> itemList = createItemList();

        List<ItemDTO> expectedItemDTOList = createItemDTOList();

        when(itemRepository.saveAll(any())).thenReturn(itemList);
        when(itemMapper.toDto(itemList)).thenReturn(expectedItemDTOList);

        List<ItemDTO> result = itemService.uploadItemsFromCsv(csvFile);

        verify(itemRepository).saveAll(any());
        verify(itemMapper).toDto(itemList);

        assertEquals(expectedItemDTOList.get(0).getId(), result.get(0).getId());
        assertEquals(expectedItemDTOList.get(0).getName(), result.get(0).getName());
        assertEquals(expectedItemDTOList.get(0).getDescription(), result.get(0).getDescription());
        assertEquals(expectedItemDTOList.get(0).getCategoryId(), result.get(0).getCategoryId());
        assertEquals(expectedItemDTOList.get(0).getCategoryName(), result.get(0).getCategoryName());
        assertEquals(expectedItemDTOList.get(0).getImageSrc(), result.get(0).getImageSrc());

        assertEquals(expectedItemDTOList.get(1).getId(), result.get(1).getId());
        assertEquals(expectedItemDTOList.get(1).getName(), result.get(1).getName());
        assertEquals(expectedItemDTOList.get(1).getDescription(), result.get(1).getDescription());
        assertEquals(expectedItemDTOList.get(1).getCategoryId(), result.get(1).getCategoryId());
        assertEquals(expectedItemDTOList.get(1).getCategoryName(), result.get(1).getCategoryName());
        assertEquals(expectedItemDTOList.get(1).getImageSrc(), result.get(1).getImageSrc());
    }

    @Test
    public void testGetItemsInCategory() {
        List<Item> itemList = createItemList();

        List<ItemDTO> expectedItemDTOList = createItemDTOList();

        Pageable pageable = PageRequest.of(0, 5);

        Page<Item> itemPage = new PageImpl<>(itemList);

        when(itemRepository.findByCategoryId(any(Long.class), any(Pageable.class))).thenReturn(itemPage);
        when(itemMapper.toDto(any(Item.class))).thenAnswer(invocationOnMock -> {
            Item item = invocationOnMock.getArgument(0);
            Long id = item.getId();
            return expectedItemDTOList.stream()
                    .filter(dto -> dto.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        });

        Page<ItemDTO> result = itemService.getItemsInCategory(1L, pageable);

        verify(itemRepository).findByCategoryId(any(Long.class), any(Pageable.class));
        verify(itemMapper, times(2)).toDto(any(Item.class));

        assertEquals(2, result.getTotalElements());

        assertEquals(expectedItemDTOList.get(0).getId(), result.getContent().get(0).getId());
        assertEquals(expectedItemDTOList.get(0).getName(), result.getContent().get(0).getName());
        assertEquals(expectedItemDTOList.get(0).getDescription(), result.getContent().get(0).getDescription());
        assertEquals(expectedItemDTOList.get(0).getCategoryId(), result.getContent().get(0).getCategoryId());
        assertEquals(expectedItemDTOList.get(0).getCategoryName(), result.getContent().get(0).getCategoryName());
        assertEquals(expectedItemDTOList.get(0).getImageSrc(), result.getContent().get(0).getImageSrc());

        assertEquals(expectedItemDTOList.get(1).getId(), result.getContent().get(1).getId());
        assertEquals(expectedItemDTOList.get(1).getName(), result.getContent().get(1).getName());
        assertEquals(expectedItemDTOList.get(1).getDescription(), result.getContent().get(1).getDescription());
        assertEquals(expectedItemDTOList.get(1).getCategoryId(), result.getContent().get(1).getCategoryId());
        assertEquals(expectedItemDTOList.get(1).getCategoryName(), result.getContent().get(1).getCategoryName());
        assertEquals(expectedItemDTOList.get(1).getImageSrc(), result.getContent().get(1).getImageSrc());
    }

    @Test
    public void testSearchItem_WhenItemDoesNotExist_ShouldReturnEmptyPage() {
        String searchName = "Non-item";
        Boolean hasImage = true;
        Long categoryId = 123L;

        Pageable pageable = PageRequest.of(0, 5);

        when(itemRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(Page.empty());

        Page<ItemDTO> result = itemService.searchItems(pageable, searchName, hasImage, categoryId);

        verify(itemRepository).findAll(any(Specification.class), eq(pageable));

        assertEquals(Page.empty(), result);
    }

    @Test
    public void testGetItem_WhenItemDoesNotExist_ShouldThrowException() {
        Long id = 1L;

        when(itemRepository.findById(id)).thenThrow(new DataNotFoundException("There is no item with id 1"));

        assertThrows(DataNotFoundException.class, () -> itemService.getItemWithCategoryInfo(id));
    }

    @Test
    public void testUploadItems_WhenHeaderIsMissing_ShouldThrowException() {
        String csvContent = """
                name,categoryId,imageSrc
                Item1,1,Image src 1
                Item2,1,Image src 2
                """;

        MultipartFile headerIsMissingFile = new MockMultipartFile("csvFile",
                "headerIsMissing.csv",
                "text/csv",
                csvContent.getBytes());

        assertThrows(InvalidDataException.class, () -> itemService.uploadItemsFromCsv(headerIsMissingFile));
    }

    @Test
    public void testUploadItemsFromCsv_WhenHeadersCountIsIncorrect_ShouldThrowException() {
        String csvContent = """
                name,description,categoryId,imageSrc,extraColumn,
                Item1,1,Image src 1
                Item2,1,Image src 2
                """;

        MultipartFile extraHeaderFile = new MockMultipartFile("csvFile",
                "extraHeader.csv",
                "text/csv",
                csvContent.getBytes());

        assertThrows(InvalidDataException.class, () -> itemService.uploadItemsFromCsv(extraHeaderFile));
    }

    @Test
    public void testUploadItemsFromCsv_WhenFileIsEmpty_ShouldThrowException() {
        MultipartFile emptyFile = new MockMultipartFile("csvFile", new byte[0]);

        assertThrows(InvalidDataException.class, () -> itemService.uploadItemsFromCsv(emptyFile));
    }

    @Test
    public void testCreateItem_WhenCategoryIdDoesNotExist_ShouldThrowException() {
        ItemCreateDTO itemCreateDTO = new ItemCreateDTO();
        itemCreateDTO.setCategoryId(123L);

        when(itemRepository.existsByCategoryId(itemCreateDTO.getCategoryId())).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> itemService.createItem(itemCreateDTO));

        verify(itemRepository).existsByCategoryId(itemCreateDTO.getCategoryId());
    }


    @Test
    public void testUpdateItem_WhenItemDoesNotExist_ShouldThrowException() {
        ItemUpdateDTO itemUpdateDTO = new ItemUpdateDTO();
        itemUpdateDTO.setId(123L);
        itemUpdateDTO.setCategoryId(123L);

        when(itemRepository.existsById(itemUpdateDTO.getId())).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> itemService.updateItem(itemUpdateDTO));

        verify(itemRepository).existsById(itemUpdateDTO.getId());
    }

    @Test
    public void testUpdateItem_WhenCategoryDoesNotExist_ShouldThrowException() {
        ItemUpdateDTO itemUpdateDTO = new ItemUpdateDTO();
        itemUpdateDTO.setId(123L);
        itemUpdateDTO.setCategoryId(123L);

        when(itemRepository.existsById(itemUpdateDTO.getId())).thenReturn(true);
        when(itemRepository.existsByCategoryId(itemUpdateDTO.getCategoryId())).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> itemService.updateItem(itemUpdateDTO));

        verify(itemRepository).existsById(itemUpdateDTO.getId());
        verify(itemRepository).existsByCategoryId(itemUpdateDTO.getCategoryId());
    }

    @Test
    public void testDeleteItem_WhenItemDoesNotExist_ShouldThrowException() {
        Long id = 123L;

        when(itemRepository.existsById(id)).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> itemService.deleteItem(id));

        verify(itemRepository).existsById(id);
    }

    private List<Item> createItemList() {
        List<Item> itemList = new ArrayList<>();

        Category category = new Category();
        category.setId(1L);
        category.setName("Category");
        category.setDescription("Description");

        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item 1");
        item1.setDescription("Description 1");
        item1.setCategory(category);
        item1.setImageSrc("Image src 1");

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Item 2");
        item2.setDescription("Description 2");
        item2.setCategory(category);
        item2.setImageSrc("Image src 2");

        itemList.add(item1);
        itemList.add(item2);

        return itemList;
    }

    private List<ItemDTO> createItemDTOList() {
        List<ItemDTO> itemDTOList = new ArrayList<>();

        ItemDTO itemDTO1 = new ItemDTO();
        itemDTO1.setId(1L);
        itemDTO1.setName("Item 1");
        itemDTO1.setDescription("Description 1");
        itemDTO1.setCategoryId(1L);
        itemDTO1.setCategoryName("Category");
        itemDTO1.setImageSrc("Image src 1");

        ItemDTO itemDTO2 = new ItemDTO();
        itemDTO2.setId(2L);
        itemDTO2.setName("Item 2");
        itemDTO2.setDescription("Description");
        itemDTO2.setCategoryId(1L);
        itemDTO2.setCategoryName("Category");
        itemDTO2.setImageSrc("Image src 2");

        itemDTOList.add(itemDTO1);
        itemDTOList.add(itemDTO2);

        return itemDTOList;
    }
}