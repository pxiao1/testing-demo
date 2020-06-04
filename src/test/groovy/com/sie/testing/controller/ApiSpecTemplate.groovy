package com.sie.testing.controller

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 8088)
class ApiSpecTemplate extends Specification{

    def "0. Happy Path: valid request with successful response"(){
        given: "stub and setup request"

        when: "send request"

        then: "hello service error returned"
    }


    def "1. Negative: request validation failed, 422 error returned"(){
        given: "setup request with missing field"

        when: "send request"

        then: "422 error response back"
    }

    def "2. Negative: runtime exception occurred during validation phase"(){
        given: "setup request with exception error"

        when: "send request"

        then: "exception observed"
    }

    def "3. Negative: hello service return error response, error response returned"(){
        given: "setup request and stub error request from hello service"

        when: "send request"

        then: "hello service error returned"
    }

    def "4. Negative: world service return error response, error response returned"(){
        given: "setup request and stub error request from world service"

        when: "send request"

        then: "world service error returned"
    }

}
