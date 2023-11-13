package com.twitter.microservice.kafka.to.elastic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class KafkaToElasticServiceApplication {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaToElasticServiceApplication.class);

    public static void main(String[] args) {
        LOG.info("Starting Kafka-to-elatic-service at {}", LocalDateTime.now());
        SpringApplication.run(KafkaToElasticServiceApplication.class, args);
        LOG.info("Stated Kafka-to-elastic-service at {}", LocalDateTime.now());
    }
}
