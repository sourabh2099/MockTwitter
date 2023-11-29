package com.twitter.microservice.elastic.query.service.model.assembler;

import com.twitter.microservice.elastic.model.impl.TwitterIndexModel;
import com.twitter.microservice.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import com.twitter.microservice.elastic.query.service.common.transformer.IndexClientToResponseModel;
import com.twitter.microservice.elastic.query.service.controller.ElasticQueryController;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ElasticQueryAssemblerResponseModel extends RepresentationModelAssemblerSupport<TwitterIndexModel, ElasticQueryServiceResponseModel> {
    private final IndexClientToResponseModel transformer;
    public ElasticQueryAssemblerResponseModel(Class<?> controllerClass, Class<ElasticQueryAssemblerResponseModel> resourceType, IndexClientToResponseModel transformer) {
        super(ElasticQueryController.class, ElasticQueryServiceResponseModel.class);
        this.transformer = transformer;
    }

    @Override
    public ElasticQueryServiceResponseModel toModel(TwitterIndexModel entity) {
        ElasticQueryServiceResponseModel responseModel = transformer.getResponseModel(entity);
        responseModel.add(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ElasticQueryController.class)
                        .getDocumentById(entity.getId())).withSelfRel()
        );
        responseModel.add(
                WebMvcLinkBuilder.linkTo(ElasticQueryController.class)
                        .withRel("document")
        );
        return responseModel;
    }

    public List<ElasticQueryServiceResponseModel> toModels(List<TwitterIndexModel> entities){
        return entities.stream().map(this::toModel).collect(Collectors.toList());
    }
}
