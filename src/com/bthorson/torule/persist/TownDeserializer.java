package com.bthorson.torule.persist;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Faction;
import com.bthorson.torule.town.Building;
import com.bthorson.torule.town.Town;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 12/4/12
 * Time: 5:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class TownDeserializer {


    public Map<Integer, JsonObjectEntityPair<Town>> deserialize(File file) throws FileNotFoundException {
        Map<Integer, JsonObjectEntityPair<Town>> towns = new HashMap<Integer, JsonObjectEntityPair<Town>>();

        JsonArray array = new JsonParser().parse(new JsonReader(new FileReader(file))).getAsJsonArray();
        for (JsonElement element : array){
            Gson gson = new GsonBuilder()
                    .addDeserializationExclusionStrategy(new TownDeserializeExclude())
                    .registerTypeAdapter(Color.class, new ColorDeserializer())
                    .create();

            JsonObject object = element.getAsJsonObject();
            Town town = gson.fromJson(object, Town.class);
            towns.put(town.id, new JsonObjectEntityPair<Town>(object, town));
        }
        return towns;
    }

    private class TownDeserializeExclude implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return Arrays.asList("buildings").contains(f.getName());
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return Arrays.asList(Creature.class, Building.class, Faction.class).contains(clazz);
        }
    }
}
