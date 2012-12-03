package com.bthorson.torule.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Ben Thorson
 * Date: 12/2/12
 * Time: 4:08 PM
 */
public enum ItemType {

    INSTANCE;

    private Map<String, JsonObject> catalog = new HashMap<String, JsonObject>();


    private ItemType(){
        load();
    }

    private void load() {
        try {
            String armor = FileUtils.readFileToString(new File("resources/items/itemType.json"));
            JsonObject jo = new JsonParser().parse(armor).getAsJsonObject();
            JsonArray items = jo.get("itemTypes").getAsJsonArray();
            for (JsonElement item : items){
                catalog.put(item.getAsJsonObject().get("type").getAsString(), item.getAsJsonObject());
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public int getItemRelativeWorth(Item item){
        JsonObject object = catalog.get(item.getType());
        JsonObject weights = object.get("worthWeight").getAsJsonObject();
        int totalWorth = 0;
        for (Map.Entry<String, JsonElement> blah : weights.entrySet()){
            int val = item.getAttributes().get(blah.getKey());
            int multiplier = blah.getValue().getAsInt();
            totalWorth += val * multiplier;
        }
        return totalWorth;
    }

}
