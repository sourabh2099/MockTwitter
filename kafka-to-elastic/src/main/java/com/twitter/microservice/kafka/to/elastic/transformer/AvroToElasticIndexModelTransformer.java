package com.twitter.microservice.kafka.to.elastic.transformer;

import com.twitter.microservice.elastic.model.impl.TwitterIndexModel;
import com.twitter.microservice.kafka.model.TwitterStatusModel;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AvroToElasticIndexModelTransformer {
    public List<TwitterIndexModel> convertAvroToIndexModel(List<TwitterStatusModel> twitterStatusModel) {
        return twitterStatusModel.stream().map(item ->
                        TwitterIndexModel.builder()
                                .id(String.valueOf(item.getId()))
                                .text(item.getText())
                                .userId(item.getUserId())
                                .createdAt(ZonedDateTime.ofInstant(Instant.ofEpochMilli(item.getCreatedAt()),
                                        ZoneId.systemDefault()))
                                .build())
                .collect(Collectors.toList());
    }
}
