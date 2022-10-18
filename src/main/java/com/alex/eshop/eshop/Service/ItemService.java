package com.alex.eshop.eshop.Service;

import com.alex.eshop.eshop.DTO.ItemDTO;

import java.util.List;

public interface ItemService {
    ItemDTO getItemWithCategoryInfo(Long id);

    List<ItemDTO> getLastFiveItems();

    List<ItemDTO> getItemsInCategory(Long id);
}
