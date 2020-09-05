package com.ple.example.icommerce.service.impl;

import com.ple.example.icommerce.dao.CartItemRepository;
import com.ple.example.icommerce.dao.CartRepository;
import com.ple.example.icommerce.dao.ProductRepository;
import com.ple.example.icommerce.dto.CartItemRequest;
import com.ple.example.icommerce.entity.Cart;
import com.ple.example.icommerce.entity.CartItem;
import com.ple.example.icommerce.entity.Product;
import com.ple.example.icommerce.exp.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CartServiceTest {

    @Autowired
    private CartServiceImpl cartService;

    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private CartItemRepository cartItemRepository;

    @MockBean
    private ProductRepository productRepository;


    @Test
    public void createCart_When_StoreSuccess_Expect_Success () {
        // given
        Cart cartMock = new Cart();

        // when
        when(cartRepository.save(any())).thenReturn(cartMock);

        // then
        Cart cart = cartService.create();
        assertThat(cart).isNotNull();
        verify(cartRepository).save(any());
    }

    @Test
    public void addProductToCart_When_CartNotFound_Expect_ExceptionOccur () {
        // given
        Long cardKey = 1L;
        CartItemRequest cartItemRequest = new CartItemRequest();

        // when
        when(cartRepository.findById(eq(cardKey))).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> cartService.addProduct(cardKey, cartItemRequest));
        verify(cartRepository).findById(eq(cardKey));
        verify(cartRepository, never()).save(any());
    }

    @Test
    public void addProductToCart_When_CartItemNotFound_Expect_Success () {
        // given
        Long cardKey = 1L;
        Long productKey = 1L;
        CartItemRequest cartItemRequest = CartItemRequest.builder()
                .productKey(productKey)
                .quantity(10).build();

        Cart cartMock = new Cart();
        cartMock.setKey(cardKey);

        Product productMock = new Product();
        productMock.setKey(productKey);

        // when
        when(cartRepository.findById(eq(cardKey))).thenReturn(Optional.of(cartMock));
        when(productRepository.getOne(eq(productKey))).thenReturn(productMock);

        // then
        Cart cart = cartService.addProduct(cardKey, cartItemRequest);
        assertThat(cart).isNotNull();
        verify(cartRepository).findById(eq(cardKey));
        verify(cartItemRepository).save(any());
        verify(productRepository).getOne(eq(productKey));
    }

    @Test
    public void addProductToCart_When_CartItemFound_Expect_Success () {
        // given
        Long cardKey = 1L;
        Long productKey = 1L;
        int quantity = 10;
        int currentItemQuantity = 1;
        CartItemRequest cartItemRequest = CartItemRequest.builder()
                .productKey(productKey)
                .quantity(10).build();

        Product productMock = new Product();
        productMock.setKey(productKey);

        Cart cartMock = new Cart();
        cartMock.setKey(cardKey);
        Set<CartItem> cartItems = new HashSet<>();
        cartMock.setCartItems(cartItems);

        CartItem cartItemMock = new CartItem();
        cartItemMock.setCart(cartMock);
        cartItemMock.setKey(1L);
        cartItemMock.setProduct(productMock);
        cartItemMock.setQuantity(currentItemQuantity);
        cartItems.add(cartItemMock);

        // when
        when(cartRepository.findById(eq(cardKey))).thenReturn(Optional.of(cartMock));

        // then
        Cart cart = cartService.addProduct(cardKey, cartItemRequest);
        assertThat(cart).isNotNull();
        assertThat(cart.getCartItems().size() == 1);
        assertThat(cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getQuantity() == (quantity + currentItemQuantity))
                .findFirst());
        verify(cartRepository).findById(eq(cardKey));
        verify(cartItemRepository).save(any());
        verify(productRepository, never()).getOne(eq(productKey));
    }

    @Test
    public void updateProductQuantity_When_CartNotFound_Expect_ExceptionOccur() {
        // given
        Long cardKey = 1L;
        Long productKey = 1L;
        int quantity = 10;

        // when
        when(cartRepository.findById(eq(cardKey))).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> cartService.updateProductQuantity(cardKey, productKey, quantity));
        verify(cartRepository).findById(eq(cardKey));
        verify(cartRepository, never()).save(any());
    }

    @Test
    public void updateProductQuantity_When_CartItemNotFound_Expect_NotUpdateCartItemQuantity() {
        // given
        Long cardKey = 1L;
        Long productKey = 1L;
        int quantity = 10;

        Cart cartMock = new Cart();
        cartMock.setKey(cardKey);

        // when
        when(cartRepository.findById(eq(cardKey))).thenReturn(Optional.of(cartMock));

        // then
        Cart cart = cartService.updateProductQuantity(cardKey, productKey, quantity);
        assertThat(cart).isNotNull();
        verify(cartRepository).findById(eq(cardKey));
        verify(cartItemRepository, never()).save(any());
    }

    @Test
    public void updateProductQuantity_When_CartItemFound_Expect_CartItemQuantityUpdated() {
        // given
        Long cardKey = 1L;
        Long productKey = 1L;
        int quantity = 10;

        Product productMock = new Product();
        productMock.setKey(productKey);

        Cart cartMock = new Cart();
        cartMock.setKey(cardKey);
        Set<CartItem> cartItems = new HashSet<>();
        cartMock.setCartItems(cartItems);

        CartItem cartItemMock = new CartItem();
        cartItemMock.setCart(cartMock);
        cartItemMock.setKey(1L);
        cartItemMock.setProduct(productMock);
        cartItemMock.setQuantity(1);
        cartItems.add(cartItemMock);

        // when
        when(cartRepository.findById(eq(cardKey))).thenReturn(Optional.of(cartMock));

        // then
        Cart cart = cartService.updateProductQuantity(cardKey, productKey, quantity);
        assertThat(cart).isNotNull();
        assertThat(cart.getCartItems().size() == 1);
        assertThat(cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getQuantity() == (quantity))
                .findFirst());
        verify(cartRepository).findById(eq(cardKey));
        verify(cartItemRepository).save(any());
    }

    @Test
    public void removeProduct_When_CartNotFound_Expect_ExceptionOccur() {
        // given
        Long cardKey = 1L;
        Long productKey = 1L;

        // when
        when(cartRepository.findById(eq(cardKey))).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> cartService.removeProduct(cardKey, productKey));
        verify(cartRepository).findById(eq(cardKey));
        verify(cartRepository, never()).delete(any());
    }

    @Test
    public void removeProduct_When_CartItemNotFound_Expect_NotDeleteCartItem() {
        // given
        Long cardKey = 1L;
        Long productKey = 1L;

        Cart cartMock = new Cart();
        cartMock.setKey(cardKey);

        // when
        when(cartRepository.findById(eq(cardKey))).thenReturn(Optional.of(cartMock));

        // then
        Cart cart = cartService.removeProduct(cardKey, productKey);
        assertThat(cart).isNotNull();
        verify(cartRepository).findById(eq(cardKey));
        verify(cartItemRepository, never()).delete(any());
    }

    @Test
    public void removeProduct_When_CartItemFound_Expect_CartItemDelete() {
        // given
        Long cardKey = 1L;
        Long productKey = 1L;

        Product productMock = new Product();
        productMock.setKey(productKey);

        Cart cartMock = new Cart();
        cartMock.setKey(cardKey);
        Set<CartItem> cartItems = new HashSet<>();
        cartMock.setCartItems(cartItems);

        CartItem cartItemMock = new CartItem();
        cartItemMock.setCart(cartMock);
        cartItemMock.setKey(1L);
        cartItemMock.setProduct(productMock);
        cartItemMock.setQuantity(1);
        cartItems.add(cartItemMock);

        // when
        when(cartRepository.findById(eq(cardKey))).thenReturn(Optional.of(cartMock));

        // then
        Cart cart = cartService.removeProduct(cardKey, productKey);
        assertThat(cart).isNotNull();
        assertThat(cart.getCartItems().size() == 0);
        verify(cartRepository).findById(eq(cardKey));
        verify(cartItemRepository).delete(any());
    }

    @Test
    public void get_When_CartNotFound_Expect_ExceptionOccur() {
        // given
        Long cardKey = 1L;

        // when
        when(cartRepository.findById(eq(cardKey))).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> cartService.get(cardKey));
        verify(cartRepository).findById(eq(cardKey));
    }

    @Test
    public void get_When_CartFound_Expect_Success() {
        // given
        Long cardKey = 1L;

        Cart cartMock = new Cart();
        cartMock.setKey(cardKey);

        // when
        when(cartRepository.findById(eq(cardKey))).thenReturn(Optional.of(cartMock));

        // then
        Cart cart = cartService.get(cardKey);
        assertThat(cart).isNotNull();
        verify(cartRepository).findById(eq(cardKey));
    }

}
