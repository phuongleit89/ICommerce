package com.ple.example.icommerce.entity.tenant;

import com.ple.example.icommerce.entity.AuditModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@MappedSuperclass
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class TenantModel extends AuditModel {

    @Column(name = "shop_id")
    @NotNull
    @Min(0)
    private Integer shopId;
}
