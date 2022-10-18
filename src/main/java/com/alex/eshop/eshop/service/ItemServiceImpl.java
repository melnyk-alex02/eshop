package com.alex.eshop.eshop.service;

import com.alex.eshop.eshop.dto.ItemDTO;
import com.alex.eshop.eshop.exception.DataNotFound;
import com.alex.eshop.eshop.mapper.ItemMapper;
import com.alex.eshop.eshop.repository.ItemRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemServiceImpl(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public ItemDTO getItemWithCategoryInfo(Long id) {
        return itemMapper.toDto(itemRepository.findById(id).orElseThrow(()->new DataNotFound("There is no item with id " + id)));
    }

    @Override
    public List<ItemDTO> getLastFiveItems() {
        return itemMapper.toDto(itemRepository.findAll(PageRequest.of(0,5, Sort.Direction.DESC, "id")).getContent());
    }

    @Override
    public List<ItemDTO> getItemsInCategory(Long categoryId) {
        return itemMapper.toDto(itemRepository.findByCategoryId(categoryId));
    }
}
