package com.bthorson.torule.persist;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.item.Item;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 12/4/12
 * Time: 7:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class ItemDeserializer {
    public Map<Integer, JsonObjectEntityPair<Item>> deserialize(File file) throws IOException {
        Map<Integer, JsonObjectEntityPair<Item>> items = new HashMap<Integer, JsonObjectEntityPair<Item>>();

        JsonArray array = new JsonParser().parse(new JsonReader(new FileReader(file))).getAsJsonArray();
        for (JsonElement element : array) {
            Gson gson = new GsonBuilder()
                    .addDeserializationExclusionStrategy(new ItemDeserializeExclude())
                    .registerTypeAdapter(Color.class, new ColorDeserializer())
                    .create();

            JsonObject object = element.getAsJsonObject();
            Item item = gson.fromJson(object, Item.class);
            items.put(item.id, new JsonObjectEntityPair<Item>(object, item));
        }
        return items;
    }

    private class ItemDeserializeExclude implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return Arrays.asList(Creature.class).contains(clazz);
        }
    }


}
