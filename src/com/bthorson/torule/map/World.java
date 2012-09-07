package com.bthorson.torule.map;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 12:07 PM
 */
public class World {

    private Region[][] regions;

    public int width() {
        return 1000;
    }

    public int height() {
        return 1000;
    }

    public World(){
        regions = new Region[1][1];
        for (int x = 0; x < 1; x++){
            for (int y = 0; y < 1; y++){
                regions[x][y] = new Region(x,y);
            }
        }
    }

    public Tile tile(int x, int y){
        return regions[x/1000][y/1000].tile(x%1000,y%1000);
    }
}
