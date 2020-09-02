package com.ple.example.icommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class ICommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ICommerceApplication.class, args);
    }

}
