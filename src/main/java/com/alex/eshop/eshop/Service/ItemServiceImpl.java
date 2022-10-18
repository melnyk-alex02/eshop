package com.alex.eshop.eshop.Service;

import com.alex.eshop.eshop.DTO.ItemDTO;
import com.alex.eshop.eshop.Exception.DataNotFound;
import com.alex.eshop.eshop.Mapper.ItemMapper;
import com.alex.eshop.eshop.Repository.ItemRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemDTO getItemWithCategoryInfo(Long id) {
        return ItemMapper.INSTANCE.toDto(itemRepository.findById(id).orElseThrow(()->new DataNotFound("There is no item with id " + id)));
    }

    @Override
    public List<ItemDTO> getLastFiveItems() {
        return ItemMapper.INSTANCE.toDto(itemRepository.findAll(PageRequest.of(0,5, Sort.Direction.DESC, "id")).getContent());
    }

    @Override
    public List<ItemDTO> getItemsInCategory(Long categoryId) {
        return ItemMapper.INSTANCE.toDto(itemRepository.findByCategoryId(categoryId));
    }
}
