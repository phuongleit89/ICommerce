package com.ple.example.icommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductResponse {

    @JsonProperty("key")
    private Long key;

    @JsonProperty("name")
    private String name;

    @JsonProperty("sku")
    private String sku;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("quantity")
    private Integer quantity;

}
