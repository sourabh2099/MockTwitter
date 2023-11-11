package com.twitter.microservice.kafka.producer.config.service;

import org.apache.avro.specific.SpecificRecordBase;

import java.io.Serializable;

public interface KafkaProducer<K extends Serializable,V extends SpecificRecordBase> {
    public void sendMsg(String topicsName,K key,V value);
}
