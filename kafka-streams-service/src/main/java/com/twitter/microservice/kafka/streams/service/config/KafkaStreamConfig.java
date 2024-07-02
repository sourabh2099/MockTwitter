package com.twitter.microservice.kafka.streams.service.config;

import com.twitter.microservice.config.KafkaConfigData;
import com.twitter.microservice.config.KafkaStreamsConfigData;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KafkaStreamConfig {
    private final KafkaStreamsConfigData kafkaStreamsConfigData;
    private final KafkaConfigData kafkaConfigData;

    public KafkaStreamConfig(KafkaStreamsConfigData kafkaStreamsConfigData,
                             KafkaConfigData kafkaConfigData) {
        this.kafkaStreamsConfigData = kafkaStreamsConfigData;
        this.kafkaConfigData = kafkaConfigData;
    }
    @Bean
    @Qualifier("streamConfiguration")
    public Properties streamConfiguration(){
        Properties streamConfiguration = new Properties();
        streamConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaConfigData.getBootstrapServers());
        streamConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG,kafkaStreamsConfigData.getApplicationId());
        streamConfiguration.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,kafkaConfigData.getSchemaRegistryUrl());
        streamConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamConfiguration.put(StreamsConfig.STATE_DIR_CONFIG,kafkaStreamsConfigData.getStateFileLocation());
        streamConfiguration.put(StreamsConfig.REPLICATION_FACTOR_CONFIG,1);
        return streamConfiguration;
    }
}
