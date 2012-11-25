package com.bthorson.torule.map;

import com.bthorson.torule.geom.Point;

import java.io.PrintWriter;

import static com.bthorson.torule.map.MapConstants.*;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 12:10 PM
 */
public class Region {

    private Local[][] locals = new Local[REGION_X_IN_LOCALS][REGION_Y_IN_LOCALS];

    public Region(int worldX, int worldY) {
        for (int x = 0; x < REGION_X_IN_LOCALS; x++) {
            for (int y = 0; y < REGION_Y_IN_LOCALS; y++) {
                locals[x][y] = new LocalBuilder().makeGrassland().build(new Point(worldX * REGION_SIZE_X + x * LOCAL_SIZE_X,
                                                                                  worldY * REGION_SIZE_Y + y * LOCAL_SIZE_Y));
            }
        }
    }

    public Tile tile(int x, int y) {
        return locals[x / LOCAL_SIZE_X][y / LOCAL_SIZE_Y].tile(x % LOCAL_SIZE_X, y % LOCAL_SIZE_Y);

    }

    public void serialize(PrintWriter writer) {
        writer.println("\tRegion metadata:  Dimension width:" + locals.length + " height: " + locals[0].length);
        for (int x = 0; x < REGION_X_IN_LOCALS; x++) {
            for (int y = 0; y < REGION_Y_IN_LOCALS; y++) {
                writer.println("\tL x:" + x + " y:" + y + ";");
                locals[x][y].serialize(writer);
            }
        }
    }

    public Local getLocal(int x, int y){
        return locals[x][y];
    }
}
