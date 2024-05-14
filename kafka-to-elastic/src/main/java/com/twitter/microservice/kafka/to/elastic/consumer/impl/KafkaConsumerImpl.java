package com.twitter.microservice.kafka.to.elastic.consumer.impl;

import com.twitter.microservice.config.KafkaConfigData;
import com.twitter.microservice.config.KafkaConsumerConfigData;
import com.twitter.microservice.elastic.index.client.service.ElasticIndexClient;
import com.twitter.microservice.elastic.model.impl.TwitterIndexModel;
import com.twitter.microservice.kafka.admin.client.KafkaAdminClient;
import com.twitter.microservice.kafka.model.TwitterStatusModel;
import com.twitter.microservice.kafka.to.elastic.consumer.KafkaConsumer;
import com.twitter.microservice.kafka.to.elastic.transformer.AvroToElasticIndexModelTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class KafkaConsumerImpl implements KafkaConsumer<TwitterStatusModel>
        , ApplicationListener<ApplicationStartedEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumerImpl.class);

    private final KafkaAdminClient kafkaAdminClient;
    private final KafkaConfigData kafkaConfigData;
    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final AvroToElasticIndexModelTransformer avroToElasticIndexModelTransformer;
    private final ElasticIndexClient<TwitterIndexModel> elasticIndexClient;
    private final KafkaConsumerConfigData kafkaConsumerConfigData;

    public KafkaConsumerImpl(KafkaAdminClient kafkaAdminClient,
                             KafkaConfigData kafkaConfigData,
                             KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
                             AvroToElasticIndexModelTransformer avroToElasticIndexModelTransformer,
                             ElasticIndexClient<TwitterIndexModel> elasticIndexClient,
                             KafkaConsumerConfigData kafkaConsumerConfigData) {
        this.kafkaAdminClient = kafkaAdminClient;
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        this.avroToElasticIndexModelTransformer = avroToElasticIndexModelTransformer;
        this.elasticIndexClient = elasticIndexClient;
        this.kafkaConsumerConfigData = kafkaConsumerConfigData;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        kafkaAdminClient.checkTopicsCreated();
        LOG.info("Received on App Start event topics {} are ready to use topics to check on {}", kafkaConfigData.getTopicNamesToCreate().toArray());
        Objects.requireNonNull(
                kafkaListenerEndpointRegistry.getListenerContainer(kafkaConsumerConfigData.getConsumerGroupId())).start();
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.consumer-group-id}", topics = "${kafka-config.topic-name}")
    public void receive(@Payload List<TwitterStatusModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<Long> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        LOG.info("Found {} messages with keys {}, partitions {} ,offserts {}",
                messages.size(),
                keys,
                partitions,
                offsets);
        List<TwitterIndexModel> twitterIndexModels = avroToElasticIndexModelTransformer.
                convertAvroToIndexModel(messages);
        List<String> documentIds = elasticIndexClient.save(twitterIndexModels);
        LOG.info("Documents saved to elasticsearch with ids {}",documentIds.toArray());
    }
}
