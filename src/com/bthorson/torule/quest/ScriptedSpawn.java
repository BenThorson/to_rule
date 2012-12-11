package com.bthorson.torule.quest;

import com.bthorson.torule.worldgen.spawn.Spawn;

/**
 * User: Ben Thorson
 * Date: 12/6/12
 * Time: 2:35 PM
 */
public class ScriptedSpawn implements Spawn {

    private String creatureType;
    private int min;
    private int max;

    public String getCreatureType() {
        return creatureType;
    }

    public void setCreatureType(String creatureType) {
        this.creatureType = creatureType;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
