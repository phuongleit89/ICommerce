package com.ple.example.icommerce.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
@Builder
public class ProductFilter {

    private String name;
    private String sku;
    private double minPrice;
    private Double maxPrice;
    private int minQuantity;
    private Integer maxQuantity;
    Pageable pagingSort;

}
