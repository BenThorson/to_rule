package com.bthorson.torule.map;

import com.bthorson.torule.geom.Point;

import java.util.Random;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 1:02 PM
 */
public class LocalBuilder {

    private Tile[][] tiles;

    public LocalBuilder() {
        tiles = new Tile[Local.WIDTH][Local.HEIGHT];
    }

    public LocalBuilder makeGrassland() {
        for (int x = 0; x < Local.WIDTH; x++) {
            for (int y = 0; y < Local.HEIGHT; y++) {
                if (new Random().nextInt(50) > 48){
                    tiles[x][y] = Tile.getGrass();
                } else {
                    tiles[x][y] = Tile.getGrass();
                }
            }
        }
        return this;
    }

    public Local build(Point nwBound){
        return new Local(nwBound, tiles);
    }

}
