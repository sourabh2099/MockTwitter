package com.twitter.microservice.elastic.query.service.security;

import com.twitter.microservice.config.ElasticQueryServiceConfigData;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
@Qualifier("elastic-query-service-audience-validator")
@Service
public class AudienceValidator implements OAuth2TokenValidator<Jwt> {
    private final ElasticQueryServiceConfigData elasticQueryServiceConfigData;

    public AudienceValidator(ElasticQueryServiceConfigData elasticQueryServiceConfigData) {
        this.elasticQueryServiceConfigData = elasticQueryServiceConfigData;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        if (token.getAudience().contains(elasticQueryServiceConfigData.getCustomAudience())) {
            return OAuth2TokenValidatorResult.success();
        }
        OAuth2Error audienceError = new OAuth2Error("invalid Token",
               " The required audience " + elasticQueryServiceConfigData.getCustomAudience() + " is missing!",null);
        return OAuth2TokenValidatorResult.failure(audienceError);
    }
}
