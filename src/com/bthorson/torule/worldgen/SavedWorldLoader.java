package com.bthorson.torule.worldgen;

import com.bthorson.torule.entity.Creature;
import com.bthorson.torule.entity.Entity;
import com.bthorson.torule.entity.EntityManager;
import com.bthorson.torule.entity.Faction;
import com.bthorson.torule.item.Item;
import com.bthorson.torule.map.Local;
import com.bthorson.torule.map.MapConstants;
import com.bthorson.torule.map.World;
import com.bthorson.torule.persist.JsonObjectEntityPair;
import com.bthorson.torule.player.Player;
import com.bthorson.torule.town.Building;
import com.bthorson.torule.town.Town;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: Ben Thorson
 * Date: 12/5/12
 * Time: 9:23 AM
 */
public class SavedWorldLoader implements WorldLoader {


    private Local[][] world;
    private final Map<Integer, JsonObjectEntityPair<Building>> buildings;
    private final Map<Integer, JsonObjectEntityPair<Town>> towns;
    private final Map<Integer, JsonObjectEntityPair<Creature>> creatures;
    private final JsonObjectEntityPair<Player> player;
    private final Map<Integer, JsonObjectEntityPair<Item>> items;
    private final Map<Integer, JsonObjectEntityPair<Faction>> factions;
    private final long turn;
    private final int idSerialGen;

    public SavedWorldLoader(Local[][] world, Map<Integer, JsonObjectEntityPair<Building>> buildings, Map<Integer, JsonObjectEntityPair<Town>> towns,
                            Map<Integer, JsonObjectEntityPair<Creature>> creatures, JsonObjectEntityPair<Player> player,
                            Map<Integer, JsonObjectEntityPair<Item>> items, Map<Integer, JsonObjectEntityPair<Faction>> factions,
                            long turn, int idSerialGen) {
        this.world = world;
        this.buildings = buildings;
        this.towns = towns;
        this.creatures = creatures;
        this.player = player;
        this.items = items;
        this.factions = factions;
        this.turn = turn;
        this.idSerialGen = idSerialGen;
    }

    public void startWorld(){
        EntityManager.destroy();
        World.destroy();
        Entity.setCurrentId(idSerialGen);
        registerEntities();

        World.getInstance().startWorld(this);



    }

    private void registerEntities() {
        for (Integer key : towns.keySet()){
            EntityManager.getInstance().addTown(towns.get(key).getEntity().getRegionalPosition(),
                                                towns.get(key).getEntity());
        }
        for (Integer key : creatures.keySet()){
            EntityManager.getInstance().addCreature(creatures.get(key).getEntity());
        }
        for (Integer key : items.keySet()){
            Item item = items.get(key).getEntity();
            EntityManager.getInstance().addItem(item);
            if (!item.isOwned()){
                EntityManager.getInstance().addFreeItem(item);
            }
        }
        EntityManager.getInstance().setPlayer(player.getEntity());

        List<Faction> factns = new ArrayList<Faction>();
        for (Integer key : factions.keySet()){
            factns.add(factions.get(key).getEntity());
        }
        EntityManager.getInstance().setFactions(factns);

    }

    @Override
    public Local[][] getLocals() {
        return world;
    }

    @Override
    public long getTurnCounter() {
        return turn;
    }

    @Override
    public int worldWidth() {
        return world.length * MapConstants.LOCAL_SIZE_X;
    }

    @Override
    public int worldHeight() {
        return world[0].length * MapConstants.LOCAL_SIZE_Y;
    }
}
