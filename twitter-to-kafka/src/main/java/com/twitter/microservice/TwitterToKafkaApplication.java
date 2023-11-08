package com.twitter.microservice;

import com.twitter.microservice.genStream.StreamRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
@Slf4j
public class TwitterToKafkaApplication implements CommandLineRunner {

    private final StreamRunner streamRunner;

    public TwitterToKafkaApplication(StreamRunner streamRunner) {
        this.streamRunner = streamRunner;
    }

    public static void main(String[] args) {
        log.info("Preparing to start application ");
        SpringApplication.run(TwitterToKafkaApplication.class, args);
        log.info("Application Started at  {}", LocalDateTime.now());
    }


    @Override
    public void run(String... args) throws Exception {
        streamRunner.initStream();
    }
}