package com.ple.example.icommerce.service.impl;

import com.ple.example.icommerce.dao.CartRepository;
import com.ple.example.icommerce.dao.OrderRepository;
import com.ple.example.icommerce.dto.OrderRequest;
import com.ple.example.icommerce.dto.OrderStatusRequest;
import com.ple.example.icommerce.entity.*;
import com.ple.example.icommerce.exp.CommerceBadRequestException;
import com.ple.example.icommerce.exp.NotFoundException;
import com.ple.example.icommerce.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public Order createOrder(Long cartKey, OrderRequest orderRequest) {
        Cart cart = cartRepository.findById(cartKey).orElseThrow(NotFoundException::new);
        Set<CartItem> cartItems = cart.getCartItems();
        if (CollectionUtils.isEmpty(cartItems)) {
            throw new CommerceBadRequestException(CommerceBadRequestException.CART_ITEMS_EMPTY);
        }

        Order order = new Order();
        BeanUtils.copyProperties(orderRequest, order);

        order.setStatus(OrderStatus.INITIATE);
        Set<OrderItem> orderItems = new HashSet<>();
        order.setOrderItems(orderItems);
        order.setTotalPrice(new BigDecimal(0));

        cartItems.forEach(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            Product product = cartItem.getProduct();
            orderItem.setProduct(product);
            Double price = product.getPrice();
            orderItem.setPrice(price);
            Integer quantity = cartItem.getQuantity();
            orderItem.setQuantity(quantity);
            orderItems.add(orderItem);
            order.setTotalPrice(order.getTotalPrice().add(BigDecimal.valueOf(price * quantity)));
        });

        return orderRepository.save(order);
    }

    @Override
    public Order updateOrderStatus(Long orderKey, OrderStatusRequest statusRequest) {
        Order order = orderRepository.findById(orderKey).orElseThrow(NotFoundException::new);
        OrderStatus status = order.getStatus();
        if (!isAllowUpdateStatus(status)) {
            throw new CommerceBadRequestException(CommerceBadRequestException.NOT_ALLOW_UPDATE_ORDER_STATUS);
        }

        order.setStatus(statusRequest.getStatus());
        return orderRepository.save(order);
    }

    private boolean isAllowUpdateStatus(OrderStatus status) {
        return status != OrderStatus.CANCELED && status != OrderStatus.FINISHED;
    }

    @Override
    public Order get(Long orderKey) {
        return orderRepository.findById(orderKey).orElseThrow(NotFoundException::new);
    }

}
