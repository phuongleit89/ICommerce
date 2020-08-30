package com.ple.example.icommerce.service.impl;

import com.ple.example.icommerce.dao.ProductRepository;
import com.ple.example.icommerce.dto.ProductRequest;
import com.ple.example.icommerce.entity.Product;
import com.ple.example.icommerce.exp.CommerceBadRequestException;
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
import static org.mockito.Mockito.*;

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
    void create_WhenSkuIsDuplicate_ThenExceptionOccur() {
        // given
        Long key = 1L;
        String sku = "001001";
        ProductRequest productRequest = ProductRequest.builder()
                .name("name")
                .sku(sku)
                .price(12000d)
                .quantity(12).build();

        Product productMock = new Product();
        productMock.setKey(key);
        BeanUtils.copyProperties(productRequest, productMock);

        // when
        when(productRepository.findBySku(eq(sku))).thenReturn(productMock);

        // then
        CommerceBadRequestException ex = assertThrows(CommerceBadRequestException.class, () -> productService.create(productRequest));
        assertThat(ex.getMessage()).isEqualTo(CommerceBadRequestException.PRODUCT_SKU_IS_EXISTING);
        verify(productRepository, never()).save(any());
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
    void update_WhenSkuChangeAndValid_ThenSuccess() {
        // given
        Long key = 1L;
        String sku = "001001";
        ProductRequest productRequest = ProductRequest.builder()
                .name("name")
                .sku(sku)
                .price(12000d)
                .quantity(12).build();

        Product productMock = new Product();
        BeanUtils.copyProperties(productRequest, productMock);
        productMock.setKey(key);
        productMock.setSku("001002");

        // when
        when(productRepository.findById(eq(key))).thenReturn(Optional.of(productMock));
        when(productRepository.save(any())).thenReturn(productMock);

        // then
        Optional<Product> updatedProduct = productService.update(key, productRequest);
        assertThat(updatedProduct.isPresent()).isTrue();
        assertThat(updatedProduct.get().getKey()).isEqualTo(key);
    }

    @Test
    void update_WhenSkuChangeAndDuplicate_ThenExceptionOccur() {
        // given
        Long key = 1L;
        String sku = "001001";
        ProductRequest productRequest = ProductRequest.builder()
                .name("name")
                .sku(sku)
                .price(12000d)
                .quantity(12).build();

        Product productMock = new Product();
        BeanUtils.copyProperties(productRequest, productMock);
        productMock.setKey(key);
        productMock.setSku("001002");

        Product foundSkuProduct = new Product();
        productMock.setKey(key + 1);
        BeanUtils.copyProperties(productRequest, foundSkuProduct);

        // when
        when(productRepository.findById(eq(key))).thenReturn(Optional.of(productMock));
        when(productRepository.findBySku(eq(sku))).thenReturn(foundSkuProduct);

        // then
        CommerceBadRequestException ex = assertThrows(CommerceBadRequestException.class, () -> productService.update(key, productRequest));
        assertThat(ex.getMessage()).isEqualTo(CommerceBadRequestException.PRODUCT_SKU_IS_EXISTING);
        verify(productRepository, never()).save(any());
    }

    @Test
    void update_WhenSkuDoNotChange_ThenSuccess() {
        // given
        Long key = 1L;
        String sku = "001001";
        ProductRequest productRequest = ProductRequest.builder()
                .name("name")
                .sku(sku)
                .price(12000d)
                .quantity(12).build();

        Product productMock = new Product();
        BeanUtils.copyProperties(productRequest, productMock);
        productMock.setKey(key);

        // when
        when(productRepository.findById(eq(key))).thenReturn(Optional.of(productMock));
        when(productRepository.save(any())).thenReturn(productMock);

        // then
        Optional<Product> updatedProduct = productService.update(key, productRequest);
        assertThat(updatedProduct.isPresent()).isTrue();
        assertThat(updatedProduct.get().getKey()).isEqualTo(key);
        verify(productRepository, never()).findBySku(eq(sku));
    }

}