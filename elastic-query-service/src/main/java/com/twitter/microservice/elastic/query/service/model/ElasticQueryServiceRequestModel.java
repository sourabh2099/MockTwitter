package com.twitter.microservice.elastic.query.service.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ElasticQueryServiceRequestModel {
    private String id;
    @NotNull
    private String text;
}
