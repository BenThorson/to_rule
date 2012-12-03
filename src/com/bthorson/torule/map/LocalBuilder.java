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
    private Random random = new Random();
    private final int rockFrequency;
    private final int treeFrequency;
    private final int brushFrequency;

    public LocalBuilder(int rockFrequency, int treeFrequency, int brushFrequency) {
        this.rockFrequency = rockFrequency;
        this.treeFrequency = treeFrequency;
        this.brushFrequency = brushFrequency;
        tiles = new Tile[MapConstants.LOCAL_SIZE_X][MapConstants.LOCAL_SIZE_Y];
    }

    public LocalBuilder makeGrassland() {
        for (int x = 0; x < MapConstants.LOCAL_SIZE_X; x++) {
            for (int y = 0; y < MapConstants.LOCAL_SIZE_Y; y++) {
                tiles[x][y] = Tile.getGrass();
                if (random.nextInt(100) < brushFrequency){
                    tiles[x][y] = Tile.getBrush();
                }
                if (random.nextInt(100) < rockFrequency){
                    tiles[x][y] = Tile.ROCK;
                }
                if (random.nextInt(100) < treeFrequency){
                    tiles[x][y] = Tile.getTree();
                }
            }
        }
        return this;
    }

    public Local build(Point nwBound){
        return new Local(nwBound, tiles);
    }

}
