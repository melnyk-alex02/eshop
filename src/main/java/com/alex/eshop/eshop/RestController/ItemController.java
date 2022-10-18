package com.alex.eshop.eshop.RestController;

import com.alex.eshop.eshop.DTO.ItemDTO;
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
    public List<ItemDTO> lastFiveItems() {
        return itemService.getLastFiveItems();
    }

    @GetMapping("items/{id}")
    public ItemDTO getItem(@PathVariable Long id) {
        return itemService.getItemWithCategoryInfo(id);
    }

    @GetMapping("/items")
    public List<ItemDTO> getItemWithCategory(@RequestParam Long categoryId) {
        return itemService.getItemsInCategory(categoryId);
    }
}
