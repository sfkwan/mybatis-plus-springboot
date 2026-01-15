package com.example.restservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

/**
 * Configuration class for MyBatis Plus framework.
 * Sets up interceptors for pagination and other database operations.
 * 
 * @author Application Development Team
 * @since 1.0
 */
@Configuration
@MapperScan("com.example.restservice.user.mapper")

public class MyBatisPlusConfig {
    /**
     * Configures the MyBatis Plus interceptor with pagination support.
     * 
     * @return configured MybatisPlusInterceptor with PaginationInnerInterceptor for
     *         H2 database
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // interceptor.addInnerInterceptor(new DataPermissionInterceptor(new
        // CustomDataPermissionHandler()));

        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));
        return interceptor;
    }
}