package com.alex.eshop.service;

import com.alex.eshop.dto.ItemCreateDTO;
import com.alex.eshop.dto.ItemDTO;
import com.alex.eshop.dto.ItemUpdateDTO;
import com.alex.eshop.entity.Item;
import com.alex.eshop.exception.DataNotFound;
import com.alex.eshop.mapper.ItemCreateMapper;
import com.alex.eshop.mapper.ItemMapper;
import com.alex.eshop.mapper.ItemUpdateMapper;
import com.alex.eshop.repository.ItemRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ItemService {
    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    private final ItemCreateMapper itemCreateMapper;

    private final ItemUpdateMapper itemUpdateMapper;

    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper,
                        ItemCreateMapper itemCreateMapper, ItemUpdateMapper itemUpdateMapper) {
                       ItemCreateMapper itemCreateMapper, ItemUpdateMapper itemUpdateMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.itemCreateMapper = itemCreateMapper;
        this.itemUpdateMapper = itemUpdateMapper;
    }

    public ItemDTO getItemWithCategoryInfo(Long id) {
        return itemMapper.toDto(itemRepository.findById(id).orElseThrow(()->new DataNotFound("There is no item with id " + id)));
    }

    public List<ItemDTO> getLastFiveItems() {
        return itemMapper.toDto(itemRepository.findAll(PageRequest.of(0,5, Sort.Direction.DESC, "id")).getContent());
    }

    public List<ItemDTO> getItemsInCategory(Long categoryId, Pageable pageable) {
        return itemMapper.toDto(itemRepository.findByCategoryId(categoryId, pageable));
    }
    public Item createItem(ItemCreateDTO itemCreateDTO){
        return itemRepository.save(itemCreateMapper.toEntity(itemCreateDTO));
    }

    public void updateItem(ItemUpdateDTO itemUpdateDTO){
        ItemUpdateDTO itemUpdate = itemUpdateMapper
                .toDto(itemRepository.findById(itemUpdateDTO.getId())
                .orElseThrow(() -> new DataNotFound("There is no item with id" + itemUpdateDTO.getId())));

        itemUpdate.setName(itemUpdateDTO.getName());
        itemUpdate.setDescription(itemUpdateDTO.getDescription());
        itemUpdate.setCategoryId(itemUpdateDTO.getCategoryId());
        itemUpdate.setImageSrc(itemUpdateDTO.getImageSrc());

        itemRepository.save(itemUpdateMapper.toEntity(itemUpdate));
    }
    public void deleteItem(Long id){
        itemRepository.delete(itemRepository
                .findById(id).orElseThrow(() -> new DataNotFound("There is no item with id" + id)));
    }
}
