package com.alex.eshop.service;

import com.alex.eshop.dto.ItemCreateDTO;
import com.alex.eshop.dto.ItemDTO;
import com.alex.eshop.dto.ItemUpdateDTO;
import com.alex.eshop.exception.DataNotFoundException;
import com.alex.eshop.exception.InvalidDataException;
import com.alex.eshop.mapper.ItemMapper;
import com.alex.eshop.repository.ItemRepository;
import com.alex.eshop.utils.FormatChecker;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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
        List<ItemCreateDTO> itemCreateDTOList = new ArrayList<>();

        String[] headers = {"name", "description", "categoryId", "imageSrc"};

        if (FormatChecker.isCsv(file)) {
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
        } else {
            throw new InvalidDataException("Unsupported file format");
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