package com.alex.eshop.restcontroller;

import com.alex.eshop.dto.ItemCreateDTO;
import com.alex.eshop.dto.ItemDTO;
import com.alex.eshop.dto.ItemUpdateDTO;
import com.alex.eshop.service.ItemService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/items/last")
    public List<ItemDTO> lastFiveItems() {
        return itemService.getLastFiveItems();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("items/{id}")
    public ItemDTO getItem(@PathVariable Long id) {
        return itemService.getItemWithCategoryInfo(id);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/items")
    public List<ItemDTO> getItemWithCategory(@RequestParam("categoryId") Long categoryId,
                                             Pageable pageable) {
        return itemService.getItemsInCategory(categoryId, pageable);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/items")
    public ItemDTO createItem(@RequestBody ItemCreateDTO itemCreateDTO) {
        return itemService.createItem(itemCreateDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/items")
    public ItemDTO updateItem(@RequestBody ItemUpdateDTO itemUpdateDTO) {
        return itemService.updateItem(itemUpdateDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/items/{id}")
    public void deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
    }
}
