package com.bthorson.torule.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * User: ben
 * Date: 9/12/12
 * Time: 4:39 PM
 */
public class Faction {
    public static final Faction TEST = new Faction("test");

    private Set<Faction> enemies;
    private Set<Faction> allies;
    private String name;

    public Faction(String name){
        this.name = name;
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
}
