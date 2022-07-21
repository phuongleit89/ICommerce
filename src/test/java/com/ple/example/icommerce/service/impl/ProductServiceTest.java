package com.ple.example.icommerce.service.impl;

import com.ple.example.icommerce.dao.PriceHistoryRepository;
import com.ple.example.icommerce.dao.ProductRepository;
import com.ple.example.icommerce.dto.ProductRequest;
import com.ple.example.icommerce.entity.PriceHistory;
import com.ple.example.icommerce.entity.tenant.Product;
import com.ple.example.icommerce.exp.CommerceBadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductServiceImpl productService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private PriceHistoryRepository priceHistoryRepository;


    @Test
    void createProduct_When_DataIsValid_Expect_Success() {
        // given
        Long key = 1L;
        double price = 12000d;
        ProductRequest productRequest = ProductRequest.builder()
                .name("name")
                .sku("001001")
                .price(price)
                .quantity(12).build();

        Product productMock = new Product();
        productMock.setKey(key);
        productMock.setShopId(10);
        BeanUtils.copyProperties(productRequest, productMock);

        PriceHistory priceHistoryMock = PriceHistory.builder()
                .key(1L)
                .price(price)
                .product(productMock).build();

        // when
        when(productRepository.save(any())).thenReturn(productMock);
        when(priceHistoryRepository.save(any())).thenReturn(priceHistoryMock);

        // then
        Product createdProduct = productService.create(productRequest);
        assertThat(createdProduct).isNotNull();
        assertThat(createdProduct.getKey()).isEqualTo(key);
        verify(productRepository).save(any());
        verify(priceHistoryRepository).save(any());
    }

    @Test
    void createProduct_When_SkuIsDuplicate_Expect_ExceptionOccur() {
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
        productMock.setShopId(10);
        BeanUtils.copyProperties(productRequest, productMock);

        // when
        when(productRepository.findBySku(eq(sku))).thenReturn(productMock);

        // then
        CommerceBadRequestException ex = assertThrows(CommerceBadRequestException.class, () -> productService.create(productRequest));
        assertThat(ex.getMessage()).isEqualTo(CommerceBadRequestException.PRODUCT_SKU_IS_EXISTING);
        verify(productRepository, never()).save(any());
        verify(priceHistoryRepository, never()).save(any());
    }

    @Test
    void getProductByKey_When_KeyExisted_Expect_Success() {
        // given
        Long key = 1L;
        Product productMock = Product.builder()
                .key(key)
                .name("name")
                .sku("sku")
                .price(12000d)
                .quantity(100)
                .build();
        // when
        when(productRepository.findById(eq(key))).thenReturn(Optional.of(productMock));
        // then
        Optional<Product> product = productService.get(key);
        assertThat(product.isPresent()).isTrue();
        assertThat(product.get().getKey()).isEqualTo(key);
        verify(productRepository).findById(eq(key));
    }

    @Test
    void getProductByKey_When_KeyNotFound_Expect_ReturnEmpty() {
        // given
        Long key = 1L;
        // when
        when(productRepository.findById(eq(key))).thenReturn(Optional.empty());
        // then
        Optional<Product> product = productService.get(key);
        assertThat(product.isPresent()).isFalse();
        verify(productRepository).findById(eq(key));
    }

    @Test
    void updateProduct_When_SkuChangeAndValid_Expect_Success() {
        // given
        Long key = 1L;
        String sku = "001001";
        double price = 12000d;
        ProductRequest productRequest = ProductRequest.builder()
                .name("name")
                .sku(sku)
                .price(price)
                .quantity(12).build();

        Product productMock = new Product();
        BeanUtils.copyProperties(productRequest, productMock);
        productMock.setKey(key);
        productMock.setSku("001002");
        productMock.setShopId(10);

        // when
        when(productRepository.findById(eq(key))).thenReturn(Optional.of(productMock));
        when(productRepository.save(any())).thenReturn(productMock);

        // then
        Optional<Product> updatedProduct = productService.update(key, productRequest);
        assertThat(updatedProduct.isPresent()).isTrue();
        assertThat(updatedProduct.get().getKey()).isEqualTo(key);
        verify(productRepository).findBySku(eq(sku));
        verify(productRepository).save(any());
        verify(priceHistoryRepository, never()).save(any());
    }

    @Test
    void updateProduct_When_SkuChangeAndDuplicate_Expect_ExceptionOccur() {
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
        productMock.setShopId(10);

        Product foundSkuProduct = new Product();
        productMock.setKey(key + 1);
        BeanUtils.copyProperties(productRequest, foundSkuProduct);

        // when
        when(productRepository.findById(eq(key))).thenReturn(Optional.of(productMock));
        when(productRepository.findBySku(eq(sku))).thenReturn(foundSkuProduct);

        // then
        CommerceBadRequestException ex = assertThrows(CommerceBadRequestException.class, () -> productService.update(key, productRequest));
        assertThat(ex.getMessage()).isEqualTo(CommerceBadRequestException.PRODUCT_SKU_IS_EXISTING);
        verify(productRepository).findBySku(eq(sku));
        verify(productRepository, never()).save(any());
        verify(priceHistoryRepository, never()).save(any());
    }

    @Test
    void updateProduct_When_SkuDoNotChange_Expect_Success() {
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
        productMock.setShopId(10);

        // when
        when(productRepository.findById(eq(key))).thenReturn(Optional.of(productMock));
        when(productRepository.save(any())).thenReturn(productMock);

        // then
        Optional<Product> updatedProduct = productService.update(key, productRequest);
        assertThat(updatedProduct.isPresent()).isTrue();
        assertThat(updatedProduct.get().getKey()).isEqualTo(key);
        verify(priceHistoryRepository, never()).save(any());
        verify(productRepository, never()).findBySku(eq(sku));
        verify(productRepository).save(any());
    }

    @Test
    void updateProduct_When_PriceChange_Expect_Success() {
        // given
        Long key = 1L;
        String sku = "001001";
        double price = 12000d;
        ProductRequest productRequest = ProductRequest.builder()
                .name("name")
                .sku(sku)
                .price(price)
                .quantity(12).build();

        Product productMock = new Product();
        BeanUtils.copyProperties(productRequest, productMock);
        productMock.setKey(key);
        productMock.setPrice(20000d);
        productMock.setShopId(10);

        PriceHistory priceHistory = PriceHistory.builder()
                .key(1L)
                .price(price)
                .product(productMock).build();

        // when
        when(productRepository.findById(eq(key))).thenReturn(Optional.of(productMock));
        when(productRepository.save(any())).thenReturn(productMock);
        when(priceHistoryRepository.save(any())).thenReturn(priceHistory);

        // then
        Optional<Product> updatedProduct = productService.update(key, productRequest);
        assertThat(updatedProduct.isPresent()).isTrue();
        assertThat(updatedProduct.get().getKey()).isEqualTo(key);
        verify(productRepository, never()).findBySku(any());
        verify(productRepository).save(any());
        verify(priceHistoryRepository).save(any());
    }

}