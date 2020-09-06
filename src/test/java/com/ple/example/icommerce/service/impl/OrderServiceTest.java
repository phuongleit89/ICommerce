package com.ple.example.icommerce.service.impl;

import com.ple.example.icommerce.dao.CartRepository;
import com.ple.example.icommerce.dao.OrderRepository;
import com.ple.example.icommerce.dao.ProductRepository;
import com.ple.example.icommerce.dto.OrderRequest;
import com.ple.example.icommerce.dto.OrderStatusRequest;
import com.ple.example.icommerce.entity.Cart;
import com.ple.example.icommerce.entity.CartItem;
import com.ple.example.icommerce.entity.Order;
import com.ple.example.icommerce.entity.OrderStatus;
import com.ple.example.icommerce.entity.Product;
import com.ple.example.icommerce.exp.CommerceBadRequestException;
import com.ple.example.icommerce.exp.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderServiceTest {

    private final long cartKey = 1;
    private final long productKey = 1;
    private final long orderKey = 1;
    private final Set<CartItem> cartItemsMock = new HashSet<>();
    @Autowired
    private OrderServiceImpl orderService;
    @MockBean
    private CartRepository cartRepository;
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private ProductRepository productRepository;
    private Product productMock;
    private Cart cartMock;
    private CartItem cartItemMock;
    private Order orderMock;

    @BeforeEach
    public void setup() {
        productMock = new Product();
        productMock.setKey(productKey);
        productMock.setPrice(12000d);
        productMock.setQuantity(100);

        cartMock = new Cart();
        cartMock.setKey(cartKey);

        cartItemMock = new CartItem();
        cartItemMock.setCart(cartMock);
        cartItemMock.setKey(1L);
        cartItemMock.setProduct(productMock);
        cartItemMock.setQuantity(12);

        orderMock = new Order();
        orderMock.setKey(orderKey);
        orderMock.setStatus(OrderStatus.INITIATE);
    }

    @Test
    public void createOrder_When_StoreSuccess_Expect_Success() {
        // given
        OrderRequest orderRequest = OrderRequest.builder().build();
        cartMock.setCartItems(cartItemsMock);
        cartItemsMock.add(cartItemMock);

        // when
        when(cartRepository.findById(eq(cartKey))).thenReturn(Optional.of(cartMock));
        when(orderRepository.save(any())).thenReturn(orderMock);

        // then
        Order order = orderService.createOrder(cartKey, orderRequest);
        assertThat(order).isNotNull();
        verify(cartRepository).findById(eq(cartKey));
        verify(orderRepository).save(any());
        verify(productRepository).save(any());
        verify(cartRepository).delete(any());
    }

    @Test
    public void createOrder_When_CartNotFound_Expect_NotFoundError() {
        // given
        OrderRequest orderRequest = OrderRequest.builder().build();

        // when
        when(cartRepository.findById(eq(cartKey))).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> orderService.createOrder(cartKey, orderRequest));
        verify(cartRepository).findById(eq(cartKey));
        verify(orderRepository, never()).save(any());
        verify(productRepository, never()).save(any());
        verify(cartRepository, never()).delete(any());
    }

    @Test
    public void createOrder_When_CartWithEmptyItem_Expect_BadRequestException() {
        // given
        OrderRequest orderRequest = OrderRequest.builder().build();

        // when
        when(cartRepository.findById(eq(cartKey))).thenReturn(Optional.of(cartMock));

        // then
        CommerceBadRequestException ex = assertThrows(CommerceBadRequestException.class, () -> orderService.createOrder(cartKey, orderRequest));
        assertThat(ex.getMessage()).isEqualTo(CommerceBadRequestException.CART_ITEMS_EMPTY);
        verify(cartRepository).findById(eq(cartKey));
        verify(orderRepository, never()).save(any());
        verify(productRepository, never()).save(any());
        verify(cartRepository, never()).delete(any());
    }

    @Test
    public void createOrder_When_OrderItemQuantityOutOfStock_Expect_BadRequestException() {
        // given
        OrderRequest orderRequest = OrderRequest.builder().build();
        cartMock.setCartItems(cartItemsMock);
        cartItemsMock.add(cartItemMock);

        cartItemMock.setQuantity(10);
        productMock.setQuantity(5);

        // when
        when(cartRepository.findById(eq(cartKey))).thenReturn(Optional.of(cartMock));
        when(orderRepository.save(any())).thenReturn(orderMock);

        // when
        when(cartRepository.findById(eq(cartKey))).thenReturn(Optional.of(cartMock));

        // then
        CommerceBadRequestException ex = assertThrows(CommerceBadRequestException.class, () -> orderService.createOrder(cartKey, orderRequest));
        assertThat(ex.getMessage()).isEqualTo(CommerceBadRequestException.PRODUCT_QUANTITY_OUT_OF_STOCK);
        verify(cartRepository).findById(eq(cartKey));
        verify(orderRepository, never()).save(any());
        verify(productRepository, never()).save(any());
        verify(cartRepository, never()).delete(any());
    }

    @Test
    public void updateOrder_When_UpdateSuccess_Expect_Success() {
        // given
        OrderStatusRequest orderStatusRequest = new OrderStatusRequest();
        OrderStatus processing = OrderStatus.PROCESSING;
        orderStatusRequest.setStatus(processing);

        // when
        when(orderRepository.findById(eq(orderKey))).thenReturn(Optional.of(orderMock));
        when(orderRepository.save(any())).thenReturn(orderMock);

        // then
        Order order = orderService.updateOrderStatus(orderKey, orderStatusRequest);
        assertThat(order).isNotNull();
        assertThat(order.getStatus()).isEqualTo(processing);
        verify(orderRepository).findById(eq(orderKey));
        verify(orderRepository).save(any());
    }

    @Test
    public void updateOrder_When_UpdateFinishedOrder_Expect_Error() {
        // given
        OrderStatusRequest orderStatusRequest = new OrderStatusRequest();
        OrderStatus processing = OrderStatus.PROCESSING;
        orderStatusRequest.setStatus(processing);

        orderMock.setStatus(OrderStatus.FINISHED);

        // when
        when(orderRepository.findById(eq(orderKey))).thenReturn(Optional.of(orderMock));

        // then
        CommerceBadRequestException ex = assertThrows(CommerceBadRequestException.class, () -> orderService.updateOrderStatus(orderKey, orderStatusRequest));
        assertThat(ex.getMessage()).isEqualTo(CommerceBadRequestException.NOT_ALLOW_UPDATE_ORDER_STATUS);
        verify(orderRepository).findById(eq(orderKey));
        verify(orderRepository, never()).save(any());
    }

    @Test
    public void updateOrder_When_UpdateCanceledOrder_Expect_Error() {
        // given
        OrderStatusRequest orderStatusRequest = new OrderStatusRequest();
        OrderStatus processing = OrderStatus.PROCESSING;
        orderStatusRequest.setStatus(processing);

        orderMock.setStatus(OrderStatus.CANCELED);

        // when
        when(orderRepository.findById(eq(orderKey))).thenReturn(Optional.of(orderMock));

        // then
        CommerceBadRequestException ex = assertThrows(CommerceBadRequestException.class, () -> orderService.updateOrderStatus(orderKey, orderStatusRequest));
        assertThat(ex.getMessage()).isEqualTo(CommerceBadRequestException.NOT_ALLOW_UPDATE_ORDER_STATUS);
        verify(orderRepository).findById(eq(orderKey));
        verify(orderRepository, never()).save(any());
    }

    @Test
    public void getOrder_When_GetSuccess_Expect_OrderReturned() {
        // when
        when(orderRepository.findById(eq(orderKey))).thenReturn(Optional.of(orderMock));

        // then
        Order order = orderService.get(orderKey);
        assertThat(order).isNotNull();
        verify(orderRepository).findById(eq(orderKey));
    }

    @Test
    public void getOrder_When_OrderNotFound_Expect_NotFoundError() {
        // when
        when(orderRepository.findById(eq(orderKey))).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> orderService.get(orderKey));
        verify(orderRepository).findById(eq(orderKey));
    }

}
