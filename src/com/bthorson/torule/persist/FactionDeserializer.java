package com.bthorson.torule.persist;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Faction;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 12/4/12
 * Time: 7:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class FactionDeserializer {
    public Map<Integer, JsonObjectEntityPair<Faction>> deserialize(File file) throws IOException {
        Map<Integer, JsonObjectEntityPair<Faction>> factions = new HashMap<Integer, JsonObjectEntityPair<Faction>>();

        JsonArray array = new JsonParser().parse(new JsonReader(new FileReader(file))).getAsJsonArray();
        for (JsonElement element : array){
            Gson gson = new GsonBuilder()
                    .addDeserializationExclusionStrategy(new FactionDeserializeExclude())
                    .registerTypeAdapter(Color.class, new ColorDeserializer())
                    .create();

            JsonObject object = element.getAsJsonObject();
            Faction faction = gson.fromJson(object, Faction.class);
            factions.put(faction.id, new JsonObjectEntityPair<Faction>(object, faction));
        }
        return factions;
    }

    private class FactionDeserializeExclude implements ExclusionStrategy {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return Arrays.asList("enemies", "allies").contains(f.getName());
            }

                @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }



}
