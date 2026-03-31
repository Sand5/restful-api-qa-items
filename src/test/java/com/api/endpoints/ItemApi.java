package com.api.endpoints;

import com.api.client.RestClient;
import com.api.model.ItemRequest;
import com.api.utils.ConfigReader;
import io.restassured.response.Response;

public class ItemApi {
    private static final String BASE_URL = ConfigReader.get("base.url");

    public Response createItem(ItemRequest request) {
        return RestClient.request()
                .body(request)
                .post(BASE_URL);
    }

    public Response getItem(String id) {
        return RestClient.request()
                .get(BASE_URL + "/" + id);
    }

    public Response getAllItems() {
        return RestClient.request()
                .get(BASE_URL);
    }

    public Response deleteItem(String id) {
        return RestClient.request()
                .delete(BASE_URL + "/" + id);
    }
}
