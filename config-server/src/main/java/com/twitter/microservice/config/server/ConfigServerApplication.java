package com.twitter.microservice.config.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

import java.time.LocalDateTime;

@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigServerApplication.class);
    public static void main(String[] args) {
        LOG.info("Starting Config Server App at {}", LocalDateTime.now());
        SpringApplication.run(ConfigServerApplication.class, args);
        LOG.info("Started Config Server App at {}", LocalDateTime.now());
    }
}
