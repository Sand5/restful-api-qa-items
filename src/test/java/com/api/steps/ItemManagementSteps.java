package com.api.steps;

import com.api.builder.ItemRequestBuilder;
import com.api.endpoints.ItemApi;
import com.api.model.ItemRequest;
import com.api.service.ItemService;
import com.api.utils.ApiTestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.PendingException;
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
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
  public void anItemExistsWithNameCpuModelAndPrice(
      final String itemName, final String cpuModel, final double price) {
    logger.info("Preparing item data: {}, {}, {}", itemName, cpuModel, price);

    final ItemRequest request =
        new ItemRequestBuilder().withName(itemName).withCpuModel(cpuModel).withPrice(price).build();

    // ONLY store request — DO NOT call API here
    apiTestContext.setItemRequest(request);
  }

  @Given("an item exists")
  public void anItemExists() {
    logger.info("Creating item with default values for test setup");
    final ItemRequest request = new ItemRequestBuilder().withDefaultValues().build();

    apiTestContext.setItemRequest(request);

    logger.info("Item prepared for creation (not created yet)");
  }

    @Given("an item id that does not exist")
    public void anItemIdThatDoesNotExist() {
        apiTestContext.setObjectId("non-existing-id-12345");
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

  @When("the request to create the item is made")
  public void theRequestToCreateTheItemIsMade() {

    final ItemRequest request = apiTestContext.getItemRequest();
    logger.info("Creating item: {}", request.name());

    final Response response = itemService.createItem(request);

    final String id = response.jsonPath().getString("id");

    apiTestContext.setResponse(response);
    apiTestContext.setObjectId(id);
    logger.info("Item created with ID: {}", id);
  }

  @When("the request to get the item by id is made")
  public void theRequestToGetTheItemByIdIsMade() {
    final String id = apiTestContext.getObjectId();
    logger.info("Making GET request to fetch item by id: {}", id);

    final Response response = itemService.getItemById(id);
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

  @Then("the response status code is {int}")
  public void aResponseStatusCodeIs(final int expectedStatusCode) {
    logger.info("Verifying response code: {}", expectedStatusCode);
    apiTestContext.getResponse().then().statusCode(expectedStatusCode);
  }

    @Then("the response contains the item")
    public void theResponseContainsTheItem() {

        final ItemRequest expected = apiTestContext.getItemRequest();
        final Response response = apiTestContext.getResponse();

        final String actualName = response.jsonPath().getString("name");
        final String actualCpu = response.jsonPath().getString("data.cpuModel");
        final double actualPrice = response.jsonPath().getDouble("data.price");

        assertEquals(expected.name(), actualName);
        assertEquals(expected.data().cpuModel(), actualCpu);
        assertEquals(expected.data().price(), actualPrice);
    }
  @Then("the response contains multiple items")
  public void theResponseContainsMultipleItems() {
    final List<Map<String, Object>> items = apiTestContext.getItems();
    assertThat(items.size(), greaterThan(1));
  }

  @Then("the item is deleted successfully")
  public void verifyDeleted() {
    final String id = apiTestContext.getObjectId();
    logger.info("Verifying item with id {} no longer exists", id);

     final Response response = itemService.getItemById(id);
      assertEquals(404, response.statusCode());
      apiTestContext.setResponse(response);
  }

  @Then("the item is created successfully")
  public void theItemIsCreatedSuccessfully() {
    // 1. Get expected data from context
    final ItemRequest expected = apiTestContext.getItemRequest();
    final String id = apiTestContext.getObjectId();

    logger.info("Verifying item creation for ID: {}", id);

    // 2. Call GET API to fetch created item
    final Response response = itemService.getItemById(id);

    // 3. Extract actual values
    final String actualName = response.jsonPath().getString("name");
    final String actualCpu = response.jsonPath().getString("data.cpuModel");
    final double actualPrice = response.jsonPath().getDouble("data.price");

    // 4. Assertions
    assertEquals(expected.name(), actualName);
    assertEquals(expected.data().cpuModel(), actualCpu);
    assertEquals(expected.data().price(), actualPrice);

    logger.info("Item verification successful for: {}", actualName);
  }

}
