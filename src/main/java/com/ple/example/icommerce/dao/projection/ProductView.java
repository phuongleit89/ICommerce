package com.ple.example.icommerce.dao.projection;

import org.springframework.beans.factory.annotation.Value;

public interface ProductView {
    // closed projection
    Long getKey();
    String getSku();
    String getName();
    Integer getQuantity();
    Integer getShopId();

    // open projection
    @Value("#{target.name + ' - ' + target.quantity}")
    String getDetail();
}
