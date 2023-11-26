package com.twitter.microservice.config.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain webSecurityCustomizer(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(requsts -> requsts
                    .requestMatchers(new AntPathRequestMatcher("/actuator/**"),
                            new AntPathRequestMatcher("/decrypt/**"),
                            new AntPathRequestMatcher("/encrypt/**"))
                    .permitAll()
                    .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());
       return httpSecurity.build();
    }
}
