package com.twitter.microservice.elastic.query.service.security;


import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.core.convert.converter.Converter;
import java.util.*;
import java.util.stream.Collectors;

import static com.twitter.microservice.elastic.query.service.Constants.NA;
public class TwitterQueryUserJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final TwitterQueryUserDetailsService twitterQueryUserDetailsService;
    private static final String REALM_ACCESS_CLAIM = "realm_access";
    private static final String ROLES_CLAIM = "roles";
    private static final String SCOPE_CLAIM = "scope";
    private static final String USERNAME_CLAIM = "preferred_username";
    private static final String DEFAULT_ROLE_PREFIX = "ROLE_";
    private static final String DEFAULT_SCOPE_PREFIX = "SCOPE_";
    private static final String SCOPE_SEPARATOR = " ";

    public TwitterQueryUserJwtConverter(TwitterQueryUserDetailsService twitterQueryUserDetailsService) {
        this.twitterQueryUserDetailsService = twitterQueryUserDetailsService;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<SimpleGrantedAuthority> authoritiesFromJwt = getAuthoritiesFromJwt(jwt);
        return Optional.ofNullable(
                twitterQueryUserDetailsService.loadUserByUsername(jwt.getClaimAsString(USERNAME_CLAIM)))
                .map(userDetails -> {
                    ((TwitterUserModel) userDetails).setAuthorities(authoritiesFromJwt);
                    return new UsernamePasswordAuthenticationToken(userDetails, NA, authoritiesFromJwt);
                }).orElseThrow(
                        () -> new BadCredentialsException("User could not be found!")
                );
    }

    Collection<SimpleGrantedAuthority> getAuthoritiesFromJwt(Jwt jwt){
        return getCombinedAuthorities(jwt).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private Collection<String> getCombinedAuthorities(Jwt jwt){
        Collection<String> roles = getRoles(jwt);
        roles.addAll(getScopes(jwt));
        return roles;
    }
    @SuppressWarnings("unchecked")
    private Collection<String> getRoles(Jwt jwt) {
        Object roles = ((Map<String, Object>) jwt.getClaims().get(REALM_ACCESS_CLAIM)).get(ROLES_CLAIM);
        if(roles instanceof Collection){
           return  ((Collection<String>) roles).stream()
                    .map(authority -> DEFAULT_ROLE_PREFIX + authority.toUpperCase())
                    .collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }

    private Collection<String> getScopes(Jwt jwt){
        Object scope = jwt.getClaims().get(SCOPE_CLAIM);
        if(scope instanceof String){
            return Arrays.stream(((String) scope).split(SCOPE_SEPARATOR))
                    .map(authority -> DEFAULT_SCOPE_PREFIX + authority.toUpperCase())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


}
