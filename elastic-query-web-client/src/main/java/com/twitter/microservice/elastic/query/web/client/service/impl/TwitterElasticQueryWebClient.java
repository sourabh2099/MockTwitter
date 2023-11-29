package com.twitter.microservice.elastic.query.web.client.service.impl;

import com.twitter.microservice.config.ElasticQueryClientConfigData;
import com.twitter.microservice.elastic.query.web.client.commom.model.ElasticQueryWebClientRequestModel;
import com.twitter.microservice.elastic.query.web.client.commom.model.ElasticQueryWebClientResponseModel;
import com.twitter.microservice.elastic.query.web.client.service.ElasticQueryWebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class TwitterElasticQueryWebClient implements ElasticQueryWebClient {
    private final Logger LOG = LoggerFactory.getLogger(TwitterElasticQueryWebClient.class);
    private final WebClient.Builder webClient;
    private final ElasticQueryClientConfigData configData;

    public TwitterElasticQueryWebClient(@Qualifier("webClientBuilder") WebClient.Builder webClient,
                                        ElasticQueryClientConfigData configData) {
        this.webClient = webClient;
        this.configData = configData;
    }

    @Override
    public List<ElasticQueryWebClientResponseModel> getDataByText(ElasticQueryWebClientRequestModel requestModel) {
        return getWebClient(requestModel).bodyToFlux(ElasticQueryWebClientResponseModel.class).collectList().block();
    }

    private WebClient.ResponseSpec getWebClient(ElasticQueryWebClientRequestModel requestModel){
        return webClient.build()
                .method(HttpMethod.valueOf(configData.getQueryByText().getMethod()))
                .uri(configData.getQueryByText().getUri())
                .accept(MediaType.valueOf(configData.getQueryByText().getAccept()))
                .body(BodyInserters.fromProducer(Mono.just(requestModel),createParameterizedTypeReference())).retrieve();

    }

    private <T> ParameterizedTypeReference<T> createParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {
        };
    }
}
