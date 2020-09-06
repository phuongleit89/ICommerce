package com.ple.example.icommerce.exp;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CommerceBadRequestException extends RuntimeException {

    public static final String PRODUCT_SKU_IS_EXISTING = "E001";
    public static final String CART_ITEMS_EMPTY = "E002";
    public static final String NOT_ALLOW_UPDATE_ORDER_STATUS = "EOO3";
    public static final String PRODUCT_QUANTITY_OUT_OF_STOCK = "EOO4";

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
