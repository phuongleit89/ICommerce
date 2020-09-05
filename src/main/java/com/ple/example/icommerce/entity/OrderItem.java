package com.ple.example.icommerce.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "order_item_table")
@Data
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "key")
    private Long key;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_key", nullable = false)
    private Order order;

    @EqualsAndHashCode.Exclude
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_key", nullable = false)
    private Product product;

    @Column(name = "quantity")
    @NotNull
    @Min(value = 1)
    private Integer quantity;

    @Column(name = "price")
    @NotNull
    @Min(value = 0)
    private Double price;

}
