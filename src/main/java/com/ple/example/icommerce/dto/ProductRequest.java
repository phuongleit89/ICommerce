package com.ple.example.icommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductRequest {

    @JsonProperty("name")
    @NotNull(message = "Product name can not be null")
    @Size(max = 255, message = "Product name should not be greater than 255")
    private String name;

    @JsonProperty("sku")
    @NotNull(message = "Product SKU can not be null")
    @Size(max = 255, message = "Product SKU should not be greater than 255")
    private String sku;

    @JsonProperty("price")
    @NotNull(message = "Product price can not be null")
    private Double price;

    @JsonProperty("quantity")
    @NotNull(message = "Product quantity can not be null")
    private Integer quantity;

}
