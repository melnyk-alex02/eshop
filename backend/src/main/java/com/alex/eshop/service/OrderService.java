package com.alex.eshop.service;

import com.alex.eshop.constants.OrderStatus;
import com.alex.eshop.dto.cartDTOs.CartItemDTO;
import com.alex.eshop.dto.orderDTOs.OrderDTO;
import com.alex.eshop.entity.Item;
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
import java.util.Objects;

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

    public OrderDTO createOrder(List<CartItemDTO> cartItemDTOList, String userId, String email) {
        if (cartItemDTOList.isEmpty()) {
            throw new InvalidDataException("Your cart is empty");
        }
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
            Item item = itemRepository.getReferenceById(cartItemDTO.itemId());
            orderItem.setOrderItemId(new OrderItemId(order.getId(), item.getId()));
            orderItem.setOrder(order);
            orderItem.setItem(itemRepository.getReferenceById(item.getId()));

            if (cartItemDTO.count() < 1) {
                throw new InvalidDataException("Please check count of items in your cart");
            }
            orderItem.setCount(cartItemDTO.count());

            orderItem.setItemPrice(item.getPrice());

            orderItemList.add(orderItem);

            BigDecimal priceOfItem = item.getPrice().multiply(BigDecimal.valueOf(cartItemDTO.count()));

            totalPrice = totalPrice.add(priceOfItem);
        }
        logger.info(email);
        order.setEmail(email);
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

    public OrderDTO getOrderByEmailAndOrderForUnauthenticatedUsers(String orderNumber, String email) {
        String currentEmail = currentUserService.getCurrentUserEmail();

        logger.info(currentEmail);

        logger.info("{}",currentUserService.isUserAuthenticatedAndNotAnonymous());

        if(currentUserService.isUserAuthenticatedAndNotAnonymous()) {
           if (!Objects.equals(email, currentEmail)) {
               throw new InvalidDataException("You can't get order for another user");
           }
        }

        if (!orderRepository.existsByNumberAndEmail(orderNumber, email)) {
            throw new DataNotFoundException("There is no order with number " + orderNumber + " for current logged user");
        }

        return orderMapper.toDto(orderRepository.findByEmailAndNumber(email, orderNumber));
    }

    public OrderDTO getOrderByEmailAndOrderNumber(String orderNumber) {
        String email = currentUserService.getCurrentUserEmail();

        if (!orderRepository.existsByNumberAndEmail(orderNumber, email)) {
            throw new DataNotFoundException("There is no order with number " + orderNumber + " for current logged user");
        }

        Order order = orderRepository.findByEmailAndNumber(email, orderNumber);

        logger.info(order.toString());

        return orderMapper.toDto(order);
    }

    public Page<OrderDTO> getAllOrdersByUserId(Pageable pageable) {
        String userId = currentUserService.getCurrentUserUuid();

        return orderRepository.findAllByUserId(userId, pageable).map(orderMapper::toDto);
    }

    public Page<OrderDTO> getAllOrdersByEmail(Pageable pageable) {
        String email = currentUserService.getCurrentUserEmail();

        return orderRepository.findAllByEmail(email, pageable).map(orderMapper::toDto);
    }

    public void cancelOrder(String orderNumber) {
        String email = currentUserService.getCurrentUserEmail();

        if (!orderRepository.existsByNumberAndEmail(orderNumber, email)) {
            throw new DataNotFoundException("There is no order with number " + orderNumber + " for current logged user");
        }

        Order order = orderRepository.findByEmailAndNumber(email, orderNumber);

        order.setStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);
    }

    public void confirmOrder(String orderNumber) {
        String email = currentUserService.getCurrentUserEmail();

        if (!orderRepository.existsByNumberAndEmail(orderNumber, email)) {
            throw new DataNotFoundException("There is no order for current logged user with number " + orderNumber);
        }

        Order order = orderRepository.findByEmailAndNumber(email, orderNumber);

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