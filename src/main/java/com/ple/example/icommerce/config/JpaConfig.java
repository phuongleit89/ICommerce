package com.ple.example.icommerce.config;

import com.ple.example.icommerce.dao.custom.BaseJpaTransactionManager;
import com.ple.example.icommerce.dao.custom.BaseRepositoryFactoryBean;
import com.ple.example.icommerce.dao.custom.BaseRepositoryImpl;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(
        basePackages = "com.ple.example.icommerce.dao",
        repositoryBaseClass = BaseRepositoryImpl.class,
        transactionManagerRef = "baseJpaTransactionManager",
        repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)
@EnableTransactionManagement(proxyTargetClass = true, mode = AdviceMode.PROXY)
public class JpaConfig {

    @Bean(name = "projectionFactory")
    public ProjectionFactory projectionFactory() {
        return new SpelAwareProxyProjectionFactory();
    }

    @Bean(name = "baseJpaTransactionManager")
    public PlatformTransactionManager transactionManager() {
        return new BaseJpaTransactionManager();
    }
}
