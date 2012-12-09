package com.bthorson.torule.town;

import com.bthorson.torule.map.Local;
import com.bthorson.torule.map.Tile;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 12/9/12
 * Time: 12:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class ContstructionUtil {

    private ContstructionUtil(){}

    public static void fillRect(Local toBuildOn, int x, int y, int w, int h, Tile tileType) {
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                toBuildOn.getTiles()[x + i][y + j] = tileType;
            }
        }
    }

    public static void buildWall(Local toBuildOn, int x, int y, int w, int h) {
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
    }


}
