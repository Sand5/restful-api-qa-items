package com.api.steps;

import com.api.model.ItemData;
import com.api.model.ItemRequest;
import com.api.utils.ApiTestContext;
import com.api.utils.ConfigReader;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class ItemManagementSteps {

  private static final Logger logger = LoggerFactory.getLogger(ItemManagementSteps.class);
  private static final String BASE_URL = ConfigReader.get("base.url");
  private static final String API_KEY = ConfigReader.get("api.key");

  private final ApiTestContext apiTestContext;

  public ItemManagementSteps(ApiTestContext apiTestContext) {
    this.apiTestContext = apiTestContext;
  }

  // -------------------------
  // GIVEN STEPS
  // -------------------------

  @Given("an item exists with name {string}, CPU model {string}, and price {double}")
  public void anItemExistsWithNameCpuModelAndPrice(String itemName, String cpuModel, double price) {
    logger.info("Creating item with name: {}, CPU model: {}, price: {}", itemName, cpuModel, price);

    apiTestContext.setItemName(itemName);
    apiTestContext.setCpuModel(cpuModel);
    apiTestContext.setPrice(price);

    ItemData itemData = new ItemData(cpuModel, price);
    ItemRequest request = new ItemRequest(itemName, itemData);

    Response response =
        given()
            .contentType(ContentType.JSON)
            .header("x-api-key", API_KEY)
            .body(request)
            .when()
            .post(BASE_URL)
            .then()
            .statusCode(200)
            .extract()
            .response();

    String id = response.jsonPath().getString("id");

    logger.info("Item created with ID: {}", id);
    logger.debug("Response body: {}", response.asString());

    apiTestContext.setResponse(response);
    apiTestContext.setObjectId(id);
  }

  @Given("multiple items exist")
  public void multipleItemsExist(@NotNull DataTable dataTable) {
    List<Map<String, String>> items = dataTable.asMaps();
    logger.info("Creating multiple items for test setup");

    for (Map<String, String> item : items) {
      String name = item.get("name");
      String cpuModel = item.get("cpuModel");
      double price = Double.parseDouble(item.get("price"));

      ItemData data = new ItemData(cpuModel, price);
      ItemRequest request = new ItemRequest(name, data);

      Response response =
          given()
              .contentType(ContentType.JSON)
              .header("x-api-key", API_KEY)
              .body(request)
              .when()
              .post(BASE_URL)
              .then()
              .statusCode(200)
              .extract()
              .response();

      logger.info("Created item '{}' with id: {}", name, response.jsonPath().getString("id"));
    }
  }

  // -------------------------
  // WHEN STEPS
  // -------------------------

  @When("the request to add the item is made")
  public void theRequestToAddTheItemIsMade() {
    ItemData data = new ItemData(apiTestContext.getCpuModel(), apiTestContext.getPrice());
    ItemRequest request = new ItemRequest(apiTestContext.getItemName(), data);

    logger.info("Making POST request to add item: {}", apiTestContext.getItemName());

    Response response =
        given()
            .contentType(ContentType.JSON)
            .header("x-api-key", API_KEY)
            .body(request)
            .when()
            .post(BASE_URL)
            .then()
            .statusCode(200)
            .extract()
            .response();

    String id = response.jsonPath().getString("id");
    logger.info("Item created with id: {}", id);

    apiTestContext.setResponse(response);
    apiTestContext.setObjectId(id);
  }

  @When("the request to get the item by id is made")
  public void theRequestToGetTheItemByIdIsMade() {
    String id = apiTestContext.getObjectId();
    logger.info("Making GET request to fetch item by id: {}", id);

    Response response =
        given()
            .contentType(ContentType.JSON)
            .header("x-api-key", API_KEY)
            .when()
            .get(BASE_URL + "/" + id)
            .then()
            .extract()
            .response();

    apiTestContext.setResponse(response);
  }

  @When("the request to list all items is made")
  public void theRequestToListAllItemsIsMade() {
    logger.info("Making GET request to list all items");

    Response response =
        given().contentType(ContentType.JSON).header("x-api-key", API_KEY).when().get(BASE_URL).then().extract().response();

    apiTestContext.setResponse(response);

    List<Map<String, Object>> items = response.jsonPath().getList("$");

    logger.info("List of items returned ({} items):", items.size());

    items.forEach(
        item -> {
          logger.info(
              "- ID: {}, Name: {}, Data: {}", item.get("id"), item.get("name"), item.get("data"));
        });
  }

  @When("the request to delete the item is made")
  public void theRequestToDeleteTheItemIsMade() {
    String id = apiTestContext.getObjectId();
    logger.info("Making DELETE request to remove item with id: {}", id);

    Response response =
        given()
            .header("x-api-key", API_KEY)
            .when()
            .delete(BASE_URL + "/" + id)
            .then()
            .extract()
            .response();

    logger.info("DELETE response code: {}", response.statusCode());
    apiTestContext.setResponse(response);
  }

  // -------------------------
  // THEN STEPS
  // -------------------------

  @Then("a {int} response code is returned")
  public void aResponseCodeIsReturned(int expectedStatusCode) {
    logger.info("Verifying response code: {}", expectedStatusCode);
    apiTestContext.getResponse().then().statusCode(expectedStatusCode);
  }

  @Then("the item with name {string} is created")
  public void theItemWithNameIsCreated(String expectedName) {
    logger.info("Verifying item '{}' was successfully created", expectedName);

    String id = apiTestContext.getObjectId();
    logger.info("Fetching item by ID {} to verify creation", id);

    Response response =
        given()
            .contentType(ContentType.JSON)
            .header("x-api-key", API_KEY)
            .when()
            .get(BASE_URL + "/" + id)
            .then()
            .statusCode(200)
            .body("name", equalTo(expectedName))
            .extract()
            .response();

    apiTestContext.setResponse(response);
  }

  @Then("the response contains the item with name {string}")
  public void theResponseContainsTheItemWithName(String expectedName) {
    logger.info("Verifying response contains item with name: {}", expectedName);
    apiTestContext.getResponse().then().body("name", equalTo(expectedName));
  }

  @Then("the response contains multiple items")
  public void theResponseContainsMultipleItems() {
    logger.info("Verifying response contains multiple items");
    apiTestContext.getResponse().then().body("size()", greaterThan(1));
  }

  @Then("the item with name {string} no longer exists")
  public void theItemWithNameNoLongerExists(String name) {
    String id = apiTestContext.getObjectId();
    logger.info("Verifying item with id {} and name '{}' no longer exists", id, name);
    given().header("x-api-key", API_KEY).when().get(BASE_URL + "/" + id).then().statusCode(404);
  }
}
