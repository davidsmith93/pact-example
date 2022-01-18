package com.booking.tbu.contract;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactBrokerAuth;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import au.com.dius.pact.provider.junitsupport.loader.VersionSelector;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.matching.AnythingPattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.net.MalformedURLException;
import java.net.URL;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@Provider("test_provider")
@PactFolder("../consumer/build/pacts")
//@PactBroker(host = "${pact.broker.url}",
//    consumerVersionSelectors = {
//        @VersionSelector(tag = "master"),
//        @VersionSelector(tag = "live"),
//        @VersionSelector(tag = "production")
//    },
//    scheme = "https",
//    authentication = @PactBrokerAuth(
//        username = "${pact.broker.username}",
//        password = "${pact.broker.password}"
//    )
//)
public class ProviderPactTest {
    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort())
        .build();

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context)
    {
        context.verifyInteraction();
    }

    @BeforeEach
    void before(PactVerificationContext context) throws MalformedURLException {
        WireMockRuntimeInfo wireMockRuntimeInfo = wireMockExtension.getRuntimeInfo();
        context.setTarget(HttpTestTarget.fromUrl(new URL(wireMockRuntimeInfo.getHttpBaseUrl())));
    }

    @State("200 response")
    void verify200() {
        wireMockExtension.stubFor(WireMock.post("/example")
            .withRequestBody(new AnythingPattern())
            .willReturn(WireMock.aResponse()
                .withStatus(200)
                .withBody("""
                {
                    "response_string": "string",
                    "response_number": 0,
                    "response_object": {
                        "response_boolean": true
                    },
                    "response_array":[
                        0.0
                    ]
                }
                """)
                .withHeader("Content-Type","application/json; charset=UTF-8"))
        );
    }

    @State("400 response")
    void verify400() {
        wireMockExtension.stubFor(WireMock.post("/example")
            .withRequestBody(new AnythingPattern())
            .willReturn(WireMock.aResponse()
                .withStatus(400)
                .withBody("""
                {
                     "error_enum": "VALIDATION",
                     "error_string": "string"
                 }
                """)
                .withHeader("Content-Type","application/json; charset=UTF-8"))
        );
    }
}
