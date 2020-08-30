package com.ple.example.icommerce.service.impl;

import com.ple.example.icommerce.dao.ProductRepository;
import com.ple.example.icommerce.dto.ProductRequest;
import com.ple.example.icommerce.entity.Product;
import com.ple.example.icommerce.exp.CommerceBadRequestException;
import com.ple.example.icommerce.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;


    @Override
    public Product create(ProductRequest productRequest) {
        validateSku(productRequest.getSku());

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
            log.debug("Product is not found: #key: {}", key);
            return productOpt;
        }

        Product product = productOpt.get();
        if (!productRequest.getSku().equalsIgnoreCase(product.getSku())) {
            validateSku(productRequest.getSku());
        }

        BeanUtils.copyProperties(productRequest, product);
        return Optional.of(productRepository.save(product));
    }

    private void validateSku(String sku) {
        Product foundBySku = productRepository.findBySku(sku);
        if (foundBySku != null) {
            throw new CommerceBadRequestException(CommerceBadRequestException.PRODUCT_SKU_IS_EXISTING);
        }
    }

}
