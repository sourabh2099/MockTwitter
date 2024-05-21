package com.twitter.microservice.elastic.query.service.common.exception;

public class ElasticQueryServiceException extends Exception{
    public ElasticQueryServiceException() {
        super();
    }

    public ElasticQueryServiceException(String message) {
        super(message);
    }

    public ElasticQueryServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
