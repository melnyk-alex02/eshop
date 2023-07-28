package com.alex.eshop.service;

import com.alex.eshop.dto.itemDTOs.ItemCreateDTO;
import com.alex.eshop.dto.itemDTOs.ItemDTO;
import com.alex.eshop.dto.itemDTOs.ItemUpdateDTO;
import com.alex.eshop.entity.Item;
import com.alex.eshop.exception.DataNotFoundException;
import com.alex.eshop.filterSpecifications.ItemSpecification;
import com.alex.eshop.mapper.ItemMapper;
import com.alex.eshop.repository.ItemRepository;
import com.alex.eshop.utils.CsvHeaderChecker;
import com.alex.eshop.utils.FormatChecker;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class ItemService {
    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    public Page<ItemDTO> getAllItems(Pageable pageable) {
        return itemRepository.findAll(pageable).map(itemMapper::toDto);
    }

    public List<ItemDTO> searchItems(String name, Boolean hasImage, Long categoryId) {
        Specification<Item> itemSpecification = ItemSpecification.hasNameContaining(name)
                .and(ItemSpecification.hasImage(hasImage))
                .and(ItemSpecification.hasCategoryId(categoryId));

        List<ItemDTO> itemDTOList = itemMapper.toDto(itemRepository.findAll(itemSpecification));
        if (itemDTOList.isEmpty()) {
            throw new DataNotFoundException("There are no items found with your search preferences");
        } else {
            return itemDTOList;
        }
    }

    public ItemDTO getItemWithCategoryInfo(Long id) {
        return itemMapper.toDto(itemRepository.findById(id).orElseThrow(() -> new DataNotFoundException("There is no item with id " + id)));
    }

    public List<ItemDTO> getLastFiveItems() {
        return itemMapper.toDto(itemRepository.findAll(PageRequest.of(0, 5, Sort.Direction.DESC, "id")).getContent());
    }

    public Page<ItemDTO> getItemsInCategory(Long categoryId, Pageable pageable) {
        return itemRepository.findByCategoryId(categoryId, pageable).map(itemMapper::toDto);
    }

    public ItemDTO createItem(ItemCreateDTO itemCreateDTO) {
        if (!itemRepository.existsByCategoryId(itemCreateDTO.getCategoryId())) {
            throw new DataNotFoundException("There is no category with id " + itemCreateDTO.getCategoryId());
        }
        return itemMapper.toDto(itemRepository.save(itemMapper.toEntity(itemCreateDTO)));
    }

    public List<ItemDTO> uploadItemsFromCsv(MultipartFile file) {
        FormatChecker.isCsv(file);

        String[] headers = {"name", "description", "categoryId", "imageSrc"};

        CsvHeaderChecker.checkHeaders(file, headers);

        List<ItemCreateDTO> itemCreateDTOList = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)
        );
             CSVParser csvParser = new CSVParser(
                     fileReader,
                     CSVFormat.DEFAULT.builder()
                             .setHeader(headers)
                             .setSkipHeaderRecord(true)
                             .build()
             )
        ) {
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                ItemCreateDTO itemCreateDTO = new ItemCreateDTO();

                itemCreateDTO.setName(csvRecord.get("name"));
                itemCreateDTO.setDescription(csvRecord.get("description"));
                itemCreateDTO.setCategoryId(Long.parseLong(csvRecord.get("categoryId")));
                itemCreateDTO.setImageSrc(csvRecord.get("imageSrc"));

                itemCreateDTOList.add(itemCreateDTO);
            }
            return itemMapper.toDto(itemRepository.saveAll(itemMapper.toEntity(itemCreateDTOList)));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ItemDTO updateItem(ItemUpdateDTO itemUpdateDTO) {
        if (!itemRepository.existsById(itemUpdateDTO.getId())) {
            throw new DataNotFoundException("There is no item with id " + itemUpdateDTO.getId());
        }
        if (!itemRepository.existsByCategoryId(itemUpdateDTO.getCategoryId())) {
            throw new DataNotFoundException("There is no category with id" + itemUpdateDTO.getCategoryId());
        }
        return itemMapper.toDto(itemRepository.save(itemMapper.toEntity(itemUpdateDTO)));
    }

    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new DataNotFoundException("There is no item with id " + id);
        }
        itemRepository.deleteById(id);
    }
}