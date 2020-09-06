package com.ple.example.icommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CartResponse {

    @JsonProperty("key")
    private Long key;

    @JsonProperty("cartItems")
    private Set<CartItemResponse> cartItems;

}
