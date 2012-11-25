package com.bthorson.torule.player;

import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.World;

/**
 * User: ben
 * Date: 9/13/12
 * Time: 9:51 PM
 */
public class ExploredMap {

    private boolean[][] explored;

    public ExploredMap(){
        this.explored = new boolean[World.getInstance().width()][World.getInstance().height()];
    }

    public void explore(Point point){
        explored[point.x()][point.y()] = true;
    }

    public boolean hasExplored(Point point) {
        return explored[point.x()][point.y()];
    }
}
