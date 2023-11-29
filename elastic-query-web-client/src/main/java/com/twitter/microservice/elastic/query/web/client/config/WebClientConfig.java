package com.twitter.microservice.elastic.query.web.client.config;

import com.twitter.microservice.config.ElasticQueryClientConfigData;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {
    private final ElasticQueryClientConfigData.WebClient elasticQueryClientConfigData;

    public WebClientConfig(ElasticQueryClientConfigData elasticQueryClientConfigData) {
        this.elasticQueryClientConfigData = elasticQueryClientConfigData.getWebClient();
    }
    @LoadBalanced
    @Bean("webClientBuilder")
    public WebClient.Builder webClientBuilder(){
        return WebClient.builder()
                .baseUrl(elasticQueryClientConfigData.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, elasticQueryClientConfigData.getContentType())
                .defaultHeader(HttpHeaders.ACCEPT, elasticQueryClientConfigData.getAcceptType())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(getTcpClient())))
                .codecs(clientCodecConfigurer -> clientCodecConfigurer
                        .defaultCodecs()
                        .maxInMemorySize(elasticQueryClientConfigData.getMaxInMemorySize()));

    }

    private TcpClient getTcpClient() {
        return TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,elasticQueryClientConfigData.getConnectTimeoutMs())
                .doOnConnected(connection -> {
                    connection.addHandlerLast(
                            new ReadTimeoutHandler(elasticQueryClientConfigData.getReadTimeoutMs(), TimeUnit.MILLISECONDS)
                    );
                    connection.addHandlerLast(
                            new WriteTimeoutHandler(elasticQueryClientConfigData.getWriteTimeoutMs(),TimeUnit.MILLISECONDS)
                    );
                });
    }
}
