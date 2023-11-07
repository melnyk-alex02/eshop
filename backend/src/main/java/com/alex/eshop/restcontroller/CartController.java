package com.alex.eshop.restcontroller;

import com.alex.eshop.constants.Role;
import com.alex.eshop.dto.cartDTOs.CartDTO;
import com.alex.eshop.service.CartAndOrderCreationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/cart")
public class CartController {
    private final CartAndOrderCreationService cartService;

    public CartController(CartAndOrderCreationService cartService){
        this.cartService = cartService;
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @PostMapping
    public CartDTO addItemToCart(Long itemId, Principal principal) {
        return cartService.addItemToCart(itemId, principal.getName());
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @GetMapping("/all")
    public List<CartDTO> getAllCarts(Principal principal) {
        return cartService.getAllCarts(principal.getName());
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @PutMapping
    public CartDTO updateCountOfItem(Principal principal, Long itemId, Integer count){
        return cartService.updateCountOfItem(itemId, count, principal.getName());
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @DeleteMapping("/{itemId}")
    public void deleteCartByItemId(@PathVariable Long itemId, Principal principal) {
        cartService.deleteCartByItemId(itemId, principal.getName());
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @PostMapping("/create-order")
    public void createOrderFromCart(Principal principal) {
          cartService.createOrderFromCart(principal.getName());
    }
}