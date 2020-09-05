package com.ple.example.icommerce.service;

import com.ple.example.icommerce.dto.CartItemRequest;
import com.ple.example.icommerce.entity.Cart;

public interface CartService {

    Cart create();

    Cart addProduct(Long cartKey, CartItemRequest cartItemRequest);

    Cart updateProductQuantity(Long cartKey, Long productKey, int quantity);

    Cart removeProduct(Long cartKey, Long productKey);

    Cart get(Long cartKey);

}
