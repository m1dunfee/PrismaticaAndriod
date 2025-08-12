package com.prismatica.iotInterface.Model;


public class Item {
    // Variables
    public int id;
    public String name;
    public int qty;

    public Item(int id, String name, int qty) {
        this.id = id;
        this.name = name;
        this.qty = qty;
    }
}