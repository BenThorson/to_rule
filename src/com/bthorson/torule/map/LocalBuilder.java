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
        tiles = new Tile[MapConstants.LOCAL_SIZE_X][MapConstants.LOCAL_SIZE_Y];
    }

    public LocalBuilder makeGrassland() {
        for (int x = 0; x < MapConstants.LOCAL_SIZE_X; x++) {
            for (int y = 0; y < MapConstants.LOCAL_SIZE_Y; y++) {
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
