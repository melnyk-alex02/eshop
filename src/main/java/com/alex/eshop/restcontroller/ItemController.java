package com.alex.eshop.restcontroller;

import com.alex.eshop.dto.ItemDTO;
import com.alex.eshop.service.ItemService;
import org.springframework.data.domain.Pageable;
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
    public ItemDTO getItem(@PathVariable Long id){
        return itemService.getItemWithCategoryInfo(id);
    }

    @GetMapping("/items")
    public List<ItemDTO> getItemWithCategory(@RequestParam Long categoryId,
                                             @RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "size", defaultValue = "10") int size,
                                             @RequestParam(value = "sort", defaultValue = "id,asc") List<String> sort,
                                             Pageable pageable) {
        return itemService.getItemsInCategory(categoryId, pageable);
    }
}
