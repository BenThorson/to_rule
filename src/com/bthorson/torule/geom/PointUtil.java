package com.bthorson.torule.geom;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User: ben
 * Date: 9/13/12
 * Time: 1:21 PM
 */
public class PointUtil {

    public static final Point POINT_ORIGIN = new Point(0,0);

    private PointUtil(){}

    public static int getDiagDist(Point p1, Point p2) {
        int x1Dist = Math.abs(p1.x() - p2.x());
        int y1Dist = Math.abs(p1.y() - p2.y());
        return Math.max(x1Dist, y1Dist);
    }

    public static int diagMoves(Point p1, Point p2) {
        int xDiags = Math.abs(p1.x() - p2.x());
        int yDiags = Math.abs(p1.y() - p2.y());
        return Math.min(xDiags, yDiags);
    }

    public static int manhattanDist(Point p1, Point p2){
        Point diff = p1.absoluteDifference(p2);
        return diff.x() + diff.y();
    }

    public static Point randomPoint(Point nwCorner, Point seBound) {
        Random random = new Random();
        return new Point(random.nextInt(seBound.x() - nwCorner.x()), random.nextInt(seBound.y() - nwCorner.y())).add(nwCorner);
    }

    public static Point randomPoint(Point seBound) {
        return randomPoint(PointUtil.POINT_ORIGIN, seBound);
    }

    public static List<Point> getPointsInRange(Point nw, Point se) {
        List<Point> points = new ArrayList<Point>();
        for (int x = nw.x(); x < se.x(); x++){
            for (int y = nw.y(); y < se.y(); y++){
                points.add(new Point(x,y));
            }
        }
        return points;
    }

    public static Point toRegional(Point position) {
        return position.divide(100);
    }

    public static Point floorToNearest100(Point point){
        return toRegional(point).multiply(100);
    }
}
