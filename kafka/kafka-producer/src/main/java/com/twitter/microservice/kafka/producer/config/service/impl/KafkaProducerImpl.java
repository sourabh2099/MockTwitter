package com.twitter.microservice.kafka.producer.config.service.impl;

import com.twitter.microservice.kafka.model.TwitterStatusModel;
import com.twitter.microservice.kafka.producer.config.service.KafkaProducer;
import jakarta.annotation.PreDestroy;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

@Component
public class KafkaProducerImpl implements KafkaProducer<Long, TwitterStatusModel> {
    private final Logger LOG = LoggerFactory.getLogger(KafkaProducerImpl.class);
    private final KafkaTemplate<Long,TwitterStatusModel> kafkaTemplate;

    public KafkaProducerImpl(KafkaTemplate<Long, TwitterStatusModel> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    @PreDestroy
    public void close(){
        if (kafkaTemplate != null) {
            LOG.info("Closing kafka producer!");
            kafkaTemplate.destroy();
        }
    }


    @Override
    public void sendMsg(String topicsName, Long key, TwitterStatusModel value) {
        LOG.info("sending message to topic {} with key {} and value {}",topicsName,key,value);
        CompletableFuture<SendResult<Long, TwitterStatusModel>> kafkaResultFuture =
                kafkaTemplate.send(topicsName, key, value);
        kafkaResultFuture.whenComplete(getCallback(topicsName,value));
       // afterSendProcess(topicsName,value,kafkaResultFuture);

    }

    private BiConsumer<SendResult<Long, TwitterStatusModel>, Throwable> getCallback(String topicName, TwitterStatusModel message) {
        return (result, ex) -> {
            if (ex == null) {
                RecordMetadata metadata = result.getRecordMetadata();
                LOG.info("Received new metadata. Topic: {}; Partition {}; Offset {}; Timestamp {}, at time {}",
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp(),
                        System.nanoTime());
            } else {
                LOG.error("Error while sending message {} to topic {}", message.toString(), topicName, ex);
            }
        };
    }
}
