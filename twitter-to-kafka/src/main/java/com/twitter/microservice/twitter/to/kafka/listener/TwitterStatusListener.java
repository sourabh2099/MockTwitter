package com.twitter.microservice.twitter.to.kafka.listener;

import com.twitter.microservice.config.KafkaConfigData;
import com.twitter.microservice.dto.TwitterStatus;
import com.twitter.microservice.kafka.model.TwitterStatusModel;
import com.twitter.microservice.kafka.producer.config.service.KafkaProducer;
import com.twitter.microservice.twitter.to.kafka.transformer.PojoToAvroTwitterStatus;
import org.springframework.stereotype.Component;

@Component
public class TwitterStatusListener {
    private final KafkaProducer<Long, TwitterStatusModel> producer;

    private final PojoToAvroTwitterStatus statusTransformer;
    private final KafkaConfigData kafkaConfigData;

    public TwitterStatusListener(KafkaProducer<Long, TwitterStatusModel> producer,
                                 PojoToAvroTwitterStatus statusTransformer,
                                 KafkaConfigData kafkaConfigData) {
        this.producer = producer;
        this.statusTransformer = statusTransformer;
        this.kafkaConfigData = kafkaConfigData;
    }
    public void onStatus(TwitterStatus status){
        TwitterStatusModel avroTwitterStatusModel = statusTransformer.getAvroTwitterStatusModel(status);
        producer.sendMsg(kafkaConfigData.getTopicName(), avroTwitterStatusModel.getId(), avroTwitterStatusModel);
    }


}
