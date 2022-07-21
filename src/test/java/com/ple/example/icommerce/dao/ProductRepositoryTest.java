package com.ple.example.icommerce.dao;

import com.ple.example.icommerce.config.JpaConfig;
import com.ple.example.icommerce.context.SecurityContextHolder;
import com.ple.example.icommerce.dao.projection.ProductDto;
import com.ple.example.icommerce.dao.projection.ProductView;
import com.ple.example.icommerce.entity.tenant.Product;
import com.ple.example.icommerce.spec.ProductSpecifications;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(JpaConfig.class)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private static Integer shopId = 10;
    static {
        SecurityContextHolder.setShopId(shopId);
        System.out.println("ProductRepositoryTest.Init...");
    }

    @BeforeEach
    public void setup() {
        System.out.println("ProductRepositoryTest.BeforeEach...");
    }

    @Test
    public void saveProduct_When_ValidData_Expect_Success() {
        String sku = "001002";
        String name = "name 01";
        Product createdProduct = createProduct(sku, name, 100);
        assertThat(createdProduct).isNotNull();
        assertThat(createdProduct.getSku()).isEqualTo(sku);
        assertThat(createdProduct.getShopId()).isEqualTo(shopId);
    }

    @Test
    public void saveProduct_When_NameIsNull_Expect_ExceptionOccur() {
        String sku = "002002";
        Product product = Product.builder()
                .sku(sku)
                .price(12000d)
                .quantity(100)
                .build();
        assertThrows(ConstraintViolationException.class, () -> productRepository.save(product));
    }

    @Test
    public void saveProduct_When_SkuIsDuplicated_Expect_ExceptionOccur() {
        String sku = "001003";
        Product createdProduct = createProduct(sku, "name 01", 100);
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
        Product createdProduct = createProduct(sku, "name 01", 100);
        assertThat(createdProduct).isNotNull();
        assertThat(createdProduct.getKey()).isNotNull();
        Long key = createdProduct.getKey();

        Optional<Product> foundProduct = productRepository.findById(key);
        assertTrue(foundProduct.isPresent());
        assertThat(foundProduct.get().getShopId()).isEqualTo(shopId);
    }

    @Test
    public void findProductBySku_When_SkuNotFound_Expect_Success() {
        Product product = productRepository.findBySku("000000");
        assertThat(product).isNull();
    }

    @Test
    public void findProductBySku_When_SkuIsExisted_Expect_Success() {
        String sku = "001005";
        Product createdProduct = createProduct(sku, "name 01", 100);
        assertThat(createdProduct).isNotNull();
        assertThat(createdProduct.getKey()).isNotNull();

        Product foundProduct = productRepository.findBySku(sku);
        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getShopId()).isEqualTo(shopId);
    }

    @Test
    public void findProductByKey_When_KeyExpected_And_UseProjection_Success() {
        String sku = "001005";
        String name = "name 02";
        int quantity = 100;
        Product createdProduct = createProduct(sku, name, quantity);
        assertThat(createdProduct).isNotNull();
        Long key = createdProduct.getKey();
        assertThat(key).isNotNull();

        ProductView foundProduct = productRepository.findByKey(key);
        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getKey()).isEqualTo(key);
        assertThat(foundProduct.getSku()).isEqualTo(sku);
        assertThat(foundProduct.getDetail()).isEqualTo(name + " - " + quantity);
        assertThat(foundProduct.getShopId()).isEqualTo(shopId);
    }

    @Test
    public void findProductBySku_When_SkuExpected_And_UseDynamicProjection_Success() {
        String sku = "001005";
        String name = "name 02";
        int quantity = 100;
        Product createdProduct = createProduct(sku, name, quantity);
        assertThat(createdProduct).isNotNull();
        Long key = createdProduct.getKey();
        assertThat(key).isNotNull();

        // return entity type
        Product foundProduct = productRepository.findBySku(sku, Product.class);
        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getSku()).isEqualTo(sku);
        assertThat(foundProduct.getShopId()).isEqualTo(shopId);

        // return interface projection
        ProductView productView = productRepository.findBySku(sku, ProductView.class);
        assertThat(productView).isNotNull();
        assertThat(productView.getSku()).isEqualTo(sku);
        assertThat(foundProduct.getShopId()).isEqualTo(shopId);

        // return class projection
        ProductDto productDto = productRepository.findBySku(sku, ProductDto.class);
        assertThat(productDto).isNotNull();
        assertThat(productDto.getSku()).isEqualTo(sku);
        assertThat(productDto.getShopId()).isEqualTo(shopId);
    }

    @Test
    public void findAllProduct_When_UseSpecific_Success() {
        Product product01 = createProduct("001006", "name 001006", 100);
        assertThat(product01).isNotNull();
        Long key01 = product01.getKey();
        assertThat(key01).isNotNull();

        Product product02 = createProduct("00100", "name 001007", 100);
        assertThat(product02).isNotNull();
        Long key02 = product02.getKey();
        assertThat(key02).isNotNull();

        Specification<Product> specs = ProductSpecifications.nameLike("001006");
        Sort sort = Sort.by(Sort.Direction.ASC, "key");
        List<ProductView> list = productRepository.findAll(specs, 0, 100, sort, ProductView.class);
        System.out.println("list = " + list);

        assertThat(list.size()).isEqualTo(1);
        ProductView foundProductView = list.get(0);
        assertThat(foundProductView.getKey()).isEqualTo(key01);
        assertThat(foundProductView.getShopId()).isEqualTo(shopId);
        System.out.println("list.get(0).getDetail() = " + foundProductView.getDetail());
    }


    // --------------------
    // PRIVATE METHOD
    // --------------------

    private Product createProduct(String sku, String name, int quantity) {
        Product product = Product.builder()
                .name(name)
                .sku(sku)
                .price(12000d)
                .quantity(quantity).build();
        return productRepository.save(product);
    }

}