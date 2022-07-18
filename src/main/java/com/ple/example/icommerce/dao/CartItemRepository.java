package com.ple.example.icommerce.dao;

import com.ple.example.icommerce.dao.custom.BaseRepository;
import com.ple.example.icommerce.entity.CartItem;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends BaseRepository<CartItem, Long> {
}
