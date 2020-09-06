package com.ple.example.icommerce.dto.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ErrorResponse {

    @JsonProperty("errors")
    private List<ErrorItem> errors = new ArrayList<>();

    public void addError(ErrorItem error) {
        this.errors.add(error);
    }

}
