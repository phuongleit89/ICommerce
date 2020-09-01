package com.ple.example.icommerce.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorItem {

    @JsonProperty(value = "code")
    private String code;

    @JsonProperty(value = "message")
    private String message;

}
