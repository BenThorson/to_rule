package com.bthorson.torule.map;

import com.bthorson.torule.entity.Creature;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 12:10 PM
 */
public class Local {

    public static final int WIDTH = 100;
    public static final int HEIGHT = 100;

    private int worldX;
    private int worldY;
    private int regionX;
    private int regionY;

    private Tile[][] tiles;


    public Local(int worldX, int worldY, int regionX, int regionY, Tile[][] tiles) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.regionX = regionX;
        this.regionY = regionY;
        this.tiles = tiles;
    }

    public Tile tile(int x, int y){
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT)
            return Tile.BOUNDS;
        else
            return tiles[x][y];
    }
}
