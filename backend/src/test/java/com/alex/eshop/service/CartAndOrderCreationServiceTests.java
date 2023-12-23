package com.alex.eshop.service;

import com.alex.eshop.constants.OrderStatus;
import com.alex.eshop.dto.cartDTOs.CartDTO;
import com.alex.eshop.dto.itemDTOs.ItemDTO;
import com.alex.eshop.dto.orderDTOs.OrderDTO;
import com.alex.eshop.dto.orderDTOs.OrderItemDTO;
import com.alex.eshop.entity.*;
import com.alex.eshop.exception.DataNotFoundException;
import com.alex.eshop.exception.InvalidDataException;
import com.alex.eshop.mapper.CartMapper;
import com.alex.eshop.mapper.OrderMapper;
import com.alex.eshop.repository.CartRepository;
import com.alex.eshop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CartAndOrderCreationServiceTests {
    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartMapper cartMapper;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private CartAndOrderCreationService cartAndOrderCreationService;

    @Mock
    private CurrentUserService currentUserService;

    @Test
    public void testGetAllCartOfUser() {
        String userId = "user";

        List<Cart> cartList = createCartList();
        List<CartDTO> expectedCartDTOList = createCartDTOList();

        when(currentUserService.getCurrentUserUuid()).thenReturn(userId);
        when(cartRepository.existsAllByUserId(any(String.class))).thenReturn(true);
        when(cartRepository.findAllByUserId(userId)).thenReturn(cartList);
        when(cartMapper.toDto(cartList)).thenReturn(expectedCartDTOList);


        List<CartDTO> result = cartAndOrderCreationService.getAllCarts();

        verify(cartRepository).findAllByUserId(userId);
        verify(cartMapper).toDto(any(List.class));

        assertEquals(expectedCartDTOList.get(0).getUserId(), result.get(0).getUserId());
        assertEquals(expectedCartDTOList.get(0).getId(), result.get(0).getId());
        assertEquals(expectedCartDTOList.get(0).getItemId(), result.get(0).getItemId());
        assertEquals(expectedCartDTOList.get(0).getItemName(), result.get(0).getItemName());
        assertEquals(expectedCartDTOList.get(0).getItemPrice(), result.get(0).getItemPrice());
        assertEquals(expectedCartDTOList.get(0).getCount(), result.get(0).getCount());

        assertEquals(expectedCartDTOList.get(1).getUserId(), result.get(1).getUserId());
        assertEquals(expectedCartDTOList.get(1).getId(), result.get(1).getId());
        assertEquals(expectedCartDTOList.get(1).getItemId(), result.get(1).getItemId());
        assertEquals(expectedCartDTOList.get(1).getItemName(), result.get(1).getItemName());
        assertEquals(expectedCartDTOList.get(1).getItemPrice(), result.get(1).getItemPrice());
        assertEquals(expectedCartDTOList.get(1).getCount(), result.get(1).getCount());
    }


    @Test
    public void testAddItemToCart() {
        String userId = "userId";

        Long itemId = 1L;

        CartDTO expectedCartDTO = createCartDTOList().get(0);

        Cart cartToSave = createCartList().get(0);

        Cart savedCart = createCartList().get(0);

        when(currentUserService.getCurrentUserUuid()).thenReturn(userId);
        when(cartMapper.toEntity(any(CartDTO.class))).thenReturn(cartToSave);
        when(cartRepository.existsByItemIdAndUserId(itemId, userId)).thenReturn(false);
        when(cartRepository.save(cartToSave)).thenReturn(savedCart);
        when(cartMapper.toDto(savedCart)).thenReturn(expectedCartDTO);


        CartDTO result = cartAndOrderCreationService.addItemToCart(itemId);

        verify(currentUserService).getCurrentUserUuid();
        verify(cartMapper).toEntity(any(CartDTO.class));
        verify(cartRepository).existsByItemIdAndUserId(itemId, userId);
        verify(cartRepository).save(cartToSave);
        verify(cartMapper).toDto(savedCart);

        assertThat(expectedCartDTO.getUserId()).isEqualTo(userId);

        assertEquals(expectedCartDTO.getId(), result.getId());
        assertEquals(expectedCartDTO.getUserId(), result.getUserId());
        assertEquals(expectedCartDTO.getItemId(), result.getItemId());
        assertEquals(expectedCartDTO.getItemName(), result.getItemName());
        assertEquals(expectedCartDTO.getItemPrice(), result.getItemPrice());
        assertEquals(expectedCartDTO.getCount(), result.getCount());
    }

    @Test
    public void testUpdateCountOfItem() {
        String userId = "userId";
        Long itemId = 1L;
        Integer count = 2;

        Cart cart = createCartList().get(0);

        CartDTO cartDTO = createCartDTOList().get(0);

        cartDTO.setCount(count);

        Cart cartToSave = cart;
        cartToSave.setCount(count);

        when(currentUserService.getCurrentUserUuid()).thenReturn(userId);
        when(cartRepository.existsByItemIdAndUserId(itemId, userId)).thenReturn(true);
        when(cartRepository.findCartByItemIdAndUserId(itemId, userId)).thenReturn(cart);
        when(cartMapper.toEntity(any(CartDTO.class))).thenReturn(cartToSave);
        when(cartRepository.save(any())).thenReturn(cartToSave);
        when(cartMapper.toDto(any(Cart.class))).thenReturn(cartDTO);

        CartDTO result = cartAndOrderCreationService.updateCountOfItem(itemId, count);

        verify(currentUserService).getCurrentUserUuid();
        verify(cartRepository).existsByItemIdAndUserId(itemId, userId);
        verify(cartRepository).findCartByItemIdAndUserId(itemId, userId);
        verify(cartMapper).toEntity(any(CartDTO.class));
        verify(cartRepository).save(any());
        verify(cartMapper, times(2)).toDto(any(Cart.class));

        assertEquals(count, result.getCount());
    }

    @Test
    public void testDeleteItemFromCart() {
        String userId = "userId";
        Long itemId = 1L;

        when(currentUserService.getCurrentUserUuid()).thenReturn(userId);
        when(cartRepository.existsByItemIdAndUserId(itemId, userId)).thenReturn(true);

        cartAndOrderCreationService.deleteItemFromCart(itemId);

        verify(cartRepository).deleteCartByItemId(itemId);
    }

    @Test
    public void testCreateOrderFromCart() {
        String userId = "userId";

        List<Cart> cartList = createCartList();

        List<CartDTO> cartDTOList = createCartDTOList();

        Integer count = 0;
        BigDecimal price = BigDecimal.ZERO;

        Order order = new Order();
        order.setNumber("number");
        order.setCreatedDate(ZonedDateTime.now());
        order.setStatus(OrderStatus.NEW);
        order.setUserId(userId);

        List<OrderItem> orderItemList = new ArrayList<>();
        for (CartDTO cartDTO : cartDTOList) {
            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            if (cartDTO.getCount() < 1) {
                throw new InvalidDataException("Please check count of items in your cart");
            }
            orderItem.setCount(cartDTO.getCount());

            orderItemList.add(orderItem);


            price = price.add(cartDTO.getItemPrice()).multiply(BigDecimal.valueOf(cartDTO.getCount()));
            count += cartDTO.getCount();
        }
        order.setCount(count);
        order.setPrice(price);
        order.setOrderItemList(orderItemList);


        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setNumber("number");
        orderDTO.setCreatedDate(ZonedDateTime.now());
        orderDTO.setStatus(OrderStatus.NEW);
        orderDTO.setUserId(userId);

        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
        Integer count1 = 0;
        BigDecimal price1 = BigDecimal.ZERO;
        for (CartDTO cartDTO : cartDTOList) {
            OrderItemDTO orderItemDTO = new OrderItemDTO();

            orderItemDTO.setOrderNumber(orderDTO.getNumber());
            orderItemDTO.setItemId(cartDTO.getItemId());
            if (cartDTO.getCount() < 1) {
                throw new InvalidDataException("Please check count of items in your cart");
            }
            orderItemDTO.setCount(cartDTO.getCount());

            orderItemDTOList.add(orderItemDTO);


            price1 = price1.add(cartDTO.getItemPrice()).multiply(BigDecimal.valueOf(cartDTO.getCount()));
            count1 += cartDTO.getCount();
        }
        orderDTO.setCount(count1);
        orderDTO.setPrice(price1);
        orderDTO.setOrderItemDTOList(orderItemDTOList);

        when(currentUserService.getCurrentUserUuid()).thenReturn(userId);
        when(cartRepository.existsAllByUserId(userId)).thenReturn(true);
        when(cartRepository.findAllByUserId(userId)).thenReturn(cartList);
        when(cartMapper.toDto(cartList)).thenReturn(cartDTOList);

        OrderDTO result = cartAndOrderCreationService.createOrderFromCart();

        verify(currentUserService, times(3)).getCurrentUserUuid();
        verify(cartRepository, times(2)).existsAllByUserId(userId);
        verify(cartRepository).findAllByUserId(userId);
        verify(cartMapper).toDto(cartList);

        assertEquals(orderDTO.getCount(), result.getCount());
        assertEquals(orderDTO.getUserId(), result.getUserId());
        assertEquals(orderDTO.getPrice(), result.getPrice());
        assertEquals(orderDTO.getOrderItemDTOList().size(), result.getOrderItemDTOList().size());
    }

    @Test
    public void testGetAllCarts_WhenCartIsEmpty_ShouldThrowException() {
        assertThrows(DataNotFoundException.class, () -> cartAndOrderCreationService.getAllCarts());
    }

    @Test
    public void testAddItemToCart_WhenItemIsAlreadyInCart_ShouldThrowException() {
        String userId = "userId";

        when(cartRepository.existsByItemIdAndUserId(any(Long.class), any(String.class))).thenThrow(
                new InvalidDataException("Item is already in cart")
        );

        when(currentUserService.getCurrentUserUuid()).thenReturn(userId);

        assertThrows(InvalidDataException.class, () -> cartAndOrderCreationService.addItemToCart(1L));
    }

    @Test
    public void testUpdateCountOfItem_WhenItemIsNotInCart_ShouldThrowException() {
        assertThrows(DataNotFoundException.class, () -> cartAndOrderCreationService.updateCountOfItem(1L, 2));
    }

    @Test
    public void testUpdateCountOfItem_WhenCountIsLessThanOne_ShouldThrowException() {
        Integer count = -1;
        when(currentUserService.getCurrentUserUuid()).thenReturn("userId");
        when(cartRepository.existsByItemIdAndUserId(1L, "userId")).thenReturn(true);

        assertThrows(InvalidDataException.class, () -> cartAndOrderCreationService.updateCountOfItem(1L, count));
    }

    @Test
    public void testDeleteItemFromCart_WhenItemIsNotInCart_ShouldThrowException() {
        assertThrows(DataNotFoundException.class, () -> cartAndOrderCreationService.deleteItemFromCart(1L));
    }

    @Test
    public void testCreateOrderFromCart_WhenCartIsEmpty_ShouldThrowException() {
        assertThrows(DataNotFoundException.class, () -> cartAndOrderCreationService.createOrderFromCart());
    }

    @Test
    public void testCreateOrderFromCart_WhenCountOfItemIsLessThanOne_ShouldThrowException() {
        List<Cart> cartList = createCartList();

        List<CartDTO> cartDTOList = createCartDTOList();

        cartDTOList.get(0).setCount(0);

        when(currentUserService.getCurrentUserUuid()).thenReturn("userId");
        when(cartRepository.existsAllByUserId("userId")).thenReturn(true);
        when(cartRepository.findAllByUserId("userId")).thenReturn(cartList);
        when(cartMapper.toDto(cartList)).thenReturn(cartDTOList);

        assertThrows(InvalidDataException.class, () -> cartAndOrderCreationService.createOrderFromCart());
    }

    private List<Cart> createCartList() {
        List<Cart> cartList = new ArrayList<>();

        List<Item> itemList = createItemList();

        String userId = "userId";

        Cart cart1 = new Cart();
        cart1.setId(1L);
        cart1.setUserId(userId);
        cart1.setItem(itemList.get(0));
        cart1.setCount(1);

        Cart cart2 = new Cart();
        cart2.setId(1L);
        cart2.setUserId(userId);
        cart2.setItem(itemList.get(0));
        cart2.setCount(1);

        cartList.add(cart1);
        cartList.add(cart2);

        return cartList;
    }

    private List<CartDTO> createCartDTOList() {
        List<CartDTO> cartDTOList = new ArrayList<>();

        List<ItemDTO> itemDTOList = createItemDTOList();

        String userId = "userId";

        CartDTO cartDTO1 = new CartDTO();
        cartDTO1.setId(1L);
        cartDTO1.setUserId(userId);
        cartDTO1.setItemId(itemDTOList.get(0).getId());
        cartDTO1.setItemName(itemDTOList.get(0).getName());
        cartDTO1.setItemPrice(itemDTOList.get(0).getPrice());
        cartDTO1.setCount(1);

        CartDTO cartDTO2 = new CartDTO();
        cartDTO2.setUserId(userId);
        cartDTO2.setItemId(itemDTOList.get(1).getId());
        cartDTO2.setItemName(itemDTOList.get(1).getName());
        cartDTO2.setItemPrice(itemDTOList.get(1).getPrice());
        cartDTO2.setCount(1);

        cartDTOList.add(cartDTO1);
        cartDTOList.add(cartDTO2);

        return cartDTOList;
    }

    private List<Item> createItemList() {
        List<Item> itemList = new ArrayList<>();

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
        item1.setPrice(BigDecimal.valueOf(200.99));
        item2.setImageSrc("Image src 2");

        itemList.add(item1);
        itemList.add(item2);

        return itemList;
    }

    private List<ItemDTO> createItemDTOList() {
        List<ItemDTO> itemDTOList = new ArrayList<>();

        ItemDTO itemDTO1 = new ItemDTO();
        itemDTO1.setId(1L);
        itemDTO1.setName("Item 1");
        itemDTO1.setDescription("Description 1");
        itemDTO1.setCategoryId(1L);
        itemDTO1.setCategoryName("Category");
        itemDTO1.setPrice(BigDecimal.valueOf(100.99));
        itemDTO1.setImageSrc("Image src 1");

        ItemDTO itemDTO2 = new ItemDTO();
        itemDTO2.setId(2L);
        itemDTO2.setName("Item 2");
        itemDTO2.setDescription("Description");
        itemDTO2.setCategoryId(1L);
        itemDTO2.setCategoryName("Category");
        itemDTO2.setPrice(BigDecimal.valueOf(200.99));
        itemDTO2.setImageSrc("Image src 2");

        itemDTOList.add(itemDTO1);
        itemDTOList.add(itemDTO2);

        return itemDTOList;
    }
}