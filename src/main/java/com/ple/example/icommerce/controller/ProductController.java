package com.ple.example.icommerce.controller;


import com.ple.example.icommerce.dto.ProductRequest;
import com.ple.example.icommerce.dto.ProductResponse;
import com.ple.example.icommerce.entity.Product;
import com.ple.example.icommerce.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@RestController
@RequestMapping("/product")
@Validated
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;


    @PostMapping("/add")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest productRequest) {
        log.trace("Create product: {}", productRequest);
        Product createdProduct = productService.create(productRequest);
        if (createdProduct == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fromEntity(createdProduct));
    }

    @GetMapping("/{key}")
    public ResponseEntity<ProductResponse> get(@NotNull @Min(value = 0) @PathVariable("key") Long key) {
        log.trace("Get detail product: #key: {}", key);
        Optional<Product> product = productService.get(key);
        if (product.isPresent()) {
            return ResponseEntity.ok(fromEntity(product.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{key}")
    public ResponseEntity<ProductResponse> update(@NotNull @Min(value = 0) @PathVariable("key") Long key,
                                                  @Valid @RequestBody ProductRequest productRequest ) {
        log.trace("Update the existing product: #key: {}, #product: {}", key, productRequest);
        Optional<Product> product = productService.update(key, productRequest);
        if (product.isPresent()) {
            return ResponseEntity.ok(fromEntity(product.get()));
        }
        return ResponseEntity.notFound().build();
    }


    private ProductResponse fromEntity(Product product) {
        ProductResponse productResponse = new ProductResponse();
        BeanUtils.copyProperties(product, productResponse);
        return productResponse;
    }


}
