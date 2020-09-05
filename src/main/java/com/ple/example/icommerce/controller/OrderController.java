package com.ple.example.icommerce.controller;

import com.ple.example.icommerce.dto.OrderRequest;
import com.ple.example.icommerce.dto.OrderResponse;
import com.ple.example.icommerce.dto.OrderStatusRequest;
import com.ple.example.icommerce.entity.Order;
import com.ple.example.icommerce.service.OrderService;
import com.ple.example.icommerce.utils.ModelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(value = "/order")
@Validated
@Slf4j
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create/{card_key}")
    public ResponseEntity<OrderResponse> createOrder(@NotNull @Min(value = 1) @PathVariable("card_key") Long cartKey,
                                                     @RequestBody @Valid @NotNull OrderRequest order){
        Order orderResponse = orderService.createOrder(cartKey, order);
        return response(orderResponse);
    }

    @PutMapping("/{key}/status")
    public ResponseEntity<OrderResponse> createOrder(@NotNull @Min(value = 1) @PathVariable("key") Long orderKey,
                                                     @RequestBody @Valid @NotNull OrderStatusRequest statusRequest){
        Order orderResponse = orderService.updateOrderStatus(orderKey, statusRequest);
        return response(orderResponse);
    }

    @GetMapping("/{key}")
    public ResponseEntity<OrderResponse> get(@NotNull @Min(value = 1) @PathVariable("key") Long orderKey){
        return response(orderService.get(orderKey));
    }

    private ResponseEntity<OrderResponse> response(Order order) {
        return ResponseEntity.ok(ModelUtils.toOrderResponse(order));
    }

}
