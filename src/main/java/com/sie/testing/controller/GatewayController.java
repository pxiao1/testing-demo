package com.sie.testing.controller;

import javax.xml.bind.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sie.testing.domain.HelloWorldRequest;
import com.sie.testing.domain.ValidatorResult;
import com.sie.testing.service.HelloService;
import com.sie.testing.service.WorldService;
import com.sie.testing.validator.HelloWorldRequestValidator;

@RestController
@RequestMapping("/api")
public class GatewayController {

    Logger logger = LoggerFactory.getLogger(GatewayController.class);

    private final HelloService helloService;
    private final WorldService worldService;


    private static final String HELLO_SERVICE_ERROR = "HELLO SERVICE ERROR";
    private static final String WORLD_SERVICE_ERROR = "WORLD SERVICE ERROR";

    public GatewayController(HelloService helloService, WorldService worldService){
        this.helloService = helloService;
        this.worldService = worldService;
    }

    @PostMapping(value = "/hello-world")
    public ResponseEntity<String> getHelloWorld(@RequestBody HelloWorldRequest helloRequest) throws ValidationException {

        // validate request
        ValidatorResult result = new HelloWorldRequestValidator().validate(helloRequest);
        if (!result.isValid()){
            return new ResponseEntity<>(result.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        // call dependent service 1
        ResponseEntity<String> helloResponse = this.helloService.getHello();
        logger.info("Response from hello service: {}", helloResponse.getBody());
        if ("ERROR".equalsIgnoreCase(helloResponse.getBody())){
            return new ResponseEntity<>(HELLO_SERVICE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // call dependent service 2
        ResponseEntity<String> worldResponse = this.worldService.getWorld();
        logger.info("Response from hello service: {}", helloResponse.getBody());
        if ("ERROR".equalsIgnoreCase(worldResponse.getBody())){
            return new ResponseEntity<>(WORLD_SERVICE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // concatenate result and send back
        String responseString = helloResponse.getBody() + worldResponse.getBody();
        return new ResponseEntity<>(responseString, HttpStatus.OK);
    }

}
