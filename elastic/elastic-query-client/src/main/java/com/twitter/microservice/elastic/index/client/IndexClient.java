package com.twitter.microservice.elastic.index.client;

import com.twitter.microservice.elastic.model.IndexModel;

import java.util.List;

public interface IndexClient<T extends IndexModel> {
    List<String> save();
}
