package com.twitter.microservice.kafka.to.elastic.consumer;

import org.apache.avro.specific.SpecificRecordBase;

import java.util.List;

public interface KafkaConsumer<T extends SpecificRecordBase> {
    public void receive(List<T> messages,List<Long> keys, List<Integer> partitions, List<Long> offsets);
}
