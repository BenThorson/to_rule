package com.bthorson.torule.geom;

import com.bthorson.torule.entity.Creature;

/**
 * User: ben
 * Date: 9/13/12
 * Time: 1:21 PM
 */
public class PointUtil {

    private PointUtil(){}

    public static int getDiagDist(Point p1, Point p2) {
        int x1Dist = Math.abs(p1.x() - p2.x());
        int y1Dist = Math.abs(p1.y() - p2.y());
        return Math.max(x1Dist, y1Dist);
    }

    public static int diagMoves(Point p1, Point p2) {
        int xDiags = Math.abs(p1.x() - p2.x());
        int yDiags = Math.abs(p2.x() - p2.y());
        return xDiags + yDiags;
    }
}
