package com.alex.eshop.eshop.RestController;

import com.alex.eshop.eshop.Entity.Item;
import com.alex.eshop.eshop.Service.ItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items/last")
    public List<Item> lastFiveItems() {
        return itemService.getLastFiveItems();
    }

    @GetMapping("items/{id}")
    public Item getItem(@PathVariable Long id) {
        return itemService.getItemWithCategoryInfo(id);
    }

    @GetMapping("/items")
    public List<Item> getItemWithCategory(@RequestParam(name = "categoryId") String categoryId) {
        return itemService.getItemsInCategory(Long.parseLong(categoryId));
    }
}
