package com.twitter.microservice.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "kafka-config-data")
public class KafkaConfigData {
    private List<String> kafkaBrokerList;
    private String schemaRegistryKeyUrl;
    private String schemaRegistry;
    private String topicName;
    private List<String> topicNamesToCreate;
    private Integer numOfPartitions;
    private Short replicationFactor;
}
