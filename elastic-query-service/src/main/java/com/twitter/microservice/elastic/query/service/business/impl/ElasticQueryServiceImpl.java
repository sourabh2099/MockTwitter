package com.twitter.microservice.elastic.query.service.business.impl;

import com.twitter.microservice.elastic.index.client.services.QueryClient;
import com.twitter.microservice.elastic.model.impl.TwitterIndexModel;
import com.twitter.microservice.elastic.query.service.business.ElasticQueryService;
import com.twitter.microservice.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import com.twitter.microservice.elastic.query.service.model.assembler.ElasticQueryAssemblerResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElasticQueryServiceImpl implements ElasticQueryService {

    private final QueryClient<TwitterIndexModel> queryClient;
    private final ElasticQueryAssemblerResponseModel elasticQueryAssemblerReponseModel;
    public ElasticQueryServiceImpl(QueryClient<TwitterIndexModel> queryClient,
                                   ElasticQueryAssemblerResponseModel elasticQueryAssemblerReponseModel) {
        this.queryClient = queryClient;
        this.elasticQueryAssemblerReponseModel = elasticQueryAssemblerReponseModel;
    }

    @Override
    public ElasticQueryServiceResponseModel getDocumentById(String id) {
        return elasticQueryAssemblerReponseModel.toModel(queryClient.getIndexModelById(id));
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getDocumentsByText(String text) {
        return elasticQueryAssemblerReponseModel.toModels(queryClient.getIndexModelByField(text));
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getAllDocuments() {
        return elasticQueryAssemblerReponseModel.toModels(queryClient.getAllIndexModels());
    }
}
