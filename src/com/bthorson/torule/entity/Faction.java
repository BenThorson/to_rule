package com.bthorson.torule.entity;

import com.bthorson.torule.persist.SerializeUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashSet;
import java.util.Set;

/**
 * User: ben
 * Date: 9/12/12
 * Time: 4:39 PM
 */
public class Faction extends Entity{

    private Set<Faction> enemies = new HashSet<Faction>();
    private Set<Faction> allies = new HashSet<Faction>();

    public Faction(String name){
        super(name);
    }

    public Set<Faction> getEnemies() {
        return enemies;
    }

    public Set<Faction> getAllies() {
        return allies;
    }

    public void addEnemyFaction(Faction enemy){
        if (enemies == null){
            enemies = new HashSet<Faction>();
        }
        enemies.add(enemy);
    }

    public void addAllyFaction(Faction ally){
        if (allies == null){
            allies = new HashSet<Faction>();
        }
        allies.add(ally);
    }

    @Override
    public JsonElement serialize() {
        JsonObject obj = super.serialize().getAsJsonObject();
        SerializeUtils.serializeRefCollection(enemies, obj, "enemies");
        SerializeUtils.serializeRefCollection(allies, obj, "allies");
        return obj;
    }
}
