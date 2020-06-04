package com.sie.testing.controller

import com.sie.testing.domain.HelloWorldRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import javax.xml.bind.ValidationException

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.get
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 8090)
class GatewayHelloApiSpec extends Specification{

    @Autowired
    GatewayController gatewayController

    def "0. Happy Path: valid request with successful response"(){
        given: "stub and setup request"
        stubFor(get(urlEqualTo("/hello-service/hello")).willReturn(aResponse()
                .withBody("Hello ")))
        stubFor(get(urlEqualTo("/world-service/world")).willReturn(aResponse()
                .withBody("World!")))

        def request = new HelloWorldRequest(name: username, age:    userage, address: "address1")
        //java way for this
//        HelloWorldRequest request = new HelloWorldRequest()
//        request.setAddress("address1")
//        request.setAge(16)
//        request.setName("name")

        when: "send request"
        def response = gatewayController.getHelloWorld(request)

        then: "hello service error returned"
        response.statusCode == HttpStatus.OK
        response.body == 'Hello World!'

        where:
        username    |    userage
        'joe'       |    18
        'mike'      |    20
        'jack'      |    33
    }

    def "1. Negative: request validation failed, 422 error returned"(){
        given: "setup request with missing element"
        def request = new HelloWorldRequest(age: 18, gender: "female")

        when: "send request"
        def response = gatewayController.getHelloWorld(request)

        then: "422 error response back"
        noExceptionThrown()
        response.statusCode == HttpStatus.UNPROCESSABLE_ENTITY
        response.body == 'NAME MISSING'
    }

    def "2. Negative: exception ocured during validation phase"(){
        given: "setup request with exception error"
        def request = new HelloWorldRequest(name: "EXCEPTION")

        when: "send request"
        gatewayController.getHelloWorld(request)

        then: "exception observed"
        thrown ValidationException
    }

    def "3. Negative: hello service return error response, error response returned"(){
        given: "setup request"
        stubFor(get(urlEqualTo("/hello-service/hello")).willReturn(aResponse()
                .withBody("ERROR")))

        def request = new HelloWorldRequest(name: "john doe", age:18, address: "address1")

        when: "send request"
        def response = gatewayController.getHelloWorld(request)

        then: "hello service error returned"
        response.statusCode == HttpStatus.INTERNAL_SERVER_ERROR
        response.body == 'HELLO SERVICE ERROR'
    }

    def "4. Negative: world service return error response, error response returned"(){
        given: "stub and setup request"
        stubFor(get(urlEqualTo("/hello-service/hello")).willReturn(aResponse()
                .withBody("Hello ")))
        stubFor(get(urlEqualTo("/world-service/world")).willReturn(aResponse()
                .withBody("ERROR")))

        def request = new HelloWorldRequest(name: "john doe", age:18, address: "address1")

        when: "send request"
        def response = gatewayController.getHelloWorld(request)

        then: "world service error returned"
        response.statusCode == HttpStatus.INTERNAL_SERVER_ERROR
        response.body == 'WORLD SERVICE ERROR'
    }


}
