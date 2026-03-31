package com.api.utils;

import io.restassured.response.Response;
import java.util.List;
import java.util.Map;

public class ApiTestContext {

  private Response response;
  private String objectId;
  private String itemName;
  private String cpuModel;
  private double price;
  private List<Map<String, Object>> items;

  public Response getResponse() {
    return response;
  }

  public String getObjectId() {
    return objectId;
  }

  public String getItemName() {
    return itemName;
  }

  public String getCpuModel() {
    return cpuModel;
  }

  public double getPrice() {
    return price;
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

  public void setItemName(String itemName) {
    this.itemName = itemName;
  }

  public void setCpuModel(String cpuModel) {
    this.cpuModel = cpuModel;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public void setItems(List<Map<String, Object>> items) {
    this.items = items;
  }

  public void clear() {
    clearItemData();
    clearResponse();
  }

  private void clearItemData() {
    itemName = null;
    cpuModel = null;
    price = 0.0;
  }

  private void clearResponse() {
    response = null;
    objectId = null;
  }
}
