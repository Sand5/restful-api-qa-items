package com.api.endpoints;

import com.api.client.RestClient;
import com.api.model.ItemRequest;
import com.api.utils.ConfigReader;
import io.restassured.response.Response;

public class ItemApi {

    private final String baseUrl;

    private static final String ITEMS_PATH = "/collections/products/objects";

    public ItemApi() {
        this.baseUrl = ConfigReader.get("base.url");
    }

    public ItemApi(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Response createItem(ItemRequest request) {
        return RestClient.request()
                .body(request)
                .post(baseUrl + ITEMS_PATH);
    }

    public Response getItem(String id) {
        return RestClient.request()
                .get(baseUrl + ITEMS_PATH + "/" + id);
    }

    public Response getAllItems() {
        return RestClient.request()
                .get(baseUrl + ITEMS_PATH);
    }

    public Response deleteItem(String id) {
        return RestClient.request()
                .delete(baseUrl + ITEMS_PATH + "/" + id);
    }
}