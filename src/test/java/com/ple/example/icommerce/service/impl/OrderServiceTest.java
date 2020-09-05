package com.ple.example.icommerce.service.impl;

import com.ple.example.icommerce.dao.CartItemRepository;
import com.ple.example.icommerce.dao.CartRepository;
import com.ple.example.icommerce.dao.OrderRepository;
import com.ple.example.icommerce.dao.ProductRepository;
import com.ple.example.icommerce.dto.OrderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderServiceImpl orderService;

    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private CartItemRepository cartItemRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private OrderRepository orderRepository;

    private long defaultCartKey = 1;

    @Test
    public void createOrder_When_StoreSuccess_Expect_Success(){
        // given
        OrderRequest orderRequest = OrderRequest.builder().build();

        // when
        orderService.createOrder(defaultCartKey, orderRequest);

        // then

    }

    @Test
    public void createOrder_When_CartNotFound_Expect_NotFoundError(){

    }

    @Test
    public void createOrder_When_CartWithEmptyItem_Expect_NotFoundError(){

    }

    @Test
    public void updateOrder_When_UpdateSuccess_Expect_Success(){

    }

    @Test
    public void updateOrder_When_StoreError_Expect_Error(){

    }

    @Test
    public void updateOrder_When_UpdateFinishedOrder_Expect_Error(){

    }

    @Test
    public void updateOrder_When_UpdateCanceledOrder_Expect_Error(){

    }

    @Test
    public void getOrder_When_GetSuccess_Expect_OrderReturned(){

    }

    @Test
    public void getOrder_When_OrderNotFound_Expect_NotFoundError(){

    }
}
