package com.alex.eshop.restcontroller;

import com.alex.eshop.constants.Role;
import com.alex.eshop.dto.cartDTOs.CartItemDTO;
import com.alex.eshop.dto.orderDTOs.OrderDTO;
import com.alex.eshop.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartItemService cartService;

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @PostMapping
    public CartItemDTO addItemToCart(Long itemId) {
        return cartService.addItemToCart(itemId);
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @GetMapping
    public List<CartItemDTO> getCartByCurrentUser() {
        return cartService.getCartByCurrentUser();
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @PutMapping
    public CartItemDTO updateCountOfItem(Long itemId, Integer count) {
        return cartService.updateCountOfItem(itemId, count);
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @DeleteMapping("/{itemId}")
    public void deleteFromCartByItemId(@PathVariable Long itemId) {
        cartService.deleteFromCartByItemId(itemId);
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @PostMapping("/create-order")
    public OrderDTO createOrderFromCart() {
        return cartService.createOrderFromCart();
    }
}