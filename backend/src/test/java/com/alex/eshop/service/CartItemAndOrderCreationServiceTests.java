package com.alex.eshop.service;

import com.alex.eshop.mapper.CartMapper;
import com.alex.eshop.mapper.OrderMapper;
import com.alex.eshop.repository.CartItemRepository;
import com.alex.eshop.repository.OrderRepository;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class CartItemAndOrderCreationServiceTests {
    @Mock
    private CartItemRepository cartItemRepository;

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

//    @Test
//    public void testGetAllCartOfUser() {
//        String userId = "user";
//
//        List<CartItem> cartItemList = createCartList();
//        List<CartItemDTO> expectedCartItemDTOList = createCartDTOList();
//
//        when(currentUserService.getCurrentUserUuid()).thenReturn(userId);
//        when(cartItemRepository.existsAllByUserId(any(String.class))).thenReturn(true);
//        when(cartItemRepository.findAllByCartItemId_UserId(userId)).thenReturn(cartItemList);
//        when(cartMapper.toDto(cartItemList)).thenReturn(expectedCartItemDTOList);
//
//
//        List<CartItemDTO> result = cartAndOrderCreationService.getCartByCurrentUser();
//
//        verify(cartItemRepository).findAllByCartItemId_UserId(userId);
//        verify(cartMapper).toDto(any(List.class));
//
//        assertEquals(expectedCartItemDTOList.get(0).getUserId(), result.get(0).getUserId());
//        assertEquals(expectedCartItemDTOList.get(0).getId(), result.get(0).getId());
//        assertEquals(expectedCartItemDTOList.get(0).getItemId(), result.get(0).getItemId());
//        assertEquals(expectedCartItemDTOList.get(0).getItemName(), result.get(0).getItemName());
//        assertEquals(expectedCartItemDTOList.get(0).getItemPrice(), result.get(0).getItemPrice());
//        assertEquals(expectedCartItemDTOList.get(0).getCount(), result.get(0).getCount());
//
//        assertEquals(expectedCartItemDTOList.get(1).getUserId(), result.get(1).getUserId());
//        assertEquals(expectedCartItemDTOList.get(1).getId(), result.get(1).getId());
//        assertEquals(expectedCartItemDTOList.get(1).getItemId(), result.get(1).getItemId());
//        assertEquals(expectedCartItemDTOList.get(1).getItemName(), result.get(1).getItemName());
//        assertEquals(expectedCartItemDTOList.get(1).getItemPrice(), result.get(1).getItemPrice());
//        assertEquals(expectedCartItemDTOList.get(1).getCount(), result.get(1).getCount());
//    }
//
//
//    @Test
//    public void testAddItemToCart() {
//        String userId = "userId";
//
//        Long itemId = 1L;
//
//        CartItemDTO expectedCartItemDTO = createCartDTOList().get(0);
//
//        CartItem cartItemToSave = createCartList().get(0);
//
//        CartItem savedCartItem = createCartList().get(0);
//
//        when(currentUserService.getCurrentUserUuid()).thenReturn(userId);
//        when(cartMapper.toEntity(any(CartItemDTO.class))).thenReturn(cartItemToSave);
//        when(cartItemRepository.existsByItemIdAndUserId(itemId, userId)).thenReturn(false);
//        when(cartItemRepository.save(cartItemToSave)).thenReturn(savedCartItem);
//        when(cartMapper.toDto(savedCartItem)).thenReturn(expectedCartItemDTO);
//
//
//        CartItemDTO result = cartAndOrderCreationService.addItemToCart(itemId);
//
//        verify(currentUserService).getCurrentUserUuid();
//        verify(cartMapper).toEntity(any(CartItemDTO.class));
//        verify(cartItemRepository).existsByItemIdAndUserId(itemId, userId);
//        verify(cartItemRepository).save(cartItemToSave);
//        verify(cartMapper).toDto(savedCartItem);
//
//        assertThat(expectedCartItemDTO.getUserId()).isEqualTo(userId);
//
//        assertEquals(expectedCartItemDTO.getId(), result.getId());
//        assertEquals(expectedCartItemDTO.getUserId(), result.getUserId());
//        assertEquals(expectedCartItemDTO.getItemId(), result.getItemId());
//        assertEquals(expectedCartItemDTO.getItemName(), result.getItemName());
//        assertEquals(expectedCartItemDTO.getItemPrice(), result.getItemPrice());
//        assertEquals(expectedCartItemDTO.getCount(), result.getCount());
//    }
//
//    @Test
//    public void testUpdateCountOfItem() {
//        String userId = "userId";
//        Long itemId = 1L;
//        Integer count = 2;
//
//        CartItem cartItem = createCartList().get(0);
//
//        CartItemDTO cartItemDTO = createCartDTOList().get(0);
//
//        cartItemDTO.setCount(count);
//
//        CartItem cartItemToSave = cartItem;
//        cartItemToSave.setCount(count);
//
//        when(currentUserService.getCurrentUserUuid()).thenReturn(userId);
//        when(cartItemRepository.existsByItemIdAndUserId(itemId, userId)).thenReturn(true);
//        when(cartItemRepository.findCartByItemIdAndUserId(itemId, userId)).thenReturn(cartItem);
//        when(cartMapper.toEntity(any(CartItemDTO.class))).thenReturn(cartItemToSave);
//        when(cartItemRepository.save(any())).thenReturn(cartItemToSave);
//        when(cartMapper.toDto(any(CartItem.class))).thenReturn(cartItemDTO);
//
//        CartItemDTO result = cartAndOrderCreationService.updateCountOfItem(itemId, count);
//
//        verify(currentUserService).getCurrentUserUuid();
//        verify(cartItemRepository).existsByItemIdAndUserId(itemId, userId);
//        verify(cartItemRepository).findCartByItemIdAndUserId(itemId, userId);
//        verify(cartMapper).toEntity(any(CartItemDTO.class));
//        verify(cartItemRepository).save(any());
//        verify(cartMapper, times(2)).toDto(any(CartItem.class));
//
//        assertEquals(count, result.getCount());
//    }
//
//    @Test
//    public void testDeleteItemFromCart() {
//        String userId = "userId";
//        Long itemId = 1L;
//
//        when(currentUserService.getCurrentUserUuid()).thenReturn(userId);
//        when(cartItemRepository.existsByItemIdAndUserId(itemId, userId)).thenReturn(true);
//
//        cartAndOrderCreationService.deleteItemFromCart(itemId);
//
//        verify(cartItemRepository).deleteCartByItemId(itemId);
//    }
//
//    @Test
//    public void testCreateOrderFromCart() {
//        String userId = "userId";
//
//        List<CartItem> cartItemList = createCartList();
//
//        List<CartItemDTO> cartItemDTOList = createCartDTOList();
//
//        Integer count = 0;
//        BigDecimal price = BigDecimal.ZERO;
//
//        Order order = new Order();
//        order.setNumber("number");
//        order.setCreatedDate(ZonedDateTime.now());
//        order.setStatus(OrderStatus.NEW);
//        order.setUserId(userId);
//
//        List<OrderItemDTO> orderItemList = new ArrayList<>();
//        for (CartItemDTO cartItemDTO : cartItemDTOList) {
//            OrderItemDTO orderItem = new OrderItemDTO();
//
//            orderItem.setOrder(order);
//            if (cartItemDTO.getCount() < 1) {
//                throw new InvalidDataException("Please check count of items in your cart");
//            }
//            orderItem.setCount(cartItemDTO.getCount());
//
//            orderItemList.add(orderItem);
//
//
//            price = price.add(cartItemDTO.getItemPrice()).multiply(BigDecimal.valueOf(cartItemDTO.getCount()));
//            count += cartItemDTO.getCount();
//        }
//        order.setCount(count);
//        order.setPrice(price);
//        order.setOrderItemList(orderItemList);
//
//
//        OrderDTO orderDTO = new OrderDTO();
//        orderDTO.setNumber("number");
//        orderDTO.setCreatedDate(ZonedDateTime.now());
//        orderDTO.setStatus(OrderStatus.NEW);
//        orderDTO.setUserId(userId);
//
//        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
//        Integer count1 = 0;
//        BigDecimal price1 = BigDecimal.ZERO;
//        for (CartItemDTO cartItemDTO : cartItemDTOList) {
//            OrderItemDTO orderItemDTO = new OrderItemDTO();
//
//            orderItemDTO.setOrderNumber(orderDTO.getNumber());
//            orderItemDTO.setItemId(cartItemDTO.getItemId());
//            if (cartItemDTO.getCount() < 1) {
//                throw new InvalidDataException("Please check count of items in your cart");
//            }
//            orderItemDTO.setCount(cartItemDTO.getCount());
//
//            orderItemDTOList.add(orderItemDTO);
//
//
//            price1 = price1.add(cartItemDTO.getItemPrice()).multiply(BigDecimal.valueOf(cartItemDTO.getCount()));
//            count1 += cartItemDTO.getCount();
//        }
//        orderDTO.setCount(count1);
//        orderDTO.setPrice(price1);
//        orderDTO.setOrderItemDTOList(orderItemDTOList);
//
//        when(currentUserService.getCurrentUserUuid()).thenReturn(userId);
//        when(cartItemRepository.existsAllByUserId(userId)).thenReturn(true);
//        when(cartItemRepository.findAllByCartItemId_UserId(userId)).thenReturn(cartItemList);
//        when(cartMapper.toDto(cartItemList)).thenReturn(cartItemDTOList);
//
//        OrderDTO result = cartAndOrderCreationService.createOrderFromCart();
//
//        verify(currentUserService, times(3)).getCurrentUserUuid();
//        verify(cartItemRepository, times(2)).existsAllByUserId(userId);
//        verify(cartItemRepository).findAllByCartItemId_UserId(userId);
//        verify(cartMapper).toDto(cartItemList);
//
//        assertEquals(orderDTO.getCount(), result.getCount());
//        assertEquals(orderDTO.getUserId(), result.getUserId());
//        assertEquals(orderDTO.getPrice(), result.getPrice());
//        assertEquals(orderDTO.getOrderItemDTOList().size(), result.getOrderItemDTOList().size());
//    }
//
//    @Test
//    public void testGetAllCarts_WhenCartIsEmpty_ShouldThrowException() {
//        assertThrows(DataNotFoundException.class, () -> cartAndOrderCreationService.getCartByCurrentUser());
//    }
//
//    @Test
//    public void testAddItemToCart_WhenItemIsAlreadyInCart_ShouldThrowException() {
//        String userId = "userId";
//
//        when(cartItemRepository.existsByItemIdAndUserId(any(Long.class), any(String.class))).thenThrow(
//                new InvalidDataException("Item is already in cart")
//        );
//
//        when(currentUserService.getCurrentUserUuid()).thenReturn(userId);
//
//        assertThrows(InvalidDataException.class, () -> cartAndOrderCreationService.addItemToCart(1L));
//    }
//
//    @Test
//    public void testUpdateCountOfItem_WhenItemIsNotInCart_ShouldThrowException() {
//        assertThrows(DataNotFoundException.class, () -> cartAndOrderCreationService.updateCountOfItem(1L, 2));
//    }
//
//    @Test
//    public void testUpdateCountOfItem_WhenCountIsLessThanOne_ShouldThrowException() {
//        Integer count = -1;
//        when(currentUserService.getCurrentUserUuid()).thenReturn("userId");
//        when(cartItemRepository.existsByItemIdAndUserId(1L, "userId")).thenReturn(true);
//
//        assertThrows(InvalidDataException.class, () -> cartAndOrderCreationService.updateCountOfItem(1L, count));
//    }
//
//    @Test
//    public void testDeleteItemFromCart_WhenItemIsNotInCart_ShouldThrowException() {
//        assertThrows(DataNotFoundException.class, () -> cartAndOrderCreationService.deleteItemFromCart(1L));
//    }
//
//    @Test
//    public void testCreateOrderFromCart_WhenCartIsEmpty_ShouldThrowException() {
//        assertThrows(DataNotFoundException.class, () -> cartAndOrderCreationService.createOrderFromCart());
//    }
//
//    @Test
//    public void testCreateOrderFromCart_WhenCountOfItemIsLessThanOne_ShouldThrowException() {
//        List<CartItem> cartItemList = createCartList();
//
//        List<CartItemDTO> cartItemDTOList = createCartDTOList();
//
//        cartItemDTOList.get(0).setCount(0);
//
//        when(currentUserService.getCurrentUserUuid()).thenReturn("userId");
//        when(cartItemRepository.existsAllByUserId("userId")).thenReturn(true);
//        when(cartItemRepository.findAllByCartItemId_UserId("userId")).thenReturn(cartItemList);
//        when(cartMapper.toDto(cartItemList)).thenReturn(cartItemDTOList);
//
//        assertThrows(InvalidDataException.class, () -> cartAndOrderCreationService.createOrderFromCart());
//    }
//
//    private List<CartItem> createCartList() {
//        List<CartItem> cartItemList = new ArrayList<>();
//
//        List<Item> itemList = createItemList();
//
//        String userId = "userId";
//
////        CartItem cartItem1 = new CartItem();
////        cartItem1.setId(1L);
////        cartItem1.setUserId(userId);
////        cartItem1.setItem(itemList.get(0));
////        cartItem1.setCount(1);
////
////        CartItem cartItem2 = new CartItem();
////        cartItem2.setId(1L);
////        cartItem2.setUserId(userId);
////        cartItem2.setItem(itemList.get(0));
////        cartItem2.setCount(1);
////
////        cartItemList.add(cartItem1);
////        cartItemList.add(cartItem2);
//
//        return cartItemList;
//    }
//
//    private List<CartItemDTO> createCartDTOList() {
//        List<CartItemDTO> cartItemDTOList = new ArrayList<>();
//
//        List<ItemDTO> itemDTOList = createItemDTOList();
//
//        String userId = "userId";
//
//        CartItemDTO cartItemDTO1 = new CartItemDTO();
//        cartItemDTO1.setId(1L);
//        cartItemDTO1.setUserId(userId);
//        cartItemDTO1.setItemId(itemDTOList.get(0).getId());
//        cartItemDTO1.setItemName(itemDTOList.get(0).getName());
//        cartItemDTO1.setItemPrice(itemDTOList.get(0).getPrice());
//        cartItemDTO1.setCount(1);
//
//        CartItemDTO cartItemDTO2 = new CartItemDTO();
//        cartItemDTO2.setUserId(userId);
//        cartItemDTO2.setItemId(itemDTOList.get(1).getId());
//        cartItemDTO2.setItemName(itemDTOList.get(1).getName());
//        cartItemDTO2.setItemPrice(itemDTOList.get(1).getPrice());
//        cartItemDTO2.setCount(1);
//
//        cartItemDTOList.add(cartItemDTO1);
//        cartItemDTOList.add(cartItemDTO2);
//
//        return cartItemDTOList;
//    }
//
//    private List<Item> createItemList() {
//        List<Item> itemList = new ArrayList<>();
//
//        Category category = new Category();
//        category.setId(1L);
//        category.setName("Category");
//        category.setDescription("Description");
//
//        Item item1 = new Item();
//        item1.setId(1L);
//        item1.setName("Item 1");
//        item1.setDescription("Description 1");
//        item1.setCategory(category);
//        item1.setPrice(BigDecimal.valueOf(100.99));
//        item1.setImageSrc("Image src 1");
//
//        Item item2 = new Item();
//        item2.setId(2L);
//        item2.setName("Item 2");
//        item2.setDescription("Description 2");
//        item2.setCategory(category);
//        item1.setPrice(BigDecimal.valueOf(200.99));
//        item2.setImageSrc("Image src 2");
//
//        itemList.add(item1);
//        itemList.add(item2);
//
//        return itemList;
//    }
//
//    private List<ItemDTO> createItemDTOList() {
//        List<ItemDTO> itemDTOList = new ArrayList<>();
//
//        ItemDTO itemDTO1 = new ItemDTO();
//        itemDTO1.setId(1L);
//        itemDTO1.setName("Item 1");
//        itemDTO1.setDescription("Description 1");
//        itemDTO1.setCategoryId(1L);
//        itemDTO1.setCategoryName("Category");
//        itemDTO1.setPrice(BigDecimal.valueOf(100.99));
//        itemDTO1.setImageSrc("Image src 1");
//
//        ItemDTO itemDTO2 = new ItemDTO();
//        itemDTO2.setId(2L);
//        itemDTO2.setName("Item 2");
//        itemDTO2.setDescription("Description");
//        itemDTO2.setCategoryId(1L);
//        itemDTO2.setCategoryName("Category");
//        itemDTO2.setPrice(BigDecimal.valueOf(200.99));
//        itemDTO2.setImageSrc("Image src 2");
//
//        itemDTOList.add(itemDTO1);
//        itemDTOList.add(itemDTO2);
//
//        return itemDTOList;
//    }
}