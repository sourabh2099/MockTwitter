package com.twitter.microservice.elastic.model.impl;

import com.twitter.microservice.elastic.model.IndexModel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
@Data
@Builder
@Document(indexName = "#{elasticConfigData.indexName}")
public class TwitterIndexModel implements IndexModel {
    private String id;
    private String text;
    private Long userId;
    @Field(type = FieldType.Date, format = DateFormat.date)
    private LocalDateTime createAt;

}
