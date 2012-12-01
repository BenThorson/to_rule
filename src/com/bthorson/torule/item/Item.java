package com.bthorson.torule.item;

import java.util.Map;

/**
 * User: Ben Thorson
 * Date: 12/1/12
 * Time: 10:39 AM
 */
public class Item {

    private String name;
    private int price;
    private int weight;
    private ItemType type;
    private Map<String, Integer> attributes;

    public Item(String name, int price, int weight, Map<String, Integer> attributes) {
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.attributes = attributes;
    }

    public int getPrice() {
        return price;
    }

    public int getWeight() {
        return weight;
    }

    public Map<String, Integer> getAttributes() {
        return attributes;
    }

    public String getName() {
        return name;
    }
}
