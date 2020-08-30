package com.ple.example.icommerce.dao;

import com.ple.example.icommerce.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void save_WhenValidData_ThenSuccess() {
        String sku = "001002";
        Product product = Product.builder()
                .name("name 01")
                .sku(sku)
                .price(12000d)
                .quantity(100).build();
        Product createdProduct = productRepository.save(product);
        assertThat(createdProduct).isNotNull();
        assertThat(createdProduct.getSku()).isEqualTo(sku);
    }

    @Test
    public void save_WhenSkuIsDuplicated_ThenExceptionOccur() {
        String sku = "001003";
        Product product = Product.builder()
                .name("name 01")
                .sku(sku)
                .price(12000d)
                .quantity(100).build();
        Product createdProduct = productRepository.save(product);
        assertThat(createdProduct).isNotNull();
        assertThat(createdProduct.getSku()).isEqualTo(sku);

        Product existedSkuProduct = Product.builder()
                .name("name 01")
                .sku(sku)
                .price(10000d)
                .quantity(12).build();
        assertThrows(Exception.class, () -> productRepository.save(existedSkuProduct));
    }

    @Test
    public void findById_WhenKeyNotFound_ThenSuccess() {
        Optional<Product> product = productRepository.findById(0L);
        assertFalse(product.isPresent());
    }

    @Test
    public void findById_WhenKeyIsExisted_ThenSuccess() {
        String sku = "001004";
        Product product = Product.builder()
                .name("name 01")
                .sku(sku)
                .price(12000d)
                .quantity(100).build();
        Product createdProduct = productRepository.save(product);
        assertThat(createdProduct).isNotNull();
        assertThat(createdProduct.getKey()).isNotNull();
        Long key = createdProduct.getKey();

        Optional<Product> foundProduct = productRepository.findById(key);
        assertTrue(foundProduct.isPresent());
    }

    @Test
    public void findBySku_WhenSkuNotFound_ThenSuccess() {
        Product product = productRepository.findBySku("000000");
        assertThat(product).isNull();
    }

    @Test
    public void findBySku_WhenSkuIsExisted_ThenSuccess() {
        String sku = "001005";
        Product product = Product.builder()
                .name("name 01")
                .sku(sku)
                .price(12000d)
                .quantity(100).build();
        Product createdProduct = productRepository.save(product);
        assertThat(createdProduct).isNotNull();
        assertThat(createdProduct.getKey()).isNotNull();

        Product foundProduct = productRepository.findBySku(sku);
        assertThat(foundProduct).isNotNull();
    }

}