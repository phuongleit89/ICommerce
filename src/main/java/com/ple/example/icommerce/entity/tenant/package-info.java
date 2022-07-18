// Ref: https://docs.jboss.org/hibernate/orm/5.5/userguide/html_single/Hibernate_User_Guide.html#basic-provided
@FilterDefs({
        @FilterDef(
                name = "filterByShopId",
                parameters = {@ParamDef(name = "shopId", type = "java.lang.Integer")},
                defaultCondition = "shop_id = :shopId")})
package com.ple.example.icommerce.entity.tenant;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.ParamDef;
