package com.twitter.microservice.elastic.query.service.controller;

import com.twitter.microservice.elastic.query.service.business.ElasticQueryService;
import com.twitter.microservice.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class ElasticQueryController {
    private final ElasticQueryService elasticQueryService;

    public ElasticQueryController(ElasticQueryService elasticQueryService) {
        this.elasticQueryService = elasticQueryService;
    }

    public ResponseEntity<ElasticQueryServiceResponseModel> getDocumentById(String id){
       return ResponseEntity.ok(elasticQueryService.getDocumentById(id));
    }

    public ResponseEntity<List<ElasticQueryServiceResponseModel>> getDocumentByText(String text){
        return ResponseEntity.ok(elasticQueryService.getDocumentsByText(text));
    }

    public ResponseEntity<List<ElasticQueryServiceResponseModel>> getAllDocuments(){
        return ResponseEntity.ok( elasticQueryService.getAllDocuments());
    }

}
