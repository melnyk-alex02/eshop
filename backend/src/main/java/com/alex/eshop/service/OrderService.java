package com.alex.eshop.service;

import com.alex.eshop.constants.OrderStatus;
import com.alex.eshop.dto.orderDTOs.OrderDTO;
import com.alex.eshop.entity.Order;
import com.alex.eshop.exception.DataNotFoundException;
import com.alex.eshop.mapper.OrderMapper;
import com.alex.eshop.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CurrentUserService currentUserService;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper,
                        CurrentUserService currentUserService) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.currentUserService = currentUserService;
    }

    public OrderDTO getOrderByUserIdAndOrderNumber(String orderNumber) {
        String userId = currentUserService.getCurrentUserUuid();

        if (!orderRepository.existsByNumberAndUserId(orderNumber, userId)) {
            throw new DataNotFoundException("There is no order with number " + orderNumber + " for current logged user");
        }

        if (orderRepository.getReferenceById(orderNumber).getStatus().equals(OrderStatus.EXPIRED)) {
            throw new DataNotFoundException("Order with number " + orderNumber + " is expired");
        }
        return orderMapper.toDto(orderRepository.findOrderByUserIdAndNumber(userId, orderNumber));
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

        OrderDTO orderDTO = orderMapper.toDto(orderRepository.findOrderByUserIdAndNumber(userId, orderNumber));

        orderRepository.save(orderMapper.toEntity(orderDTO.withStatus(OrderStatus.CANCELLED)));
    }

    public void confirmOrder(String orderNumber) {
        String userId = currentUserService.getCurrentUserUuid();

        if (!orderRepository.existsByNumberAndUserId(orderNumber, userId)) {
            throw new DataNotFoundException("There is no order for current logged user with number " + orderNumber);
        }

        OrderDTO orderDTO = orderMapper.toDto(orderRepository.findOrderByUserIdAndNumber(userId, orderNumber));

        orderRepository.save(orderMapper.toEntity(orderDTO
                .withStatus(OrderStatus.DONE)
                .withPurchasedDate(ZonedDateTime.now())));
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