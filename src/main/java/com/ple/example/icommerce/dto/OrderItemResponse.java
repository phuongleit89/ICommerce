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
public class OrderItemResponse {

    @JsonProperty(value = "product")
    private ProductResponse product;

    @JsonProperty(value = "quantity")
    private Integer quantity;

    @JsonProperty(value = "price")
    private Double price;

}
