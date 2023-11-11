package com.twitter.microservice.kafka.producer.config.service.impl;

import com.twitter.microservice.kafka.model.TwitterStatusModel;
import com.twitter.microservice.kafka.producer.config.service.KafkaProducer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class KafkaProducerImpl implements KafkaProducer<Long, TwitterStatusModel> {
    private final Logger LOG = LoggerFactory.getLogger(KafkaProducerImpl.class);
    private final KafkaTemplate<Long,TwitterStatusModel> kafkaTemplate;

    public KafkaProducerImpl(KafkaTemplate<Long, TwitterStatusModel> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendMsg(String topicsName, Long key, TwitterStatusModel value) {
        LOG.info("sending message to topic {} with key {} and value {}",topicsName,key,value);
        ListenableFuture<SendResult<Long, TwitterStatusModel>> kafkaResultFuture =
                kafkaTemplate.send(topicsName, key, value);
        afterSendProcess(topicsName,value,kafkaResultFuture);

    }

    private void afterSendProcess(String topicsName,TwitterStatusModel value, ListenableFuture<SendResult<Long, TwitterStatusModel>> kafkaResultFuture) {
        kafkaResultFuture.addCallback(new ListenableFutureCallback<SendResult<Long, TwitterStatusModel>>() {
            @Override
            public void onFailure(Throwable ex) {
                LOG.error("Found error while trying to send message {} to topic {}",value,topicsName);
            }

            @Override
            public void onSuccess(SendResult<Long, TwitterStatusModel> result) {
                RecordMetadata recordMetadata = result.getRecordMetadata();
                LOG.info("Successfully sent msg {} {} {} {}",recordMetadata.offset(),recordMetadata.partition(),
                        recordMetadata.topic(),recordMetadata.timestamp());
            }
        });
    }
}
