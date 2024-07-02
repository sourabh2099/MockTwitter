package com.twitter.microservice.gateway.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {
    private static final Logger LOG = LoggerFactory.getLogger(FallbackController.class);
    @PostMapping("query-fallback")
    public ResponseEntity<?> queryServiceFallback(){
        LOG.info("Returning fallback result for elastic-query-service");
        return ResponseEntity.ok("this is the fallback method for QueryService");
    }
}
