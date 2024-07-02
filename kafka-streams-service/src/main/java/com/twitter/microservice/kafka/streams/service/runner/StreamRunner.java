package com.twitter.microservice.kafka.streams.service.runner;

public interface StreamRunner<K,V> {
    void start();
    default V getValueByKey(K key){
        return null;
    }
}
