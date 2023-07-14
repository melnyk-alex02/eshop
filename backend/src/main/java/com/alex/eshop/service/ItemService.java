package com.alex.eshop.service;

import com.alex.eshop.dto.ItemCreateDTO;
import com.alex.eshop.dto.ItemDTO;
import com.alex.eshop.dto.ItemUpdateDTO;
import com.alex.eshop.exception.DataNotFoundException;
import com.alex.eshop.mapper.ItemMapper;
import com.alex.eshop.repository.ItemRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    public Page<ItemDTO> getAllItems(Pageable pageable){
        return itemRepository.findAll(pageable).map(itemMapper::toDto);
    }

    public ItemDTO getItemWithCategoryInfo(Long id) {
        return itemMapper.toDto(itemRepository.findById(id).orElseThrow(()->new DataNotFoundException("There is no item with id " + id)));
    }

    public List<ItemDTO> getLastFiveItems() {
        return itemMapper.toDto(itemRepository.findAll(PageRequest.of(0,5, Sort.Direction.DESC, "id")).getContent());
    }

    public Page<ItemDTO>getItemsInCategory(Long categoryId, Pageable pageable) {
        return itemRepository.findByCategoryId(categoryId, pageable).map(itemMapper::toDto);
    }
    public ItemDTO createItem(ItemCreateDTO itemCreateDTO){
        if(!itemRepository.existsByCategoryId(itemCreateDTO.getCategoryId())){
            throw new DataNotFoundException("There is no category with id " + itemCreateDTO.getCategoryId());
        }
        return itemMapper.toDto(itemRepository.save(itemMapper.toEntity(itemCreateDTO)));
    }

    public ItemDTO updateItem(ItemUpdateDTO itemUpdateDTO){
        if(!itemRepository.existsById(itemUpdateDTO.getId())) {
            throw new DataNotFoundException("There is no item with id " + itemUpdateDTO.getId());
        }
        if(!itemRepository.existsByCategoryId(itemUpdateDTO.getCategoryId())){
            throw new DataNotFoundException("There is no category with id" + itemUpdateDTO.getCategoryId());
        }
        return itemMapper.toDto(itemRepository.save(itemMapper.toEntity(itemUpdateDTO)));
    }
    public void deleteItem(Long id){
        if(!itemRepository.existsById(id)){
            throw new DataNotFoundException("There is no item with id " + id);
        }
        itemRepository.deleteById(id);
    }
}