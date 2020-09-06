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
public class OrderRequest {

    @JsonProperty("name")
    @NotNull
    @Size(max = 255)
    private String name;

    @JsonProperty("address")
    @NotNull
    @Size(max = 255)
    private String address;

    @JsonProperty("city")
    @NotNull
    @Size(max = 255)
    private String city;

    @JsonProperty("zip")
    @Size(max = 255)
    private String zip;

    @JsonProperty("comment")
    @NotNull
    @Size(max = 1024)
    private String comment;

}
