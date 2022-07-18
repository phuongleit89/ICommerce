package com.ple.example.icommerce.dao;

import com.ple.example.icommerce.dao.custom.BaseRepository;
import com.ple.example.icommerce.entity.Cart;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends BaseRepository<Cart, Long> {
}
