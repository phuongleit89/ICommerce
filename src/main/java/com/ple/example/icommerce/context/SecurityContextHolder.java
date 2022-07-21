package com.ple.example.icommerce.context;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecurityContextHolder {

    public static final ThreadLocal<Integer> shopId = new ThreadLocal<>();

    public static Integer getShopId() {
        return shopId.get();
    }

    public static void setShopId(Integer shopIdVal) {
        shopId.set(shopIdVal);
        log.debug("Set Context: shopId = {}", shopIdVal);
    }

}
