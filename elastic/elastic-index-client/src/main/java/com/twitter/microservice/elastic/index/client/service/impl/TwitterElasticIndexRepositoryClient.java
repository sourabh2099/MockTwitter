package com.twitter.microservice.elastic.index.client.service.impl;

import com.twitter.microservice.common.util.CollectionUtil;
import com.twitter.microservice.elastic.index.client.repository.TwitterElasticIndexClientRepository;
import com.twitter.microservice.elastic.index.client.service.ElasticIndexClient;
import com.twitter.microservice.elastic.model.impl.TwitterIndexModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Primary
@Service
public class TwitterElasticIndexRepositoryClient implements ElasticIndexClient<TwitterIndexModel> {
    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticIndexRepositoryClient.class);
    private final TwitterElasticIndexClientRepository twitterElasticIndexClientRepository;

    public TwitterElasticIndexRepositoryClient(TwitterElasticIndexClientRepository twitterElasticIndexClientRepository) {
        this.twitterElasticIndexClientRepository = twitterElasticIndexClientRepository;
    }

    @Override
    public List<String> save(List<TwitterIndexModel> documents) {
        List<TwitterIndexModel> twitterIndexModels = CollectionUtil.getInstance().convertIterableToList(twitterElasticIndexClientRepository.saveAll(documents));
        List<String> ids = twitterIndexModels.stream().map(TwitterIndexModel::getId).collect(Collectors.toList());
        LOG.info("Documents indexed successfully with type: {} and ids: {}", TwitterIndexModel.class, ids);
        return ids;
    }
}
