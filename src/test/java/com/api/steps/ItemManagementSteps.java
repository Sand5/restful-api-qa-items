package com.api.steps;

import com.api.builder.ItemRequestBuilder;
import com.api.endpoints.ItemApi;
import com.api.model.ItemRequest;
import com.api.service.ItemService;
import com.api.utils.ApiTestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class ItemManagementSteps {

  private static final Logger logger = LoggerFactory.getLogger(ItemManagementSteps.class);
  private final ApiTestContext apiTestContext;
  private final ItemService itemService;

  public ItemManagementSteps(final ApiTestContext apiTestContext) {
    this.apiTestContext = apiTestContext;
    // Pass a new ItemApi to ItemService
    this.itemService = new ItemService(new ItemApi());
  }

  // -------------------------
  // GIVEN STEPS
  // -------------------------
  @Given("an item exists with name {string}, CPU model {string}, and price {double}")
  public void anItemExistsWithNameCpuModelAndPrice(final String itemName, final String cpuModel, final double price) {
    logger.info("Creating item with name: {}, CPU model: {}, price: {}", itemName, cpuModel, price);

    // Save values to api test context
    apiTestContext.setItemName(itemName);
    apiTestContext.setCpuModel(cpuModel);
    apiTestContext.setPrice(price);

    // Use builder to create ItemRequest
    final ItemRequest request =
        new ItemRequestBuilder().withName(itemName).withCpuModel(cpuModel).withPrice(price).build();

    // Call service to create item
    final Response response = itemService.createItem(request);
    final String id = response.jsonPath().getString("id");
    logger.info("Item created with ID: {}", id);

    // Save response and objectId in the test context
    apiTestContext.setResponse(response);
    apiTestContext.setObjectId(id);
  }

  @Given("multiple items exist")
  public void multipleItemsExist(@NotNull final DataTable dataTable) {
    final var items = dataTable.asMaps(String.class, String.class);
    logger.info("Creating multiple items for test setup");

    for (final var item : items) {
      final String name = item.get("name");
      final String cpuModel = item.get("cpuModel");
      final double price = Double.parseDouble(item.get("price"));

      final ItemRequest request =
          new ItemRequestBuilder().withName(name).withCpuModel(cpuModel).withPrice(price).build();

      final Response response = itemService.createItem(request);
      final String id = response.jsonPath().getString("id");
      logger.info("Created item '{}' with ID: {}", name, id);
    }
  }

  // -------------------------
  // WHEN STEPS
  // -------------------------

  @When("the request to add the item is made")
  public void theRequestToAddTheItemIsMade() {
    // Build the request from context
    final ItemRequest request =
        new ItemRequestBuilder()
            .withName(apiTestContext.getItemName())
            .withCpuModel(apiTestContext.getCpuModel())
            .withPrice(apiTestContext.getPrice())
            .build();

    logger.info("Making POST request to add item: {}", apiTestContext.getItemName());

    final Response response = itemService.createItem(request);
    final String id = response.jsonPath().getString("id");
    logger.info("Item created with ID: {}", id);

    apiTestContext.setResponse(response);
    apiTestContext.setObjectId(id);
  }

  @When("the request to get the item by id is made")
  public void theRequestToGetTheItemByIdIsMade() {
    final String id = apiTestContext.getObjectId();
    logger.info("Making GET request to fetch item by id: {}", id);

    final Response response = itemService.getItem(id);
    apiTestContext.setResponse(response);
  }

  @When("the request to list all items is made")
  public void theRequestToListAllItemsIsMade() {
    logger.info("Making request to list all items");
    // Call the service to get the list of items
    final List<Map<String, Object>> items = itemService.getAllItems();

    apiTestContext.setItems(items);

    logger.info("List of items returned ({} items):", items.size());
    items.forEach(
        item ->
            logger.info(
                "- ID: {}, Name: {}, Data: {}",
                item.get("id"),
                item.get("name"),
                item.get("data")));
  }

  @When("the request to delete the item is made")
  public void theRequestToDeleteTheItemIsMade() {
    final String id = apiTestContext.getObjectId();
    logger.info("Making DELETE request to remove item with id: {}", id);

    final Response response = itemService.deleteItem(id);
    logger.info("DELETE response code: {}", response.statusCode());
    apiTestContext.setResponse(response);
  }

  // -------------------------
  // THEN STEPS
  // -------------------------

  @Then("a {int} response code is returned")
  public void aResponseCodeIsReturned(final int expectedStatusCode) {
    logger.info("Verifying response code: {}", expectedStatusCode);
    apiTestContext.getResponse().then().statusCode(expectedStatusCode);
  }

  @Then("the item with name {string} is created")
  public void theItemWithNameIsCreated(final String expectedName) {
    logger.info("Verifying item '{}' was successfully created", expectedName);

    final String id = apiTestContext.getObjectId();
    logger.info("Fetching item by ID {} to verify creation", id);

    final Response response = itemService.assertItemName(id, expectedName);
    apiTestContext.setResponse(response);
  }

  @Then("the response contains the item with name {string}")
  public void theResponseContainsTheItemWithName(final String expectedName) {
    logger.info("Verifying response contains item with name: {}", expectedName);
    apiTestContext.getResponse().then().body("name", equalTo(expectedName));
  }

  @Then("the response contains multiple items")
  public void theResponseContainsMultipleItems() {
    final List<Map<String, Object>> items = apiTestContext.getItems();
    assertThat(items.size(), greaterThan(1));
  }

  @Then("the item with name {string} no longer exists")
  public void theItemWithNameNoLongerExists(final String name) {
    final String id = apiTestContext.getObjectId();
    logger.info("Verifying item with id {} and name '{}' no longer exists", id, name);
    final Response response = itemService.assertItemNotFound(id);
    apiTestContext.setResponse(response);
  }
}
