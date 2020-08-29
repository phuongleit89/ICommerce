package com.ple.example.icommerce.dao;

import com.ple.example.icommerce.entity.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {

}
