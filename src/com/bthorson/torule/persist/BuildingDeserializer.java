package com.bthorson.torule.persist;

import com.bthorson.torule.entity.Creature;
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
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 12/4/12
 * Time: 5:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class BuildingDeserializer {


    public Map<Integer, JsonObjectEntityPair<Building>> deserialize(File file) throws FileNotFoundException {
        Map<Integer, JsonObjectEntityPair<Building>> buildings = new HashMap<Integer, JsonObjectEntityPair<Building>>();

        JsonArray array = new JsonParser().parse(new JsonReader(new FileReader(file))).getAsJsonArray();
        for (JsonElement element : array){
            Gson gson = new GsonBuilder()
                    .addDeserializationExclusionStrategy(new BuildingDeserializeExclude())
                    .registerTypeAdapter(Color.class, new ColorDeserializer())
                    .create();

            JsonObject object = element.getAsJsonObject();
            Building building = gson.fromJson(object, Building.class);
            buildings.put(building.id, new JsonObjectEntityPair<Building>(object, building));
        }
        return buildings;
    }

    private class BuildingDeserializeExclude implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return "inventory".equalsIgnoreCase(f.getName());
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return Arrays.asList(Creature.class, Item.class).contains(clazz);
        }
    }
}
