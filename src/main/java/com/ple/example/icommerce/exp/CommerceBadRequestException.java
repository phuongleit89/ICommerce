package com.ple.example.icommerce.exp;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CommerceBadRequestException extends RuntimeException {

    public final static String PRODUCT_SKU_IS_EXISTING = "E001";
    public final static String CART_ITEMS_EMPTY = "E002";
    public final static String NOT_ALLOW_UPDATE_ORDER_STATUS = "EOO3";

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
