package com.twitter.microservice.elastic.query.service.common.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
@Builder
@Data
public class ElasticQueryServiceResponseModelV2 extends RepresentationModel<ElasticQueryServiceResponseModelV2> {
    private Long id;
    private Long userId;
    private String text;
    private String text2;
}
