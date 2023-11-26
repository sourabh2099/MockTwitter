package com.twitter.microservice.elastic.query.service.business.impl;

import com.twitter.microservice.elastic.index.client.services.QueryClient;
import com.twitter.microservice.elastic.model.impl.TwitterIndexModel;
import com.twitter.microservice.elastic.query.service.business.ElasticQueryService;
import com.twitter.microservice.elastic.query.service.model.ElasticQueryServiceResponseModel;
import com.twitter.microservice.elastic.query.service.transformer.IndexClientToResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElasticQueryServiceImpl implements ElasticQueryService {

    private final QueryClient<TwitterIndexModel> queryClient;
    private final IndexClientToResponseModel indexClientToResponseModel;

    public ElasticQueryServiceImpl(QueryClient<TwitterIndexModel> queryClient,
                                   IndexClientToResponseModel indexClientToResponseModel) {
        this.queryClient = queryClient;
        this.indexClientToResponseModel = indexClientToResponseModel;
    }

    @Override
    public ElasticQueryServiceResponseModel getDocumentById(String id) {
        return indexClientToResponseModel.getResponseModel(queryClient.getIndexModelById(id));
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getDocumentsByText(String text) {
        return queryClient.getIndexModelByField(text).stream()
                .map(indexClientToResponseModel::getResponseModel).collect(Collectors.toList());
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getAllDocuments() {
        return queryClient.getAllIndexModels().stream()
                .map(indexClientToResponseModel::getResponseModel).collect(Collectors.toList());

    }
}
