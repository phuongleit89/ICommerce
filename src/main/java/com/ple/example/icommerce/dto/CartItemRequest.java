package com.ple.example.icommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CartItemRequest {

    @JsonProperty(value = "productKey")
    @NotNull
    @Min(value = 1)
    private Long productKey;

    @JsonProperty(value = "quantity")
    @NotNull
    @Min(value = 1)
    private Integer quantity;

}
