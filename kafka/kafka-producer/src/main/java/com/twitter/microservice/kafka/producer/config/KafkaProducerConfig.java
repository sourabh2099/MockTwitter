package com.twitter.microservice.kafka.producer.config;

import com.twitter.microservice.config.KafkaConfigData;
import com.twitter.microservice.config.KafkaProducerConfigData;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerConfig;
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
    private final KafkaProducerConfigData producerConfigData;
    private final KafkaConfigData kafkaConfigData;

    public KafkaProducerConfig(KafkaProducerConfigData producerConfigData,
                               KafkaConfigData kafkaConfigData) {
        this.producerConfigData = producerConfigData;
        this.kafkaConfigData = kafkaConfigData;
    }
    @Bean
    public Map<String, Object> createConfig() {
        Map<String, Object> map = new HashMap<>();
        map.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigData.getKafkaBrokerList());
        map.put(ProducerConfig.BATCH_SIZE_CONFIG, producerConfigData.getBatchSize()
                * producerConfigData.getBatchSizeBoostFactor());
        map.put(ProducerConfig.LINGER_MS_CONFIG,producerConfigData.getAcks());
        map.put(ProducerConfig.ACKS_CONFIG,producerConfigData.getAcks());
        map.put(ProducerConfig.COMPRESSION_TYPE_CONFIG,producerConfigData.getCompressionType());
        map.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, producerConfigData.getRequestTimeoutMs());
        map.put(ProducerConfig.RETRIES_CONFIG, producerConfigData.getRetryCount());
        map.put(kafkaConfigData.getSchemaRegistryKeyUrl(), kafkaConfigData.getSchemaRegistry());
        map.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, producerConfigData.getKeySerializerClass());
        return map;
    }
    @Bean
    public ProducerFactory<K,V>  producerFactory(){
        return new DefaultKafkaProducerFactory<>(createConfig());
    }
    KafkaTemplate<K,V> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }



}
