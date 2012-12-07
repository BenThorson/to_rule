package com.bthorson.torule.item;

import com.bthorson.torule.entity.EntityManager;
import com.google.gson.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Ben Thorson
 * Date: 12/1/12
 * Time: 10:47 AM
 */
public enum ItemFactory {

    INSTANCE;

    private Gson gson = new Gson();

    private Map<String, JsonObject> catalog = new HashMap<String, JsonObject>();

    private ItemFactory(){
        load();
    }

    private void load() {
        try {
            String armor = FileUtils.readFileToString(new File("resources/items/items.json"));
            JsonObject jo = new JsonParser().parse(armor).getAsJsonObject();
            JsonArray items = jo.get("items").getAsJsonArray();
            for (JsonElement item : items){
                catalog.put(item.getAsJsonObject().get("itemId").getAsString(), item.getAsJsonObject());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Item createItemOfId(String itemId){
        JsonObject obj = catalog.get(itemId);
        Item item = gson.fromJson(obj, Item.class);
        EntityManager.getInstance().addItem(item);
        return item;
    }

    public Item createGoldDrop(int quantity){
        JsonObject obj = catalog.get("gold");
        Item item = gson.fromJson(obj, Item.class);
        item.getAttributes().put("quantity", quantity);
        EntityManager.getInstance().addItem(item);
        return item;
    }

    public static void main(String[] args) {
        ItemFactory.INSTANCE.createItemOfId("cloth.chest.1");
    }
}
