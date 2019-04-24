package com.zhixinhuixue.armor.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author sch
 * @DATE 2019/4/16 19:38
 */
@Configuration
public class DataSourceConfig {
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "secondDatasource")
    @ConfigurationProperties(prefix = "spring.second-datasource.druid")
    public DataSource secondDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public JdbcTemplate primaryJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "secondJdbcTemplate")
    public JdbcTemplate secondJdbcTemplate(@Qualifier("secondDatasource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
