package com.alex.eshop.restcontroller;

import com.alex.eshop.dto.cartDTOs.CartItemDTO;
import com.alex.eshop.dto.orderDTOs.OrderDTO;
import com.alex.eshop.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartItemService cartService;

    @PostMapping
    public CartItemDTO addItemToCart(Long itemId) {
        return cartService.addItemToCart(itemId);
    }

    @GetMapping
    public List<CartItemDTO> getCartByCurrentUser() {
        return cartService.getCartByCurrentUser();
    }

    @PutMapping
    public CartItemDTO updateCountOfItem(Long itemId, Integer count) {
        return cartService.updateCountOfItem(itemId, count);
    }

    @DeleteMapping("/{itemId}")
    public void deleteFromCartByItemId(@PathVariable Long itemId) {
        cartService.deleteFromCartByItemId(itemId);
    }

    @PostMapping("/create-order")
    public OrderDTO createOrderFromCart() {
        return cartService.createOrderFromCart();
    }

    @PostMapping("/create-order/{email}")
    public OrderDTO createOrderFromCartForUserLoggedInAfterAddedItemToCart(@PathVariable String email, @RequestBody List<CartItemDTO>
            cartItemDTOList) {
        return cartService.createOrderFromCart(email, cartItemDTOList);
    }
}