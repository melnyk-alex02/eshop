package com.alex.eshop.service;

import com.alex.eshop.dto.orderDTOs.OrderDTO;
import com.alex.eshop.exception.DataNotFoundException;
import com.alex.eshop.mapper.OrderMapper;
import com.alex.eshop.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public OrderDTO getOrderByUserIdAndOrderNumber(String userId, String orderNumber) {
        if (!orderRepository.existsByNumberAndUserId(orderNumber, userId)) {
            throw new DataNotFoundException("There is no order with number " + orderNumber + " for current logged user");
        }
        return orderMapper.toDto(orderRepository.findOrderByUserIdAndNumber(userId, orderNumber));
    }
}