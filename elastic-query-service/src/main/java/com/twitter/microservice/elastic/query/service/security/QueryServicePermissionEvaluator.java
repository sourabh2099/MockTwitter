package com.twitter.microservice.elastic.query.service.security;

import com.twitter.microservice.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.Serializable;
@Service
public class QueryServicePermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication,
                                 Object targetDomain,
                                 Object permission) {
        if(targetDomain instanceof ElasticQueryServiceResponseModel){
            return preAuthorize(authentication,((ElasticQueryServiceResponseModel) targetDomain).getId(),permission);
        }
        return false;
    }

    private boolean preAuthorize(Authentication authentication, String id, Object permission) {
        TwitterUserModel principal = (TwitterUserModel) authentication.getPrincipal();
        PermissionType userPermission = principal.getPermissions().get(id);
        return hasPermission((String) permission,userPermission);
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId,
                                 String targetType,
                                 Object permission) {
        return false;
    }

    private boolean hasPermission(String requiredPermission, PermissionType userPermission) {
        return userPermission != null && userPermission.getType().equals(requiredPermission);
    }
}
