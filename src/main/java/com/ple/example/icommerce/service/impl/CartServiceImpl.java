package com.ple.example.icommerce.service.impl;

import com.ple.example.icommerce.dao.CartItemRepository;
import com.ple.example.icommerce.dao.CartRepository;
import com.ple.example.icommerce.dao.ProductRepository;
import com.ple.example.icommerce.dto.CartItemRequest;
import com.ple.example.icommerce.entity.Cart;
import com.ple.example.icommerce.entity.CartItem;
import com.ple.example.icommerce.exp.NotFoundException;
import com.ple.example.icommerce.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public Cart create() {
        Cart cart = new Cart();
        return cartRepository.save(cart);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public Cart addProduct(Long cartKey, CartItemRequest cartItemRequest) {
        Cart cart = cartRepository.findById(cartKey).orElseThrow(NotFoundException::new);

        Long productKey = cartItemRequest.getProductKey();
        Integer quantity = cartItemRequest.getQuantity();

        Set<CartItem> cartItems = Optional.ofNullable(cart.getCartItems()).orElse(new HashSet<>());
        Optional<CartItem> foundItem = cartItems.stream()
                .filter(cartItem -> cartItem.getProduct().getKey().compareTo(productKey) == 0).findFirst();
        if (foundItem.isPresent()) {
            CartItem cartItem = foundItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(productRepository.getOne(productKey));
            newItem.setQuantity(quantity);
            cartItems.add(newItem);
            cartItemRepository.save(newItem);
        }

        return cart;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public Cart updateProductQuantity(Long cartKey, Long productKey, int quantity) {
        Cart cart = cartRepository.findById(cartKey).orElseThrow(NotFoundException::new);

        Set<CartItem> cartItems = Optional.ofNullable(cart.getCartItems()).orElse(new HashSet<>());
        Optional<CartItem> foundItem = cartItems.stream()
                .filter(cartItem -> cartItem.getProduct().getKey().compareTo(productKey) == 0).findFirst();
        if (foundItem.isPresent()) {
            CartItem cartItem = foundItem.get();
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }

        return cart;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public Cart removeProduct(Long cartKey, Long productKey) {
        Cart cart = cartRepository.findById(cartKey).orElseThrow(NotFoundException::new);

        Set<CartItem> cartItems = Optional.ofNullable(cart.getCartItems()).orElse(new HashSet<>());
        Optional<CartItem> foundItem = cartItems.stream()
                .filter(cartItem -> cartItem.getProduct().getKey().compareTo(productKey) == 0).findFirst();
        if (foundItem.isPresent()) {
            CartItem item = foundItem.get();
            cartItemRepository.delete(item);
            cartItems.remove(item);
        }
        return cart;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, readOnly = true)
    public Cart get(Long cartKey) {
        return cartRepository.findById(cartKey).orElseThrow(NotFoundException::new);
    }

}
