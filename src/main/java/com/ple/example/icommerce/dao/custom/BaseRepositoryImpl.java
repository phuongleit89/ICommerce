package com.ple.example.icommerce.dao.custom;

import com.ple.example.icommerce.context.SecurityContextHolder;
import com.ple.example.icommerce.entity.tenant.TenantModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.projection.ProjectionFactory;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class BaseRepositoryImpl<T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID>
        implements BaseRepository<T, ID> {

    private final EntityManager em;
    private final ProjectionFactory projectionFactory;

    public BaseRepositoryImpl(Class<T> domainClass, EntityManager em, ProjectionFactory projectionFactory) {
        super(domainClass, em);
        this.em = em;
        this.projectionFactory = projectionFactory;
    }

    public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager em, ProjectionFactory projectionFactory) {
        super(entityInformation, em);
        this.em = em;
        this.projectionFactory = projectionFactory;
    }

    @Override
    public <S extends T> S save(S entity) {
        // auto saving with tenant
        if (entity instanceof TenantModel) {
            Integer shopId = Objects.requireNonNull(SecurityContextHolder.getShopId());
            ((TenantModel) entity).setShopId(shopId);
            log.debug("Save with tenant: shopId = {}", shopId);
        }
        return super.save(entity);
    }

    @Override
    public <P> List<P> findAll(Specification spec, int start, int limit, Sort sort, Class<P> clazz) {
        CriteriaBuilder builder = this.em.getCriteriaBuilder();
        Class<T> domainClass = this.getDomainClass();
        CriteriaQuery<Tuple> query = builder.createTupleQuery();
        Root<T> root = query.from(domainClass);

        Set<Selection<?>> selections = new HashSet<>();
        List<PropertyDescriptor> inputProperties = this.projectionFactory.getProjectionInformation(clazz).getInputProperties();
        for (PropertyDescriptor inputProperty : inputProperties) {
            String name = inputProperty.getName();
            Selection<?> alias = root.get(name).alias(name);
            selections.add(alias);
        }

        query.multiselect(new ArrayList<>(selections))
                .where(spec.toPredicate(root, query, builder))
                .orderBy(QueryUtils.toOrders(sort, root, builder));

        TypedQuery<Tuple> typedQuery = em.createQuery(query);

        List<Tuple> resultList = typedQuery.getResultList();

        List<P> results = new ArrayList<>();
        for (Tuple tuple : resultList) {
            Map<String, Object> itemMap = new HashMap<>();
            for (TupleElement<?> element : tuple.getElements()) {
                String alias = element.getAlias();
                itemMap.put(alias, tuple.get(alias));
            }
            results.add(projectionFactory.createNullableProjection(clazz, itemMap));
        }

        return results;
    }
}
