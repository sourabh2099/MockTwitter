package com.twitter.microservice.elastic.query.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@SpringBootApplication
@ComponentScan("com.twitter.microservice")
public class ElasticQueryServiceApplication {
    private static Logger LOG = LoggerFactory.getLogger(ElasticQueryServiceApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(ElasticQueryServiceApplication.class,args);
    }

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event){
        ApplicationContext applicationContext = event.getApplicationContext();
        LOG.info("Inside event Handler");
        RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping) applicationContext.getBean("requestMappingHandlerMapping");
        requestMappingHandlerMapping.getHandlerMethods().forEach(
                (key,value) -> LOG.info(" key {} value {}",key,value));
    }
}
