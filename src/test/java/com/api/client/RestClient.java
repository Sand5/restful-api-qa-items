package com.api.client;

import com.api.utils.ConfigReader;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RestClient {

    public static RequestSpecification request() {
        return given()
                .contentType(ContentType.JSON)
                .header("x-api-key", ConfigReader.get("api.key"));
    }
}
