package com.alex.eshop.eshop.Service;

import com.alex.eshop.eshop.Entity.ItemEntity;

import java.util.List;

public interface ItemService {
    List<ItemEntity> allItemService();

    ItemEntity getItem(int id);


}
