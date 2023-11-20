package com.twitter.microservice.elastic.index.client.repository;

import com.twitter.microservice.elastic.model.impl.TwitterIndexModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwitterElasticIndexClientRepository extends ElasticsearchRepository<TwitterIndexModel,String>{
}
