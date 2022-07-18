package com.ple.example.icommerce.dao.custom;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.EntityManager;
import java.util.Objects;

@Slf4j
public class BaseJpaTransactionManager extends JpaTransactionManager {

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        super.doBegin(transaction, definition);

        EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager
                .getResource(Objects.requireNonNull(getEntityManagerFactory()));
        EntityManager em = Objects.requireNonNull(emHolder).getEntityManager();

        // TODO: @ple - constant
        // TODO: @ple - shopId - waiting get from jwt token
        Integer shopId = 10;
        // force filter by shop id
        Filter filter = em.unwrap(Session.class).enableFilter("filterByShopId");
        filter.setParameter("shopId", shopId);
        filter.validate();
    }
}
