package com.twitter.microservice.kafka.streams.service;

import com.twitter.microservice.kafka.streams.service.init.StreamInitializer;
import com.twitter.microservice.kafka.streams.service.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.twitter.microservice")
public class KafkaStreamsApplication implements CommandLineRunner {
    private final StreamInitializer streamInitializer;
    private final StreamRunner<String,Long> streamRunner;
    private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamsApplication.class);

    public KafkaStreamsApplication(StreamInitializer streamInitializer,
                                   StreamRunner<String, Long> streamRunner) {
        this.streamInitializer = streamInitializer;
        this.streamRunner = streamRunner;
    }

    public static void main(String[] args) {
        LOG.info("Starting kafkaStreamsApplication");
        SpringApplication.run(KafkaStreamsApplication.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("starting to stream messages !!");
        streamInitializer.init();
        streamRunner.start();
    }
}
