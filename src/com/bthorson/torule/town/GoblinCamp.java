package com.bthorson.torule.town;

import com.bthorson.torule.map.Local;
import com.bthorson.torule.map.LocalType;
import com.bthorson.torule.map.MapConstants;
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
        local.setType(LocalType.GOBLIN_CAMP);
        for (int x = 0; x < MapConstants.LOCAL_SIZE_X; x++){
            for (int y = 0; y < MapConstants.LOCAL_SIZE_Y; y++){
                local.getTiles()[x][y] = Tile.getGrass();
            }
        }
        ContstructionUtil.fillRect(local, 35, 35, 35, 35, Tile.getDirt());

        ContstructionUtil.fillRect(local, 35, 35, 35, 35, Tile.ROUGH_WALL);
        ContstructionUtil.fillRect(local, 36, 36, 33, 33, Tile.getDirt());
        ContstructionUtil.fillRect(local, 51, 68, 3, 3, Tile.getDirt());
    }
}
