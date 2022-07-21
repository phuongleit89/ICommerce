package com.ple.example.icommerce.dao.projection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
// Note:
// Class projection: the parameter names of its constructor must match properties of the root entity class.
// So we can not define @NoArgsConstructor is here.
@AllArgsConstructor
@Builder
@Data
public class ProductDto {
    private String sku;
    private Integer shopId;
}
