package com.alex.eshop.eshop.Service;

import com.alex.eshop.eshop.Entity.Item;

import java.util.List;

public interface ItemService {
    List<String> getItemWithCategoryInfo(long id);

    List<Item> lastFiveItems();
}
