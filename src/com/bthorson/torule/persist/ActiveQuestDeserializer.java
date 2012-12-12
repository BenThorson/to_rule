package com.bthorson.torule.persist;

import com.bthorson.torule.quest.ActiveQuest;
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
 * Date: 12/6/12
 * Time: 9:51 PM
 */
public class ActiveQuestDeserializer {
    public Map<Integer, JsonObjectEntityPair<ActiveQuest>> deserialize(File file) throws FileNotFoundException {

        Map<Integer, JsonObjectEntityPair<ActiveQuest>> activeQuests = new HashMap<Integer, JsonObjectEntityPair<ActiveQuest>>();

        JsonArray array = new JsonParser().parse(new JsonReader(new FileReader(file))).getAsJsonArray();
        for (JsonElement element : array) {
            Gson gson = new GsonBuilder()
                    .addDeserializationExclusionStrategy(new ActiveQuestDeserializerExclude())
                    .registerTypeAdapter(Color.class, new ColorDeserializer())
                    .create();

            JsonObject object = element.getAsJsonObject();
            ActiveQuest activeQuest = gson.fromJson(object, ActiveQuest.class);
            activeQuests.put(activeQuest.id, new JsonObjectEntityPair<ActiveQuest>(object, activeQuest));
        }
        return activeQuests;

    }

    private class ActiveQuestDeserializerExclude implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return Arrays.asList("questGiver", "creatures").contains(f.getName());
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }

}
