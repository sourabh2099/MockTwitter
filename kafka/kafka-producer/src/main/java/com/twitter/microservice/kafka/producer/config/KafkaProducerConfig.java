package com.twitter.microservice.kafka.producer.config;

import com.twitter.microservice.config.KafkaConfigData;
import com.twitter.microservice.config.KafkaProducerConfigData;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig<K extends Serializable,V extends SpecificRecordBase>{
    private final KafkaProducerConfigData kafkaProducerConfigData;
    private final KafkaConfigData kafkaConfigData;
    private static Logger LOG = LoggerFactory.getLogger(KafkaProducerConfig.class);

    public KafkaProducerConfig(KafkaProducerConfigData producerConfigData,
                               KafkaConfigData kafkaConfigData) {
        this.kafkaProducerConfigData = producerConfigData;
        this.kafkaConfigData = kafkaConfigData;
    }
    @Bean
    public Map<String, Object> createConfig() {
        Map<String, Object> props = new HashMap<>();
        LOG.info("fetched config data {}",kafkaConfigData);
        LOG.info("fetched producer config data {}",kafkaProducerConfigData);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigData.getBootstrapServers());
        props.put(kafkaConfigData.getSchemaRegistryUrlKey(), kafkaConfigData.getSchemaRegistryUrl());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProducerConfigData.getKeySerializerClass());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProducerConfigData.getValueSerializerClass());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProducerConfigData.getBatchSize() *
                kafkaProducerConfigData.getBatchSizeBoostFactor());
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerConfigData.getLingerMs());
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, kafkaProducerConfigData.getCompressionType());
        props.put(ProducerConfig.ACKS_CONFIG, kafkaProducerConfigData.getAcks());
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaProducerConfigData.getRequestTimeoutMs());
        props.put(ProducerConfig.RETRIES_CONFIG, kafkaProducerConfigData.getRetryCount());
        return props;
    }
    @Bean
    public ProducerFactory<K,V>  producerFactory(){
        return new DefaultKafkaProducerFactory<>(createConfig());
    }
    @Bean
    KafkaTemplate<K,V> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }



}
