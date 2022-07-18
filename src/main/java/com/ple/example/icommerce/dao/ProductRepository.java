package com.ple.example.icommerce.dao;

import com.ple.example.icommerce.dao.custom.BaseRepository;
import com.ple.example.icommerce.dao.projection.ProductView;
import com.ple.example.icommerce.entity.tenant.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends BaseRepository<Product, Long> {

    Product findBySku(String sku);

    // dynamic projection
    <T> T findBySku(String sku, Class<T> clazz);

    // use interface projection
    ProductView findByKey(Long key);

}