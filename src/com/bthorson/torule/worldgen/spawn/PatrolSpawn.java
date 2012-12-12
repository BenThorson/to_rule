package com.bthorson.torule.worldgen.spawn;

import java.util.List;

/**
 * User: Ben Thorson
 * Date: 12/11/12
 * Time: 12:36 PM
 */
public class PatrolSpawn {

    String patrolType;
    List<CritterSpawn> spawns;

    public PatrolSpawn(String patrolType, List<CritterSpawn> spawns) {
        this.patrolType = patrolType;
        this.spawns = spawns;
    }

    public String getPatrolType() {
        return patrolType;
    }

    public List<CritterSpawn> getSpawns() {
        return spawns;
    }
}
