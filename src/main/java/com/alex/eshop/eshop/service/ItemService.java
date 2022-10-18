package com.alex.eshop.eshop.service;

import com.alex.eshop.eshop.dto.ItemDTO;

import java.util.List;

public interface ItemService {
    ItemDTO getItemWithCategoryInfo(Long id);

    List<ItemDTO> getLastFiveItems();

    List<ItemDTO> getItemsInCategory(Long id);
}
