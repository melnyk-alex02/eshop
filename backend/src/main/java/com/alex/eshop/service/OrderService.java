package com.alex.eshop.service;

import com.alex.eshop.constants.OrderStatus;
import com.alex.eshop.dto.orderDTOs.OrderDTO;
import com.alex.eshop.exception.DataNotFoundException;
import com.alex.eshop.mapper.OrderMapper;
import com.alex.eshop.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

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

    public Page<OrderDTO> getAllOrdersByUserId(String userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable).map(orderMapper::toDto);
    }

    public void cancelOrder(String userId, String orderNumber) {
        if (!orderRepository.existsByNumberAndUserId(orderNumber, userId)) {
            throw new DataNotFoundException("There is no order with number " + orderNumber + " for current logged user");
        }
        OrderDTO orderDTO = orderMapper.toDto(orderRepository.findOrderByUserIdAndNumber(userId, orderNumber));

        orderDTO.setStatus(OrderStatus.CANCELLED);

        orderRepository.save(orderMapper.toEntity(orderDTO));
    }

    public void confirmOrder(String orderNumber, String userId) {
        if (!orderRepository.existsByNumberAndUserId(orderNumber, userId)) {
            throw new DataNotFoundException("There is no order for current logged user with number " + orderNumber);
        }

        OrderDTO orderDTO = orderMapper.toDto(orderRepository.findOrderByUserIdAndNumber(userId, orderNumber));
        orderDTO.setStatus(OrderStatus.DONE);
        orderDTO.setPurchasedDate(ZonedDateTime.now());

        orderMapper.toDto(orderRepository.save(orderMapper.toEntity(orderDTO)));
    }

    public Page<OrderDTO> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderMapper::toDto);
    }
}