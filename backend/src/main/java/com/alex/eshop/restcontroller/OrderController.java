package com.alex.eshop.restcontroller;

import com.alex.eshop.constants.Role;
import com.alex.eshop.dto.orderDTOs.OrderDTO;
import com.alex.eshop.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @GetMapping("order/{orderNumber}")
    public OrderDTO getOrderByUserId(Principal principal, @PathVariable String orderNumber) {
        return orderService.getOrderByUserIdAndOrderNumber(principal.getName(), orderNumber);
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @GetMapping("orders")
    public Page<OrderDTO> getAllOrdersByUserId(Principal principal, Pageable pageable) {
        return orderService.getAllOrdersByUserId(principal.getName(), pageable);
    }

    @PreAuthorize("hasRole('" + Role.ADMIN + "')")
    @GetMapping("orders/all")
    public Page<OrderDTO> getAllOrders(Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @DeleteMapping("order/{orderNumber}")
    public void cancelOrder(@PathVariable String orderNumber, Principal principal) {
        orderService.cancelOrder(principal.getName(), orderNumber);
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @PutMapping("order/confirm/{orderNumber}")
    public void confirmOrder(@PathVariable String orderNumber, Principal principal) {
        orderService.confirmOrder(orderNumber, principal.getName());
    }
}