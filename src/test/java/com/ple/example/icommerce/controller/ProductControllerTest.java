package com.ple.example.icommerce.controller;

import com.ple.example.icommerce.entity.Product;
import com.ple.example.icommerce.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ProductServiceImpl productService;

    @Test
    public void getProduct_IsExisted_Success() throws Exception {
        long key = 1;
        Product product = Product.builder()
                .key(key).build();
        when(productService.get(eq(key)))
                .thenReturn(Optional.of(product));
        mvc.perform(MockMvcRequestBuilders.get("/product/{key}", key)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key").value(key));
    }

    @Test
    public void getProduct_IsNotFound_Fail() throws Exception {
        long key = 1;
        when(productService.get(eq(key)))
                .thenReturn(Optional.empty());
        mvc.perform(MockMvcRequestBuilders.get("/product/{key}", key)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(404));
    }

}
