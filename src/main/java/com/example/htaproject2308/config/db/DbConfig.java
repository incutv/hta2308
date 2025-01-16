package com.example.htaproject2308.config.db;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@MapperScan(value = "com.example.htaproject2308.mapper")
public class DbConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create().build();
    }


    @Bean
    public AbstractRoutingDataSource routingDataSource(
            DataSource masterDataSource,
            DataSource slaveDataSource
    ) {
        // AbstractRoutingDataSource클래스에 determineCurrentLookupKey를 이용하여
        // Master나 Slave중 사용할 DataSource를 라우팅
        AbstractRoutingDataSource routingDataSource = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                return RoutingDataSourceManager.getCurrentDataSourceName();
            }
        };

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(SetDataSource.DataSourceType.MASTER, masterDataSource);
        targetDataSources.put(SetDataSource.DataSourceType.SLAVE, slaveDataSource);

        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);

        return routingDataSource;
    }

    // 쿼리를 실행할 때 DataSource를 정할 수 있도록 DataSource 연결을 늦춰주도록
    // LazyConnectionDataSourceProxy를 이용하여 현재 RoutingDataSource를 감싸서 구현
    @Bean
    public LazyConnectionDataSourceProxy lazyRoutingDataSource(DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager(
            @Qualifier(value = "lazyRoutingDataSource") DataSource lazyRoutingDataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(lazyRoutingDataSource);
        return transactionManager;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier(value = "lazyRoutingDataSource") DataSource lazyRoutingDataSource, ApplicationContext applicationContext) throws Exception{
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(lazyRoutingDataSource);
        sessionFactory.setMapperLocations(applicationContext.getResources("classpath:mapper/**/*.xml"));
        return sessionFactory.getObject();
    }
}
