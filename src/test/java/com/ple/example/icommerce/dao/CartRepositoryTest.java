package com.ple.example.icommerce.dao;

import com.ple.example.icommerce.config.JpaConfig;
import com.ple.example.icommerce.entity.Cart;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(JpaConfig.class)
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Test
    public void saveCart_When_ValidData_Expect_Success() {
        Cart cart = new Cart();
        Cart createdCart = cartRepository.save(cart);
        assertThat(createdCart).isNotNull();
        assertThat(createdCart.getKey()).isGreaterThan(0);
    }

    @Test
    public void findCartByKey_When_KeyNotFound_Expect_Success() {
        Optional<Cart> cart = cartRepository.findById(0L);
        assertFalse(cart.isPresent());
    }

    @Test
    public void findCartByKey_When_KeyIsExisted_Expect_Success() {
        Cart cart = new Cart();
        Cart createdCart = cartRepository.save(cart);
        assertThat(createdCart).isNotNull();
        assertThat(createdCart.getKey()).isGreaterThan(0);
        Long key = createdCart.getKey();

        Optional<Cart> foundCart = cartRepository.findById(key);
        assertTrue(foundCart.isPresent());
    }

}
