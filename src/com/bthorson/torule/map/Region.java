package com.bthorson.torule.map;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 12:10 PM
 */
public class Region {

    private Local[][] locals;

    private int wx;
    private int wy;

    public Region(int worldX, int worldY) {
        this.wx = worldX;
        this.wy = worldY;

        locals = new Local[10][10];
        for (int x = 0; x < 10; x++){
            for (int y = 0; y < 10; y++){
                locals[x][y] = new LocalBuilder().makeGrassland().build(worldX, worldY, x, y);
            }
        }
    }


    public Tile tile(int x, int y) {
        return locals[x/100][y/100].tile(x % 100, y % 100);

    }
}
