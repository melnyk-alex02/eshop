package com.alex.eshop.service;

import com.alex.eshop.dto.cartDTOs.CartItemDTO;
import com.alex.eshop.dto.orderDTOs.OrderDTO;
import com.alex.eshop.entity.CartItem;
import com.alex.eshop.entity.compositeIds.CartItemId;
import com.alex.eshop.exception.DataNotFoundException;
import com.alex.eshop.exception.InvalidDataException;
import com.alex.eshop.mapper.CartMapper;
import com.alex.eshop.repository.CartItemRepository;
import com.alex.eshop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;
    private final OrderService orderService;

    private final ItemRepository itemRepository;
    private final CurrentUserService currentUserService;

    public List<CartItemDTO> getCartByCurrentUser() {
        String userId = currentUserService.getCurrentUserUuid();

        return cartMapper.toDto(cartItemRepository.findAllByUserId(userId));
    }

    public CartItemDTO addItemToCart(Long itemId) {
        String userId = currentUserService.getCurrentUserUuid();
        if (cartItemRepository.existsByCartItemId(new CartItemId(userId, itemId))) {
            throw new InvalidDataException("This item is already in your cart");
        }
        if (!itemRepository.existsById(itemId)) {
            throw new DataNotFoundException("There is no item with id " + itemId);
        }

        CartItem cartItem = new CartItem();
        cartItem.setCartItemId(new CartItemId(userId, itemId));
        cartItem.setUserId(userId);
        cartItem.setItem(itemRepository.getReferenceById(itemId));
        cartItem.setCount(1);

        return cartMapper.toDto(cartItemRepository.save(cartItem));
    }

    public CartItemDTO updateCountOfItem(Long itemId, Integer count) {
        String userId = currentUserService.getCurrentUserUuid();
        if (!cartItemRepository.existsByCartItemId(new CartItemId(userId, itemId))) {
            throw new DataNotFoundException("There is no cart for user with id " + userId + " and items with id " + itemId);
        }
        if (count < 1) {
            throw new InvalidDataException("Count can't be less than 1, please check input data");
        }
        CartItem cartItem = cartItemRepository.findByCartItemId(new CartItemId(userId, itemId));
        cartItem.setCount(count);

        return cartMapper.toDto(cartItemRepository.save(cartItem));
    }

    public void deleteFromCartByItemId(Long itemId) {
        if (!cartItemRepository.existsByCartItemId(new CartItemId(currentUserService.getCurrentUserUuid(), itemId))) {
            throw new DataNotFoundException("There is no items in cart with id " + itemId + "for current logged user");
        }
        cartItemRepository.deleteCartByItemId(itemId);
    }

    public OrderDTO createOrderFromCart() {
        String userId = currentUserService.getCurrentUserUuid();

        if (!cartItemRepository.existsAllByUserId(userId)) {
            throw new DataNotFoundException("There is no cart for current logged user");
        }

        OrderDTO orderDTO = orderService.createOrder(getCartByCurrentUser(), userId);

        deleteCart();

        return orderDTO;
    }

    public void deleteCart() {
        cartItemRepository.deleteAllByUserId(currentUserService.getCurrentUserUuid());
    }
}