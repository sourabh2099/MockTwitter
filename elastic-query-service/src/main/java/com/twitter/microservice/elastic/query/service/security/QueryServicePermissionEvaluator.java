package com.twitter.microservice.elastic.query.service.security;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.Serializable;
@Service
public class QueryServicePermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication,
                                 Object targetDomainObject,
                                 Object permission) {

        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId,
                                 String targetType,
                                 Object permission) {
        return false;
    }
}
