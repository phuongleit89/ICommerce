package com.ple.example.icommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ple.example.icommerce.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderResponse {

    @JsonProperty("key")
    private Long key;

    @JsonProperty("name")
    private String name;

    @JsonProperty("address")
    private String address;

    @JsonProperty("city")
    private String city;

    @JsonProperty("zip")
    private String zip;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("status")
    private OrderStatus status;

    @JsonProperty("totalPrice")
    private BigDecimal totalPrice;

    @JsonProperty("orderItems")
    private Set<OrderItemResponse> orderItems;

}
