package serviceTests;

import com.alex.eshop.dto.itemDTOs.ItemCreateDTO;
import com.alex.eshop.dto.itemDTOs.ItemDTO;
import com.alex.eshop.dto.itemDTOs.ItemUpdateDTO;
import com.alex.eshop.entity.Category;
import com.alex.eshop.entity.Item;
import com.alex.eshop.exception.DataNotFoundException;
import com.alex.eshop.mapper.ItemMapper;
import com.alex.eshop.repository.ItemRepository;
import com.alex.eshop.service.ItemService;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        Category category = new Category();
        category.setId(1L);
        category.setName("Category");
        category.setDescription("Description");

        List<Item> itemList = new ArrayList<>();

        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item 1");
        item1.setDescription("Description 1");
        item1.setImageSrc("Image src 1");
        item1.setCategory(category);

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Item 2");
        item2.setDescription("Description");
        item2.setImageSrc("Image src 2");
        item2.setCategory(category);

        itemList.add(item1);
        itemList.add(item2);

        List<ItemDTO> expectedItemDTOList = new ArrayList<>();

        ItemDTO itemDTO1 = new ItemDTO();
        itemDTO1.setId(1L);
        itemDTO1.setName("Item 1");
        itemDTO1.setDescription("Description 1");
        itemDTO1.setImageSrc("Image src 1");
        itemDTO1.setCategoryId(1L);
        itemDTO1.setCategoryName("Category");

        ItemDTO itemDTO2 = new ItemDTO();
        itemDTO2.setId(2L);
        itemDTO2.setName("Item 2");
        itemDTO2.setDescription("Description 2");
        itemDTO2.setImageSrc("Image src 2");
        itemDTO2.setCategoryId(1L);
        itemDTO2.setCategoryName("Category");

        expectedItemDTOList.add(itemDTO1);
        expectedItemDTOList.add(itemDTO2);

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

        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(expectedDto.getName(), result.getName());
        assertEquals(expectedDto.getDescription(), result.getDescription());
        assertEquals(expectedDto.getCategoryId(), result.getCategoryId());
        assertEquals(expectedDto.getImageSrc(), result.getImageSrc());
        assertEquals(expectedDto.getCategoryName(), result.getCategoryName());
    }

    @Test
    public void testGetLastFiveItems() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Category");
        category.setDescription("Description");

        List<Item> itemList = new ArrayList<>();
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item1");
        item1.setDescription("Description1");
        item1.setCategory(category);
        item1.setImageSrc("ImgSrc1");

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Item2");
        item2.setDescription("Description2");
        item2.setCategory(category);
        item2.setImageSrc("ImgSrc2");

        itemList.add(item2);
        itemList.add(item1);

        Page<Item> itemsPage = new PageImpl<>(itemList);

        List<ItemDTO> expectedItemDTOList = new ArrayList<>();

        ItemDTO itemDTO1 = new ItemDTO();
        itemDTO1.setId(1L);
        itemDTO1.setName("Item1");
        itemDTO1.setDescription("Description1");
        itemDTO1.setCategoryId(1L);
        itemDTO1.setCategoryName("Category");
        itemDTO1.setImageSrc("ImgSrc1");

        ItemDTO itemDTO2 = new ItemDTO();
        itemDTO2.setId(2L);
        itemDTO2.setName("Item2");
        itemDTO2.setDescription("Description2");
        itemDTO2.setCategoryId(1L);
        itemDTO2.setCategoryName("Category");
        itemDTO2.setImageSrc("ImgSrc2");

        expectedItemDTOList.add(itemDTO2);
        expectedItemDTOList.add(itemDTO1);

        when(itemRepository.findAll(PageRequest.of(
                0, 5, Sort.Direction.DESC, "id"))).thenReturn(itemsPage);
        when(itemMapper.toDto(itemList)).thenReturn(expectedItemDTOList);

        List<ItemDTO> result = itemService.getLastFiveItems();

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
        category.setName("Category");
        category.setDescription("Description");

        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("Description");
        item.setImageSrc("ImgSrc");
        item.setCategory(category);

        ItemDTO expectedItemDTO = new ItemDTO();
        expectedItemDTO.setId(1L);
        expectedItemDTO.setName("item");
        expectedItemDTO.setDescription("Description");
        expectedItemDTO.setCategoryId(1L);
        expectedItemDTO.setCategoryName("Category");
        expectedItemDTO.setImageSrc("ImgSrc");

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemMapper.toDto(any(Item.class))).thenReturn(expectedItemDTO);

        ItemDTO result = itemService.getItemWithCategoryInfo(1L);

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
        itemCreateDTO.setName("Item");
        itemCreateDTO.setDescription("Description");
        itemCreateDTO.setImageSrc("Image src");
        itemCreateDTO.setCategoryId(1L);

        Category category = new Category();
        category.setId(1L);
        category.setName("Category");
        category.setDescription("Description");

        Item itemToSave = new Item();
        itemToSave.setId(1L);
        itemToSave.setName("Item");
        itemToSave.setDescription("Description");
        itemToSave.setCategory(category);

        Item savedItem = new Item();
        savedItem.setId(1L);
        savedItem.setName("Item");
        savedItem.setDescription("Description");
        savedItem.setCategory(category);
        savedItem.setImageSrc("Image src");

        ItemDTO expectedItemDTO = new ItemDTO();
        expectedItemDTO.setId(1L);
        expectedItemDTO.setName("Item");
        expectedItemDTO.setDescription("Description");
        expectedItemDTO.setCategoryId(1L);
        expectedItemDTO.setCategoryName("Category");
        expectedItemDTO.setImageSrc("Image src");

        when(itemMapper.toEntity(itemCreateDTO)).thenReturn(itemToSave);
        when(itemRepository.existsByCategoryId(category.getId())).thenReturn(true);
        when(itemRepository.save(itemToSave)).thenReturn(savedItem);
        when(itemMapper.toDto(savedItem)).thenReturn(expectedItemDTO);

        ItemDTO result = itemService.createItem(itemCreateDTO);

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

        Item item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setDescription("Description");
        item.setImageSrc("ImgSrc");
        item.setCategory(category);

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

        assertEquals(expectedItemDTO.getId(), result.getId());
        assertEquals(expectedItemDTO.getName(), result.getName());
        assertEquals(expectedItemDTO.getDescription(), result.getDescription());

    }

    @Test
    public void testDeleteItem() {
        Long itemIdToDelete = 1L;

        Category category = new Category();
        category.setId(1L);
        category.setName("Category");
        category.setDescription("Description");

        Item existingItem = new Item();
        existingItem.setId(itemIdToDelete);
        existingItem.setName("Item");
        existingItem.setDescription("Description");
        existingItem.setCategory(category);
        existingItem.setImageSrc("Image src");

        when(itemRepository.existsById(itemIdToDelete)).thenReturn(true);

        itemService.deleteItem(itemIdToDelete);

        verify(itemRepository).deleteById(itemIdToDelete);
    }

    @Test
    public void testUploadItems() {
        String csvContent = """
                name,description,categoryId,imageSrc
                Item1,Description1,1,ImgSrc1
                Item2,Description2,1,ImgSrc2
                Item3,Description3,1,ImgSrc3""";

        MultipartFile csvFile = new MockMultipartFile("csvFile",
                "valid.csv",
                "text/csv",
                csvContent.getBytes());

        Category category = new Category();
        category.setId(1L);
        category.setName("Category");
        category.setDescription("Description");

        List<Item> itemList = new ArrayList<>();

        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item1");
        item1.setDescription("Description1");
        item1.setImageSrc("ImageSrc1");
        item1.setCategory(category);

        Item item2 = new Item();
        item1.setId(2L);
        item1.setName("Item2");
        item1.setDescription("Description2");
        item1.setImageSrc("ImageSrc2");
        item1.setCategory(category);

        Item item3 = new Item();
        item1.setId(3L);
        item1.setName("Item3");
        item1.setDescription("Description3");
        item1.setImageSrc("ImageSrc3");
        item1.setCategory(category);

        itemList.add(item1);
        itemList.add(item2);
        itemList.add(item3);

        List<ItemCreateDTO> itemCreateDTOList = new ArrayList<>();

        ItemCreateDTO itemCreateDTO1 = new ItemCreateDTO();
        itemCreateDTO1.setName("Item1");
        itemCreateDTO1.setDescription("Description1");
        itemCreateDTO1.setCategoryId(1L);
        itemCreateDTO1.setImageSrc("ImgSrc1");

        ItemCreateDTO itemCreateDTO2 = new ItemCreateDTO();
        itemCreateDTO2.setName("Item2");
        itemCreateDTO2.setDescription("Description2");
        itemCreateDTO2.setCategoryId(1L);
        itemCreateDTO2.setImageSrc("ImgSrc2");

        ItemCreateDTO itemCreateDTO3 = new ItemCreateDTO();
        itemCreateDTO3.setName("Item3");
        itemCreateDTO3.setDescription("Description3");
        itemCreateDTO3.setCategoryId(1L);
        itemCreateDTO3.setImageSrc("ImgSrc3");

        itemCreateDTOList.add(itemCreateDTO1);
        itemCreateDTOList.add(itemCreateDTO2);
        itemCreateDTOList.add(itemCreateDTO3);

        List<ItemDTO> expectedItemDTOList = new ArrayList<>();

        ItemDTO itemDTO1 = new ItemDTO();
        itemDTO1.setId(1L);
        itemDTO1.setName("Item1");
        itemDTO1.setDescription("Description1");
        itemDTO1.setCategoryId(1L);
        itemDTO1.setCategoryName("Category");
        itemDTO1.setImageSrc("ImgSrc1");

        ItemDTO itemDTO2 = new ItemDTO();
        itemDTO2.setId(2L);
        itemDTO2.setName("Item2");
        itemDTO2.setDescription("Description2");
        itemDTO2.setCategoryId(1L);
        itemDTO2.setCategoryName("Category");
        itemDTO2.setImageSrc("ImgSrc2");

        ItemDTO itemDTO3 = new ItemDTO();
        itemDTO3.setId(3L);
        itemDTO3.setName("Item3");
        itemDTO3.setDescription("Description3");
        itemDTO3.setCategoryId(1L);
        itemDTO3.setCategoryName("Category");
        itemDTO3.setImageSrc("ImgSrc3");

        expectedItemDTOList.add(itemDTO1);
        expectedItemDTOList.add(itemDTO2);
        expectedItemDTOList.add(itemDTO3);

//        when(itemMapper.toEntity(itemCreateDTOList)).thenReturn(itemList);
        when(itemRepository.saveAll(any())).thenReturn(itemList);
        when(itemMapper.toDto(itemList)).thenReturn(expectedItemDTOList);

        List<ItemDTO> result = itemService.uploadItemsFromCsv(csvFile);

        assertEquals(expectedItemDTOList.get(0).getId(), result.get(0).getId());
        assertEquals(expectedItemDTOList.get(0).getName(), result.get(0).getName());
        assertEquals(expectedItemDTOList.get(0).getDescription(), result.get(0).getName());
        assertEquals(expectedItemDTOList.get(0).getCategoryId(), result.get(0).getCategoryId());
        assertEquals(expectedItemDTOList.get(0).getCategoryName(), result.get(0).getCategoryName());
        assertEquals(expectedItemDTOList.get(0).getImageSrc(), result.get(0).getImageSrc());


        assertEquals(expectedItemDTOList.get(1).getId(), result.get(1).getId());
        assertEquals(expectedItemDTOList.get(1).getName(), result.get(1).getName());
        assertEquals(expectedItemDTOList.get(1).getDescription(), result.get(1).getName());
        assertEquals(expectedItemDTOList.get(1).getCategoryId(), result.get(1).getCategoryId());
        assertEquals(expectedItemDTOList.get(1).getCategoryName(), result.get(1).getCategoryName());
        assertEquals(expectedItemDTOList.get(1).getImageSrc(), result.get(1).getImageSrc());

        assertEquals(expectedItemDTOList.get(2).getId(), result.get(2).getId());
        assertEquals(expectedItemDTOList.get(2).getName(), result.get(2).getName());
        assertEquals(expectedItemDTOList.get(2).getDescription(), result.get(2).getName());
        assertEquals(expectedItemDTOList.get(2).getCategoryId(), result.get(2).getCategoryId());
        assertEquals(expectedItemDTOList.get(2).getCategoryName(), result.get(2).getCategoryName());
        assertEquals(expectedItemDTOList.get(2).getImageSrc(), result.get(2).getImageSrc());
    }

    @Test
    public void testGetItemsInCategory() {
        Category category = new Category();

        category.setId(1L);
        category.setName("Category");
        category.setDescription("Description");

        List<Item> itemList = new ArrayList<>();
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item1");
        item1.setDescription("Description1");
        item1.setCategory(category);
        item1.setImageSrc("ImgSrc1");

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Item2");
        item2.setDescription("Description2");
        item2.setCategory(category);
        item2.setImageSrc("ImgSrc2");

        itemList.add(item1);
        itemList.add(item2);

        List<ItemDTO> expectedItemDTOList = new ArrayList<>();
        ItemDTO itemDTO1 = new ItemDTO();
        itemDTO1.setId(1L);
        itemDTO1.setName("Item1");
        itemDTO1.setDescription("Description1");
        itemDTO1.setCategoryId(1L);
        itemDTO1.setCategoryName("Category");
        itemDTO1.setImageSrc("ImgSrc1");

        ItemDTO itemDTO2 = new ItemDTO();
        itemDTO2.setId(2L);
        itemDTO2.setName("Item2");
        itemDTO2.setDescription("Description2");
        itemDTO2.setCategoryId(1L);
        itemDTO2.setCategoryName("Category");
        itemDTO2.setImageSrc("ImgSrc2");

        expectedItemDTOList.add(itemDTO1);
        expectedItemDTOList.add(itemDTO2);

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

        Page<ItemDTO> result = itemService.getItemsInCategory(category.getId(), pageable);

        assertEquals(2, result.getTotalElements());

        assertEquals(expectedItemDTOList.get(0).getId(), result.getContent().get(0).getId());
        assertEquals(expectedItemDTOList.get(0).getName(), result.getContent().get(0).getName());
        assertEquals(expectedItemDTOList.get(0).getDescription(), result.getContent().get(0).getName());
        assertEquals(expectedItemDTOList.get(0).getCategoryId(), result.getContent().get(0).getCategoryId());
        assertEquals(expectedItemDTOList.get(0).getCategoryName(), result.getContent().get(0).getCategoryName());
        assertEquals(expectedItemDTOList.get(0).getImageSrc(), result.getContent().get(0).getImageSrc());

        assertEquals(expectedItemDTOList.get(1).getId(), result.getContent().get(1).getId());
        assertEquals(expectedItemDTOList.get(1).getName(), result.getContent().get(1).getName());
        assertEquals(expectedItemDTOList.get(1).getDescription(), result.getContent().get(1).getName());
        assertEquals(expectedItemDTOList.get(1).getCategoryId(), result.getContent().get(1).getCategoryId());
        assertEquals(expectedItemDTOList.get(1).getCategoryName(), result.getContent().get(1).getCategoryName());
        assertEquals(expectedItemDTOList.get(1).getImageSrc(), result.getContent().get(1).getImageSrc());
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

        assertEquals(expectedItemDTOList.size(), result.getTotalElements());
        assertEquals(expectedItemDTOList.get(0).getId(), result.getContent().get(0).getId());
        assertEquals(expectedItemDTOList.get(0).getName(), result.getContent().get(0).getName());
        assertEquals(expectedItemDTOList.get(0).getDescription(), result.getContent().get(0).getDescription());
        assertEquals(expectedItemDTOList.get(0).getImageSrc(), result.getContent().get(0).getImageSrc());
        assertEquals(expectedItemDTOList.get(0).getCategoryId(), result.getContent().get(0).getCategoryId());
        assertEquals(expectedItemDTOList.get(0).getCategoryName(), result.getContent().get(0).getCategoryName());


    }

    @Test
    public void testGetItem_Exception() {
        Long id = 1L;

        when(itemRepository.findById(id)).thenThrow(new DataNotFoundException("There is no item with id 1"));

        assertThrows(DataNotFoundException.class, () -> itemService.getItemWithCategoryInfo(id));
    }

    @Test
    public void testCreateItem_Exception() {
        ItemCreateDTO itemCreateDTO = new ItemCreateDTO();
        itemCreateDTO.setCategoryId(123L);

        when(itemRepository.existsByCategoryId(itemCreateDTO.getCategoryId())).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> itemService.createItem(itemCreateDTO));

    }

    @Test
    public void testUpdateItem_ExceptionOfItemId() {
        ItemUpdateDTO itemUpdateDTO = new ItemUpdateDTO();
        itemUpdateDTO.setId(123L);
        itemUpdateDTO.setCategoryId(123L);

        when(itemRepository.existsById(itemUpdateDTO.getId())).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> itemService.updateItem(itemUpdateDTO));
    }

    @Test
    public void testUpdateItem_ExceptionOfCategoryId() {
        ItemUpdateDTO itemUpdateDTO = new ItemUpdateDTO();
        itemUpdateDTO.setId(123L);
        itemUpdateDTO.setCategoryId(123L);

        when(itemRepository.existsById(itemUpdateDTO.getId())).thenReturn(true);
        when(itemRepository.existsByCategoryId(itemUpdateDTO.getCategoryId())).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> itemService.updateItem(itemUpdateDTO));
    }

    @Test
    public void testDeleteItem_Exception() {
        Long id = 123L;

        when(itemRepository.existsById(id)).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> itemService.deleteItem(id));
    }

    @Test
    public void testSearchItem_Exception() {
        String searchName = "Non-item";
        Boolean hasImage = true;
        Long categoryId = 123L;

        Pageable pageable = PageRequest.of(0, 5);

        Page<Item> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(itemRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(emptyPage);

        assertThrows(DataNotFoundException.class, () ->
                itemService.searchItems(pageable, searchName, hasImage, categoryId));
    }
}