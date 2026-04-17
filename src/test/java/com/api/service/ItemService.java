package com.api.service;

import com.api.endpoints.ItemApi;
import com.api.model.ItemRequest;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;

public class ItemService {

  private static final Logger logger = LoggerFactory.getLogger(ItemService.class);

  private final ItemApi itemApi;

  public ItemService(ItemApi itemApi) {
    this.itemApi = itemApi;
  }

  /**
   * Sends item request to API.
   *
   * @param  request the item request payload
   * @return the response from the API
   */
  public Response createItem(@NotNull ItemRequest request) {
      logger.info("Creating item with name: {}", request.name());

      final Response response = itemApi.createItem(request);

      logger.info("STATUS: {}", response.statusCode());
      logger.info("CONTENT-TYPE: {}", response.getContentType());
      logger.info("BODY: {}", response.asString());

      // Handle rate limit explicitly
      if (response.statusCode() == 429) {
          throw new RuntimeException(
                  "Rate limit exceeded. Stop or slow down tests. Body: " + response.asString()
          );
      }

      // Fail fast for any non-success response
      if (response.statusCode() != 200 && response.statusCode() != 201) {
          throw new RuntimeException(
                  "Create item failed. Status: " +
                          response.statusCode() +
                          ", Body: " +
                          response.asString()
          );
      }

      // Safe JSON parsing only after validation
      final String id = response.jsonPath().getString("id");

      logger.info("Item created with ID: {}", id);

      return response;
  }

  /**
   * Get an item by ID.
   *
   * @param id the item ID
   * @return the response from the API
   */
  public Response getItemById(String id) {
    logger.info("Fetching item with ID: {}", id);
    final Response response = itemApi.getItem(id);
    logger.info("Received response with status: {}", response.statusCode());
    return response;
  }

  /**
   * Delete an item by ID.
   *
   * @param id the item ID
   * @return the response from the API
   */
  public Response deleteItem(String id) {
    logger.info("Deleting item with ID: {}", id);
    final Response response = itemApi.deleteItem(id);
    logger.info("Delete response status for ID {}: {}", id, response.statusCode());
    return response;
  }

  /**
   * Safe delete: used for cleanup in hooks. Will not fail if the item is already deleted (404).
   *
   * @param id the item ID
   */
  public void deleteItemIfExists(String id) {
    logger.info("Cleaning up item with ID: {}", id);
    final Response response = itemApi.deleteItem(id);
    final int status = response.statusCode();
    if (status == 200) {
      logger.info("Item with ID {} deleted successfully", id);
    }
    else if (status == 404) {
      logger.info("Item with ID {} already deleted", id);
    }
    else {
      logger.warn("Unexpected status code {} when deleting item {}", status, id);
    }
  }

  /**
   * Get all items.
   *
   * @return a list of all items as maps
   */
  public List<Map<String, Object>> getAllItems() {
    logger.info("Fetching all items from the API");
    final Response response = itemApi.getAllItems();
    // Extract list of items from the JSON response
    final List<Map<String, Object>> items = response.jsonPath().getList("$");
    // Log the total amount of items
    logger.info("Received {} items", items.size());

    // Return parsed list
    return items;
  }
}
