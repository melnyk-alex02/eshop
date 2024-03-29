package com.alex.eshop.service;

import com.alex.eshop.constants.OrderStatus;
import com.alex.eshop.dto.cartDTOs.CartItemDTO;
import com.alex.eshop.dto.orderDTOs.OrderDTO;
import com.alex.eshop.entity.Order;
import com.alex.eshop.entity.OrderItem;
import com.alex.eshop.entity.compositeIds.OrderItemId;
import com.alex.eshop.exception.DataNotFoundException;
import com.alex.eshop.exception.InvalidDataException;
import com.alex.eshop.mapper.OrderMapper;
import com.alex.eshop.repository.ItemRepository;
import com.alex.eshop.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.alex.eshop.utils.OrderNumberGenerator.generateOrderNumber;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CurrentUserService currentUserService;
    private final ItemRepository itemRepository;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);


    public OrderDTO createOrder(List<CartItemDTO> cartItemDTOList, String userId) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        Order order = new Order();
        order.setNumber(generateOrderNumber());
        order.setUserId(userId);
        order.setCreatedDate(ZonedDateTime.now());
        order.setStatus(OrderStatus.NEW);
        order.setUserId(userId);

        List<OrderItem> orderItemList = new ArrayList<>();
        for (CartItemDTO cartItemDTO : cartItemDTOList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderItemId(new OrderItemId(order.getId(), cartItemDTO.itemId()));
            orderItem.setOrder(order);
            orderItem.setItem(itemRepository.getReferenceById(cartItemDTO.itemId()));

            if (cartItemDTO.count() < 1) {
                throw new InvalidDataException("Please check count of items in your cart");
            }
            orderItem.setCount(cartItemDTO.count());

            orderItem.setItemPrice(cartItemDTO.itemPrice());

            orderItemList.add(orderItem);

            BigDecimal priceOfItem = cartItemDTO.itemPrice().multiply(BigDecimal.valueOf(cartItemDTO.count()));

            totalPrice = totalPrice.add(priceOfItem);
        }
        
        order.setPrice(totalPrice);
        order.setOrderItemList(orderItemList);
        orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    public OrderDTO getOrderByUserIdAndOrderNumber(String orderNumber) {
        String userId = currentUserService.getCurrentUserUuid();

        if (!orderRepository.existsByNumberAndUserId(orderNumber, userId)) {
            throw new DataNotFoundException("There is no order with number " + orderNumber + " for current logged user");
        }

        Order order = orderRepository.findOrderByUserIdAndNumber(userId, orderNumber);

        logger.info(order.toString());

        return orderMapper.toDto(order);
    }

    public Page<OrderDTO> getAllOrdersByUserId(Pageable pageable) {
        String userId = currentUserService.getCurrentUserUuid();

        return orderRepository.findAllByUserId(userId, pageable).map(orderMapper::toDto);
    }

    public void cancelOrder(String orderNumber) {
        String userId = currentUserService.getCurrentUserUuid();

        if (!orderRepository.existsByNumberAndUserId(orderNumber, userId)) {
            throw new DataNotFoundException("There is no order with number " + orderNumber + " for current logged user");
        }

        Order order = orderRepository.findOrderByUserIdAndNumber(userId, orderNumber);

        order.setStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);
    }

    public void confirmOrder(String orderNumber) {
        String userId = currentUserService.getCurrentUserUuid();

        if (!orderRepository.existsByNumberAndUserId(orderNumber, userId)) {
            throw new DataNotFoundException("There is no order for current logged user with number " + orderNumber);
        }

        Order order = orderRepository.findOrderByUserIdAndNumber(userId, orderNumber);

        order.setStatus(OrderStatus.DONE);
        order.setPurchasedDate(ZonedDateTime.now());

        orderRepository.save(order);
    }

    public Page<OrderDTO> getAllOrders(Pageable pageable) {
        checkExpiredOrders();
        return orderRepository.findAll(pageable).map(orderMapper::toDto);
    }

    @Scheduled(cron = "0 * * * * *")
    protected void checkExpiredOrders() {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime expirationTime = now.minusHours(24);
        List<Order> unconfirmedOrders = orderRepository.findByStatusAndCreatedDateBefore(OrderStatus.NEW, expirationTime);
        System.out.println(unconfirmedOrders.toString());
        for (Order order : unconfirmedOrders) {
            order.setStatus(OrderStatus.EXPIRED);
            orderRepository.save(order);
        }
    }
}