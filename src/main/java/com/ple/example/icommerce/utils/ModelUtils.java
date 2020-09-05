package com.ple.example.icommerce.utils;

import com.ple.example.icommerce.dto.CartItemResponse;
import com.ple.example.icommerce.dto.CartResponse;
import com.ple.example.icommerce.dto.ProductResponse;
import com.ple.example.icommerce.entity.Cart;
import com.ple.example.icommerce.entity.CartItem;
import com.ple.example.icommerce.entity.Product;
import org.springframework.beans.BeanUtils;

import java.util.Optional;
import java.util.stream.Collectors;

public class ModelUtils {

    public static final ProductResponse toProductResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();
        BeanUtils.copyProperties(product, productResponse);
        return productResponse;
    }

    public static final CartItemResponse toCardItemResponse (CartItem cartItem) {
        CartItemResponse cartItemResponse = new CartItemResponse();
        BeanUtils.copyProperties(cartItem, cartItemResponse);
        cartItemResponse.setProduct(toProductResponse(cartItem.getProduct()));
        return cartItemResponse;
    }

    public static final CartResponse toCartResponse(Cart cart) {
        CartResponse cartResponse = new CartResponse();
        BeanUtils.copyProperties(cart, cartResponse);

        Optional.ofNullable(cart.getCartItems()).ifPresent(cartItems -> {
            cartResponse.setCartItems(cartItems.stream().map(ModelUtils::toCardItemResponse).collect(Collectors.toSet()));
        });
        return cartResponse;
    }

}
