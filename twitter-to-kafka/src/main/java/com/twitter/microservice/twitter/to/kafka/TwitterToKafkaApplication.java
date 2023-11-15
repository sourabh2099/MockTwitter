package com.twitter.microservice.twitter.to.kafka;

import com.twitter.microservice.twitter.to.kafka.init.StreamInit;
import com.twitter.microservice.twitter.to.kafka.runner.StreamRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDateTime;

@Slf4j
@SpringBootApplication
@ComponentScan("com.twitter.microservice")
public class TwitterToKafkaApplication implements CommandLineRunner {
    private final StreamRunner streamRunner;
    private final StreamInit streamInit;

    public TwitterToKafkaApplication(StreamRunner streamRunner,
                                     StreamInit streamInit) {
        this.streamRunner = streamRunner;
        this.streamInit = streamInit;
    }

    public static void main(String[] args) {
        log.info("Preparing to start application ");
        SpringApplication.run(TwitterToKafkaApplication.class, args);
        log.info("Application Started at  {}", LocalDateTime.now());
    }


    @Override
    public void run(String... args) throws Exception {
        streamInit.init();
        streamRunner.start();
    }
}
