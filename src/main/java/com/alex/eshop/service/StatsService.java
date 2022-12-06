package com.alex.eshop.service;

import com.alex.eshop.dto.StatsDTO;
import com.alex.eshop.repository.ItemRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class StatsService {
    private final ItemRepository itemRepository;
    public StatsService(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }
    public List<StatsDTO> getStats(){
        return itemRepository.getStats();
    }
}
