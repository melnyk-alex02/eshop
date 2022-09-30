package com.alex.eshop.eshop.Service;

import com.alex.eshop.eshop.Entity.Category;
import com.alex.eshop.eshop.Entity.Item;
import com.alex.eshop.eshop.Repository.ItemRepository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Component
@Transactional
public class ItemServiceImpl implements ItemService {
    @PersistenceContext private EntityManager entityManager;

    private final ItemRepository itemRepository;

    @Override
    public List<String> getItemWithCategoryInfo(long id) {
        Item item = null;
        Optional<Item> categoryEntityOptional = itemRepository.findById(id);
        if(categoryEntityOptional.isPresent()){
            item = categoryEntityOptional.get();
        }
        Category category = item.getCategory();

        long categoryId = category.getId();
        String categoryName = category.getName();

        ArrayList<String> result = new ArrayList<>();
        result.add(categoryName);
        result.add(category.toString());

        return result;
    }
    @Override
    public List<Item> lastFiveItems(){
        List<Item> lastFiveItems = itemRepository.lastFiveItems();
        return lastFiveItems;
    }

    public ItemServiceImpl(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }
}
