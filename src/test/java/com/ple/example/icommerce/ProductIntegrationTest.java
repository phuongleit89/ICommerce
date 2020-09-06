package com.ple.example.icommerce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ple.example.icommerce.dao.ProductRepository;
import com.ple.example.icommerce.dto.ProductRequest;
import com.ple.example.icommerce.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;


    @Test
    public void createProduct_When_IsValidData_Expect_Success() throws Exception {
        String sku = "001002";
        ProductRequest productRequest = ProductRequest.builder()
                .name("name 01")
                .sku(sku)
                .price(12000d)
                .quantity(12).build();

        Product product = createProduct(sku, productRequest);
        assertThat(product.getSku()).isEqualTo(sku);
    }

    @Test
    public void createProduct_When_IsInvalidData_Expect_Fail() throws Exception {
        ProductRequest productRequest = ProductRequest.builder()
                .name("name 01")
                .sku("001002")
                .price(12000d)
                .quantity(-12).build();

        mvc.perform(post("/product/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().is(400));
    }

    @Test
    public void updateProduct_When_IsValidData_Expect_Success() throws Exception {
        String sku = "001003";
        ProductRequest productRequest = ProductRequest.builder()
                .name("name 01")
                .sku(sku)
                .price(12000d)
                .quantity(12).build();

        Product product = createProduct(sku, productRequest);
        assertThat(product.getSku()).isEqualTo(sku);
        Long key = product.getKey();

        String nameToUpdate = "name 02";
        productRequest.setName(nameToUpdate);

        mvc.perform(put("/product/{key}", key)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk());

        product = productRepository.findBySku(sku);
        assertThat(product.getSku()).isEqualTo(sku);
        assertThat(product.getKey()).isEqualTo(key);
        assertThat(product.getName()).isEqualTo(nameToUpdate);
    }

    @Test
    public void updateProduct_When_IsInvalidData_Expect_Fail() throws Exception {
        String sku = "001004";
        ProductRequest productRequest = ProductRequest.builder()
                .name("name 03")
                .sku(sku)
                .price(12000d)
                .quantity(12).build();

        Product product = createProduct(sku, productRequest);
        assertThat(product.getSku()).isEqualTo(sku);
        Long key = product.getKey();

        productRequest.setPrice(-12000d);

        mvc.perform(put("/product/{key}", key)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().is(400));
    }

    private Product createProduct(String sku, ProductRequest productRequest) throws Exception {
        mvc.perform(post("/product/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk());

        return productRepository.findBySku(sku);
    }

}
