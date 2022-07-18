package com.ple.example.icommerce.dao.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;

public class BaseRepositoryFactoryBean extends JpaRepositoryFactoryBean {

    @Autowired
    private ProjectionFactory projectionFactory;

    public BaseRepositoryFactoryBean(Class repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new BaseJpaRepositoryFactory(entityManager, projectionFactory);
    }
}
