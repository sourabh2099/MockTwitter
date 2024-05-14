package com.twitter.microservice.elastic.index.client.service.impl;

import com.twitter.microservice.config.ElasticConfigData;
import com.twitter.microservice.elastic.index.client.service.ElasticIndexClient;
import com.twitter.microservice.elastic.index.client.utils.IndexUtil;
import com.twitter.microservice.elastic.model.impl.TwitterIndexModel;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TwitterElasticIndexClient implements ElasticIndexClient<TwitterIndexModel> {
    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticConfigData elasticConfigData;
    private final IndexUtil<TwitterIndexModel> elasticIndexUtil;

    public TwitterElasticIndexClient(ElasticsearchOperations elasticsearchOperations,
                                     ElasticConfigData elasticConfigData,
                                     IndexUtil<TwitterIndexModel> elasticIndexUtil) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.elasticConfigData = elasticConfigData;
        this.elasticIndexUtil = elasticIndexUtil;
    }

    @Override
    public List<String> save(List<TwitterIndexModel> documents) {
        List<IndexQuery> indexQueries = elasticIndexUtil.getIndexQueries(documents);
        List<IndexedObjectInformation> indexedObjectInformation = elasticsearchOperations.bulkIndex(indexQueries, IndexCoordinates.of(elasticConfigData.getIndexName()));
//        return indexedObjectInformation.stream().map(item -> item.id()).collect(Collectors.toList());
        return null;
    }
}
