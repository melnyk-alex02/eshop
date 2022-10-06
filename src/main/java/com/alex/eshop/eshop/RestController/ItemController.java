package com.alex.eshop.eshop.RestController;

import com.alex.eshop.eshop.Entity.Item;
import com.alex.eshop.eshop.Service.ItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ItemController {
    private final ItemService itemService;
    public ItemController(ItemService itemService){
        this.itemService = itemService;
    }
    @GetMapping("/items/last")
    public List<Item> lastFiveItems(){
        return itemService.getLastFiveItems();
    }
    @GetMapping("items/{id}")
    public Item getItem(@PathVariable Long id, Item item){
        item = itemService.getItemWithCategoryInfo(id);
        return item;
    }
}
