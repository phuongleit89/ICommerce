package com.ple.example.icommerce.service.impl;

import com.ple.example.icommerce.dao.ProductRepository;
import com.ple.example.icommerce.dto.ProductRequest;
import com.ple.example.icommerce.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductServiceImpl productService;

    @MockBean
    private ProductRepository productRepository;


    @Test
    void create_WhenDataIsValid_ThenSuccess() {
        // given
        Long key = 1L;
        ProductRequest productRequest = ProductRequest.builder()
                .name("name")
                .sku("001001")
                .price(12000d)
                .quantity(12).build();
        Product productMock = new Product();
        productMock.setKey(key);
        BeanUtils.copyProperties(productRequest, productMock);

        // when
        when(productRepository.save(any())).thenReturn(productMock);

        // then
        Product createdProduct = productService.create(productRequest);
        assertThat(createdProduct).isNotNull();
        assertThat(createdProduct.getKey()).isEqualTo(key);
    }

    @Test
    void getByKey_WhenKeyExisted_ThenSuccess() {
        // given
        Long key = 1L;
        Product productMock = Product.builder()
                .key(key)
                .name("name")
                .sku("sku")
                .price(12000d)
                .quantity(100).build();
        // when
        when(productRepository.findById(eq(key))).thenReturn(Optional.of(productMock));
        // then
        Optional<Product> product = productService.get(key);
        assertThat(product.isPresent()).isTrue();
        assertThat(product.get().getKey()).isEqualTo(key);
    }

    @Test
    void getByKey_WhenKeyNotFound_ThenReturnEmpty() {
        // given
        Long key = 1L;
        // when
        when(productRepository.findById(eq(key))).thenReturn(Optional.empty());
        // then
        Optional<Product> product = productService.get(key);
        assertThat(product.isPresent()).isFalse();
    }

    @Test
    void update_WhenDataIsValid_ThenSuccess() {
        // given
        Long key = 1L;
        ProductRequest productRequest = ProductRequest.builder()
                .name("name")
                .sku("001001")
                .price(12000d)
                .quantity(12).build();
        Product productMock = new Product();
        productMock.setKey(key);
        BeanUtils.copyProperties(productRequest, productMock);

        // when
        when(productRepository.findById(eq(key))).thenReturn(Optional.of(productMock));
        when(productRepository.save(any())).thenReturn(productMock);

        // then
        Product createdProduct = productService.create(productRequest);
        assertThat(createdProduct).isNotNull();
        assertThat(createdProduct.getKey()).isEqualTo(key);
    }

}