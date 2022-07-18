package com.ple.example.icommerce.dao;

import com.ple.example.icommerce.dao.custom.BaseRepository;
import com.ple.example.icommerce.entity.PriceHistory;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceHistoryRepository extends BaseRepository<PriceHistory, Long> {

}
