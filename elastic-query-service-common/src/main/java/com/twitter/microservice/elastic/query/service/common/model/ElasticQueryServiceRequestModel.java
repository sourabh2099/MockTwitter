package com.twitter.microservice.elastic.query.service.common.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ElasticQueryServiceRequestModel  {
    private String id;
    @NotNull
    private String text;
}
