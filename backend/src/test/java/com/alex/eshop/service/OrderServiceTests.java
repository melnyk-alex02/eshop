package com.alex.eshop.service;

import com.alex.eshop.constants.OrderStatus;
import com.alex.eshop.dto.orderDTOs.OrderDTO;
import com.alex.eshop.entity.Order;
import com.alex.eshop.mapper.OrderMapper;
import com.alex.eshop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTests {
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void testGetOrderByUserIdAndOrderNumber() {
        Order order = createOrderList().get(0);
        OrderDTO expectedOrderDTO = createOrderDTOList().get(0);

        when(currentUserService.getCurrentUserUuid()).thenReturn("userId");
        when(orderRepository.existsByNumberAndUserId(expectedOrderDTO.getNumber(), "userId")).thenReturn(true);
        when(orderRepository.findOrderByUserIdAndNumber("userId", order.getNumber())).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(expectedOrderDTO);

        OrderDTO result = orderService.getOrderByUserIdAndOrderNumber(order.getNumber());

        verify(currentUserService).getCurrentUserUuid();
        verify(orderRepository).existsByNumberAndUserId(expectedOrderDTO.getNumber(), "userId");
        verify(orderRepository).findOrderByUserIdAndNumber("userId", order.getNumber());
        verify(orderMapper).toDto(order);

        assertEquals(expectedOrderDTO.number(), result.number());
        assertEquals(expectedOrderDTO.status(), result.status());
        assertEquals(expectedOrderDTO.createdDate(), result.createdDate());
        assertEquals(expectedOrderDTO.price(), result.price());
        assertEquals(expectedOrderDTO.count(), result.count());
        assertEquals(expectedOrderDTO.userId(), result.userId());
        assertEquals(expectedOrderDTO.orderItemDTOList().size(), result.orderItemDTOList().size());
    }

    @Test
    public void testGetAllOrderByUserId() {
        List<Order> orderList = createOrderList();
        List<OrderDTO> expectedOrderDTOList = createOrderDTOList();

        String userId = "userId";

        Page<Order> orderPage = new PageImpl<>(orderList);

        when(currentUserService.getCurrentUserUuid()).thenReturn(userId);
        when(orderRepository.existsAllByUserId(userId)).thenReturn(true);
        when(orderRepository.findAllByUserId(userId, Pageable.unpaged())).thenReturn(orderPage);
        when(orderMapper.toDto(any(Order.class))).thenAnswer(invocationOnMock -> {
            Order order = invocationOnMock.getArgument(0);
            String orderNumber = order.getNumber();
            return expectedOrderDTOList.stream()
                    .filter(dto -> dto.number().equals(orderNumber))
                    .findFirst()
                    .orElse(null);
        });


        Page<OrderDTO> result = orderService.getAllOrdersByUserId(Pageable.unpaged());

        verify(currentUserService).getCurrentUserUuid();
        verify(orderRepository).existsAllByUserId(userId);
        verify(orderRepository).findAllByUserId(userId, Pageable.unpaged());
        verify(orderMapper, times(2)).toDto(any(Order.class));

        assertEquals(expectedOrderDTOList.size(), result.getContent().size());

        assertEquals(expectedOrderDTOList.get(0).number(), result.getContent().get(0).number());
        assertEquals(expectedOrderDTOList.get(0).status(), result.getContent().get(0).status());
        assertEquals(expectedOrderDTOList.get(0).createdDate(), result.getContent().get(0).createdDate());
        assertEquals(expectedOrderDTOList.get(0).price(), result.getContent().get(0).price());
        assertEquals(expectedOrderDTOList.get(0).count(), result.getContent().get(0).count());
        assertEquals(expectedOrderDTOList.get(0).userId(), result.getContent().get(0).userId());
        assertEquals(expectedOrderDTOList.get(0).orderItemDTOList().size(), result.getContent().get(0).orderItemDTOList().size());

        assertEquals(expectedOrderDTOList.get(1).number(), result.getContent().get(1).number());
        assertEquals(expectedOrderDTOList.get(1).status(), result.getContent().get(1).status());
        assertEquals(expectedOrderDTOList.get(1).createdDate(), result.getContent().get(1).createdDate());
        assertEquals(expectedOrderDTOList.get(1).price(), result.getContent().get(1).price());
        assertEquals(expectedOrderDTOList.get(1).count(), result.getContent().get(1).count());
        assertEquals(expectedOrderDTOList.get(1).userId(), result.getContent().get(1).userId());
        assertEquals(expectedOrderDTOList.get(1).orderItemDTOList().size(), result.getContent().get(1).orderItemDTOList().size());
    }

    @Test
    public void testCancelOrder() {
        Order order = createOrderList().get(0);
        OrderDTO orderDTO = createOrderDTOList().get(0);

        String userId = "userId";

        OrderDTO expectedOrderDTO = createOrderDTOList().get(0);
        expectedOrderDTO.withStatus(OrderStatus.CANCELLED);

        when(currentUserService.getCurrentUserUuid()).thenReturn(userId);
        when(orderRepository.existsByNumberAndUserId(expectedOrderDTO.number(), userId)).thenReturn(true);
        when(orderRepository.findOrderByUserIdAndNumber(userId, order.getNumber())).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderDTO);
        when(orderRepository.save(orderMapper.toEntity(expectedOrderDTO))).thenReturn(order);

        orderService.cancelOrder("number");

        verify(orderRepository).existsByNumberAndUserId(expectedOrderDTO.number(), userId);
        verify(currentUserService).getCurrentUserUuid();
        verify(orderRepository).findOrderByUserIdAndNumber(userId, expectedOrderDTO.number());
        verify(orderMapper).toDto(order);
        verify(orderRepository).save(orderMapper.toEntity(expectedOrderDTO));
    }

    @Test
    public void testConfirmOrder() {
        Order order = createOrderList().get(0);
        OrderDTO orderDTO = createOrderDTOList().get(0);

        OrderDTO expectedOrderDTO = createOrderDTOList().get(0);
        expectedOrderDTO.withStatus(OrderStatus.DONE);

        when(currentUserService.getCurrentUserUuid()).thenReturn("userId");
        when(orderRepository.existsByNumberAndUserId(expectedOrderDTO.number(), "userId")).thenReturn(true);
        when(orderRepository.findOrderByUserIdAndNumber("userId", order.getNumber())).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderDTO);
        when(orderRepository.save(orderMapper.toEntity(expectedOrderDTO))).thenReturn(order);

        orderService.confirmOrder("number");

        verify(orderRepository).existsByNumberAndUserId(expectedOrderDTO.number(), "userId");
    }

    @Test
    public void testGetAllOrders() {
        List<Order> orderList = createOrderList();
        List<OrderDTO> expectedOrderDTOList = createOrderDTOList();

        Page<Order> orderPage = new PageImpl<>(orderList);

        when(orderRepository.findAll(Pageable.unpaged())).thenReturn(orderPage);
        when(orderMapper.toDto(any(Order.class))).thenAnswer(invocationOnMock -> {
            Order order = invocationOnMock.getArgument(0);
            String orderNumber = order.getNumber();
            return expectedOrderDTOList.stream()
                    .filter(dto -> dto.getNumber().equals(orderNumber))
                    .findFirst()
                    .orElse(null);
        });
        Page<OrderDTO> result = orderService.getAllOrders(Pageable.unpaged());

        verify(orderRepository).findAll(Pageable.unpaged());
        verify(orderMapper, times(2)).toDto(any(Order.class));

        assertEquals(expectedOrderDTOList.size(), result.getContent().size());

        assertEquals(expectedOrderDTOList.get(0).getNumber(), result.getContent().get(0).getNumber());
        assertEquals(expectedOrderDTOList.get(0).getStatus(), result.getContent().get(0).getStatus());
        assertEquals(expectedOrderDTOList.get(0).getCreatedDate(), result.getContent().get(0).getCreatedDate());
        assertEquals(expectedOrderDTOList.get(0).getPrice(), result.getContent().get(0).getPrice());
        assertEquals(expectedOrderDTOList.get(0).getCount(), result.getContent().get(0).getCount());
        assertEquals(expectedOrderDTOList.get(0).getUserId(), result.getContent().get(0).getUserId());
        assertEquals(expectedOrderDTOList.get(0).getOrderItemDTOList().size(), result.getContent().get(0).getOrderItemDTOList().size());

        assertEquals(expectedOrderDTOList.get(1).getNumber(), result.getContent().get(1).getNumber());
        assertEquals(expectedOrderDTOList.get(1).getStatus(), result.getContent().get(1).getStatus());
        assertEquals(expectedOrderDTOList.get(1).getCreatedDate(), result.getContent().get(1).getCreatedDate());
        assertEquals(expectedOrderDTOList.get(1).getPrice(), result.getContent().get(1).getPrice());
        assertEquals(expectedOrderDTOList.get(1).getCount(), result.getContent().get(1).getCount());
        assertEquals(expectedOrderDTOList.get(1).getUserId(), result.getContent().get(1).getUserId());
        assertEquals(expectedOrderDTOList.get(1).getOrderItemDTOList().size(), result.getContent().get(1).getOrderItemDTOList().size());
    }

    @Test
    public void testGetOrderByUserIdAndOrderNumber_WhenNoOrderFoundForUser_ShouldThrowException() {
        String userId = "userId";

        when(currentUserService.getCurrentUserUuid()).thenReturn(userId);
        when(orderRepository.existsByNumberAndUserId("number", userId)).thenThrow(
                new DataNotFoundException("There is no order with number number for current logged user")
        );

        assertThrows(DataNotFoundException.class, () -> orderService.getOrderByUserIdAndOrderNumber("number"));
    }

    @Test
    public void testGetAllOrdersByUserId_WhenNoOrdersFoundForUser_ShouldThrowException() {
        String userId = "userId";

        when(currentUserService.getCurrentUserUuid()).thenReturn(userId);
        when(orderRepository.existsAllByUserId(userId)).thenThrow(
                new DataNotFoundException("There is no orders for current logged user")
        );

        assertThrows(DataNotFoundException.class, () -> orderService.getAllOrdersByUserId(Pageable.unpaged()));
    }

    @Test
    public void testCancelOrder_WhenNoOrderFoundForUser_ShouldThrowException() {
        String userId = "userId";

        when(currentUserService.getCurrentUserUuid()).thenReturn(userId);
        when(orderRepository.existsByNumberAndUserId("number", userId)).thenThrow(
                new DataNotFoundException("There is no order with number number for current logged user")
        );

        assertThrows(DataNotFoundException.class, () -> orderService.cancelOrder("number"));
    }

    @Test
    public void testConfirmOrder_WhenNoOrderFoundForUser_ShouldThrowException() {
        String userId = "userId";

        when(currentUserService.getCurrentUserUuid()).thenReturn(userId);
        when(orderRepository.existsByNumberAndUserId("number", userId)).thenThrow(
                new DataNotFoundException("There is no order for current logged user with number number")
        );

        assertThrows(DataNotFoundException.class, () -> orderService.confirmOrder("number"));
    }

    private List<Order> createOrderList() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Category");
        category.setDescription("Description");

        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item 1");
        item1.setDescription("Description 1");
        item1.setCategory(category);
        item1.setPrice(BigDecimal.valueOf(100.99));
        item1.setImageSrc("Image src 1");

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Item 2");
        item2.setDescription("Description 2");
        item2.setCategory(category);
        item1.setPrice(BigDecimal.valueOf(99.00));
        item2.setImageSrc("Image src 2");

        Order order1 = new Order();
        order1.setNumber("number");
        order1.setStatus("NEW");
        order1.setCreatedDate(ZonedDateTime.of(LocalDateTime.of(2023, 1, 1, 0, 0),
                ZoneId.of("UTC")));
        order1.setPrice(BigDecimal.valueOf(199, 99));
        order1.setCount(2);
        order1.setUserId("userId");

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setCount(1);
        orderItem1.setItem(item1);
        orderItem1.setId(1L);
        orderItem1.setOrder(order1);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setCount(1);
        orderItem2.setItem(item2);
        orderItem2.setId(1L);
        orderItem2.setOrder(order1);

        order1.setOrderItemList(List.of(orderItem1, orderItem2));

        Order order2 = new Order();
        order2.setNumber("number1");
        order2.setStatus("NEW");
        order2.setCreatedDate(ZonedDateTime.of(LocalDateTime.of(2023, 11, 23, 1, 0),
                ZoneId.of("UTC")));
        order2.setPrice(BigDecimal.valueOf(199, 99));
        order2.setCount(2);
        order2.setUserId("userId");

        OrderItem orderItem3 = new OrderItem();
        orderItem3.setCount(1);
        orderItem3.setItem(item1);
        orderItem3.setId(1L);
        orderItem3.setOrder(order2);

        OrderItem orderItem4 = new OrderItem();
        orderItem4.setCount(1);
        orderItem4.setItem(item2);
        orderItem4.setId(1L);
        orderItem4.setOrder(order2);

        order2.setOrderItemList(List.of(orderItem3, orderItem4));

        return List.of(order1, order2);
    }

    private List<OrderDTO> createOrderDTOList() {
        OrderDTO orderDTO1 = new OrderDTO();
        orderDTO1.setNumber("number");
        orderDTO1.setStatus("NEW");
        orderDTO1.setCreatedDate(ZonedDateTime.of(LocalDateTime.of(2023, 1, 1, 0, 0),
                ZoneId.of("UTC")));
        orderDTO1.setPrice(BigDecimal.valueOf(199, 99));
        orderDTO1.setCount(2);
        orderDTO1.setUserId("userId");

        OrderItemDTO orderItemDTO1 = new OrderItemDTO();
        orderItemDTO1.setCount(1);
        orderItemDTO1.setItemId(1L);
        orderItemDTO1.setId(1L);
        orderItemDTO1.setOrderNumber(orderDTO1.getNumber());

        OrderItemDTO orderItemDTO2 = new OrderItemDTO();
        orderItemDTO2.setCount(1);
        orderItemDTO2.setItemId(2L);
        orderItemDTO2.setId(1L);
        orderItemDTO2.setOrderNumber(orderDTO1.getNumber());

        orderDTO1.setOrderItemDTOList(List.of(orderItemDTO1, orderItemDTO2));

        OrderDTO orderDTO2 = new OrderDTO();
        orderDTO2.setNumber("number1");
        orderDTO2.setStatus("NEW");
        orderDTO2.setCreatedDate(ZonedDateTime.of(LocalDateTime.of(2023, 11, 23, 1, 0),
                ZoneId.of("UTC")));
        orderDTO2.setPrice(BigDecimal.valueOf(199, 99));
        orderDTO2.setCount(2);
        orderDTO2.setUserId("userId");

        OrderItemDTO orderItemDTO3 = new OrderItemDTO();
        orderItemDTO3.setCount(1);
        orderItemDTO3.setItemId(1L);
        orderItemDTO3.setItemPrice(BigDecimal.valueOf(100.99));
        orderItemDTO3.setItemName("Item 1");
        orderItemDTO3.setId(1L);
        orderItemDTO3.setOrderNumber("number1");

        OrderItemDTO orderItemDTO4 = new OrderItemDTO();
        orderItemDTO4.setCount(1);
        orderItemDTO4.setItemId(2L);
        orderItemDTO4.setItemName("Item 2");
        orderItemDTO4.setItemPrice(BigDecimal.valueOf(99.00));
        orderItemDTO4.setId(1L);
        orderItemDTO4.setOrderNumber("number1");

        orderDTO2.setOrderItemDTOList(List.of(orderItemDTO3, orderItemDTO4));

        return List.of(orderDTO1, orderDTO2);
    }
}