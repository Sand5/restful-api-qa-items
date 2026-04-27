package com.api.contract;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;

import com.api.builder.ItemRequestBuilder;
import com.api.endpoints.ItemApi;
import com.api.model.ItemRequest;
import com.api.service.ItemService;

import io.restassured.response.Response;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = PactConfig.PROVIDER)
@Tag("contract")
public class ItemCreateContractTest {

    @SuppressWarnings("DataFlowIssue")
    @Pact(consumer = PactConfig.CONSUMER)
    public V4Pact createItemPact(PactBuilder builder) {

        return builder
                .usingLegacyDsl()
                .given("item can be created")
                .uponReceiving("a request to create item")
                .path("/collections/products/objects")
                .method("POST")
                .headers("Content-Type", "application/json")
                .body(new PactDslJsonBody()
                        .stringType("name", "New Item")
                        .object("data")
                        .stringType("cpuModel", "Default CPU")
                        .numberType("price", 100.0)
                        .closeObject()
                )
                .willRespondWith()
                .status(201)
                .body(new PactDslJsonBody()
                        .stringType("id", "10")
                        .stringType("name", "New Item")
                )
                .toPact(V4Pact.class);
    }

    @Test
    @Tag("create-item-pact")
    void testCreateItem(MockServer mockServer) {

        // Arrange
        final ItemApi api = new ItemApi(mockServer.getUrl());
        final ItemService service = new ItemService(api);

        final ItemRequest request = new ItemRequestBuilder()
                .withName("New Item")
                .withCpuModel("Default CPU")
                .withPrice(100.0)
                .build();

        // Act
        final Response response = service.createItem(request);

        // Assert
        assertEquals(201, response.statusCode());
        assertEquals("New Item", response.jsonPath().getString("name"));
        assertEquals("10", response.jsonPath().getString("id"));
    }
}