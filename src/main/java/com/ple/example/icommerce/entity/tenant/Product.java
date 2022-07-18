package com.ple.example.icommerce.entity.tenant;

import com.ple.example.icommerce.entity.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "product_table")
@Filters({@Filter(name = "filterByShopId")})
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
    @Min(0)
    private Double price;

    @Column(name = "quantity")
    @NotNull
    @Min(0)
    private Integer quantity;

    @Column(name = "shopId")
    @NotNull
    @Min(0)
    private Integer shopId;

}
