package com.ple.example.icommerce.dao.custom;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.RepositoryInformation;

import javax.persistence.EntityManager;

public class BaseJpaRepositoryFactory extends JpaRepositoryFactory {

    private ProjectionFactory projectionFactory;

    public BaseJpaRepositoryFactory(EntityManager entityManager, ProjectionFactory projectionFactory) {
        super(entityManager);
        this.projectionFactory = projectionFactory;
    }

    @Override
    protected JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
        return new BaseRepositoryImpl<>(getEntityInformation(information.getDomainType()), entityManager, projectionFactory);
    }
}
