package com.alex.eshop.restcontroller;

import com.alex.eshop.constants.Role;
import com.alex.eshop.dto.orderDTOs.OrderDTO;
import com.alex.eshop.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('" + Role.USER +"')")
    @GetMapping("order/{orderNumber}")
    public OrderDTO getOrderByUserId(Principal principal, @PathVariable String orderNumber) {
        return orderService.getOrderByUserIdAndOrderNumber(principal.getName(), orderNumber);
    }
}