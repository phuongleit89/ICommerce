package com.ple.example.icommerce.exp;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CommerceBadRequestException extends RuntimeException {

    public final static String PRODUCT_SKU_IS_EXISTING = "Product SKU is existing";

    public CommerceBadRequestException() {
        super();
    }

    public CommerceBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommerceBadRequestException(String message) {
        super(message);
    }

    public CommerceBadRequestException(Throwable cause) {
        super(cause);
    }

}
