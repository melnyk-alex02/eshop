package com.alex.eshop.service;

import com.alex.eshop.constants.OrderStatus;
import com.alex.eshop.dto.cartDTOs.CartItemDTO;
import com.alex.eshop.dto.itemDTOs.ItemDTO;
import com.alex.eshop.dto.orderDTOs.OrderDTO;
import com.alex.eshop.dto.orderDTOs.OrderItemDTO;
import com.alex.eshop.entity.CartItem;
import com.alex.eshop.entity.Category;
import com.alex.eshop.entity.Item;
import com.alex.eshop.entity.compositeIds.CartItemId;
import com.alex.eshop.exception.DataNotFoundException;
import com.alex.eshop.exception.InvalidDataException;
import com.alex.eshop.mapper.CartMapper;
import com.alex.eshop.repository.CartItemRepository;
import com.alex.eshop.repository.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CartItemAndOrderCreationServiceTests {
    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartMapper cartMapper;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private CartItemService cartItemService;

    @Mock
    private CurrentUserService currentUserService;

    @Test
    public void testGetAllCartOfUser() {
        String userId = "user";

        List<CartItem> cartItemList = createCartList();
        List<CartItemDTO> expectedCartItemDTOList = createCartDTOList();

        when(currentUserService.getCurrentUserUuid()).thenReturn(userId);
        when(cartItemRepository.findAllByUserId(userId)).thenReturn(cartItemList);
        when(cartMapper.toDto(cartItemList)).thenReturn(expectedCartItemDTOList);

        List<CartItemDTO> result = cartItemService.getCartByCurrentUser();

        verify(cartItemRepository).findAllByUserId(userId);
        verify(cartMapper).toDto(any(List.class));

        assertEquals(expectedCartItemDTOList.get(0).userId(), result.get(0).userId());
        assertEquals(expectedCartItemDTOList.get(0).itemId(), result.get(0).itemId());
        assertEquals(expectedCartItemDTOList.get(0).itemName(), result.get(0).itemName());
        assertEquals(expectedCartItemDTOList.get(0).itemPrice(), result.get(0).itemPrice());
        assertEquals(expectedCartItemDTOList.get(0).count(), result.get(0).count());

        assertEquals(expectedCartItemDTOList.get(1).userId(), result.get(1).userId());
        assertEquals(expectedCartItemDTOList.get(1).itemId(), result.get(1).itemId());
        assertEquals(expectedCartItemDTOList.get(1).itemName(), result.get(1).itemName());
        assertEquals(expectedCartItemDTOList.get(1).itemPrice(), result.get(1).itemPrice());
        assertEquals(expectedCartItemDTOList.get(1).count(), result.get(1).count());
    }


    @Test
    public void testAddItemToCart() {
        String userId = "userId";
        Long itemId = 1L;

        CartItem cartItem = createCartList().get(0);

        CartItemDTO expectedCartItemDTO = createCartDTOList().get(0);

        when(currentUserService.getCurrentUserUuid()).thenReturn(userId);
        when(cartItemRepository.existsByCartItemId(new CartItemId(userId, itemId))).thenReturn(false);
        when(itemRepository.existsById(itemId)).thenReturn(true);
        when(itemRepository.getReferenceById(itemId)).thenReturn(new Item());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartMapper.toDto(cartItem)).thenReturn(expectedCartItemDTO);

        CartItemDTO result = cartItemService.addItemToCart(itemId);

        verify(currentUserService).getCurrentUserUuid();
        verify(cartItemRepository).existsByCartItemId(new CartItemId(userId, itemId));
        verify(itemRepository).existsById(itemId);
        verify(itemRepository).getReferenceById(itemId);
        verify(cartItemRepository).save(any(CartItem.class));
        verify(cartMapper).toDto(cartItem);

        assertEquals(expectedCartItemDTO, result);
    }

    @Test
    public void testUpdateCountOfItem() {
        String userId = "userId";
        Long itemId = 1L;
        Integer count = 2;

        CartItem cartItem = createCartList().get(0);

        CartItemDTO expectedCartItemDTO = createCartDTOList().get(0);

        when(currentUserService.getCurrentUserUuid()).thenReturn(userId);
        when(cartItemRepository.existsByCartItemId(new CartItemId(userId, itemId))).thenReturn(true);
        when(cartItemRepository.findByCartItemId(new CartItemId(userId, itemId))).thenReturn(cartItem);
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartMapper.toDto(cartItem)).thenReturn(expectedCartItemDTO);

        CartItemDTO result = cartItemService.updateCountOfItem(itemId, count);

        verify(currentUserService).getCurrentUserUuid();
        verify(cartItemRepository).existsByCartItemId(new CartItemId(userId, itemId));
        verify(cartItemRepository).findByCartItemId(new CartItemId(userId, itemId));
        verify(cartItemRepository).save(any(CartItem.class));
        verify(cartMapper).toDto(cartItem);

        assertEquals(expectedCartItemDTO, result);
    }

    @Test
    public void testDeleteItemFromCart() {
        String userId = "userId";
        Long itemId = 1L;

        when(currentUserService.getCurrentUserUuid()).thenReturn(userId);
        when(cartItemRepository.existsByCartItemId(new CartItemId(userId, itemId))).thenReturn(true);

        cartItemService.deleteFromCartByItemId(itemId);

        verify(cartItemRepository).deleteCartByItemId(itemId);
    }

    @Test
    public void testCreateOrderFromCart() {
        String userId = "userId";

        List<CartItemDTO> cartItemDTOList = createCartDTOList();
        OrderDTO expectedOrderDTO = new OrderDTO(
                1L,
                "number",
                OrderStatus.NEW,
                ZonedDateTime.now(),
                BigDecimal.valueOf(301.98),
                List.of(
                        new OrderItemDTO(
                                1L,
                                "number",
                                1L,
                                "Item 1",
                                BigDecimal.valueOf(100.99),
                                2
                        ),
                        new OrderItemDTO(
                                1L,
                                "number",
                                2L,
                                "Item 2",
                                BigDecimal.valueOf(200.99),
                                2
                        )
                ),
                null,
                userId,
                null
        );

        when(currentUserService.getCurrentUserUuid()).thenReturn(userId);
        when(cartItemRepository.existsAllByUserId(userId)).thenReturn(true);
        when(cartItemService.getCartByCurrentUser()).thenReturn(cartItemDTOList);
        when(orderService.createOrder(cartItemDTOList, userId, null)).thenReturn(expectedOrderDTO);

        OrderDTO result = cartItemService.createOrderFromCart();

        verify(currentUserService, times(4)).getCurrentUserUuid();
        verify(cartItemRepository).existsAllByUserId(userId);
        verify(cartItemRepository, times(2)).findAllByUserId(userId);
        verify(orderService).createOrder(cartItemDTOList, userId, null);
        verify(cartItemRepository).deleteAllByUserId(userId);

        assertEquals(expectedOrderDTO, result);
    }

    @Test
    public void testGetAllCarts_WhenCartIsEmpty_ShouldThrowException() {
        assertEquals(cartItemService.getCartByCurrentUser(), new ArrayList<>());
    }

    @Test
    public void testAddItemToCart_WhenItemIsAlreadyInCart_ShouldThrowException() {
        String userId = "userId";

        when(currentUserService.getCurrentUserUuid()).thenReturn(userId);
        when(cartItemRepository.existsByCartItemId(new CartItemId(userId, 1L))).thenThrow(
                new InvalidDataException("Item is already in cart")
        );

        assertThrows(InvalidDataException.class, () -> cartItemService.addItemToCart(1L));
    }

    @Test
    public void testUpdateCountOfItem_WhenItemIsNotInCart_ShouldThrowException() {
        assertThrows(DataNotFoundException.class, () -> cartItemService.updateCountOfItem(1L, 2));
    }

    @Test
    public void testUpdateCountOfItem_WhenCountIsLessThanOne_ShouldThrowException() {
        Integer count = -1;
        when(currentUserService.getCurrentUserUuid()).thenReturn("userId");
        when(cartItemRepository.existsByCartItemId(new CartItemId("userId", 1L))).thenReturn(true);

        assertThrows(InvalidDataException.class, () -> cartItemService.updateCountOfItem(1L, count));
    }

    @Test
    public void testDeleteItemFromCart_WhenItemIsNotInCart_ShouldThrowException() {
        assertThrows(DataNotFoundException.class, () -> cartItemService.deleteFromCartByItemId(1L));
    }

    @Test
    public void testCreateOrderFromCart_WhenCartIsEmpty_ShouldThrowException() {
        assertThrows(DataNotFoundException.class, () -> cartItemService.createOrderFromCart());
    }

    private List<CartItem> createCartList() {
        List<Item> itemList = createItemList();

        String userId = "userId";

        CartItem cartItem1 = new CartItem();
        cartItem1.setCartItemId(new CartItemId(userId, 1L));
        cartItem1.setUserId(userId);
        cartItem1.setItem(itemList.get(0));
        cartItem1.setCount(1);

        CartItem cartItem2 = new CartItem();
        cartItem1.setCartItemId(new CartItemId(userId, 2L));
        cartItem2.setUserId(userId);
        cartItem2.setItem(itemList.get(0));
        cartItem2.setCount(1);


        return List.of(cartItem1, cartItem2);
    }

    private List<CartItemDTO> createCartDTOList() {
        List<ItemDTO> itemDTOList = createItemDTOList();

        String userId = "userId";

        CartItemDTO cartItemDTO1 = new CartItemDTO(
                userId,
                1L,
                itemDTOList.get(0).name(),
                itemDTOList.get(0).price(),
                1);

        CartItemDTO cartItemDTO2 = new CartItemDTO(
                userId,
                2L,
                itemDTOList.get(1).name(),
                itemDTOList.get(1).price(),
                1);

        return List.of(cartItemDTO1, cartItemDTO2);
    }

    private List<Item> createItemList() {
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

        return List.of(item1, item2);
    }

    private List<ItemDTO> createItemDTOList() {
        ItemDTO itemDTO1 = new ItemDTO(
                1L,
                "Item 1",
                1L,
                "Category",
                "Description 1",
                BigDecimal.valueOf(100.99),
                "Image src 1"
        );

        ItemDTO itemDTO2 = new ItemDTO(
                2L,
                "Item 2",
                1L,
                "Category",
                "Description 2",
                BigDecimal.valueOf(200.99),
                "Image src 2"
        );

        return List.of(itemDTO1, itemDTO2);
    }
}