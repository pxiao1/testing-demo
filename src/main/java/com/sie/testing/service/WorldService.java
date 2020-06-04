package com.sie.testing.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WorldService {

    Logger logger = LoggerFactory.getLogger(WorldService.class);

    @Value( "${service.world.url}" )
    private String serviceUrl;

    public ResponseEntity<String> getWorld(){
        String url = serviceUrl + "/world";
        logger.info("Calling API at {}", url);
        return new RestTemplate().getForEntity(url, String.class);
    }

}
