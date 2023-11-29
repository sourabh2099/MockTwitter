package com.twitter.microservice.elastic.query.web.client.commom.exception;

public class ElasticQueryWebClientException extends RuntimeException{
    public ElasticQueryWebClientException() {
    }

    public ElasticQueryWebClientException(String message) {
        super(message);
    }

    public ElasticQueryWebClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
