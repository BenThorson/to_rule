package com.bthorson.torule.persist;

import com.bthorson.torule.entity.*;
import com.bthorson.torule.entity.ai.*;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.item.Item;
import com.bthorson.torule.map.Local;
import com.bthorson.torule.map.World;
import com.bthorson.torule.player.Player;
import com.bthorson.torule.town.Building;
import com.bthorson.torule.town.Town;
import com.bthorson.torule.worldgen.SavedWorldLoader;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Ben Thorson
 * Date: 12/4/12
 * Time: 4:11 PM
 */
public class LoadAction {


    private Local[][] world;
    private Map<Integer, JsonObjectEntityPair<Building>> buildings;
    private Map<Integer, JsonObjectEntityPair<Town>> towns;
    private Map<Integer, JsonObjectEntityPair<Creature>> creatures;
    private JsonObjectEntityPair<Player> player;
    private Map<Integer, JsonObjectEntityPair<Item>> items;
    private Map<Integer, JsonObjectEntityPair<Faction>> factions;
    private long turn;
    private int idSerialGen;
    public SavedWorldLoader load(String name){
        try {
            String prefix = "save/" + name;

            WorldParser parser = new WorldParser();
            world = parser.parse(new BufferedReader(new FileReader(prefix + "/world.wo")));

            buildings = new BuildingDeserializer().deserialize(new File(prefix + "/Building.eo"));
            towns = new TownDeserializer().deserialize(new File(prefix + "/Town.eo"));
            creatures = new CreatureDeserializer().deserialize(new File(prefix + "/Creature.eo"));
            player = new PlayerDeserializer().deserialize(new File(prefix + "/Player.eo"));
            items = new ItemDeserializer().deserialize(new File(prefix + "/Item.eo"));
            factions = new FactionDeserializer().deserialize(new File(prefix + "/Faction.eo"));

            JsonElement element = new JsonParser().parse(new JsonReader(new FileReader(new File(prefix + "/metadata.eo"))));
            JsonObject jsonObject = element.getAsJsonObject();
            turn = jsonObject.get("turn").getAsLong();
            idSerialGen = jsonObject.get("idSerialGen").getAsInt();

            relinkPlayer();
            creatures.put(player.getEntity().id, new JsonObjectEntityPair<Creature>(player.getJsonObject(), player.getEntity()));
            relinkBuildings();
            relinkTowns();
            relinkCreatures();
            relinkItems();
            relinkFactions();


        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return new SavedWorldLoader(world, buildings, towns, creatures, player, items, factions, turn, idSerialGen);

    }

    private void relinkFactions() {
        for (Integer key : factions.keySet()){
            JsonObject object = factions.get(key).getJsonObject();
            Faction faction = factions.get(key).getEntity();
            if (object.has("enemies")){
                JsonArray array = object.get("enemies").getAsJsonArray();
                for (JsonElement element : array) {
                    faction.addEnemyFaction(factions.get(element.getAsInt()).getEntity());
                }
            }
            if (object.has("allies")){
                JsonArray array = object.get("allies").getAsJsonArray();
                for (JsonElement element : array) {
                    faction.addAllyFaction(factions.get(element.getAsInt()).getEntity());
                }
            }
        }
    }

    private void relinkItems() {
        for (Integer key : items.keySet()){
            JsonObject object = items.get(key).getJsonObject();
            Item item = items.get(key).getEntity();
            if (object.has("ownedBy")){
                item.setOwnedBy(getEntityOrNull(creatures, object.get("ownedBy").getAsInt()));
            }
        }
    }

    private static <T extends Entity> T getEntityOrNull(Map<Integer, JsonObjectEntityPair<T>> map, int number){
        if (map.containsKey(number)){
            return map.get(number).getEntity();
        } else return null;
    }

    private void relinkPlayer() {
        if (player.getJsonObject().has("followers")){
            JsonArray array = player.getJsonObject().get("followers").getAsJsonArray();
            for (JsonElement element : array) {
                player.getEntity().addFollower(creatures.get(element.getAsInt()).getEntity());
            }

        }
    }

    private void relinkCreatures() {
        for (Integer key : creatures.keySet()){
            Creature creature = creatures.get(key).getEntity();
            JsonObject json = creatures.get(key).getJsonObject();

            if (json.has("ai")){
                creature.setAi(getAiAndInstantiate(json.get("ai").getAsJsonObject()));
            }

            if(json.has("leader")){
                creature.setLeader(getEntityOrNull(creatures, json.get("leader").getAsInt()));
            }
            if(json.has("faction")){
                creature.setFaction(getEntityOrNull(factions, json.get("faction").getAsInt()));
            }
            if (json.has("properties")){
                JsonObject object = json.get("properties").getAsJsonObject();
                for (Map.Entry<String, JsonElement> pairs : object.entrySet()){
                    creature.addOwnedProperty(pairs.getKey(), buildings.get(pairs.getValue().getAsInt()).getEntity());
                }
            }

            if (json.has("inventory")){
                JsonArray array = json.get("inventory").getAsJsonArray();
                for (JsonElement element : array) {
                    creature.addItem(items.get(element.getAsInt()).getEntity());
                }

            }


            if (json.has("equipmentSlots")){
                Map<String, EquipmentSlot> slots = new HashMap<String, EquipmentSlot>();
                Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return "item".equals(f.getName());
                    }
                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                }).create();
                JsonObject object = json.get("equipmentSlots").getAsJsonObject();
                for (Map.Entry<String, JsonElement> pairs : object.entrySet()){
                    slots.put(pairs.getKey(), gson.fromJson(pairs.getValue(), EquipmentSlot.class));
                    if (pairs.getValue().getAsJsonObject().has("item")) {
                        slots.get(pairs.getKey()).setItem(items.get(pairs.getValue().getAsJsonObject()
                                                                         .get("item").getAsInt()).getEntity());
                    }
                }
                creature.setEquipmentSlots(slots);
            }
        }
    }

    private CreatureAI getAiAndInstantiate(JsonObject ai) {
        Gson gson = new Gson();
        String name = ai.get("name").getAsString();
        if ("DeadAI".equalsIgnoreCase(name)){
            return new DeadAi();
        } else if ("FollowAI".equalsIgnoreCase(name)){
            return new FollowAI(creatures.get(ai.get("self").getAsInt()).getEntity());
        } else if ("GroupFollowAI".equalsIgnoreCase(name)){
                    return new GroupFollowAI(creatures.get(ai.get("self").getAsInt()).getEntity());
        } else if ("PlayerAI".equalsIgnoreCase(name)){
                    return new PlayerAI(creatures.get(ai.get("self").getAsInt()).getEntity());
        } else if ("SeekAI".equalsIgnoreCase(name)){
                            return new SeekAI(creatures.get(ai.get("self").getAsInt()).getEntity(),
                                              creatures.get(ai.get("target").getAsInt()).getEntity());
        } else if ("WanderAI".equalsIgnoreCase(name)){
            return new WanderAI(creatures.get(ai.get("self").getAsInt()).getEntity(),
                                gson.fromJson(ai.get("nwBound"), Point.class),
                                gson.fromJson(ai.get("seBound"), Point.class));
        }
        return null;
    }

    private void relinkTowns() {
        for (Integer key : towns.keySet()){
            Town town = towns.get(key).getEntity();
            JsonObject json = towns.get(key).getJsonObject();
            if(json.has("buildings")){
                JsonArray array = json.get("buildings").getAsJsonArray();
                for (JsonElement element : array) {
                    town.registerBuilding(buildings.get(element.getAsInt()).getEntity());
                }
            }
            if(json.has("faction")) {
                town.setFaction(getEntityOrNull(factions, json.get("faction").getAsInt()));
            }
        }
    }

    private void relinkBuildings() {
        for (Integer key : buildings.keySet()){
            Building building = buildings.get(key).getEntity();
            JsonObject json = buildings.get(key).getJsonObject();
            if (json.has("owner")) {
                building.setOwner(getEntityOrNull(creatures, json.get("owner").getAsInt()));
            }
            if (json.has("inventory")){
                JsonArray array = json.get("inventory").getAsJsonArray();
                for (JsonElement element : array) {
                    building.addItem(items.get(element.getAsInt()).getEntity());
                }
            }
        }
    }

    public static void main(String[] args) {
        new LoadAction().load("blah");
    }
}
