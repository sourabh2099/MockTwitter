package com.twitter.microservice.elastic.index.client.services.impl;

import com.twitter.microservice.common.util.CollectionUtil;
import com.twitter.microservice.elastic.index.client.exceptions.ElasticQueryClientException;
import com.twitter.microservice.elastic.index.client.repository.ElasticSearchQueryRepository;
import com.twitter.microservice.elastic.index.client.services.QueryClient;
import com.twitter.microservice.elastic.model.impl.TwitterIndexModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class TwitterElasticIndexRepositoryClient implements QueryClient<TwitterIndexModel> {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticIndexRepositoryClient.class);
    private final ElasticSearchQueryRepository elasticSearchQueryRepository;

    public TwitterElasticIndexRepositoryClient(ElasticSearchQueryRepository elasticSearchQueryRepository) {
        this.elasticSearchQueryRepository = elasticSearchQueryRepository;
    }

    @Override
    public TwitterIndexModel getIndexModelById(String s) {
        Optional<TwitterIndexModel> model = elasticSearchQueryRepository.findById(s);
        model.orElseThrow(() -> new ElasticQueryClientException("Did not found entry by id"+s));
        return model.get();
    }

    @Override
    public List<TwitterIndexModel> getIndexModelByField(String text) {
        List<TwitterIndexModel> searchResults = elasticSearchQueryRepository.findByText(text);
        return searchResults;
    }

    @Override
    public List<TwitterIndexModel> getAllIndexModels() {
        return CollectionUtil.getInstance().convertIterableToList(elasticSearchQueryRepository.findAll());
    }
}
