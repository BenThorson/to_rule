package com.bthorson.torule.worldgen.spawn;

/**
 * User: Ben Thorson
 * Date: 12/10/12
 * Time: 3:26 PM
 */
public interface Spawn {

    public int getMin();
    public int getMax();

    String getCreatureType();
}
