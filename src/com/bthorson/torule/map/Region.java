package com.bthorson.torule.map;

import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 12:10 PM
 */
public class Region {

    private Local[][] locals = new Local[10][10];

    public Region(int worldX, int worldY) {
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                locals[x][y] = new LocalBuilder().makeGrassland().build(new Point(worldX * 1000 + x * 100, worldY * 1000 + y * 100));
            }
        }
    }

    public Tile tile(int x, int y) {
        return locals[x / 100][y / 100].tile(x % 100, y % 100);

    }

    public void serialize(PrintWriter writer) {
        writer.println("\tRegion metadata:  Dimension width:" + locals.length + " height: " + locals[0].length);
        for (int x = 0; x < locals.length; x++) {
            for (int y = 0; y < locals[0].length; y++) {
                writer.println("\tL x:" + x + " y:" + y + ";");
                locals[x][y].serialize(writer);
            }
        }
    }

    public Local getLocal(int x, int y){
        return locals[x][y];
    }
}
