package com.bthorson.torule.entity.ai.pathing;

import com.bthorson.torule.geom.Point;

import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 9/13/12
 * Time: 12:05 PM
 */
public interface PathTo {

    /**
     * Takes a start point, an end point, and determines the path between them.
     * @param start     Start point
     * @param target    End point
     * @param ignoreTerrain  //todo create a straight line PathTo and deprecate/remove this
     * @return a {@link Stack} of moves
     */
    public Stack<Point> buildPath(Point start, Point target, boolean ignoreTerrain);
}
