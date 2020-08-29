package com.ple.example.icommerce.controller;


import com.ple.example.icommerce.dto.ProductRequest;
import com.ple.example.icommerce.dto.ProductResponse;
import com.ple.example.icommerce.entity.Product;
import com.ple.example.icommerce.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;


    @PostMapping("/add")
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest productRequest) {
        Product createdProduct = productService.create(productRequest);
        if (createdProduct == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fromEntity(createdProduct));
    }

    @GetMapping("/{key}")
    public ResponseEntity<ProductResponse> get(@PathVariable("key") Long key) {
        Optional<Product> product = productService.get(key);
        if (product.isPresent()) {
            return ResponseEntity.ok(fromEntity(product.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{key}")
    public ResponseEntity<ProductResponse> update(@PathVariable("key") Long key,
                                                  @RequestBody ProductRequest productRequest ) {
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
