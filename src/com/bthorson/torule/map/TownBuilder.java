package com.bthorson.torule.map;

import com.bthorson.torule.entity.CreatureFactory;
import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 11/10/12
 * Time: 9:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class TownBuilder {
    private Local toBuildOn;

    public TownBuilder(Local toBuildOn) {
        this.toBuildOn = toBuildOn;
    }

    public TownBuilder buildTownSquare(int size) {
        int startX = Local.WIDTH / 2 - (int) Math.floor((double) size / 2.0);
        int starty = Local.HEIGHT / 2 - (int) Math.floor((double) size / 2.0);
        int endX = Local.WIDTH / 2 + (int) Math.ceil((double) size / 2.0);
        int endY = Local.HEIGHT / 2 + (int) Math.ceil((double) size / 2.0);

        fillRect(startX, starty, endX - startX, endY - starty, Tile.ROAD);

        return this;
    }

    public TownBuilder buildWall(int x, int y, int w, int h) {
        toBuildOn.getTiles()[x][y] = Tile.WALL_NW;
        toBuildOn.getTiles()[x + w][y] = Tile.WALL_NE;
        toBuildOn.getTiles()[x][y + h] = Tile.WALL_SW;
        toBuildOn.getTiles()[x + w][y + h] = Tile.WALL_SE;
        for (int i = 1; i < w; i++) {
            toBuildOn.getTiles()[x + i][y] = Tile.WALL_HORIZ;
            toBuildOn.getTiles()[x + i][y + h] = Tile.WALL_HORIZ;
        }
        for (int i = 1; i < h; i++) {
            toBuildOn.getTiles()[x][y + i] = Tile.WALL_VERT;
            toBuildOn.getTiles()[x + w][y + i] = Tile.WALL_VERT;
        }
        return this;
    }

    public TownBuilder buildBuilding(int x, int y, int w, int h, Direction doorDir) {
        buildWall(x,y,w,h);
        fillRect(x + 1, y + 1, w - 1, h - 1, Tile.FLOOR);
        switch (doorDir) {
            case NORTH:
                toBuildOn.getTiles()[x + w / 2][y] = Tile.DOOR;
                break;
            case SOUTH:
                toBuildOn.getTiles()[x + w / 2][y + h] = Tile.DOOR;
                break;
            case WEST:
                toBuildOn.getTiles()[x][y + h / 2] = Tile.DOOR;
                break;
            case EAST:
                toBuildOn.getTiles()[x + w][y + h / 2] = Tile.DOOR;
                break;
        }
        return this;
    }

    private void fillRect(int x, int y, int w, int h, Tile tileType) {
        System.out.println("");
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                toBuildOn.getTiles()[x + i][y + j] = tileType;
            }
        }
    }

    public TownBuilder buildRoad(int width, int startX, int startY, int endX, int endY) {
        boolean isEastWest = startX - endX != 0;
        if (isEastWest) {
            fillRect(startX, startY - width / 2, endX - startX, width, Tile.ROAD);
        } else {
            fillRect(startX - width / 2, startY, width, endY - startY, Tile.ROAD);
        }
        return this;
    }

    public TownBuilder makeTownsmen(int numberOfTownsmen){
        for (int i = 0; i < numberOfTownsmen; i++){
            Point candidate = PointUtil.randomPoint(World.NW_CORNER, Local.seBound);
            if (!World.getInstance().isOccupied(candidate)){
                CreatureFactory.buildVillager(World.getInstance(), candidate);

            } else {
                i--;
            }

        }
        return this;
    }
}
