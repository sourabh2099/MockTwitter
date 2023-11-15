package com.twitter.microservice.kafka.admin.client;

import com.twitter.microservice.config.KafkaConfigData;
import com.twitter.microservice.config.RetryConfigData;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class KafkaAdminClient {
    private final Logger LOG = LoggerFactory.getLogger(KafkaAdminClient.class);
    private final KafkaConfigData kafkaConfigData;
    private final RetryTemplate retryTemplate;
    private final RetryConfigData retryConfigData;
    private final AdminClient adminClient;
    private final WebClient webClient;

    public KafkaAdminClient(KafkaConfigData kafkaConfigData,
                            RetryTemplate retryTemplate,
                            RetryConfigData retryConfigData,
                            AdminClient adminClient,
                            WebClient webClient) {
        this.kafkaConfigData = kafkaConfigData;
        this.retryTemplate = retryTemplate;
        this.retryConfigData = retryConfigData;
        this.adminClient = adminClient;
        this.webClient = webClient;
    }

    public void createTopics() {
        CreateTopicsResult createTopicsResult;
        try {
            createTopicsResult = retryTemplate.execute(this::doCreateTopics);
        } catch (Exception ex) {
            LOG.error("Found error while creating new topics", ex);
        }
        checkTopicsCreated();
    }

    public void checkSchemaRegistryStatus() {
        int retryCount = 1;
        Integer maxAttempts = retryConfigData.getMaxAttempts();
        int multiplier = retryConfigData.getMultiplier().intValue();
        Long sleepTimeMs = retryConfigData.getSleepTimeMs();
        while (!getSchemaRegistryStatus().is2xxSuccessful()) {
            checkMaxRetry(retryCount++,maxAttempts);
            sleep(sleepTimeMs);
            sleepTimeMs*=multiplier;
        }
    }

    private HttpStatusCode getSchemaRegistryStatus() {
        try{
            return webClient.get()
                    .uri(kafkaConfigData.getSchemaRegistry())
                    .exchangeToMono(clientResponse -> clientResponse.bodyToMono(ClientResponse.class))
                    .map(ClientResponse::statusCode)
                    .block();
        }catch (Exception e){
            return HttpStatusCode.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value());
        }
    }

    public void checkTopicsCreated() {
        Collection<TopicListing> topics = getTopics();
        int retryCount = 1;
        Integer maxAttempts = retryConfigData.getMaxAttempts();
        int multiplier = retryConfigData.getMultiplier().intValue();
        Long sleepTimeMs = retryConfigData.getSleepTimeMs();
        for (String topicsName : kafkaConfigData.getTopicNamesToCreate()) {
            if (!isTopicCreated(topics, topicsName)) {
                checkMaxRetry(retryCount++, maxAttempts);
                sleep(sleepTimeMs);
                sleepTimeMs = sleepTimeMs * multiplier;
                topics = getTopics();
            }
        }
    }

    private void sleep(Long sleepTimeMs) {
        try {
            Thread.sleep(sleepTimeMs);
        } catch (InterruptedException ex) {
            throw new RuntimeException();
        }
    }

    private void checkMaxRetry(int retryCount, int maxAttempts) {
        if (retryCount > maxAttempts) {
            throw new RuntimeException("Retry Count Excedded");
        }
    }

    private boolean isTopicCreated(Collection<TopicListing> topics, String topicsName) {
        if (topics == null) {
            return false;
        }
        return topics.stream().anyMatch(topic -> topic.name().equals(topicsName));
    }

    private Collection<TopicListing> getTopics() {
        Collection<TopicListing> topicListings;
        try {
            topicListings = this.retryTemplate.execute(this::doGetTopics);
        } catch (Throwable ex) {
            throw new RuntimeException();
        }
        return topicListings;
    }

    private Collection<TopicListing> doGetTopics(RetryContext retryContext)
            throws ExecutionException, InterruptedException {
        LOG.info("Trying to fetch Topics for count {}", retryContext.getRetryCount());
        Collection<TopicListing> topicListings = adminClient.listTopics().listings().get();
        if (topicListings != null) {
            LOG.info("Found Topics Created {}", topicListings.stream()
                    .map(TopicListing::name).collect(Collectors.toList()));
        }
        return topicListings;

    }

    private CreateTopicsResult doCreateTopics(RetryContext retryContext) {
        List<String> topicNames = kafkaConfigData.getTopicNamesToCreate();
        LOG.info("Creating topics {} with retry count as {}", topicNames.toArray(), retryContext.getRetryCount());
        List<NewTopic> newTopics = topicNames.stream()
                .map(topic -> new NewTopic(
                        topic.trim(), kafkaConfigData.getNumOfPartitions(),
                        kafkaConfigData.getReplicationFactor()
                )).collect(Collectors.toList());
        return adminClient.createTopics(newTopics);
    }
}
