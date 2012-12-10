package com.bthorson.torule.map;

import com.bthorson.torule.geom.Point;

import java.io.PrintWriter;

/**
 * User: ben
 * Date: 9/7/12
 * Time: 12:10 PM
 */
public class Local {

    private static final int WIDTH = MapConstants.LOCAL_SIZE_X;
    private static final int HEIGHT = MapConstants.LOCAL_SIZE_Y;

    private Point nwBoundWorldCoord;
    private Point seBoundWorldBound;
    //relative ferocity
    private Ferocity ferocity;
    //score to help determine it
    private int ferocityScore;
    private int distanceFromTown;


    private LocalType type;

    private Tile[][] tiles;


    public Local(Point nwBound, Tile[][] tiles) {
        nwBoundWorldCoord = nwBound;
        seBoundWorldBound = nwBoundWorldCoord.add(MapConstants.LOCAL_SIZE_POINT);
        this.tiles = tiles;
    }

    public Tile tile(int x, int y){
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT)
            return Tile.BOUNDS;
        else
            return tiles[x][y];
    }

    public void serialize(PrintWriter writer) {

        for (Tile[] tile : tiles) {
            for (Tile aTile : tile) {
                writer.print(aTile.ordinal() + " ");
            }
            writer.println();
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

    public LocalType getType() {
        return type;
    }

    public void setType(LocalType type) {
        this.type = type;
    }

    public Ferocity getFerocity() {
        return ferocity;
    }

    public void setFerocity(Ferocity ferocity) {
        this.ferocity = ferocity;
    }

    public int getFerocityScore() {
        return ferocityScore;
    }

    public void setFerocityScore(int ferocityScore) {
        this.ferocityScore = ferocityScore;
    }

    public int getDistanceFromTown() {
        return distanceFromTown;
    }

    public void setDistanceFromTown(int distanceFromTown) {
        this.distanceFromTown = distanceFromTown;
    }
}
