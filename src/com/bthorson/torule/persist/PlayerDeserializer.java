package com.bthorson.torule.persist;

import com.bthorson.torule.entity.Faction;
import com.bthorson.torule.entity.group.Group;
import com.bthorson.torule.item.Item;
import com.bthorson.torule.player.Player;
import com.bthorson.torule.town.Building;
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
 * Time: 6:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerDeserializer {

    public JsonObjectEntityPair<Player> deserialize(File file) throws IOException{

        JsonArray array = new JsonParser().parse(new JsonReader(new FileReader(file))).getAsJsonArray();
        for (JsonElement element : array){
            Gson gson = new GsonBuilder()
                    .addDeserializationExclusionStrategy(new PlayerDeserializeExclude())
                    .registerTypeAdapter(Color.class, new ColorDeserializer())
                    .create();

            JsonObject object = element.getAsJsonObject();
            Player player = gson.fromJson(object, Player.class);
            return new JsonObjectEntityPair<Player>(object, player);
        }
        return null;
    }


    private class PlayerDeserializeExclude implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return Arrays.asList("ai", "group", "leader", "inventory", "equipmentSlots", "followers").contains(f.getName());
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return Arrays.asList(Building.class, Item.class, Group.class, Faction.class).contains(clazz);
        }
    }

}
