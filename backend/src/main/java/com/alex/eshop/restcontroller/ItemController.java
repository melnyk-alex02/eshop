package com.alex.eshop.restcontroller;

import com.alex.eshop.constants.Role;
import com.alex.eshop.dto.itemDTOs.ItemCreateDTO;
import com.alex.eshop.dto.itemDTOs.ItemDTO;
import com.alex.eshop.dto.itemDTOs.ItemUpdateDTO;
import com.alex.eshop.service.ItemService;
import com.alex.eshop.wrapper.PageWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @GetMapping("/items/last")
    public List<ItemDTO> lastFiveItems() {
        return itemService.getLastFiveItems();
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @GetMapping("/items")
    public PageWrapper<ItemDTO> getAllItems(Pageable pageable) {
        Page<ItemDTO> itemPage = itemService.getAllItems(pageable);

        return new PageWrapper<>(itemPage.getContent(), itemPage.getTotalElements());
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @GetMapping("/items/search")
    public PageWrapper<ItemDTO> searchItems(Pageable pageable,
                                            @RequestParam("name") String name,
                                            @RequestParam(value = "hasImage", required = false) Boolean hasImage,
                                            @RequestParam(value = "categoryId", required = false) Long categoryId) {
        Page<ItemDTO> itemPage = itemService.searchItems(pageable, name, hasImage, categoryId);

        return new PageWrapper<>(itemPage.getContent(), itemPage.getTotalElements());
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @GetMapping("items/{id}")
    public ItemDTO getItemById(@PathVariable Long id) {
        return itemService.getItemById(id);
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @GetMapping("/item")
    public PageWrapper<ItemDTO> getItemInCategory(Long categoryId, Pageable pageable) {
        Page<ItemDTO> itemPage = itemService.getItemsInCategory(categoryId, pageable);

        return new PageWrapper<>(itemPage.getContent(), itemPage.getTotalElements());
    }

    @PreAuthorize("hasRole('" + Role.ADMIN + "')")
    @PostMapping("/items")
    public ItemDTO createItem(@RequestBody ItemCreateDTO itemCreateDTO) {
        return itemService.createItem(itemCreateDTO);
    }

    @PreAuthorize("hasRole('" + Role.ADMIN + "')")
    @PostMapping("/upload-items")
    public List<ItemDTO> uploadItemsFromCsv(MultipartFile file) {
        return itemService.uploadItemsFromCsv(file);
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