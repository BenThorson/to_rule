package com.bthorson.torule.entity;

import com.bthorson.torule.geom.Direction;
import com.bthorson.torule.geom.Point;
import com.bthorson.torule.geom.Rect;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: ben
 * Date: 9/15/12
 * Time: 1:31 AM
 */
public class Group {

    private static final int DEF_WIDTH_RATIO = 5;
    private static final int DEF_HEIGHT_RATIO = 2;


    private Map<Point, Creature> members = new HashMap<Point, Creature>();
    private Creature squadCommander;
    private Point commanderPoint;


    public Rect rect;

    public Group(List<Creature> memList, Creature squadCommander){
        this.squadCommander = squadCommander;
        buildRect(memList.size());
        mapPointsToCreature(memList, squadCommander);

    }

    private void mapPointsToCreature(List<Creature> memList, Creature squadCommander) {
        boolean leadersSquareFound = false;
        int listIndex = 0;
        for (Point[] row : rect.getPoints()){
            for (Point col : row){
                if (col.equals(new Point((rect.getPoints().length -1) / 2, (row.length -1) / 2)) && !leadersSquareFound){
                    members.put(col, squadCommander);
                    commanderPoint = col;
                } else {
                    members.put(col, memList.get(listIndex++));
                }
                if (listIndex >= memList.size()){
                    return;
                }
            }
        }
    }

    private void buildRect(int grpSize) {
        rect = new Rect(new Point(DEF_WIDTH_RATIO, DEF_HEIGHT_RATIO), grpSize + 1);
    }

    public void move(Point point){
        Direction dir = Direction.directionOf(point);
        if (dir == null){
            dir = squadCommander.getHeading();
        }
        Point offset = squadCommander.position().subtract(commanderPoint).add(point);
        switch (dir){
            case NORTH:
            case NORTHWEST:
            case NORTHEAST:
                for(Point[] row : rect.getPoints()){
                    for(Point col : row){
                        updateForPoint(col, offset);
                    }
                }
                break;
            case EAST:
                for (int y = rect.getPoints()[0].length - 1; y >= 0; y--){
                    for (int x = rect.getPoints().length - 1; x >= 0; x--){
                        updateForPoint(rect.getPoints()[x][y], offset);
                    }
                }
                break;
            case SOUTH:
            case SOUTHWEST:
            case SOUTHEAST:
                for (int x = rect.getPoints().length - 1; x >= 0; x--){
                    for (int y = rect.getPoints()[0].length - 1; y >= 0; y--){
                        updateForPoint(rect.getPoints()[x][y], offset);
                    }
                }
                break;
            case WEST:
                for (int y = 0; y < rect.getPoints()[0].length; y++){
                    for (int x = 0; x < rect.getPoints().length; x++){
                        updateForPoint(rect.getPoints()[x][y], offset);
                    }
                }
                break;

        }
    }

    private void updateForPoint(Point point, Point offset){
        if (members.get(point) == null){
            return;
        }
        members.get(point).setTarget(point.add(offset));
        members.get(point).update();
    }
}
