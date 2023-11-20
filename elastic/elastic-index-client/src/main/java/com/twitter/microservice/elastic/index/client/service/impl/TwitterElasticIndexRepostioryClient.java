package com.twitter.microservice.elastic.index.client.service.impl;

import com.twitter.microservice.elastic.index.client.repository.TwitterElasticIndexClientRepository;
import com.twitter.microservice.elastic.index.client.service.ElasticIndexClient;
import com.twitter.microservice.elastic.model.impl.TwitterIndexModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TwitterElasticIndexRepostioryClient implements ElasticIndexClient<TwitterIndexModel> {
    private final TwitterElasticIndexClientRepository twitterElasticIndexClientRepository;

    public TwitterElasticIndexRepostioryClient(TwitterElasticIndexClientRepository twitterElasticIndexClientRepository) {
        this.twitterElasticIndexClientRepository = twitterElasticIndexClientRepository;
    }

    @Override
    public List<String> save(List<TwitterIndexModel> documents) {
        List<TwitterIndexModel> twitterIndexModels = (List<TwitterIndexModel>) twitterElasticIndexClientRepository.saveAll(documents);
        return twitterIndexModels.stream().map(TwitterIndexModel::getId).collect(Collectors.toList());
    }
}
