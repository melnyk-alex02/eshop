package com.alex.eshop.restcontroller;

import com.alex.eshop.constants.Role;
import com.alex.eshop.dto.cartDTOs.CartDTO;
import com.alex.eshop.dto.orderDTOs.OrderDTO;
import com.alex.eshop.service.CartAndOrderCreationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cart")
public class CartController {
    private final CartAndOrderCreationService cartService;

    public CartController(CartAndOrderCreationService cartService) {
        this.cartService = cartService;
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @PostMapping
    public CartDTO addItemToCart(Long itemId) {
        return cartService.addItemToCart(itemId);
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @GetMapping("/all")
    public List<CartDTO> getAllCarts() {
        return cartService.getAllCarts();
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @PutMapping
    public CartDTO updateCountOfItem(Long itemId, Integer count) {
        return cartService.updateCountOfItem(itemId, count);
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @DeleteMapping("/{itemId}")
    public void deleteCartByItemId(@PathVariable Long itemId) {
        cartService.deleteItemFromCart(itemId);
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @PostMapping("/create-order")
    public OrderDTO createOrderFromCart() {
        return cartService.createOrderFromCart();
    }
}