package com.bthorson.torule.town;

import com.bthorson.torule.map.Local;
import com.bthorson.torule.map.Tile;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 12/9/12
 * Time: 12:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class GoblinCamp {

    public void build(Local local){
        ContstructionUtil.fillRect(local, 0, 0, 99, 99, Tile.WATER);
        ContstructionUtil.fillRect(local, 5, 5, 90, 90, Tile.DIRT);
        ContstructionUtil.fillRect(local, 47, 94, 5, 5, Tile.DIRT);

        ContstructionUtil.fillRect(local, 35, 35, 35, 35, Tile.ROUGH_WALL);
        ContstructionUtil.fillRect(local, 36, 36, 33, 33, Tile.DIRT);
        ContstructionUtil.fillRect(local, 51, 68, 3, 3, Tile.DIRT);
    }
}
