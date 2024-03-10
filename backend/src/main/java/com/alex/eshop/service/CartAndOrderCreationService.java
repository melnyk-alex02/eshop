package com.alex.eshop.service;

import com.alex.eshop.constants.OrderStatus;
import com.alex.eshop.dto.cartDTOs.CartItemDTO;
import com.alex.eshop.dto.orderDTOs.OrderDTO;
import com.alex.eshop.entity.CartItem;
import com.alex.eshop.entity.Order;
import com.alex.eshop.entity.OrderItem;
import com.alex.eshop.entity.compositeIds.CartItemId;
import com.alex.eshop.entity.compositeIds.OrderItemId;
import com.alex.eshop.exception.DataNotFoundException;
import com.alex.eshop.exception.InvalidDataException;
import com.alex.eshop.mapper.CartMapper;
import com.alex.eshop.mapper.OrderMapper;
import com.alex.eshop.repository.CartItemRepository;
import com.alex.eshop.repository.ItemRepository;
import com.alex.eshop.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.alex.eshop.utils.OrderNumberGenerator.generateOrderNumber;

@Service
@Transactional
public class CartAndOrderCreationService {
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final CurrentUserService currentUserService;

    public CartAndOrderCreationService(CartItemRepository cartItemRepository, CartMapper cartMapper,
                                       OrderMapper orderMapper, OrderRepository orderRepository,
                                       CurrentUserService currentUserService, ItemRepository itemRepository) {
        this.cartItemRepository = cartItemRepository;
        this.cartMapper = cartMapper;
        this.orderMapper = orderMapper;
        this.orderRepository = orderRepository;
        this.currentUserService = currentUserService;
        this.itemRepository = itemRepository;
    }

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

        System.out.println(cartItem.getCartItemId().getUserId());

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

        Integer count = 0;
        BigDecimal price = BigDecimal.ZERO;
        Order order = new Order();
        order.setNumber(generateOrderNumber());
        order.setCreatedDate(ZonedDateTime.now());
        order.setStatus(OrderStatus.NEW);
        order.setUserId(userId);

        List<CartItemDTO> cartItemDTOList = getCartByCurrentUser();

        List<OrderItem> orderItemList = new ArrayList<>();
        for (CartItemDTO cartItemDTO : cartItemDTOList) {
            OrderItem orderItem = new OrderItem();

            orderItem.setOrderItemId(new OrderItemId(order.getNumber(), cartItemDTO.itemId()));
            orderItem.setOrder(order);
            orderItem.setItem(itemRepository.getReferenceById(cartItemDTO.itemId()));
            if (cartItemDTO.count() < 1) {
                throw new InvalidDataException("Please check count of items in your cart");
            }
            orderItem.setCount(cartItemDTO.count());

            orderItemList.add(orderItem);

            price = price.add(cartItemDTO.itemPrice()).multiply(BigDecimal.valueOf(cartItemDTO.count()));
            count += cartItemDTO.count();
        }

        order.setCount(count);
        order.setPrice(price);
        order.setOrderItemList(orderItemList);

        orderRepository.save(order);

        deleteCart();

        return orderMapper.toDto(order);
    }

    public void deleteCart() {
        cartItemRepository.deleteAllByUserId(currentUserService.getCurrentUserUuid());
    }
}