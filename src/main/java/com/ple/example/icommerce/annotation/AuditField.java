package com.ple.example.icommerce.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.PARAMETER, ElementType.FIELD })
public @interface AuditField {

    String name();

}
