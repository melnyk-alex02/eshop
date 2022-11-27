package com.alex.eshop.service;

import com.alex.eshop.dto.ItemCreateDTO;
import com.alex.eshop.dto.ItemDTO;
import com.alex.eshop.dto.ItemUpdateDTO;
import com.alex.eshop.exception.DataNotFound;
import com.alex.eshop.mapper.ItemMapper;
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

    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
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
    public ItemDTO createItem(ItemCreateDTO itemCreateDTO){
        if(!itemRepository.existsByCategoryId(itemCreateDTO.getCategoryId())){
            throw new DataNotFound("There is no category with id " + itemCreateDTO.getCategoryId());
        }
        return itemMapper.toDto(itemRepository.save(itemMapper.toEntity(itemCreateDTO)));
    }

    public ItemDTO updateItem(ItemUpdateDTO itemUpdateDTO){
        if(!itemRepository.existsById(itemUpdateDTO.getId())) {
            throw new DataNotFound("There is no item with id " + itemUpdateDTO.getId());
        }
        return itemMapper.toDto(itemRepository.save(itemMapper.toEntity(itemUpdateDTO)));
    }
    public void deleteItem(Long id){
        if(!itemRepository.existsById(id)){
            throw new DataNotFound("There is no item with id " + id);
        }
        itemRepository.deleteById(id);
    }
}
