package com.bthorson.torule.map;

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
                    tiles[x][y] = Tile.TREE;
                } else {
                    tiles[x][y] = Tile.getGrass();
                }
            }
        }
        return this;
    }

    public Local build(int wX, int wY, int rX, int rY){
        return new Local(wX, wY, rX, rY, tiles);
    }

    public LocalBuilder buildBuilding(int x, int y, int w, int h){
        tiles[x][y] = Tile.WALL_NW;
        tiles[x + w][y] = Tile.WALL_NE;
        tiles[x][y+h] = Tile.WALL_SW;
        tiles[x + w][y+h] = Tile.WALL_SE;
        for (int i = 1; i < w; i++ ){
            tiles[x + i][y] = Tile.WALL_HORIZ;
            tiles[x + i][y + h] = Tile.WALL_HORIZ;
        }
        for (int i = 1; i < h; i++){
            tiles[x][y + i] = Tile.WALL_VERT;
            tiles[x + w][y + i] = Tile.WALL_VERT;
        }

        makeFloor(x + 1, y + 1, w - 1, h - 1);
        return this;
    }

    private void makeFloor(int x, int y, int w, int h) {
        for (int i = 0; i < w; i++){
            for (int j = 0; j < h; j++){
                tiles[x + i][y + j]= Tile.GROUND;
            }
        }
    }
}
