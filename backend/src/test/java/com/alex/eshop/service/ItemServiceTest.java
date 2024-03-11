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

import java.math.BigDecimal;
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
                    .filter(dto -> dto.id().equals(itemId))
                    .findFirst()
                    .orElse(null);
        });

        Page<ItemDTO> result = itemService.getAllItems(pageable);

        verify(itemRepository).findAll(any(Pageable.class));
        verify(itemMapper, times(2)).toDto(any(Item.class));


        assertEquals(expectedItemDTOList.size(), result.getTotalElements());

        assertEquals(expectedItemDTOList.get(0).id(), result.getContent().get(0).id());
        assertEquals(expectedItemDTOList.get(0).name(), result.getContent().get(0).name());
        assertEquals(expectedItemDTOList.get(0).description(), result.getContent().get(0).description());
        assertEquals(expectedItemDTOList.get(0).price(), result.getContent().get(0).price());
        assertEquals(expectedItemDTOList.get(0).imageSrc(), result.getContent().get(0).imageSrc());
        assertEquals(expectedItemDTOList.get(0).categoryId(), result.getContent().get(0).categoryId());
        assertEquals(expectedItemDTOList.get(0).categoryName(), result.getContent().get(0).categoryName());

        assertEquals(expectedItemDTOList.get(1).id(), result.getContent().get(1).id());
        assertEquals(expectedItemDTOList.get(1).name(), result.getContent().get(1).name());
        assertEquals(expectedItemDTOList.get(1).description(), result.getContent().get(1).description());
        assertEquals(expectedItemDTOList.get(1).price(), result.getContent().get(1).price());
        assertEquals(expectedItemDTOList.get(1).imageSrc(), result.getContent().get(1).imageSrc());
        assertEquals(expectedItemDTOList.get(1).categoryId(), result.getContent().get(1).categoryId());
        assertEquals(expectedItemDTOList.get(1).categoryName(), result.getContent().get(1).categoryName());
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
        ItemDTO itemDTO = new ItemDTO(1L,
                "test",
                1L,
                "Category",
                "Description",
                BigDecimal.valueOf(123),
                "ImgSrc");

        expectedItemDTOList.add(itemDTO);

        Page<Item> itemPage = new PageImpl<>(itemList, pageable, 1);

        when(itemRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(itemPage);
        when(itemMapper.toDto(any(Item.class))).thenAnswer(invocationOnMock -> {
            Item item1 = invocationOnMock.getArgument(0);
            Long id = item1.getId();
            return expectedItemDTOList.stream()
                    .filter(dto -> dto.id().equals(id))
                    .findFirst()
                    .orElse(null);
        });

        Page<ItemDTO> result = itemService.searchItems(pageable, searchName, hasImage, categoryId);

        verify(itemRepository).findAll(any(Specification.class), eq(pageable));
        verify(itemMapper).toDto(any(Item.class));

        assertEquals(expectedItemDTOList.size(), result.getTotalElements());
        assertEquals(expectedItemDTOList.get(0).id(), result.getContent().get(0).id());
        assertEquals(expectedItemDTOList.get(0).name(), result.getContent().get(0).name());
        assertEquals(expectedItemDTOList.get(0).description(), result.getContent().get(0).description());
        assertEquals(expectedItemDTOList.get(0).price(), result.getContent().get(0).price());
        assertEquals(expectedItemDTOList.get(0).imageSrc(), result.getContent().get(0).imageSrc());
        assertEquals(expectedItemDTOList.get(0).categoryId(), result.getContent().get(0).categoryId());
        assertEquals(expectedItemDTOList.get(0).categoryName(), result.getContent().get(0).categoryName());
    }

    @Test
    public void testGetItemById() {
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

        ItemDTO expectedDto = new ItemDTO(1L, "Item 1", 1L, "Category", "Description 1", BigDecimal.valueOf(123), "Image src 1");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemMapper.toDto(item)).thenReturn(expectedDto);

        ItemDTO result = itemService.getItemById(1L);

        verify(itemRepository).findById(1L);
        verify(itemMapper).toDto(any(Item.class));

        assertEquals(expectedDto.id(), result.id());
        assertEquals(expectedDto.name(), result.name());
        assertEquals(expectedDto.description(), result.description());
        assertEquals(expectedDto.categoryId(), result.categoryId());
        assertEquals(expectedDto.price(), result.price());
        assertEquals(expectedDto.imageSrc(), result.imageSrc());
        assertEquals(expectedDto.categoryName(), result.categoryName());
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

        assertEquals(expectedItemDTOList.get(0).id(), result.get(0).id());
        assertEquals(expectedItemDTOList.get(0).name(), result.get(0).name());
        assertEquals(expectedItemDTOList.get(0).description(), result.get(0).description());
        assertEquals(expectedItemDTOList.get(0).price(), result.get(0).price());
        assertEquals(expectedItemDTOList.get(0).imageSrc(), result.get(0).imageSrc());
        assertEquals(expectedItemDTOList.get(0).categoryId(), result.get(0).categoryId());
        assertEquals(expectedItemDTOList.get(0).categoryName(), result.get(0).categoryName());

        assertEquals(expectedItemDTOList.get(1).id(), result.get(1).id());
        assertEquals(expectedItemDTOList.get(1).name(), result.get(1).name());
        assertEquals(expectedItemDTOList.get(1).description(), result.get(1).description());
        assertEquals(expectedItemDTOList.get(1).price(), result.get(1).price());
        assertEquals(expectedItemDTOList.get(1).imageSrc(), result.get(1).imageSrc());
        assertEquals(expectedItemDTOList.get(1).categoryId(), result.get(1).categoryId());
        assertEquals(expectedItemDTOList.get(1).categoryName(), result.get(1).categoryName());
    }

    @Test
    public void testGetItemWithCategoryInfo() {
        List<Item> itemList = createItemList();
        List<ItemDTO> expectedItemDTOList = createItemDTOList();

        Page<Item> itemPage = new PageImpl<>(itemList);

        when(itemRepository.findByCategoryId(1L, Pageable.unpaged())).thenReturn(itemPage);
        when(itemMapper.toDto(any(Item.class))).thenAnswer(invocationOnMock -> {
            Item item = invocationOnMock.getArgument(0);
            Long id = item.getId();
            return expectedItemDTOList.stream()
                    .filter(dto -> dto.id().equals(id))
                    .findFirst()
                    .orElse(null);
        });

        Page<ItemDTO> result = itemService.getItemsInCategory(1L, Pageable.unpaged());

        verify(itemRepository).findByCategoryId(1L, Pageable.unpaged());
        verify(itemMapper, times(2)).toDto(any(Item.class));

        assertEquals(expectedItemDTOList.get(0).id(), result.getContent().get(0).id());
        assertEquals(expectedItemDTOList.get(0).name(), result.getContent().get(0).name());
        assertEquals(expectedItemDTOList.get(0).description(), result.getContent().get(0).description());
        assertEquals(expectedItemDTOList.get(0).price(), result.getContent().get(0).price());
        assertEquals(expectedItemDTOList.get(0).imageSrc(), result.getContent().get(0).imageSrc());
        assertEquals(expectedItemDTOList.get(0).categoryId(), result.getContent().get(0).categoryId());
        assertEquals(expectedItemDTOList.get(0).categoryName(), result.getContent().get(0).categoryName());

        assertEquals(expectedItemDTOList.get(1).id(), result.getContent().get(1).id());
        assertEquals(expectedItemDTOList.get(1).name(), result.getContent().get(1).name());
        assertEquals(expectedItemDTOList.get(1).description(), result.getContent().get(1).description());
        assertEquals(expectedItemDTOList.get(1).price(), result.getContent().get(1).price());
        assertEquals(expectedItemDTOList.get(1).imageSrc(), result.getContent().get(1).imageSrc());
        assertEquals(expectedItemDTOList.get(1).categoryId(), result.getContent().get(1).categoryId());
        assertEquals(expectedItemDTOList.get(1).categoryName(), result.getContent().get(1).categoryName());
    }

    @Test
    public void testCreateItem() {
        ItemCreateDTO itemCreateDTO = new ItemCreateDTO("Item 1",
                "Description 1",
                "Img Src 1",
                BigDecimal.valueOf(123),
                1L);

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
        itemToSave.setPrice(BigDecimal.valueOf(123));

        Item savedItem = new Item();
        savedItem.setId(1L);
        savedItem.setName("Item 1");
        savedItem.setDescription("Description 1");
        savedItem.setCategory(category);
        savedItem.setImageSrc("Image src 1");
        savedItem.setPrice(BigDecimal.valueOf(123));

        ItemDTO expectedItemDTO = new ItemDTO(1L,
                "Item 1",
                1L,
                "Category",
                "Description 1",
                BigDecimal.valueOf(123),
                "Img src 1");

        when(itemMapper.toEntity(itemCreateDTO)).thenReturn(itemToSave);
        when(itemRepository.existsByCategoryId(category.getId())).thenReturn(true);
        when(itemRepository.save(itemToSave)).thenReturn(savedItem);
        when(itemMapper.toDto(savedItem)).thenReturn(expectedItemDTO);

        ItemDTO result = itemService.createItem(itemCreateDTO);

        verify(itemMapper).toEntity(itemCreateDTO);
        verify(itemRepository).existsByCategoryId(category.getId());
        verify(itemRepository).save(itemToSave);
        verify(itemMapper).toDto(savedItem);

        assertEquals(expectedItemDTO.id(), result.id());
        assertEquals(expectedItemDTO.name(), result.name());
        assertEquals(expectedItemDTO.description(), result.description());
        assertEquals(expectedItemDTO.categoryId(), result.categoryId());
        assertEquals(expectedItemDTO.categoryName(), result.categoryName());
        assertEquals(expectedItemDTO.imageSrc(), result.imageSrc());
    }

    @Test
    public void testUpdateItem() {
        ItemUpdateDTO itemUpdateDTO = new ItemUpdateDTO(1L,
                "Updated Item",
                "Updated Description",
                "Updated ImgSrc",
                BigDecimal.valueOf(123),
                1L);

        Category category = new Category();
        category.setId(1L);
        category.setName("Category");
        category.setDescription("Description");

        Item updatedItem = new Item();
        updatedItem.setId(1L);
        updatedItem.setName("Updated Item");
        updatedItem.setDescription("Updated Description");
        updatedItem.setImageSrc("Updated ImgSrc");
        updatedItem.setPrice(BigDecimal.valueOf(123));
        updatedItem.setCategory(category);

        ItemDTO expectedItemDTO = new ItemDTO(1L,
                "Updated item",
                1L,
                "Category",
                "Updated Description",
                BigDecimal.valueOf(123),
                "Updated ImgSrc");

        when(itemRepository.existsById(itemUpdateDTO.id())).thenReturn(true);
        when(itemRepository.existsByCategoryId(category.getId())).thenReturn(true);
        when(itemRepository.save(any())).thenReturn(updatedItem);
        when(itemMapper.toEntity(itemUpdateDTO)).thenReturn(updatedItem);
        when(itemMapper.toDto(updatedItem)).thenReturn(expectedItemDTO);

        ItemDTO result = itemService.updateItem(itemUpdateDTO);

        verify(itemRepository).existsById(itemUpdateDTO.id());
        verify(itemRepository).existsByCategoryId(category.getId());
        verify(itemRepository).save(any());
        verify(itemMapper).toEntity(itemUpdateDTO);
        verify(itemMapper).toDto(updatedItem);

        assertEquals(expectedItemDTO.id(), result.id());
        assertEquals(expectedItemDTO.name(), result.name());
        assertEquals(expectedItemDTO.description(), result.description());
        assertEquals(expectedItemDTO.categoryId(), result.categoryId());
        assertEquals(expectedItemDTO.categoryName(), result.categoryName());
        assertEquals(expectedItemDTO.imageSrc(), result.imageSrc());
        assertEquals(expectedItemDTO.price(), result.price());
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
                name,description,categoryId,imageSrc,price
                Item1,Description1,1,Image src 1,123
                Item2,Description2,1,Image src 2,123
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

        assertEquals(expectedItemDTOList.get(0).id(), result.get(0).id());
        assertEquals(expectedItemDTOList.get(0).name(), result.get(0).name());
        assertEquals(expectedItemDTOList.get(0).description(), result.get(0).description());
        assertEquals(expectedItemDTOList.get(0).price(), result.get(0).price());
        assertEquals(expectedItemDTOList.get(0).categoryId(), result.get(0).categoryId());
        assertEquals(expectedItemDTOList.get(0).categoryName(), result.get(0).categoryName());
        assertEquals(expectedItemDTOList.get(0).imageSrc(), result.get(0).imageSrc());
        assertEquals(expectedItemDTOList.get(0).price(), result.get(0).price());

        assertEquals(expectedItemDTOList.get(1).id(), result.get(1).id());
        assertEquals(expectedItemDTOList.get(1).name(), result.get(1).name());
        assertEquals(expectedItemDTOList.get(1).description(), result.get(1).description());
        assertEquals(expectedItemDTOList.get(1).price(), result.get(1).price());
        assertEquals(expectedItemDTOList.get(1).categoryId(), result.get(1).categoryId());
        assertEquals(expectedItemDTOList.get(1).categoryName(), result.get(1).categoryName());
        assertEquals(expectedItemDTOList.get(1).imageSrc(), result.get(1).imageSrc());
        assertEquals(expectedItemDTOList.get(1).price(), result.get(1).price());
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
                    .filter(dto -> dto.id().equals(id))
                    .findFirst()
                    .orElse(null);
        });

        Page<ItemDTO> result = itemService.getItemsInCategory(1L, pageable);

        verify(itemRepository).findByCategoryId(any(Long.class), any(Pageable.class));
        verify(itemMapper, times(2)).toDto(any(Item.class));

        assertEquals(2, result.getTotalElements());

        assertEquals(expectedItemDTOList.get(0).id(), result.getContent().get(0).id());
        assertEquals(expectedItemDTOList.get(0).name(), result.getContent().get(0).name());
        assertEquals(expectedItemDTOList.get(0).description(), result.getContent().get(0).description());
        assertEquals(expectedItemDTOList.get(0).price(), result.getContent().get(0).price());
        assertEquals(expectedItemDTOList.get(0).categoryId(), result.getContent().get(0).categoryId());
        assertEquals(expectedItemDTOList.get(0).categoryName(), result.getContent().get(0).categoryName());
        assertEquals(expectedItemDTOList.get(0).imageSrc(), result.getContent().get(0).imageSrc());

        assertEquals(expectedItemDTOList.get(1).id(), result.getContent().get(1).id());
        assertEquals(expectedItemDTOList.get(1).name(), result.getContent().get(1).name());
        assertEquals(expectedItemDTOList.get(1).description(), result.getContent().get(1).description());
        assertEquals(expectedItemDTOList.get(1).price(), result.getContent().get(1).price());
        assertEquals(expectedItemDTOList.get(1).categoryId(), result.getContent().get(1).categoryId());
        assertEquals(expectedItemDTOList.get(1).categoryName(), result.getContent().get(1).categoryName());
        assertEquals(expectedItemDTOList.get(1).imageSrc(), result.getContent().get(1).imageSrc());
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

        assertThrows(DataNotFoundException.class, () -> itemService.getItemById(id));
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
        ItemCreateDTO itemCreateDTO = new ItemCreateDTO("", "", "", null, 123L);


        when(itemRepository.existsByCategoryId(itemCreateDTO.categoryId())).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> itemService.createItem(itemCreateDTO));

        verify(itemRepository).existsByCategoryId(itemCreateDTO.categoryId());
    }


    @Test
    public void testUpdateItem_WhenItemDoesNotExist_ShouldThrowException() {
        ItemUpdateDTO itemUpdateDTO = new ItemUpdateDTO(123L,
                null,
                null,
                null,
                null, 123L);

        when(itemRepository.existsById(itemUpdateDTO.id())).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> itemService.updateItem(itemUpdateDTO));

        verify(itemRepository).existsById(itemUpdateDTO.id());
    }

    @Test
    public void testUpdateItem_WhenCategoryDoesNotExist_ShouldThrowException() {
        ItemUpdateDTO itemUpdateDTO = new ItemUpdateDTO(123L,
                null,
                null,
                null,
                null, 123L);

        when(itemRepository.existsById(itemUpdateDTO.id())).thenReturn(true);
        when(itemRepository.existsByCategoryId(itemUpdateDTO.categoryId())).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> itemService.updateItem(itemUpdateDTO));

        verify(itemRepository).existsById(itemUpdateDTO.id());
        verify(itemRepository).existsByCategoryId(itemUpdateDTO.categoryId());
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
        item1.setPrice(BigDecimal.valueOf(123));
        item1.setCategory(category);
        item1.setImageSrc("Image src 1");

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Item 2");
        item2.setDescription("Description 2");
        item2.setPrice(BigDecimal.valueOf(123));
        item2.setCategory(category);
        item2.setImageSrc("Image src 2");

        itemList.add(item1);
        itemList.add(item2);

        return itemList;
    }

    private List<ItemDTO> createItemDTOList() {
        List<ItemDTO> itemDTOList = new ArrayList<>();

        ItemDTO itemDTO1 = new ItemDTO(1L,
                "Item 1",
                1L,
                "Category",
                "Description 1",
                BigDecimal.valueOf(123),
                "Image Src 1");

        ItemDTO itemDTO2 = new ItemDTO(2L,
                "Item 2",
                1L,
                "Category",
                "Description 2",
                BigDecimal.valueOf(123),
                "Image Src 2");

        itemDTOList.add(itemDTO1);
        itemDTOList.add(itemDTO2);

        return itemDTOList;
    }
}