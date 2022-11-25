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
    public List<ItemDTO> getItemWithCategory(@RequestParam ("categoryId") Long categoryId,
                                             Pageable pageable) {
        return itemService.getItemsInCategory(categoryId, pageable);
    }

    @PostMapping("/items")
    public void createItem(@RequestBody ItemDTO itemCreateDTO){
         itemService.createItem(itemCreateDTO);
    }
    @PutMapping("/items")
    public void updateItem(@RequestBody ItemDTO itemUpdateDTO){
        itemService.updateItem(itemUpdateDTO);
    }
    @DeleteMapping("/items/{id}")
    public void deleteItem(@PathVariable Long id){
        itemService.deleteItem(id);
    }
}
