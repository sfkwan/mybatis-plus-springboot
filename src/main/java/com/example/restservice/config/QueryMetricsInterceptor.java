package com.example.restservice.config;

import java.sql.Statement;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;

/**
 * MyBatis interceptor to measure database query execution time
 * and expose metrics to Prometheus via Micrometer.
 * 
 * @author Application Development Team
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
@Intercepts({
        @Signature(type = StatementHandler.class, method = "query", args = { Statement.class,
                org.apache.ibatis.session.ResultHandler.class }),
        @Signature(type = StatementHandler.class, method = "update", args = { Statement.class }),
        @Signature(type = StatementHandler.class, method = "batch", args = { Statement.class })
})
public class QueryMetricsInterceptor implements Interceptor {

    private final MeterRegistry meterRegistry;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Timer.Sample sample = Timer.start(meterRegistry);

        try {
            // Execute the actual query
            Object result = invocation.proceed();
            return result;
        } finally {
            // Record the execution time
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
            BoundSql boundSql = statementHandler.getBoundSql();

            // Extract the SQL statement type and mapper info
            MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
            MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
            String sqlId = mappedStatement.getId();
            String sqlType = mappedStatement.getSqlCommandType().name();

            // Extract mapper class and method name
            String[] parts = sqlId.split("\\.");
            String mapperClass = parts.length > 1 ? parts[parts.length - 2] : "unknown";
            String methodName = parts.length > 0 ? parts[parts.length - 1] : "unknown";

            // Record the timer with tags
            sample.stop(Timer.builder("db_query_response_time")
                    .description("Database query execution time")
                    .tag("sql_type", sqlType.toLowerCase())
                    .tag("mapper", mapperClass)
                    .tag("method", methodName)
                    .publishPercentiles(0.95, 0.99)
                    .register(meterRegistry));
        }
    }

    @Override
    public Object plugin(Object target) {
        return Interceptor.super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
        // No properties to set
    }
}
