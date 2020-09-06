package com.ple.example.icommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductPageResults {

    @JsonProperty("products")
    private List<ProductResponse> products;

    @JsonProperty("currentPage")
    private int currentPage;

    @JsonProperty("totalItems")
    private long totalItems;

    @JsonProperty("totalPages")
    private int totalPages;

}
