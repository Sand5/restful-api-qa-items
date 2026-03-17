package com.api.model;

public class ItemData {

    private String cpuModel;
    private double price;

    public ItemData() {}

    public ItemData(String cpuModel, double price) {
        this.cpuModel = cpuModel;
        this.price = price;
    }

    public String getCpuModel() {
        return cpuModel;
    }

    public double getPrice() {
        return price;
    }

    public void setCpuModel(String cpuModel) {
        this.cpuModel = cpuModel;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
