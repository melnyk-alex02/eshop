package com.alex.eshop.restcontroller;

import com.alex.eshop.constants.Role;
import com.alex.eshop.dto.orderDTOs.OrderDTO;
import com.alex.eshop.service.OrderService;
import com.alex.eshop.wrapper.PageWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @GetMapping("order/{orderNumber}")
    public OrderDTO getOrderByUserId(@PathVariable String orderNumber) {
        return orderService.getOrderByUserIdAndOrderNumber(orderNumber);
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @GetMapping("orders")
    public PageWrapper<OrderDTO> getAllOrdersByUserId(Pageable pageable) {
        Page<OrderDTO> orderPage = orderService.getAllOrdersByUserId(pageable);

        return new PageWrapper<>(orderPage.getContent(), orderPage.getTotalElements());
    }

    @PreAuthorize("hasRole('" + Role.ADMIN + "')")
    @GetMapping("orders/all")
    public PageWrapper<OrderDTO> getAllOrders(Pageable pageable) {
        Page<OrderDTO> orderPage = orderService.getAllOrders(pageable);

        return new PageWrapper<>(orderPage.getContent(), orderPage.getTotalElements());
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @DeleteMapping("order/{orderNumber}")
    public void cancelOrder(@PathVariable String orderNumber) {
        orderService.cancelOrder(orderNumber);
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    @PutMapping("order/confirm/{orderNumber}")
    public void confirmOrder(@PathVariable String orderNumber) {
        orderService.confirmOrder(orderNumber);
    }
}