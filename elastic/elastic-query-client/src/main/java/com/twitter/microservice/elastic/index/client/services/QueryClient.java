package com.twitter.microservice.elastic.index.client.services;

import com.twitter.microservice.elastic.model.IndexModel;

import java.util.List;

public interface QueryClient <T extends IndexModel> {
    T getIndexModelById(String s);

    List<T> getIndexModelByField( String text);

    List<T> getAllIndexModels();
}
