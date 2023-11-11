package com.twitter.microservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
@Builder
@ToString
@Data
public class TwitterStatus {
    private long id;
    private String userId;
    private LocalDateTime createdAt;
    private String status;
}
