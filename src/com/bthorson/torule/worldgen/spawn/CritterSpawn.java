package com.bthorson.torule.worldgen.spawn;

import com.bthorson.torule.map.Ferocity;

/**
 * User: Ben Thorson
 * Date: 12/10/12
 * Time: 3:09 PM
 */
public class CritterSpawn implements Spawn {

    private String type;
    private String creatureType;
    private int min;
    private int max;
    private String faction;
    private Ferocity[] ferocity;

    public CritterSpawn(String type, String creature, int min, int max, String faction, Ferocity[] ferocity) {
        this.type = type;
        this.creatureType = creature;
        this.min = min;
        this.max = max;
        this.faction = faction;
        this.ferocity = ferocity;
    }

    public String getType() {
        return type;
    }

    public String getCreatureType() {
        return creatureType;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public String getFaction() {
        return faction;
    }

    public Ferocity[] getFerocity() {
        return ferocity;
    }
}
