package com.api.builder;

import com.api.model.ItemData;
import com.api.model.ItemRequest;

public class ItemRequestBuilder {

  private String name = "Test Item";
  private String cpuModel = "Default CPU";
  private double price = 100.0;

  public ItemRequestBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public ItemRequestBuilder withCpuModel(String cpuModel) {
    this.cpuModel = cpuModel;
    return this;
  }

  public ItemRequestBuilder withPrice(double price) {
    this.price = price;
    return this;
  }

  public ItemRequest build() {
    return new ItemRequest(name, new ItemData(cpuModel, price));
  }
}
