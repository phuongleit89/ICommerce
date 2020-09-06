package com.ple.example.icommerce.dao;

import com.ple.example.icommerce.config.JpaConfig;
import com.ple.example.icommerce.entity.Order;
import com.ple.example.icommerce.entity.OrderItem;
import com.ple.example.icommerce.entity.OrderStatus;
import com.ple.example.icommerce.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(JpaConfig.class)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    private Long productKey;
    private Product product;
    private Order order;
    private Set<OrderItem> orderItems;
    private OrderItem orderItem;


    @BeforeEach
    public void setup() {
        product = Product.builder()
                .name("product-name")
                .price(12000d)
                .quantity(100)
                .sku(UUID.randomUUID().toString()).build();
        product = productRepository.save(product);
        productKey = product.getKey();

        order = Order.builder()
                .name("order-name")
                .address("order-address")
                .city("order-city")
                .status(OrderStatus.INITIATE)
                .totalPrice(BigDecimal.ZERO).build();

        orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setPrice(12000d);
        orderItem.setQuantity(12);

        orderItems = new HashSet<>();
    }

    @Test
    public void saveOrder_When_OrderItemEmpty_Expect_Success() {
        Order createdOrder = orderRepository.save(order);
        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getKey()).isGreaterThan(0);
    }

    @Test
    public void saveOrder_When_OrderItemNotEmpty_Expect_Success() {
        // given
        order.setOrderItems(orderItems);
        orderItems.add(orderItem);

        // then
        Order createdOrder = orderRepository.save(order);
        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getKey()).isGreaterThan(0);
        assertThat(createdOrder.getOrderItems()).isNotEmpty();
        assertThat(createdOrder.getOrderItems().size()).isEqualTo(1);
    }

    @Test
    public void findOrderByKey_When_KeyNotFound_Expect_Success() {
        Optional<Order> order = orderRepository.findById(0L);
        assertFalse(order.isPresent());
    }

    @Test
    public void findOrderByKey_When_KeyIsExisted_Expect_Success() {
        // given
        order.setOrderItems(orderItems);
        orderItems.add(orderItem);

        Order createdOrder = orderRepository.save(order);
        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getKey()).isGreaterThan(0);
        Long key = createdOrder.getKey();

        // then
        Optional<Order> foundOrder = orderRepository.findById(key);
        assertTrue(foundOrder.isPresent());
    }

}
