package com.api.contract;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactBuilder;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.api.endpoints.ItemApi;
import com.api.service.ItemService;

import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = PactConfig.PROVIDER)
@Tag("contract")
public class ItemGetContractTest {

    @Pact(consumer = PactConfig.CONSUMER)
    public V4Pact getItemPact(PactBuilder builder) {

        return builder
                .usingLegacyDsl()
                .given("item with id 1 exists")
                .uponReceiving("get item by id")
                .path("/collections/products/objects/1")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body("""
                    {
                      "id": "1",
                      "name": "Test Item",
                      "data": {
                        "cpuModel": "Intel i7",
                        "price": 1000
                      }
                    }
                """)
                .toPact(V4Pact.class);
    }

    @Test
    @Tag("get-item-pact")
    void testGetItem(MockServer mockServer) {

      final  ItemApi api = new ItemApi(mockServer.getUrl());
       final  ItemService service = new ItemService(api);

        final Response response = service.getItemById("1");

        assertEquals(200, response.statusCode());
        assertEquals("Test Item", response.jsonPath().getString("name"));
        assertEquals("Intel i7", response.jsonPath().getString("data.cpuModel"));
        assertEquals(1000, response.jsonPath().getInt("data.price"));
    }
}