package com.ple.example.icommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ple.example.icommerce.entity.OrderStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderStatusRequest {

    @JsonProperty(value = "status")
    @NotNull
    private OrderStatus status;

}
