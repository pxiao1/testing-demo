package com.sie.testing.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HelloService {

    Logger logger = LoggerFactory.getLogger(HelloService.class);

    @Value( "${service.hello.url}" )
    private String serviceUrl;

    public ResponseEntity<String> getHello(){
        String url = serviceUrl + "/hello";
        logger.info("Calling API at {}", url);
        return new RestTemplate().getForEntity(url, String.class);
    }

}
