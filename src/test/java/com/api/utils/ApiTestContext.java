package com.api.utils;

import com.api.model.ItemRequest;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;

public class ApiTestContext {

  private Response response;
  private ItemRequest itemRequest;
  private String objectId;
  private List<Map<String, Object>> items;

  public Response getResponse() {
    return response;
  }

  public String getObjectId() {
    return objectId;
  }

    public ItemRequest getItemRequest() {
        return itemRequest;
    }

  public List<Map<String, Object>> getItems() {
    return items;
  }

  public void setResponse(Response response) {
    this.response = response;
  }

  public void setObjectId(String objectId) {
    this.objectId = objectId;
  }

    public void setItemRequest(ItemRequest itemRequest) {
        this.itemRequest = itemRequest;
    }
  public void setItems(List<Map<String, Object>> items) {
    this.items = items;
  }

     public void clear() {
        response = null;
        objectId = null;
        itemRequest = null;
        items = null;
    }

    }
