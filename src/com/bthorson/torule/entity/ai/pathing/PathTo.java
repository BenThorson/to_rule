package com.bthorson.torule.entity.ai.pathing;

import com.bthorson.torule.geom.Point;
import com.bthorson.torule.map.World;

import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: benthorson
 * Date: 9/13/12
 * Time: 12:05 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PathTo {

    public Stack<Point> buildPath(World world, Point start, Point target);
}
