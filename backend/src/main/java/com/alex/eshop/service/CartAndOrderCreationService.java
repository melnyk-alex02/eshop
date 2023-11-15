package com.alex.eshop.service;

import com.alex.eshop.constants.OrderStatus;
import com.alex.eshop.dto.cartDTOs.CartDTO;
import com.alex.eshop.dto.orderDTOs.OrderDTO;
import com.alex.eshop.dto.orderDTOs.OrderItemDTO;
import com.alex.eshop.exception.DataNotFoundException;
import com.alex.eshop.exception.InvalidDataException;
import com.alex.eshop.mapper.CartMapper;
import com.alex.eshop.mapper.OrderMapper;
import com.alex.eshop.repository.CartRepository;
import com.alex.eshop.repository.OrderRepository;
import com.alex.eshop.utils.OrderNumberGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CartAndOrderCreationService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    public CartAndOrderCreationService(CartRepository cartRepository, CartMapper cartMapper,
                                       OrderMapper orderMapper, OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.orderMapper = orderMapper;
        this.orderRepository = orderRepository;
    }

    public List<CartDTO> getAllCarts(String userId) {
        return cartMapper.toDto(cartRepository.findAllByUserId(userId));
    }

    public CartDTO addItemToCart(Long itemId, String userId) {
        if (cartRepository.existsByItemIdAndUserId(itemId, userId)) {
            throw new InvalidDataException("Item with id" + itemId + "is already in your cart");
        }
        CartDTO cartDTO = new CartDTO();
        cartDTO.setUserId(userId);
        cartDTO.setItemId(itemId);
        cartDTO.setCount(1);


        return cartMapper.toDto(cartRepository.save(cartMapper.toEntity(cartDTO)));
    }

    public CartDTO updateCountOfItem(Long itemId, Integer count, String userId) {
        if (!cartRepository.existsByItemIdAndUserId(itemId, userId)) {
            throw new DataNotFoundException("There is no cart for user with id " + userId + " and items with id " + itemId);
        }
        if (!(count > 1)) {
            throw new InvalidDataException("Count can't be less than 1, please check input data");
        }
        CartDTO cartDTO = cartMapper.toDto(cartRepository.findCartByItemIdAndUserId(itemId, userId));

        cartDTO.setCount(count);

        return cartMapper.toDto(cartRepository.save(cartMapper.toEntity(cartDTO)));
    }

    public void deleteCartByItemId(Long itemId, String userId) {
        if (!cartRepository.existsByItemIdAndUserId(itemId, userId)) {
            throw new DataNotFoundException("There is no items in cart with id " + itemId + "for current logged user");
        }
        cartRepository.deleteCartByItemId(itemId);
    }


    public OrderDTO createOrderFromCart(String userId) {
        if (!cartRepository.existsAllByUserId(userId)) {
            throw new DataNotFoundException("There is no cart for current logged user");
        }

        Integer count = 0;
        BigDecimal price = BigDecimal.ZERO;
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setNumber(OrderNumberGenerator.generateOrderNumber());
        orderDTO.setCreatedDate(ZonedDateTime.now());
        orderDTO.setStatus(OrderStatus.NEW);
        orderDTO.setUserId(userId);

        List<CartDTO> cartDTOList = getAllCarts(userId);

        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
        for (CartDTO cartDTO : cartDTOList) {
            OrderItemDTO orderItemDTO = new OrderItemDTO();

            orderItemDTO.setOrderNumber(orderDTO.getNumber());
            orderItemDTO.setItemId(cartDTO.getItemId());

            orderItemDTOList.add(orderItemDTO);

            price = price.add(cartDTO.getItemPrice()).multiply(BigDecimal.valueOf(cartDTO.getCount()));
            count += cartDTO.getCount();
        }

        orderDTO.setCount(count);
        orderDTO.setPrice(price);
        orderDTO.setOrderItemDTOList(orderItemDTOList);

        orderRepository.save(orderMapper.toEntity(orderDTO));

        deleteCart(userId);

        return orderDTO;
    }

    public void deleteCart(String userId) {
        cartRepository.deleteAllByUserId(userId);
    }
}