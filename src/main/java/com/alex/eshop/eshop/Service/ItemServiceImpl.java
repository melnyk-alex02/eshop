package com.alex.eshop.eshop.Service;

import com.alex.eshop.eshop.Entity.Item;
import com.alex.eshop.eshop.Exception.DataNotFound;
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
    public Item getItemWithCategoryInfo(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new DataNotFound("There are no item with id " + id));
    }

    @Override
    public List<Item> getLastFiveItems() {
        return itemRepository.findAll(PageRequest.of(0,5, Sort.Direction.DESC, "id")).getContent();
    }

    @Override
    public List<Item> getItemsInCategory(Long categoryId) {
        return itemRepository.getItemsByCategory_Id(categoryId);
    }
}
