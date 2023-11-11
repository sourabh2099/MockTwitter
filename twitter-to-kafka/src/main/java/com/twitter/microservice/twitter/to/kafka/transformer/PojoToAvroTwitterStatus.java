package com.twitter.microservice.twitter.to.kafka.transformer;

import com.twitter.microservice.dto.TwitterStatus;
import com.twitter.microservice.kafka.model.TwitterStatusModel;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class PojoToAvroTwitterStatus {
    public TwitterStatusModel getAvroTwitterStatusModel(TwitterStatus status){
        return TwitterStatusModel.newBuilder()
                .setId(status.getId())
                .setText(status.getStatus())
                .setUserId(status.getUserId())
                .setCreatedAt(status.getCreatedAt().toEpochSecond(ZoneOffset.UTC)).build();
    }
}
