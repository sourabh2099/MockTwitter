package com.twitter.microservice.dto;

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public class TwitterStatus {
    private String userId;
    private LocalDateTime createdAt;
    private String status;
}
