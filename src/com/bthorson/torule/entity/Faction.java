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
    public static final Faction TEST = new Faction("test");

    private Set<Faction> enemies;
    private Set<Faction> allies;

    public Faction(String name){
        super(name);
        enemies = new HashSet<Faction>();
        allies = new HashSet<Faction>();
    }

    public Set<Faction> getEnemies() {
        return enemies;
    }

    public Set<Faction> getAllies() {
        return allies;
    }

    public void addEnemyFaction(Faction enemy){
        enemies.add(enemy);
    }

    public void addAllyFaction(Faction ally){
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
