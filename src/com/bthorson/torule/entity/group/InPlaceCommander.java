package com.bthorson.torule.entity.group;

import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: ben
 * Date: 9/15/12
 * Time: 9:02 PM
 */
public class InPlaceCommander implements MovementCommander {
    @Override
    public void move(Group group, Point point) {
        Direction dir = Direction.directionOf(point);
        if (dir == null){
            dir = group.getSquadCommander().getHeading();
        }
        Point offset = group.getSquadCommander().position().subtract(group.getCommanderPoint()).add(point);
        List<Point> points = new ArrayList<Point>(group.getFormation().getPositions());
        switch (dir){
            case NORTH:
            case NORTH_WEST:
            case NORTH_EAST:
                Collections.sort(points, new SortByY());
                break;
            case EAST:
                Collections.sort(points, new SortByX());
                Collections.reverse(points);
                break;
            case SOUTH:
            case SOUTH_WEST:
            case SOUTH_EAST:
                Collections.sort(points, new SortByY());
                Collections.reverse(points);
                break;
            case WEST:
                Collections.sort(points, new SortByX());
                break;
        }
        for (Point p : points){
            group.updateForPoint(p, offset);
        }
    }
}
