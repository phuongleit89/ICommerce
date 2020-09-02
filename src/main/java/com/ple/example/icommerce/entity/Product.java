package com.ple.example.icommerce.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "product")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Product extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "key")
    private Long key;

    @Column(name = "name")
    @NotNull
    @Size(max = 255)
    private String name;

    @Column(name = "sku")
    @NotNull
    @Size(max = 255)
    private String sku;

    @Column(name = "price")
    @NotNull
    @Min(value = 0)
    private Double price;

    @Column(name = "quantity")
    @NotNull
    @Min(value = 0)
    private Integer quantity;

}
