package com.twitter.microservice.kafka.streams.service.init.impl;

import com.twitter.microservice.config.KafkaConfigData;
import com.twitter.microservice.kafka.admin.client.KafkaAdminClient;
import com.twitter.microservice.kafka.streams.service.KafkaStreamsApplication;
import com.twitter.microservice.kafka.streams.service.init.StreamInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class KafkaStreamInitializer implements StreamInitializer {
    private final KafkaConfigData kafkaConfigData;
    private final KafkaAdminClient kafkaAdminClient;

    public KafkaStreamInitializer(KafkaConfigData kafkaConfigData, KafkaAdminClient kafkaAdminClient) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaAdminClient = kafkaAdminClient;
    }
    private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamsApplication.class);

    @Override
    public void init() {
        kafkaAdminClient.checkTopicsCreated();
        kafkaAdminClient.checkSchemaRegistryStatus();
        LOG.info("Topic with name {} created and is ready for operations",kafkaConfigData.getTopicNamesToCreate());
    }
}
