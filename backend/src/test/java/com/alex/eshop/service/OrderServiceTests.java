package com.alex.eshop.service;

import com.alex.eshop.constants.OrderStatus;
import com.alex.eshop.dto.orderDTOs.OrderDTO;
import com.alex.eshop.dto.orderDTOs.OrderItemDTO;
import com.alex.eshop.entity.Category;
import com.alex.eshop.entity.Item;
import com.alex.eshop.entity.Order;
import com.alex.eshop.entity.OrderItem;
import com.alex.eshop.exception.DataNotFoundException;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        when(orderRepository.existsByNumberAndUserId(expectedOrderDTO.number(), "userId")).thenReturn(true);
        when(orderRepository.findOrderByUserIdAndNumber("userId", order.getNumber())).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(expectedOrderDTO);

        OrderDTO result = orderService.getOrderByUserIdAndOrderNumber(order.getNumber());

        verify(currentUserService).getCurrentUserUuid();
        verify(orderRepository).existsByNumberAndUserId(expectedOrderDTO.number(), "userId");
        verify(orderRepository).findOrderByUserIdAndNumber("userId", order.getNumber());
        verify(orderMapper).toDto(order);

        assertEquals(expectedOrderDTO.number(), result.number());
        assertEquals(expectedOrderDTO.status(), result.status());
        assertEquals(expectedOrderDTO.createdDate(), result.createdDate());
        assertEquals(expectedOrderDTO.price(), result.price());
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
        verify(orderRepository).findAllByUserId(userId, Pageable.unpaged());
        verify(orderMapper, times(2)).toDto(any(Order.class));

        assertEquals(expectedOrderDTOList.size(), result.getContent().size());

        assertEquals(expectedOrderDTOList.get(0).number(), result.getContent().get(0).number());
        assertEquals(expectedOrderDTOList.get(0).status(), result.getContent().get(0).status());
        assertEquals(expectedOrderDTOList.get(0).createdDate(), result.getContent().get(0).createdDate());
        assertEquals(expectedOrderDTOList.get(0).price(), result.getContent().get(0).price());
        assertEquals(expectedOrderDTOList.get(0).userId(), result.getContent().get(0).userId());
        assertEquals(expectedOrderDTOList.get(0).orderItemDTOList().size(), result.getContent().get(0).orderItemDTOList().size());

        assertEquals(expectedOrderDTOList.get(1).number(), result.getContent().get(1).number());
        assertEquals(expectedOrderDTOList.get(1).status(), result.getContent().get(1).status());
        assertEquals(expectedOrderDTOList.get(1).createdDate(), result.getContent().get(1).createdDate());
        assertEquals(expectedOrderDTOList.get(1).price(), result.getContent().get(1).price());
        assertEquals(expectedOrderDTOList.get(1).orderItemDTOList().size(), result.getContent().get(1).orderItemDTOList().size());
    }

    @Test
    public void testCancelOrder() {
        Order order = createOrderList().get(0);
        OrderDTO orderDTO = createOrderDTOList().get(0);

        String userId = "userId";

        OrderItemDTO orderItemDTO1 = new OrderItemDTO(
                1L,
                "number",
                1L,
                "Item 1",
                BigDecimal.valueOf(100.99),
                1);

        OrderItemDTO orderItemDTO2 = new OrderItemDTO(
                1L,
                "number",
                2L,
                "Item 2",
                BigDecimal.valueOf(99.00),
                1);

        OrderDTO expectedOrderDTO = new OrderDTO(
                1L,
                "number",
                OrderStatus.CANCELLED,
                ZonedDateTime.of(LocalDateTime.of(2023, 1, 1, 0, 0), ZoneId.of("UTC")),
                BigDecimal.valueOf(199),
                List.of(orderItemDTO1, orderItemDTO2),
                null,
                "userId",
                null
        );

        when(currentUserService.getCurrentUserUuid()).thenReturn(userId);
        when(orderRepository.existsByNumberAndUserId(expectedOrderDTO.number(), userId)).thenReturn(true);
        when(orderRepository.findOrderByUserIdAndNumber(userId, order.getNumber())).thenReturn(order);
//        when(orderMapper.toDto(order)).thenReturn(orderDTO);
        when(orderRepository.save(any())).thenReturn(order);

        orderService.cancelOrder("number");

        verify(orderRepository).existsByNumberAndUserId(expectedOrderDTO.number(), userId);
        verify(currentUserService).getCurrentUserUuid();
        verify(orderRepository).findOrderByUserIdAndNumber(userId, expectedOrderDTO.number());
//        verify(orderMapper).toDto(order);
        verify(orderRepository).save(order);
    }

    @Test
    public void testConfirmOrder() {
        Order order = createOrderList().get(0);
        OrderDTO orderDTO = createOrderDTOList().get(0);

        OrderItemDTO orderItemDTO1 = new OrderItemDTO(
                1L,
                "number",
                1L,
                "Item 1",
                BigDecimal.valueOf(100.99),
                1);

        OrderItemDTO orderItemDTO2 = new OrderItemDTO(
                1L,
                "number",
                2L,
                "Item 2",
                BigDecimal.valueOf(99.00),
                1);

        OrderDTO expectedOrderDTO = new OrderDTO(
                1L,
                "number",
                OrderStatus.DONE,
                ZonedDateTime.of(LocalDateTime.of(2023, 1, 1, 0, 0), ZoneId.of("UTC")),
                BigDecimal.valueOf(199),
                List.of(orderItemDTO1, orderItemDTO2),
                null,
                "userId",
                null
        );

        when(currentUserService.getCurrentUserUuid()).thenReturn("userId");
        when(orderRepository.existsByNumberAndUserId(expectedOrderDTO.number(), "userId")).thenReturn(true);
        when(orderRepository.findOrderByUserIdAndNumber("userId", order.getNumber())).thenReturn(order);
//        when(orderMapper.toDto(order)).thenReturn(orderDTO);
        when(orderRepository.save(any())).thenReturn(order);

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
                    .filter(dto -> dto.number().equals(orderNumber))
                    .findFirst()
                    .orElse(null);
        });
        Page<OrderDTO> result = orderService.getAllOrders(Pageable.unpaged());

        verify(orderRepository).findAll(Pageable.unpaged());
        verify(orderMapper, times(2)).toDto(any(Order.class));

        assertEquals(expectedOrderDTOList.size(), result.getContent().size());

        assertEquals(expectedOrderDTOList.get(0).number(), result.getContent().get(0).number());
        assertEquals(expectedOrderDTOList.get(0).status(), result.getContent().get(0).status());
        assertEquals(expectedOrderDTOList.get(0).createdDate(), result.getContent().get(0).createdDate());
        assertEquals(expectedOrderDTOList.get(0).price(), result.getContent().get(0).price());
        assertEquals(expectedOrderDTOList.get(0).userId(), result.getContent().get(0).userId());
        assertEquals(expectedOrderDTOList.get(0).orderItemDTOList().size(), result.getContent().get(0).orderItemDTOList().size());

        assertEquals(expectedOrderDTOList.get(1).number(), result.getContent().get(1).number());
        assertEquals(expectedOrderDTOList.get(1).status(), result.getContent().get(1).status());
        assertEquals(expectedOrderDTOList.get(1).createdDate(), result.getContent().get(1).createdDate());
        assertEquals(expectedOrderDTOList.get(1).price(), result.getContent().get(1).price());
        assertEquals(expectedOrderDTOList.get(1).userId(), result.getContent().get(1).userId());
        assertEquals(expectedOrderDTOList.get(1).orderItemDTOList().size(), result.getContent().get(1).orderItemDTOList().size());
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
    public void testGetAllOrdersByUserId_WhenNoOrdersFoundForUser_ShouldReturnEmptyArray() {
        String userId = "userId";

        when(currentUserService.getCurrentUserUuid()).thenReturn(userId);
        when(orderRepository.findAllByUserId(userId, Pageable.unpaged())).thenReturn(Page.empty());

        Page<OrderDTO> result = orderService.getAllOrdersByUserId(Pageable.unpaged());

        assertEquals(Page.empty(), result);
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
        order1.setStatus(OrderStatus.NEW);
        order1.setCreatedDate(ZonedDateTime.of(LocalDateTime.of(2023, 1, 1, 0, 0),
                ZoneId.of("UTC")));
        order1.setPrice(BigDecimal.valueOf(199, 99));
        order1.setUserId("userId");

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setCount(1);
        orderItem1.setItem(item1);
        orderItem1.setOrder(order1);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setCount(1);
        orderItem2.setItem(item2);
        orderItem2.setOrder(order1);

        order1.setOrderItemList(List.of(orderItem1, orderItem2));

        Order order2 = new Order();
        order2.setNumber("number1");
        order2.setStatus(OrderStatus.NEW);
        order2.setCreatedDate(ZonedDateTime.of(LocalDateTime.of(2023, 11, 23, 1, 0),
                ZoneId.of("UTC")));
        order2.setPrice(BigDecimal.valueOf(199, 99));
        order2.setUserId("userId");

        OrderItem orderItem3 = new OrderItem();
        orderItem3.setCount(1);
        orderItem3.setItem(item1);
        orderItem3.setOrder(order2);

        OrderItem orderItem4 = new OrderItem();
        orderItem4.setCount(1);
        orderItem4.setItem(item2);
        orderItem4.setOrder(order2);

        order2.setOrderItemList(List.of(orderItem3, orderItem4));

        return List.of(order1, order2);
    }

    private List<OrderDTO> createOrderDTOList() {
        OrderItemDTO orderItemDTO1 = new OrderItemDTO(
                1L,
                "number",
                1L,
                "Item 1",
                BigDecimal.valueOf(100.99),
                1);

        OrderItemDTO orderItemDTO2 = new OrderItemDTO(
                1L,
                "number",
                2L,
                "Item 2",
                BigDecimal.valueOf(99.00),
                1);

        OrderDTO orderDTO1 = new OrderDTO(1L,
                "number",
                OrderStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 1, 1, 0, 0), ZoneId.of("UTC")),
                BigDecimal.valueOf(199),
                List.of(orderItemDTO1, orderItemDTO2),
                null,
                "userId",
                null
        );

        OrderItemDTO orderItemDTO3 = new OrderItemDTO(
                1L,
                "number1",
                1L,
                "Item 1",
                BigDecimal.valueOf(100.99),
                1);

        OrderItemDTO orderItemDTO4 = new OrderItemDTO(
                1L,
                "number1",
                2L,
                "Item 2",
                BigDecimal.valueOf(99.00),
                1);

        OrderDTO orderDTO2 = new OrderDTO(
                2L,
                "number1",
                OrderStatus.NEW,
                ZonedDateTime.of(LocalDateTime.of(2023, 1, 1, 0, 0), ZoneId.of("UTC")),
                BigDecimal.valueOf(199),
                List.of(orderItemDTO3, orderItemDTO4),
                null,
                "userId",
                null
        );

        return List.of(orderDTO1, orderDTO2);
    }
}