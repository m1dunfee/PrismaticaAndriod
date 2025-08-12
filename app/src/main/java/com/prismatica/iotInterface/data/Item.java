package com.prismatica.iotInterface.data;

public class Item {
    public int id;
    public String name;
    public int qty;
    public String notes;

    public Item(int id, String name, int qty, String notes) {
        this.id = id;
        this.name = name;
        this.qty = qty;
    }
}