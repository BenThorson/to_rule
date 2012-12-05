package com.bthorson.torule.persist;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.entity.Faction;
import com.bthorson.torule.item.Item;
import com.bthorson.torule.map.World;
import com.bthorson.torule.player.Player;
import com.bthorson.torule.town.Building;
import com.bthorson.torule.town.Town;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Ben Thorson
 * Date: 12/4/12
 * Time: 4:11 PM
 */
public class LoadAction {

    public void load(String name){
        try {
            String prefix = "save/" + name;

            WorldParser parser = new WorldParser();
            parser.parse(new BufferedReader(new FileReader(prefix + "/world.wo")));


            Map<Integer, JsonObjectEntityPair<Building>> buildings = new BuildingDeserializer().deserialize(new File(prefix + "/Building.eo"));
            Map<Integer, JsonObjectEntityPair<Town>> towns = new TownDeserializer().deserialize(new File(prefix + "/Town.eo"));
            Map<Integer, JsonObjectEntityPair<Creature>> creatures = new CreatureDeserializer().deserialize(new File(prefix + "/Creature.eo"));
            Map<Integer, JsonObjectEntityPair<Player>> players = new PlayerDeserializer().deserialize(new File(prefix + "/Player.eo"));
            Map<Integer, JsonObjectEntityPair<Item>> items = new ItemDeserializer().deserialize(new File(prefix + "/Item.eo"));
            Map<Integer, JsonObjectEntityPair<Faction>> factions = new FactionDeserializer().deserialize(new File(prefix + "/Faction.eo"));

            for (Integer key : buildings.keySet()){

            }

            System.out.println();




        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public static void main(String[] args) {
        new LoadAction().load("blah");
    }
}
