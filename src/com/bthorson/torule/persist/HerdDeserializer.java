package com.bthorson.torule.persist;

import com.bthorson.torule.entity.Faction;
import com.bthorson.torule.entity.Herd;
import com.bthorson.torule.entity.group.Group;
import com.bthorson.torule.item.Item;
import com.bthorson.torule.town.Building;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Ben Thorson
 * Date: 12/11/12
 * Time: 11:39 AM
 */
public class HerdDeserializer {


    public Map<Integer, JsonObjectEntityPair<Herd>> deserialize(File file) throws FileNotFoundException {

        Map<Integer, JsonObjectEntityPair<Herd>> herds = new HashMap<Integer, JsonObjectEntityPair<Herd>>();

        JsonArray array = new JsonParser().parse(new JsonReader(new FileReader(file))).getAsJsonArray();
        for (JsonElement element : array){
            Gson gson = new GsonBuilder()
                    .addDeserializationExclusionStrategy(new HerdDeserializeExclude())
                    .registerTypeAdapter(Color.class, new ColorDeserializer())
                    .create();

            JsonObject object = element.getAsJsonObject();
            Herd herd = gson.fromJson(object, Herd.class);
            herds.put(herd.id, new JsonObjectEntityPair<Herd>(object, herd));
        }
        return herds;

    }

    private class HerdDeserializeExclude implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return Arrays.asList("ai", "creatures").contains(f.getName());
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return Arrays.asList(Building.class, Item.class, Group.class, Faction.class).contains(clazz);
        }
    }

}
