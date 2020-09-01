package com.ple.example.icommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductPageResults {

    @JsonProperty(value = "products")
    private List<ProductResponse> products;

    @JsonProperty(value = "currentPage")
    private int currentPage;

    @JsonProperty(value = "totalItems")
    private long totalItems;

    @JsonProperty(value = "totalPages")
    private int totalPages;

}
