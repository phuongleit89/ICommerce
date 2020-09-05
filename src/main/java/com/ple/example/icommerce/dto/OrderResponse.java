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

    @JsonProperty(value = "key")
    private Long key;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "address")
    private String address;

    @JsonProperty(value = "city")
    private String city;

    @JsonProperty(value = "zip")
    private String zip;

    @JsonProperty(value = "comment")
    private String comment;

    @JsonProperty(value = "status")
    private OrderStatus status;

    @JsonProperty(value = "totalPrice")
    private BigDecimal totalPrice;

    @JsonProperty(value = "orderItems")
    private Set<OrderItemResponse> orderItems;

}
