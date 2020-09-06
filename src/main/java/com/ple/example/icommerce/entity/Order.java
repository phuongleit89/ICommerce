package com.ple.example.icommerce.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "order_table")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Order extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "key")
    private Long key;

    @Column(name = "name")
    @NotNull
    @Size(max = 255)
    private String name;

    @Column(name = "address")
    @NotNull
    @Size(max = 255)
    private String address;

    @Column(name = "city")
    @NotNull
    @Size(max = 255)
    private String city;

    @Column(name = "zip")
    @Size(max = 255)
    private String zip;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    @NotNull
    private OrderStatus status;

    @Column(name = "comment")
    @Size(max = 1024)
    private String comment;

    @Column(name = "total_price")
    @NotNull
    @Min(value = 0)
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderItem> orderItems;

}
