package com.example.danceschool.config;

import org.apache.commons.dbutils.QueryRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Database configuration for DBUtils approach.
 * Separates database technology from business logic.
 */
@Configuration
public class DatabaseConfig {
    
    /**
     * QueryRunner is the main class from Apache Commons DBUtils.
     * It handles all SQL execution and result mapping.
     */
    @Bean
    public QueryRunner queryRunner(DataSource dataSource) {
        return new QueryRunner(dataSource);
    }
}
