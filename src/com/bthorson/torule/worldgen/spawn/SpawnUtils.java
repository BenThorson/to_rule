package com.bthorson.torule.worldgen.spawn;

import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.PointUtil;
import com.bthorson.torule.map.World;

import java.util.ArrayList;
import java.util.List;

import static com.bthorson.torule.map.MapConstants.LOCAL_SIZE_POINT;

/**
 * User: Ben Thorson
 * Date: 12/11/12
 * Time: 1:27 PM
 */
public class SpawnUtils {

    private SpawnUtils(){}

    public static List<Point> getPlaceablePointsInRegion(int number, Point offset, Point placement) {
        for (int i = 1; i < 100; i++) {
            List<Point> attempt = new ArrayList<Point>();
            for (Point p : getValidPointsinRegion(i, placement)) {
                if (!p.add(offset).isOutOfBounds() && !World.getInstance().isOccupied(p.add(offset))){
                    attempt.add(p.add(offset));
                }
            }
            if (attempt.size() > number){
                return attempt;
            }
        }
        return new ArrayList<Point>();
    }

    private static List<Point> getValidPointsinRegion(int i, Point placement) {
        List<Point> validPoints = new ArrayList<Point>();
        for (Point p : PointUtil.getPointsInRange(placement.subtract(i), placement.add(i))) {
            if (p.withinRect(LOCAL_SIZE_POINT)){
                validPoints.add(p);
            }
        }
        return validPoints;
    }

}
