package com.ple.example.icommerce.controller;

import com.ple.example.icommerce.entity.Product;
import com.ple.example.icommerce.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
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

    private final long productKey = 1;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ProductServiceImpl productService;
    private Product productMock;

    @BeforeEach
    public void setup() {
        productMock = Product.builder()
                .key(productKey).build();
    }

    @Test
    public void getProduct_When_IsExisted_Expect_Success() throws Exception {
        when(productService.get(eq(productKey)))
                .thenReturn(Optional.of(productMock));
        mvc.perform(MockMvcRequestBuilders.get("/product/{key}", productKey)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key").value(productKey));
    }

    @Test
    public void getProduct_When_IsNotFound_Expect_Fail() throws Exception {
        when(productService.get(eq(productKey)))
                .thenReturn(Optional.empty());
        mvc.perform(MockMvcRequestBuilders.get("/product/{key}", productKey)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(404));
    }

}
