package com.twitter.microservice.elastic.query.service.common.transformer;
import com.twitter.microservice.elastic.model.impl.TwitterIndexModel;
import com.twitter.microservice.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import org.springframework.stereotype.Component;

@Component
public class IndexClientToResponseModel {
    public ElasticQueryServiceResponseModel getResponseModel(TwitterIndexModel indexModel){
        return ElasticQueryServiceResponseModel.builder()
                .id(indexModel.getId())
                .userId(indexModel.getUserId())
                .createdAt(indexModel.getCreateAt())
                .text(indexModel.getText())
                .build();
    }
}
