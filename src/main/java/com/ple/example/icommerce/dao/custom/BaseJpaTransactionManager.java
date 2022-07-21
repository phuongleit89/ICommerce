package com.ple.example.icommerce.dao.custom;

import com.ple.example.icommerce.context.SecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.EntityManager;
import java.util.Objects;

import static com.ple.example.icommerce.constant.JpaConstants.FILTER_BY_SHOP_ID_NAME;
import static com.ple.example.icommerce.constant.JpaConstants.FILTER_PARAM_SHOP_ID;

@Slf4j
public class BaseJpaTransactionManager extends JpaTransactionManager {


    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        super.doBegin(transaction, definition);

        // get em
        EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager
                .getResource(Objects.requireNonNull(getEntityManagerFactory()));
        EntityManager em = Objects.requireNonNull(emHolder).getEntityManager();

        // get tenant
        Integer shopId = SecurityContextHolder.getShopId();
        log.debug("Filter: shopId = {}", shopId);
        if (shopId == null) {
            log.warn("Filter: shopId is NULL.");
            return;
        }

        // force filter by shop id
        Filter filter = em.unwrap(Session.class).enableFilter(FILTER_BY_SHOP_ID_NAME);
        filter.setParameter(FILTER_PARAM_SHOP_ID, shopId);
        filter.validate();
    }
}
