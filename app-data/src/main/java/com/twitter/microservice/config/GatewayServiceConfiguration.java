package com.twitter.microservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("gateway-service")
public class GatewayServiceConfiguration {
    private Long timeoutMs;
    private Float failureRateThreshold;
    private Float slowCallRateThreshold;
    private Long slowCallDurationThreshold;
    private Integer permittedNumOfCallsInHalfOpenState;
    private Integer slidingWindowSize;
    private Integer minNumberOfCalls;
    private Long waitDurationInOpenState;

}
