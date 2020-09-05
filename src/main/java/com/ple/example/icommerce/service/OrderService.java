package com.ple.example.icommerce.service;

import com.ple.example.icommerce.dto.OrderRequest;
import com.ple.example.icommerce.dto.OrderStatusRequest;
import com.ple.example.icommerce.entity.Order;

public interface OrderService {

    Order createOrder(Long orderKey, OrderRequest orderRequest);

    Order updateOrderStatus(Long orderKey, OrderStatusRequest statusRequest);

    Order get(Long orderKey);

}
