package com.twitter.microservice.elastic.index.client.service;

import com.twitter.microservice.elastic.model.IndexModel;

import java.util.List;

public interface ElasticIndexClient<T extends IndexModel> {
    List<String> save(List<T> documents);
}
