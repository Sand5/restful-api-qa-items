package com.api.utils;

import io.restassured.response.Response;

public class ApiTestContext {

  private Response response;
  private String objectId;
  private String itemName;
  private String cpuModel;
  private double price;

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
}
