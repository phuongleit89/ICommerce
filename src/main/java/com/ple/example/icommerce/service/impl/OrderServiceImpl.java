package com.ple.example.icommerce.service.impl;

import com.ple.example.icommerce.dao.CartRepository;
import com.ple.example.icommerce.dao.OrderRepository;
import com.ple.example.icommerce.dao.ProductRepository;
import com.ple.example.icommerce.dto.OrderRequest;
import com.ple.example.icommerce.dto.OrderStatusRequest;
import com.ple.example.icommerce.entity.Cart;
import com.ple.example.icommerce.entity.CartItem;
import com.ple.example.icommerce.entity.Order;
import com.ple.example.icommerce.entity.OrderItem;
import com.ple.example.icommerce.entity.OrderStatus;
import com.ple.example.icommerce.entity.tenant.Product;
import com.ple.example.icommerce.exp.CommerceBadRequestException;
import com.ple.example.icommerce.exp.NotFoundException;
import com.ple.example.icommerce.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            CartRepository cartRepository,
                            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
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
        order.setTotalPrice(BigDecimal.ZERO);

        cartItems.forEach(cartItem -> {
            Integer quantity = cartItem.getQuantity();
            Product product = cartItem.getProduct();
            if (!checkStock(quantity, product)) {
                throw new CommerceBadRequestException(CommerceBadRequestException.PRODUCT_QUANTITY_OUT_OF_STOCK);
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);

            orderItem.setProduct(product);
            Double price = product.getPrice();
            orderItem.setPrice(price);

            orderItem.setQuantity(quantity);
            orderItems.add(orderItem);
            order.setTotalPrice(order.getTotalPrice().add(BigDecimal.valueOf(price * quantity)));

            updateProductStock(quantity, product);
        });

        Order createdOrder = orderRepository.save(order);
        cartRepository.delete(cart);

        return createdOrder;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public Order updateOrderStatus(Long orderKey, OrderStatusRequest statusRequest) {
        Order order = orderRepository.findById(orderKey).orElseThrow(NotFoundException::new);
        OrderStatus status = order.getStatus();
        if (!isAllowUpdateStatus(status)) {
            throw new CommerceBadRequestException(CommerceBadRequestException.NOT_ALLOW_UPDATE_ORDER_STATUS);
        }

        order.setStatus(statusRequest.getStatus());
        return orderRepository.save(order);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, readOnly = true)
    public Order get(Long orderKey) {
        return orderRepository.findById(orderKey).orElseThrow(NotFoundException::new);
    }

    private boolean isAllowUpdateStatus(OrderStatus status) {
        return status != OrderStatus.CANCELED && status != OrderStatus.FINISHED;
    }

    private void updateProductStock(Integer quantity, Product product) {
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
    }

    private boolean checkStock(Integer quantity, Product product) {
        return quantity <= product.getQuantity();
    }

}
