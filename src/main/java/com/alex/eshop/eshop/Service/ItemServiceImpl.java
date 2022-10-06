package com.alex.eshop.eshop.Service;

import com.alex.eshop.eshop.Entity.Item;
import com.alex.eshop.eshop.ExceptionHandling.NoSuchItemException;
import com.alex.eshop.eshop.Repository.ItemRepository;

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
        Item item;
        Optional<Item> itemOptional = itemRepository.findById(id);
        if (itemOptional.isPresent()) {
            item = itemOptional.get();
        }
        else{
            throw new NoSuchItemException("There is no item with id " + id);
        }
        return item;
    }

    @Override
    public List<Item> getLastFiveItems() {
        return itemRepository.getLastFiveItems().subList(0,5);
    }
}
