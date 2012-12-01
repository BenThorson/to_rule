package com.bthorson.torule.item;

import com.google.gson.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;

/**
 * User: Ben Thorson
 * Date: 12/1/12
 * Time: 10:47 AM
 */
public enum ItemFactory {

    INSTANCE;

    private EnumMap<ItemType, JsonArray> catalog = new EnumMap<ItemType, JsonArray>(ItemType.class);

    private ItemFactory(){
        load();
    }

    private void load() {
        try {
            String armor = FileUtils.readFileToString(new File("resources/items/armor.json"));
            JsonObject jo = new JsonParser().parse(armor).getAsJsonObject();
            JsonArray armorItems = jo.get("items").getAsJsonArray();
            catalog.put(ItemType.ARMOR, armorItems);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public Item createItemOfId(ItemType type, String itemId){
        JsonArray array = catalog.get(type);
        for (JsonElement element : array){
            JsonObject obj = element.getAsJsonObject();
            if(obj.get("id").getAsString().equalsIgnoreCase(itemId)){
                Item item = new Gson().fromJson(obj, Item.class);
                return item;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        ItemFactory.INSTANCE.createItemOfId(ItemType.ARMOR, "cloth.chest.1");
    }
}
