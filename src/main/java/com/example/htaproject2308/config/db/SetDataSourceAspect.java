package com.example.htaproject2308.config.db;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SetDataSourceAspect {

    @Before("@annotation(com.example.htaproject2308.config.db.SetDataSource) && @annotation(target)")
    public void setDataSource(SetDataSource target) throws Exception {

        if (target.dataSourceType() == SetDataSource.DataSourceType.MASTER
                || target.dataSourceType() == SetDataSource.DataSourceType.SLAVE) {
            RoutingDataSourceManager.setCurrentDataSourceName(target.dataSourceType());
        } else {
            throw new Exception("Wrong DataSource Type : Should Check Exception");
        }

    }

    @After("@annotation(com.example.htaproject2308.config.db.SetDataSource)")
    public void clearDataSource() {
        RoutingDataSourceManager.removeCurrentDataSourceName();
    }
}
