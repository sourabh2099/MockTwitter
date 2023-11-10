package com.twitter.microservice.dto;

import lombok.Builder;
import lombok.ToString;

import java.time.LocalDateTime;
@Builder
@ToString
public class TwitterStatus {
    private String userId;
    private LocalDateTime createdAt;
    private String status;
}
