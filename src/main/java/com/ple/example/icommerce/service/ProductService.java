package com.ple.example.icommerce.service;

import com.ple.example.icommerce.dto.ProductFilter;
import com.ple.example.icommerce.dto.ProductRequest;
import com.ple.example.icommerce.entity.Product;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ProductService {

    Product create(ProductRequest productRequest);

    Optional<Product> get(Long key);

    Optional<Product> update(Long key, ProductRequest productRequest);

    Page<Product> search(ProductFilter productFilter);

}
