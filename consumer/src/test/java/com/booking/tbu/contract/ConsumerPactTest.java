package com.booking.tbu.contract;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerPactTest {

    @Pact(provider="test_provider", consumer="test_consumer")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        return builder
            .given("200 response")
            .uponReceiving("valid body")
                .path("/example")
                .method("POST")
                .body(new PactDslJsonBody()
                    .stringType("request_string")
                    .numberType("request_number")
                    .object("request_object")
                        .booleanType("request_boolean")
                    .closeObject()
                    .array("request_array")
                        .decimalType()
                    .closeArray()
                )
            .willRespondWith()
                .status(200)
                .body(new PactDslJsonBody()
                    .stringType("response_string")
                    .numberType("response_number")
                    .object("response_object")
                        .booleanType("response_boolean")
                    .closeObject()
                    .array("response_array")
                        .decimalType()
                    .closeArray()
                )
            .given("400 response")
            .uponReceiving("invalid body")
                .path("/example")
                .method("POST")
                .body(new PactDslJsonBody()
                    .nullValue("request_string")
                    .stringType()
                    .nullValue("request_number")
                    .object("request_object")
                        .nullValue("request_boolean")
                    .closeObject()
                    .array("request_array")
                        .nullValue()
                    .closeArray()
                )
            .willRespondWith()
                .status(400)
                .body(new PactDslJsonBody()
                    .stringMatcher("error_enum", "VALIDATION|UNKNOWN", "VALIDATION")
                    .stringType("error_string")
                )
            .toPact();
    }

    @Test
    @PactTestFor(providerName = "test_provider")
    void verifyRequests(MockServer mockServer) throws IOException, InterruptedException {
        verify200(mockServer);
        verify400(mockServer);
    }

    private void verify200(MockServer mockServer) throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(URI.create(mockServer.getUrl() + "/example"))
            .method("POST", HttpRequest.BodyPublishers.ofString("""
                {
                    "request_string": "string",
                    "request_number": 0,
                    "request_object": {
                        "request_boolean": true
                    },
                    "request_array":[
                        0.0
                    ]
                }
            """))
            .header("Content-Type", "application/json; charset=UTF-8")
            .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
    }

    private void verify400(MockServer mockServer) throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(URI.create(mockServer.getUrl() + "/example"))
            .method("POST", HttpRequest.BodyPublishers.ofString("""
                {
                    "request_string": null,
                    "request_number": null,
                    "request_object": {
                        "request_boolean": null
                    },
                    "request_array":[
                        null
                    ]
                }
            """))
            .header("Content-Type", "application/json; charset=UTF-8")
            .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(400, response.statusCode());
    }

}
