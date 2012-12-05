package com.bthorson.torule.worldgen;

import com.bthorson.torule.map.Local;

/**
 * User: Ben Thorson
 * Date: 12/5/12
 * Time: 12:50 AM
 */
public interface WorldLoader {

    public Local[][] getLocals();
    public long getTurnCounter();
    public int worldWidth();
    public int worldHeight();
}
