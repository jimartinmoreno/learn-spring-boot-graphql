package com.learn.graphql.config;

import com.learn.graphql.util.ExecutorFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;

/**
 * COnfiguración para la ejecución asincrona de los Resolvers
 */
@Configuration
public class AsyncExecutorConfig {

    @Bean
    public Executor balanceExecutor(ExecutorFactory executorFactory) {
        return executorFactory.newExecutor();
    }

    @Bean
    public Executor bankAccountExecutor(ExecutorFactory executorFactory) {
        return executorFactory.newExecutor();
    }

}
