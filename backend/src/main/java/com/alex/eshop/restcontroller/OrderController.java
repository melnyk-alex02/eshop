package com.alex.eshop.restcontroller;

import com.alex.eshop.dto.orderDTOs.OrderDTO;
import com.alex.eshop.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("order/{orderNumber}")
    public OrderDTO getOrderByUserId(Principal principal, @PathVariable String orderNumber) {
        return orderService.getOrderByUserId(principal.getName(), orderNumber);
    }
}