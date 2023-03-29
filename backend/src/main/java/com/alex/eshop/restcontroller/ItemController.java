package com.alex.eshop.restcontroller;

import com.alex.eshop.constants.Role;
import com.alex.eshop.dto.ItemCreateDTO;
import com.alex.eshop.dto.ItemDTO;
import com.alex.eshop.dto.ItemUpdateDTO;
import com.alex.eshop.service.ItemService;
import org.apache.commons.collections4.iterators.IteratorEnumeration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @GetMapping("/items/last")
    public List<ItemDTO> lastFiveItems() {
        return itemService.getLastFiveItems();
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @GetMapping("/items")
    public Page<ItemDTO> getAllItems(Pageable pageable){
        return itemService.getAllItems(pageable);
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @GetMapping("items/{id}")
    public ItemDTO getItem(@PathVariable Long id) {
        return itemService.getItemWithCategoryInfo(id);
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @GetMapping("/item")
    public Page<ItemDTO> getItemWithCategory(Long categoryId,
                                             Pageable pageable) {
        return itemService.getItemsInCategory(categoryId, pageable);
    }

    @PreAuthorize("hasRole('" + Role.ADMIN + "')")
    @PostMapping("/items")
    public ItemDTO createItem(@RequestBody ItemCreateDTO itemCreateDTO) {
        return itemService.createItem(itemCreateDTO);
    }

    @PreAuthorize("hasRole('" + Role.ADMIN + "')")
    @PutMapping("/items")
    public ItemDTO updateItem(@RequestBody ItemUpdateDTO itemUpdateDTO) {
        return itemService.updateItem(itemUpdateDTO);
    }

    @PreAuthorize("hasRole('" + Role.ADMIN + "')")
    @DeleteMapping("/items/{id}")
    public void deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
    }
}
