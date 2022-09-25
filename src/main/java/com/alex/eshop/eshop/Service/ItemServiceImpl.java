package com.alex.eshop.eshop.Service;

import com.alex.eshop.eshop.Entity.ItemEntity;
import com.alex.eshop.eshop.Repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Component
@Transactional
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public List<ItemEntity> allItemService() {
        return itemRepository.findAll();
    }

    @Override
    public ItemEntity getItem(int id) {
       ItemEntity itemEntity = null;
       Optional<ItemEntity> itemEntityOptional = itemRepository.findById(id);
        if(itemEntityOptional.isPresent()){
            itemEntity = itemEntityOptional.get();
        }
        return itemEntity;
    }

    public ItemServiceImpl(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }
}
