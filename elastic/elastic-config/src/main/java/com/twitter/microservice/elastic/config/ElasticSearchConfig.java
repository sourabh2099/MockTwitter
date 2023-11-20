package com.twitter.microservice.elastic.config;

import com.twitter.microservice.config.ElasticConfigData;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.erhlc.AbstractElasticsearchConfiguration;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Configuration
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchConfig.class);

    private final ElasticConfigData elasticConfigData;

    public ElasticSearchConfig(ElasticConfigData elasticConfigData) {
        this.elasticConfigData = elasticConfigData;
    }


    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(elasticConfigData.getConnectionUrl()).build();
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(Objects.requireNonNull(uriComponents.getHost()),
                            uriComponents.getPort(),
                            uriComponents.getScheme()))
                        .setRequestConfigCallback(requestConfigBuilder ->
                            requestConfigBuilder.setSocketTimeout(elasticConfigData.getSocketTimeoutMs())
                            .setConnectTimeout(elasticConfigData.getConnectTimeoutMs()))
        );
    }
}
