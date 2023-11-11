package com.twitter.microservice.twitter.to.kafka.init.impl;

import com.twitter.microservice.config.KafkaConfigData;
import com.twitter.microservice.kafka.admin.client.KafkaAdminClient;
import com.twitter.microservice.twitter.to.kafka.init.StreamInit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TwitterStreamInit implements StreamInit {
    private final KafkaAdminClient adminClient;
    private final KafkaConfigData configData;

    public TwitterStreamInit(KafkaAdminClient adminClient, KafkaConfigData configData) {
        this.adminClient = adminClient;
        this.configData = configData;
    }

    @Override
    public void init() {
        adminClient.createTopics();
        adminClient.checkSchemaRegistryStatus();
        log.info("Created Topics {}",configData.getTopicNamesToCreate());
    }
}
