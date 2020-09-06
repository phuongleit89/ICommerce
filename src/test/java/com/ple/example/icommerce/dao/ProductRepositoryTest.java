package com.ple.example.icommerce.dao;

import com.ple.example.icommerce.config.JpaConfig;
import com.ple.example.icommerce.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(JpaConfig.class)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void saveProduct_When_ValidData_Expect_Success() {
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
    public void saveProduct_When_NameIsNull_Expect_ExceptionOccur() {
        String sku = "002002";
        Product product = Product.builder()
                .sku(sku)
                .price(12000d)
                .quantity(100).build();
        assertThrows(ConstraintViolationException.class, () -> productRepository.save(product));
    }

    @Test
    public void saveProduct_When_SkuIsDuplicated_Expect_ExceptionOccur() {
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
    public void findProductByKey_When_KeyNotFound_Expect_Success() {
        Optional<Product> product = productRepository.findById(0L);
        assertFalse(product.isPresent());
    }

    @Test
    public void findProductByKey_When_KeyIsExisted_Expect_Success() {
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
    public void findProductBySku_When_SkuNotFound_Expect_Success() {
        Product product = productRepository.findBySku("000000");
        assertThat(product).isNull();
    }

    @Test
    public void findProductBySku_When_SkuIsExisted_Expect_Success() {
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