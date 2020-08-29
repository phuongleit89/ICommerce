package com.ple.example.icommerce.service.impl;

import com.ple.example.icommerce.dao.ProductRepository;
import com.ple.example.icommerce.dto.ProductRequest;
import com.ple.example.icommerce.entity.Product;
import com.ple.example.icommerce.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;


    @Override
    public Product create(ProductRequest productRequest) {
        // TODO: validate sku is existing

        Product product = new Product();
        BeanUtils.copyProperties(productRequest, product);
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> get(Long key) {
        return productRepository.findById(key);
    }

    @Override
    public Optional<Product> update(Long key, ProductRequest productRequest) {
        Optional<Product> productOpt = get(key);
        if (!productOpt.isPresent()) {
            return productOpt;
        }

        // TODO: validate sku is existing
        Product product = productOpt.get();
        BeanUtils.copyProperties(productRequest, product);
        return Optional.of(productRepository.save(product));
    }

}
