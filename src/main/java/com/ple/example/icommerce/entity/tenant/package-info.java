// Ref: https://docs.jboss.org/hibernate/orm/5.5/userguide/html_single/Hibernate_User_Guide.html#basic-provided
@FilterDefs({
        @FilterDef(
                name = FILTER_BY_SHOP_ID_NAME,
                parameters = {@ParamDef(name = FILTER_PARAM_SHOP_ID, type = FILTER_TYPE_SHOP_ID)},
                defaultCondition = "shop_id = :shopId")})
package com.ple.example.icommerce.entity.tenant;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.ParamDef;

import static com.ple.example.icommerce.constant.JpaConstants.*;
