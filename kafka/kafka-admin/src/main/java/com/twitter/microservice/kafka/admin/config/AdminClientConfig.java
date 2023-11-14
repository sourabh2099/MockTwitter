package com.twitter.microservice.kafka.admin.config;

import com.twitter.microservice.config.KafkaConfigData;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

import java.util.Map;

@Configuration
@EnableRetry
public class AdminClientConfig {
    private final KafkaConfigData kafkaConfigData;

    public AdminClientConfig(KafkaConfigData kafkaConfigData) {
        this.kafkaConfigData = kafkaConfigData;
    }
    public AdminClient adminClient(){
        return AdminClient.create(
                Map.of(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG,kafkaConfigData.getKafkaBrokerList()));
    }
}
