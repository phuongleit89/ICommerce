package com.ple.example.icommerce.controller;

import com.ple.example.icommerce.dto.CartItemRequest;
import com.ple.example.icommerce.dto.CartResponse;
import com.ple.example.icommerce.entity.Cart;
import com.ple.example.icommerce.service.CartService;
import com.ple.example.icommerce.utils.ModelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/cart")
@Validated
@Slf4j
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<CartResponse> create() {
        return response(cartService.create());
    }

    @PutMapping("/{key}")
    public ResponseEntity<CartResponse> addProduct(@NotNull @Min(1) @PathVariable("key") Long cartKey,
                                                   @Valid @RequestBody CartItemRequest cartItem) {
        return response(cartService.addProduct(cartKey, cartItem));
    }

    @PutMapping("/{key}/quantity")
    public ResponseEntity<CartResponse> updateProductQuantity(@NotNull @Min(1) @PathVariable("key") Long cartKey,
                                                              @Valid @RequestBody CartItemRequest cartItem) {
        return response(cartService.updateProductQuantity(cartKey, cartItem.getProductKey(), cartItem.getQuantity()));
    }

    @DeleteMapping("/{key}/{product_key}")
    public ResponseEntity<CartResponse> removeItem(@NotNull @Min(1) @PathVariable("key") Long cartKey,
                                                   @NotNull @Min(1) @PathVariable("product_key") Long productKey) {
        return response(cartService.removeProduct(cartKey, productKey));
    }

    @GetMapping("/{key}")
    public ResponseEntity<CartResponse> get(@NotNull @Min(1) @PathVariable("key") Long cartKey) {
        return response(cartService.get(cartKey));
    }

    private ResponseEntity<CartResponse> response(Cart cart) {
        return ResponseEntity.ok(ModelUtils.toCartResponse(cart));
    }

}
