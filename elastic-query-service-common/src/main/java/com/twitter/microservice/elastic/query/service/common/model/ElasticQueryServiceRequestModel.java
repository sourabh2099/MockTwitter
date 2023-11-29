package com.twitter.microservice.elastic.query.service.common.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ElasticQueryServiceRequestModel  {
    private String id;
    @NotNull
    private String text;
}
