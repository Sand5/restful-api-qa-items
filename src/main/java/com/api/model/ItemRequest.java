package com.api.model;

public class ItemRequest {
    private String name;
    private ItemData data;

    public ItemRequest() {}

    public ItemRequest(String name, ItemData data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public ItemData getData() {
        return data;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setData(ItemData data) {
        this.data = data;
    }
}
