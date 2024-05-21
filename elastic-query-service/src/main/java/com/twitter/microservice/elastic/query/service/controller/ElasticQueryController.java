package com.twitter.microservice.elastic.query.service.controller;

import com.twitter.microservice.elastic.query.service.business.ElasticQueryService;
import com.twitter.microservice.elastic.query.service.common.model.ElasticQueryServiceRequestModel;
import com.twitter.microservice.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import com.twitter.microservice.elastic.query.service.common.model.ElasticQueryServiceResponseModelV2;
import jakarta.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/documents", produces = "application/vnd.api.v1.json") // using content negotiation here
public class ElasticQueryController {
    private final ElasticQueryService elasticQueryService;
    private static Logger LOG = LoggerFactory.getLogger(ElasticQueryController.class);

    public ElasticQueryController(ElasticQueryService elasticQueryService) {
        this.elasticQueryService = elasticQueryService;
    }
    @GetMapping("")
    public ResponseEntity<List<ElasticQueryServiceResponseModel>> getAllDocuments(){
        List<ElasticQueryServiceResponseModel> allDocuments = elasticQueryService.getAllDocuments();
        LOG.info("Elastic search Fetched documents of size {}",allDocuments.size());
        return ResponseEntity.ok(allDocuments);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ElasticQueryServiceResponseModel> getDocumentById(String id){
        ElasticQueryServiceResponseModel documentById = elasticQueryService.getDocumentById(id);
        LOG.debug("Elastic Search Returned document with id {}", id);
        return ResponseEntity.ok(documentById);
    }
    @GetMapping(value= "/{id}" ,produces = "application/vnd.api.v2.json")
    public ResponseEntity<ElasticQueryServiceResponseModelV2> getDocumentByIdV2(@PathVariable @NotEmpty String id){
        ElasticQueryServiceResponseModel documentById = elasticQueryService.getDocumentById(id);
        ElasticQueryServiceResponseModelV2 v2Model = getV2Model(documentById);
        return ResponseEntity.ok(v2Model);
    }
    private ElasticQueryServiceResponseModelV2 getV2Model(ElasticQueryServiceResponseModel queryServiceRequestModel){
        return ElasticQueryServiceResponseModelV2.builder()
                .id(Long.parseLong(queryServiceRequestModel.getId()))
                .text(queryServiceRequestModel.getText())
                .text2("Version 2 text")
                .build();
    }
//    @PostMapping("/{text}")
//    public ResponseEntity<List<ElasticQueryServiceResponseModel>> getDocumentByText(
//            ElasticQueryServiceRequestModel queryServiceRequestModel){
//        elasticQueryService.getDocumentsByText(queryServiceRequestModel.getText());
//        return ResponseEntity.ok();
//    }


}
