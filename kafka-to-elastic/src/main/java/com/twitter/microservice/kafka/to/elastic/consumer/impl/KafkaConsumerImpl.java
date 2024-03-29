package com.twitter.microservice.kafka.to.elastic.consumer.impl;

import com.twitter.microservice.config.KafkaConfigData;
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
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaConsumerImpl implements KafkaConsumer<TwitterStatusModel>
        , ApplicationListener<ApplicationStartedEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumerImpl.class);

    private final KafkaAdminClient kafkaAdminClient;
    private final KafkaConfigData kafkaConfigData;
    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final AvroToElasticIndexModelTransformer avroToElasticIndexModelTransformer;
    private final ElasticIndexClient<TwitterIndexModel> elasticIndexClient;

    public KafkaConsumerImpl(KafkaAdminClient kafkaAdminClient,
                             KafkaConfigData kafkaConfigData,
                             KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
                             AvroToElasticIndexModelTransformer avroToElasticIndexModelTransformer,
                             ElasticIndexClient<TwitterIndexModel> elasticIndexClient) {
        this.kafkaAdminClient = kafkaAdminClient;
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        this.avroToElasticIndexModelTransformer = avroToElasticIndexModelTransformer;
        this.elasticIndexClient = elasticIndexClient;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        kafkaAdminClient.checkTopicsCreated();
        LOG.info("Received on App Start event topics {} are ready to use", kafkaConfigData.getTopicNamesToCreate().toArray());
        kafkaListenerEndpointRegistry.getListenerContainer(kafkaConfigData.getTopicName()).start();
    }

    @Override
    @KafkaListener(id = "twitterTopicListener", topics = "${kafka-config.topic-name}")
    public void receive(List<TwitterStatusModel> messages, List<Long> keys, List<Integer> partitions, List<Long> offsets) {
        LOG.info("Found {} messages with keys {}, partitions {} ,offserts {}", messages.size(),
                keys, partitions, offsets);
        List<TwitterIndexModel> twitterIndexModels = avroToElasticIndexModelTransformer.convertAvroToIndexModel(messages);
        elasticIndexClient.save(twitterIndexModels);
    }
}
