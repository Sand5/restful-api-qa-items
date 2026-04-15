package com.api.builder;

import com.api.model.ItemData;
import com.api.model.ItemRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public ItemRequestBuilder withDefaultValues() {
       final String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        this.name = "Apple MacBook Pro 16 - " + timestamp;
        this.cpuModel = "Intel Core i9";
        this.price = 1849.99;
        return this;
    }

  public ItemRequest build() {
    return new ItemRequest(name, new ItemData(cpuModel, price));
  }
}
