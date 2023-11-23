package com.twitter.microservice.elastic.index.client.services.impl;

import com.twitter.microservice.config.ElasticConfigData;
import com.twitter.microservice.config.ElasticQueryConfigData;
import com.twitter.microservice.elastic.index.client.exceptions.ElasticQueryClientException;
import com.twitter.microservice.elastic.index.client.services.QueryClient;
import com.twitter.microservice.elastic.index.client.util.ElasticQueryUtil;
import com.twitter.microservice.elastic.model.impl.TwitterIndexModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class TwitterElasticIndexClient implements QueryClient<TwitterIndexModel> {
    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticIndexClient.class);

    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticConfigData elasticConfigData;
    private final ElasticQueryConfigData elasticQueryConfigData;
    private final ElasticQueryUtil<TwitterIndexModel> elasticQueryUtil;

    public TwitterElasticIndexClient(ElasticsearchOperations elasticsearchOperations,
                                     ElasticConfigData elasticConfigData,
                                     ElasticQueryConfigData elasticQueryConfigData,
                                     ElasticQueryUtil<TwitterIndexModel> elasticQueryUtil) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.elasticConfigData = elasticConfigData;
        this.elasticQueryConfigData = elasticQueryConfigData;
        this.elasticQueryUtil = elasticQueryUtil;
    }


    @Override
    public TwitterIndexModel getIndexModelById(String s) {
        Query searchQueryById = elasticQueryUtil.getSearchQueryById(s);
        SearchHit<TwitterIndexModel> searchHit = elasticsearchOperations.searchOne(searchQueryById, TwitterIndexModel.class, IndexCoordinates.of(elasticConfigData.getIndexName()));
        if (searchHit == null) {
            LOG.error("No document found with id {} ", s);
            throw new ElasticQueryClientException("No document found with id  " + s);
        }
        LOG.info("Found Document with id {}", s);
        return searchHit.getContent();
    }

    @Override
    public List<TwitterIndexModel> getIndexModelByField(String text) {
        Query queryByFieldText = elasticQueryUtil.getSearchQueryByFieldText(elasticQueryConfigData.getTextField(), text);
        SearchHits<TwitterIndexModel> search = elasticsearchOperations.search(queryByFieldText, TwitterIndexModel.class, IndexCoordinates.of(elasticConfigData.getIndexName()));
        return search.get().map(SearchHit::getContent).collect(Collectors.toList());
    }

    @Override
    public List<TwitterIndexModel> getAllIndexModels() {
        Query searchQueryForAll = elasticQueryUtil.getSearchQueryForAll();
        SearchHits<TwitterIndexModel> searchHits = elasticsearchOperations.search(searchQueryForAll, TwitterIndexModel.class, IndexCoordinates.of(elasticConfigData.getIndexName()));
        return searchHits.get().map(SearchHit::getContent).collect(Collectors.toList());
    }
}
