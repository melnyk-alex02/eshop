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
import lombok.RequiredArgsConstructor;
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
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public Page<ItemDTO> getAllItems(Pageable pageable) {
        return itemRepository.findAll(pageable).map(itemMapper::toDto);
    }

    public Page<ItemDTO> searchItems(Pageable pageable, String name, Boolean hasImage, Long categoryId) {
        Specification<Item> itemSpecification = ItemSpecification.hasNameContaining(name)
                .and(ItemSpecification.hasImage(hasImage))
                .and(ItemSpecification.hasCategoryId(categoryId));

        return itemRepository.findAll(itemSpecification, pageable).map(itemMapper::toDto);

    }

    public ItemDTO getItemById(Long id) {
        return itemMapper.toDto(itemRepository.findById(id).orElseThrow(() -> new DataNotFoundException("There is no item with id " + id)));
    }

    public List<ItemDTO> getLastFiveItems() {
        return itemMapper.toDto(itemRepository.findAll(PageRequest.of(0, 5, Sort.Direction.DESC, "id")).getContent());
    }

    public Page<ItemDTO> getItemsInCategory(Long categoryId, Pageable pageable) {
        return itemRepository.findByCategoryId(categoryId, pageable).map(itemMapper::toDto);
    }

    public ItemDTO createItem(ItemCreateDTO itemCreateDTO) {
        if (!itemRepository.existsByCategoryId(itemCreateDTO.categoryId())) {
            throw new DataNotFoundException("There is no category with id " + itemCreateDTO.categoryId());
        }
        return itemMapper.toDto(itemRepository.save(itemMapper.toEntity(itemCreateDTO)));
    }

    public List<ItemDTO> uploadItemsFromCsv(MultipartFile file) {
        FormatChecker.isCsv(file);

        String[] headers = {"name", "description", "categoryId", "imageSrc", "price"};

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
                ItemCreateDTO itemCreateDTO = new ItemCreateDTO(
                        csvRecord.get("name"),
                        csvRecord.get("description"),
                        csvRecord.get("imageSrc"),
                        new BigDecimal(csvRecord.get("price")),
                        Long.parseLong(csvRecord.get("categoryId"))
                );
                itemCreateDTOList.add(itemCreateDTO);
            }
            return itemMapper.toDto(itemRepository.saveAll(itemMapper.toEntity(itemCreateDTOList)));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ItemDTO updateItem(ItemUpdateDTO itemUpdateDTO) {
        if (!itemRepository.existsById(itemUpdateDTO.id())) {
            throw new DataNotFoundException("There is no item with id " + itemUpdateDTO.id());
        }
        if (!itemRepository.existsByCategoryId(itemUpdateDTO.categoryId())) {
            throw new DataNotFoundException("There is no category with id " + itemUpdateDTO.categoryId());
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