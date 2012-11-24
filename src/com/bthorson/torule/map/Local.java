package com.bthorson.torule.map;

import com.bthorson.torule.geom.Point;

import java.io.PrintWriter;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 12:10 PM
 */
public class Local {

    public static final int WIDTH = 100;
    public static final int HEIGHT = 100;

    private Point nwBoundWorldCoord;
    private Point seBoundWorldBound;
    public static Point seBound = new Point(WIDTH,HEIGHT);

    private Tile[][] tiles;


    public Local(Point nwBound, Tile[][] tiles) {
        nwBoundWorldCoord = nwBound;
        seBoundWorldBound = nwBoundWorldCoord.add(seBound);
        this.tiles = tiles;
    }

    public Tile tile(int x, int y){
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT)
            return Tile.BOUNDS;
        else
            return tiles[x][y];
    }

    public void serialize(PrintWriter writer) {
        writer.println("\t\tdimension w:" + tiles.length + " h: " + tiles[0].length);
        for (int x = 0; x < tiles.length; x++){
            for (int y = 0; y < tiles[0].length; y++){
                writer.println("\t\tT x:" + x + " y:" + y
                        + " t:" + tiles[x][y].name());
            }
        }
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public Point getNwBoundWorldCoord() {
        return nwBoundWorldCoord;
    }

    public void setNwBoundWorldCoord(Point nwBoundWorldCoord) {
        this.nwBoundWorldCoord = nwBoundWorldCoord;
    }

    public Point getSeBoundWorldBound() {
        return seBoundWorldBound;
    }

    public void setSeBoundWorldBound(Point seBoundWorldBound) {
        this.seBoundWorldBound = seBoundWorldBound;
    }
}
