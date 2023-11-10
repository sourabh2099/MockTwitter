package com.twitter.microservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;

@Configuration
public class RetryConfig {
    private final RetryConfigData retryConfigData;

    public RetryConfig(RetryConfigData retryConfigData) {
        this.retryConfigData = retryConfigData;
    }

    @Bean
    public RetryTemplate retryTemplate(){
        RetryTemplate retryTemplate = new RetryTemplate();
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(retryConfigData.getMaxAttempts());
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();

        backOffPolicy.setInitialInterval(retryConfigData.getInitialIntervalMs());
        backOffPolicy.setMultiplier(retryConfigData.getMultiplier());
        backOffPolicy.setMultiplier(retryConfigData.getMultiplier());
        retryTemplate.setBackOffPolicy(backOffPolicy);

        retryTemplate.setRetryPolicy(retryPolicy);
        return retryTemplate;
    }
}
