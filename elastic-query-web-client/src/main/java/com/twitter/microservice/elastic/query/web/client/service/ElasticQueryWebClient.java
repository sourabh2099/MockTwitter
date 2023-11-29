package com.twitter.microservice.elastic.query.web.client.service;

import com.twitter.microservice.elastic.query.web.client.commom.model.ElasticQueryWebClientRequestModel;
import com.twitter.microservice.elastic.query.web.client.commom.model.ElasticQueryWebClientResponseModel;

import java.util.List;

public interface ElasticQueryWebClient {
    List<ElasticQueryWebClientResponseModel> getDataByText(ElasticQueryWebClientRequestModel requestModel);
}
