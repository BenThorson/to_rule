package com.bthorson.torule.worldgen;

import com.bthorson.torule.geom.Point;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 11/18/12
 * Time: 9:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorldGenParams {

    private int numCities;
    private Point worldSize;
    private String playerName;


    public int getNumCities() {
        return numCities;
    }

    public void setNumCities(int numCities) {
        this.numCities = numCities;
    }

    public Point getWorldSize() {
        return worldSize;
    }

    public void setWorldSize(Point worldSize) {
        this.worldSize = worldSize;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }
}
